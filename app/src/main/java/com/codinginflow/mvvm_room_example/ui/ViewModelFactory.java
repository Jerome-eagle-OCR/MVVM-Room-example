package com.codinginflow.mvvm_room_example.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.codinginflow.mvvm_room_example.repository.NoteRepository;

import org.jetbrains.annotations.NotNull;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory sFactory;

    @NonNull
    private final NoteRepository mNoteRepository;

    private ViewModelFactory(@NotNull NoteRepository noteRepository) {
        mNoteRepository = noteRepository;
    }

    public static ViewModelFactory getInstance() {
        if (sFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (sFactory == null) {
                    sFactory = new ViewModelFactory(new NoteRepository());
                }
            }
        }
        return sFactory;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mNoteRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
