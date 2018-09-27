package com.nickagas.architerctureexample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();

    }

    public void insert(Note note) {
        new  InsertNoteAsyncTask(noteDao).execute(note);
    }
    public  void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public  void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public  void deleteAll() {
        new DeleteAllAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private static class  InsertNoteAsyncTask extends AsyncTask<Note,Void,Void> {
        private NoteDao notedao;

        private InsertNoteAsyncTask(NoteDao notedao) {
            this.notedao = notedao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            notedao.insert(notes[0]);
            return null;
        }
    }
        private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
            private NoteDao notedao;

            private UpdateNoteAsyncTask(NoteDao notedao) {
                this.notedao = notedao;
            }

            @Override
            protected Void doInBackground(Note... notes) {
                notedao.update(notes[0]);
                return null;
            }
        }

        private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
            private NoteDao notedao;

            private DeleteNoteAsyncTask(NoteDao notedao) {
                this.notedao = notedao;
            }

            @Override
            protected Void doInBackground(Note... notes) {
                notedao.delete(notes[0]);
                return null;
            }
        }

        private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
            private NoteDao notedao;

            private DeleteAllAsyncTask(NoteDao notedao) {
                this.notedao = notedao;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                notedao.deleteAllNotes();
                return null;
            }
        }

}

