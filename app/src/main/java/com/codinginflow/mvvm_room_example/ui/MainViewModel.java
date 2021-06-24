package com.codinginflow.mvvm_room_example.ui;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.codinginflow.mvvm_room_example.entity.Note;
import com.codinginflow.mvvm_room_example.repository.NoteRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;

    public MainViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
        allNotes = noteRepository.getAllNotes();
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }

    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public void deleteAllNotes() {
        noteRepository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}

