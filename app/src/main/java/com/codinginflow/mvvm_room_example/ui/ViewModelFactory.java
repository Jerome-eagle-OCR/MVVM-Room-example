package com.codinginflow.mvvm_room_example.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.codinginflow.mvvm_room_example.repository.NoteRepository;

import org.jetbrains.annotations.NotNull;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private ViewModelFactory factory;

    public ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                factory = new ViewModelFactory(new NoteRepository(App.getInstance()));
            }
        }
        return factory;
    }

    @NonNull
    private final NoteRepository noteRepository;

    public ViewModelFactory(
            @NotNull NoteRepository noteRepository
    ) {
        this.noteRepository = noteRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(noteRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
