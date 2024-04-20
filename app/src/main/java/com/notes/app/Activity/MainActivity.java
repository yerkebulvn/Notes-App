package com.notes.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import com.notes.app.Activity.Note.CreateNoteActivity;
import com.notes.app.Methods.Methods;
import com.notes.app.Methods.NavigationUtil;
import com.notes.app.R;
import com.notes.app.adapters.NoteAdapter;
import com.notes.app.database.NotesDatabase;
import com.notes.app.entities.Note;
import com.notes.app.listeners.InterAdListener;
import com.notes.app.listeners.NotesListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // Утилита әдістерін инициализациялау
    private Methods methods;

    // UI элементтері
    private NavigationView navigationView;

    // Сұрау кодтары үшін тұрақтылар
    public  static  final int REQUEST_CODE_ADD_NOTE = 1;
    public  static  final int REQUEST_CODE_UPDATE_NOTE = 2;
    private static final int REQUST_CODE_SHOW_NOTES = 3;
    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private int noteClickedPostion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Қолданба темасын орнату
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        methods = new Methods(this);

        // Interadlistener Көмегімен methods класын инициализациялау
        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, Note note, String type) {
                // Жеке жазбаларды басу оқиғаларын өңдеңіз (мысалы, интерстициальды жарнамаларды көрсету)
                noteClickedPostion = position;
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                intent.putExtra("isViemOrUpdate", true);
                intent.putExtra("note", note);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
            }

        });

        Toolbar toolbar =  findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        // Құралдар тақтасын және шарлау тартпасын орнатy
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Шарлау көрінісін орнату
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        ImageView imageViewAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageViewAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });

        // Жазбаларды көрсету Үшін RecyclerView орнатy
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // Жазбалар тізімін және адаптерді инициализациялау
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList, new NotesListener() {
            @Override
            public void onNoteClicked(Note note, int position) {
                // Жеке жазбаны басу оқиғаларын өңдеңіз
                methods.showInter(position, note, "");
            }
        });
        notesRecyclerView.setAdapter(noteAdapter);

        // Дерекқордан бар жазбаларды жүктеңіз
        getNotes(REQUST_CODE_SHOW_NOTES, false);

        // Іздеу функциясын орнату
        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Бұл тұрғыда қолданылмайды
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                noteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (noteList.size() != 0){
                    noteAdapter.searchNote(editable.toString());
                }

            }
        });

    }

    // Дерекқордан жазбаларды алу әдісі
    private void getNotes(final int requestCode, final  boolean isNoteDeleted){

        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                // Дерекқордан барлық жазбаларды шығарып алыңыз
                return NotesDatabase
                        .getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUST_CODE_SHOW_NOTES){
                    // Алынған жазбаларды тізімге қосыңыз Және Қайта Өңдеу Көрінісін жаңартыңыз
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                }else if (requestCode == REQUEST_CODE_ADD_NOTE){
                    // Тізімнің басында жаңа жазба қосыңыз
                    noteList.add(0, notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                }else if (requestCode == REQUEST_CODE_UPDATE_NOTE){
                    // Бар жазбаны жаңартыңыз немесе жойыңыз
                    noteList.remove(noteClickedPostion);
                    if (isNoteDeleted){
                        noteAdapter.notifyItemRemoved(noteClickedPostion);
                    }else {
                        noteList.add(noteClickedPostion, notes.get(noteClickedPostion));
                        noteAdapter.notifyItemChanged(noteClickedPostion);
                    }
                }
            }
        }
        new GetNotesTask().execute();   // Дерекқорды іздеу тапсырмасын орындаңыз
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            // Жаңа жазбаны қосу нәтижесін өңдеңіз
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            // Жазбаны жаңарту немесе жою нәтижесін өңдеңіз
            if (data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.nav_delete :
                // Шарлау элементін таңдауды өңдеу (мысалы, Жою Әрекетін ашу)
                NavigationUtil.DeleteActivity(MainActivity.this);
                break;

            default :
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}