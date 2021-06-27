package com.codinginflow.mvvm_room_example.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codinginflow.mvvm_room_example.databinding.ActivityMainBinding;
import com.codinginflow.mvvm_room_example.entity.Note;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private MainViewModel mainViewModel;

    private Intent mData;
    private ActivityResultLauncher<Intent> startActivityForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);

        setNoteAdapter();

        setStartActivityForResult();

        mainBinding.addNoteBtn.setOnClickListener(v -> {
            Intent addNoteActivityIntent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult.launch(addNoteActivityIntent);
        });
    }

    private void setNoteAdapter() {
        final RecyclerView noteRecyclerView = mainBinding.recyclerView;
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);

                //always scroll to bottom
                noteRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE);
            }
        });
        noteRecyclerView.setHasFixedSize(true);
        final NoteAdapter adapter = new NoteAdapter();
        noteRecyclerView.setAdapter(adapter);
        mainViewModel.getAllNotes().observe(this, adapter::setNotes);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView,
                                  @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note deletedNote = adapter.getNoteAtPostion(position);
                mainViewModel.delete(deletedNote);
                adapter.notifyItemRemoved(position);
                Snackbar.make(mainBinding.getRoot(), "Note deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                                    //insert back previously deleted note
                                    mainViewModel.insert(deletedNote);
                        }
                ).show();
            }
        }).attachToRecyclerView(noteRecyclerView);
    }

    private void setStartActivityForResult() {
        startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        mData = result.getData();
                    } else {
                        mData = null;
                    }
                    insertNote();
                }
        );
    }

    private void insertNote() {
        String snackMessage;
        if (mData != null) {
            String title = mData.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = mData.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = mData.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0);
            Note note = new Note(title, description, priority);
            mainViewModel.insert(note);
            snackMessage = "Note created";
        } else {
            snackMessage = "No note created";
        }
        Snackbar.make(mainBinding.getRoot(), snackMessage, Snackbar.LENGTH_SHORT).show();
    }
}