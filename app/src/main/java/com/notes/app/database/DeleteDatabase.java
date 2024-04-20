package com.notes.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.notes.app.NoteDao.NoteDao;
import com.notes.app.entities.Note;

// Жазбаларды басқаруға Арналған Room дерекқорының класын анықтаy
@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class DeleteDatabase extends RoomDatabase {

    // Деректер базасының синглтон данасы
    protected static DeleteDatabase deleteDatabase;

    // Деректер базасының данасын алy (синглтон үлгісі)
    public static synchronized DeleteDatabase getNotesDatabase(Context context){
        if(deleteDatabase == null){
            // Room's database Builder көмегімен мәліметтер базасын өшіру
            deleteDatabase = Room.databaseBuilder(
                    context,
                    DeleteDatabase.class,
                    "delete_db" // Деректер базасының атауы
            ).build();
        }
        return deleteDatabase;
    }

    //NoteDao интерфейсіне қол жеткізуді қамтамасыз етудің дерексіз әдісі
    public abstract NoteDao noteDao();
}
