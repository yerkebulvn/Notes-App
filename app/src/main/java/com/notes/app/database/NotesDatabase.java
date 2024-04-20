package com.notes.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.notes.app.NoteDao.NoteDao;
import com.notes.app.entities.Note;

// Жазбаларды басқаруға Арналған Room дерекқорының класын анықтаy
@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    protected static NotesDatabase notesDatabase;

    public static synchronized NotesDatabase getNotesDatabase(Context context){
        if(notesDatabase == null){
            // Room ' s database Builder көмегімен мәліметтер базасын құрy
            notesDatabase = Room.databaseBuilder(
                    context,
                    NotesDatabase.class,
                    "notes_db"  // Деректер базасының атауы
            ).build();
        }
        return notesDatabase;
    }

    // NoteDao интерфейсіне қол жеткізуді қамтамасыз етудің дерексіз әдісі
    public abstract NoteDao noteDao();
}
