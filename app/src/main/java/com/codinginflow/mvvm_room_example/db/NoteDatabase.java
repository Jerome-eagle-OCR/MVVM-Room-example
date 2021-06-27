package com.codinginflow.mvvm_room_example.db;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.codinginflow.mvvm_room_example.dao.NoteDao;
import com.codinginflow.mvvm_room_example.MainApplication;
import com.codinginflow.mvvm_room_example.entity.Note;

import org.jetbrains.annotations.NotNull;

@Database(entities = {Note.class}, exportSchema = false, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static NoteDatabase instance;
    private static final String DB_NAME = "note_database";

    public static NoteDatabase getInstance() {
        if (instance == null) {
            synchronized (NoteDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            MainApplication.getInstance(),
                            NoteDatabase.class,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback
                    ).build();
                }
            }
        }

        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title1", "Description1", 1));
            noteDao.insert(new Note("Title2", "Description2", 2));
            noteDao.insert(new Note("Title3", "Description3", 3));
            return null;
        }
    }
}
