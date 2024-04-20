package com.notes.app.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.notes.app.R;
import com.notes.app.entities.Note;
import com.notes.app.listeners.NotesListener;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    // Адаптерге арналған контекст
    private Context context;

    // Көрсетілетін жазбалар тізімі
    private List<Note> notes;

    // Ескерту үшін тыңдаушы оқиғаларды тыңдаушы
    private NotesListener notesListener;

    // UI жаңартуларын өңдеуге арналған таймер
    private Timer timer;

    // Жазбалардың түпнұсқа тізімі (сүзу үшін пайдаланылады)
    private List<Note> noteSource;

    // Конструктор адаптерді баптандырады
    public NoteAdapter(Context context,List<Note> notes, NotesListener notesListener) {
        this.context = context;
        this.notes = notes;
        this.notesListener = notesListener;
        this.noteSource = notes;
    }

    // RecyclerView Көрінісіндегі әрбір элемент үшін жаңа ViewHolder жасаy
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    // Деректерді ViewHolder-ге байланыстыру
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = notes.get(position);
        // Жазба элементі үшін click listener параметрін орнатy
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesListener.onNoteClicked(notes.get(position), position);
            }
        });

        // UI жаңартулары үшін қадамды есептеңіз
        int step = 1;
        int final_step = 1;
        for (int i = 1; i < position + 1; i++) {
            if (i == position + 1) {
                final_step = step;
            }
            step++;
        }

        // Қадам негізінде "жаңа" индикаторды көрсету немесе жасыру
        switch (step) {
            case 1:
                holder.item_new.setVisibility(View.VISIBLE);
                break;
            default:
                holder.item_new.setVisibility(View.GONE);
                break;

        }

        //Жазбаның тақырыбын, субтитрін және күнін/уақытын орнатыңыз
        holder.textTitle.setText(note.getTitle());
        if (note.getSubtitle().trim().isEmpty()){
            holder.textSubtitle.setVisibility(View.GONE);
        }else {
            holder.textSubtitle.setText(note.getSubtitle());
        }
        holder.textDateTime.setText(note.getDateTime());

        // Ескертпе түсіне негізделген өң түсі мен мәтін түстерін орнатыңыз
        GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutNote.getBackground();
        if (note.getColor() != null){
            switch (note.getColor()){
                case "#333333":

                    gradientDrawable.setColor(Color.parseColor("#121212"));
                    holder.textTitle.setTextColor(Color.parseColor("#DBDBDB"));
                    holder.textSubtitle.setTextColor(Color.parseColor("#E9A0A0A0"));
                    holder.textDateTime.setTextColor(Color.parseColor("#E9A0A0A0"));

                    break;
                default:
                    gradientDrawable.setColor(Color.parseColor(note.getColor()));
                    break;
            }
        }else {

            gradientDrawable.setColor(Color.parseColor("#121212"));
            holder.textTitle.setTextColor(Color.parseColor("#DBDBDB"));
            holder.textSubtitle.setTextColor(Color.parseColor("#E9A0A0A0"));
            holder.textDateTime.setTextColor(Color.parseColor("#E9A0A0A0"));

        }

        // Бар болса, ескертпе кескінін орнатыңыз
        if (note.getImagePath() != null){
            holder.imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
            holder.imageNote.setVisibility(View.VISIBLE);
        }else {
            holder.imageNote.setVisibility(View.GONE);
        }
    }

    // RecyclerView Көрінісіндегі элементтердің жалпы санын қайтарыңыз
    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Жеке нота элементтеріне арналған ViewHolder класы
    static class  NoteViewHolder extends RecyclerView.ViewHolder{

        // ӘРБІР ескертпе элементі ҮШІН UI элементтері
        TextView textTitle, textSubtitle, textDateTime, item_new;
        RelativeLayout layoutNote;
        RoundedImageView imageNote;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            item_new = itemView.findViewById(R.id.item_new);
            textTitle  = itemView.findViewById(R.id.item_textTitlem);
            textSubtitle  = itemView.findViewById(R.id.item_textSubTitle);
            textDateTime  = itemView.findViewById(R.id.item_textDateTime);
            layoutNote  = itemView.findViewById(R.id.item_layoutNote);
            imageNote  = itemView.findViewById(R.id.item_imageNote);
        }

    }

    public void searchNote(final  String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Егер іздеу кілт сөзі бос болса, барлық жазбаларды бастапқы көзден көрсетіңіз
                if (searchKeyword.trim().isEmpty()) {
                    notes = noteSource;
                }else {
                    // Іздеу кілт сөзіне негізделген жазбаларды сүзу (регистрге сезімтал емес)
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : noteSource) {
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }

                // НЕГІЗГІ ағындағы UI интерфейсін жаңартыңыз
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500); // Іздеуді орындамас бұрын кідірту (қажетінше реттеу)
    }

    public void cancelTimer() {
        if (timer != null){
            timer.cancel();
        }
    }

}
