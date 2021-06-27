package com.codinginflow.mvvm_room_example.ui;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinginflow.mvvm_room_example.entity.Note;
import com.codinginflow.mvvm_room_example.repository.NoteRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final NoteRepository noteRepository;
    private final LiveData<List<Note>> allNotes;
    private String intentType;
    private final MutableLiveData<String> intentResultMessage = new MutableLiveData<>();

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

    public LiveData<String> getIntentResultMessage() {
        return intentResultMessage;
    }

    public void setIntentType(String intentType) {
        this.intentType = intentType;
    }

    public void manageNote(Intent data) {
        String[] snackMessage = new String[]{"", ""};
        int snackMessageIndex = 1;
        boolean isUpdate = false;
        switch (intentType) {
            case AddEditNoteActivity.ADD_NOTE:
                snackMessage = new String[]{"New note created", "No new note created"};
                break;
            case AddEditNoteActivity.EDIT_NOTE:
                snackMessage = new String[]{"Note updated", "Note not updated"};
                isUpdate = true;
                break;
        }
        if (data != null) {
            snackMessageIndex = 0;
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            if (isUpdate) {
                int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

                if (id == -1) {
                    intentResultMessage.setValue("Note cannot be updated");
                    return;
                }

                note.setId(id);
                this.update(note);
            } else {
                this.insert(note);
            }
        }
        intentResultMessage.setValue(snackMessage[snackMessageIndex]);
    }
}

