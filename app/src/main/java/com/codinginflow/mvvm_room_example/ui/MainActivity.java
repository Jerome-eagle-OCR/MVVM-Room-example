package com.codinginflow.mvvm_room_example.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codinginflow.mvvm_room_example.R;
import com.codinginflow.mvvm_room_example.databinding.ActivityMainBinding;
import com.codinginflow.mvvm_room_example.entity.Note;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private MainViewModel mainViewModel;

    private ActivityResultLauncher<Intent> startActivityForResult;
    private Intent mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);

        setNoteAdapter();

        setFabOnClickListener();

        setStartActivityForResult();
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

        setItemTouchHelper(noteRecyclerView, adapter);

        adapter.setOnItemClickListener(note -> {
            mainViewModel.setIntentType(AddEditNoteActivity.EDIT_NOTE);
            Intent editNoteActivityIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            editNoteActivityIntent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            editNoteActivityIntent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            editNoteActivityIntent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            editNoteActivityIntent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
            startActivityForResult.launch(editNoteActivityIntent);
        });
    }

    private void setFabOnClickListener() {
        mainBinding.addNoteBtn.setOnClickListener(v -> {
            mainViewModel.setIntentType(AddEditNoteActivity.ADD_NOTE);
            Intent addNoteActivityIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivityForResult.launch(addNoteActivityIntent);
        });
    }

    private void setItemTouchHelper(RecyclerView noteRecyclerView, NoteAdapter adapter) {
        ColorDrawable swipeLftBkgnd = new ColorDrawable(getResources().getColor(android.R.color.holo_red_light));
        ColorDrawable swipeRghtBkgnd = new ColorDrawable(getResources().getColor(R.color.teal_200));
        Drawable deleteIcon = AppCompatResources.getDrawable(this, R.drawable.ic_delete_24);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView,
                                  @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull @NotNull RecyclerView.ViewHolder target
            ) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    Note deletedNote = adapter.getNoteAtPosition(position);
                    mainViewModel.delete(deletedNote);
                    adapter.notifyItemRemoved(position);
                    String snackMessage = "\"" + deletedNote.getTitle() + "\" note deleted";
                    Snackbar.make(viewHolder.itemView, snackMessage, Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> mainViewModel.insert(deletedNote))
                            .show();
                }
            }

            @Override
            public void onChildDraw(@NonNull @NotNull Canvas c,
                                    @NonNull @NotNull RecyclerView recyclerView,
                                    @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive
            ) {
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                if (dX > 0) {
                    swipeRghtBkgnd.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + (int) dX, itemView.getBottom()
                    );
                    swipeRghtBkgnd.draw(c);
                } else {
                    swipeLftBkgnd.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom()
                    );
                    deleteIcon.setBounds(itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth(),
                            itemView.getTop() + iconMargin,
                            itemView.getRight() - iconMargin, itemView.getBottom() - iconMargin
                    );
                    swipeLftBkgnd.draw(c);
                    deleteIcon.draw(c);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX / 5f, dY, actionState, isCurrentlyActive);
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
                    mainViewModel.manageNote(mData);
                    snackbarObserver();
                }
        );
    }

    private void snackbarObserver() {
        mainViewModel.getIntentResultMessage().observe(this, s -> Snackbar.make(mainBinding.getRoot(), s, Snackbar.LENGTH_SHORT).show());
    }
}