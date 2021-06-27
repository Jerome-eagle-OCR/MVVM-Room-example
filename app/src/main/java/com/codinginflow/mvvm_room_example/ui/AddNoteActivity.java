package com.codinginflow.mvvm_room_example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codinginflow.mvvm_room_example.R;
import com.codinginflow.mvvm_room_example.databinding.ActivityAddNoteBinding;
import com.google.android.material.snackbar.Snackbar;

public class AddNoteActivity extends AppCompatActivity {

    private ActivityAddNoteBinding addNoteBinding;

    public static final String EXTRA_TITLE =
            "com.codinginflow.mvvm_room_example.ui.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.codinginflow.mvvm_room_example.ui.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.codinginflow.mvvm_room_example.ui.EXTRA_PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNoteBinding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(addNoteBinding.getRoot());

        addNoteBinding.numberPickerPriority.setMinValue(1);
        addNoteBinding.numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);
        setTitle("Add Note");
    }

    private void saveNote() {
        String title = addNoteBinding.editTextTitle.getText().toString();
        String description = addNoteBinding.editTextDescription.getText().toString();
        int priority = addNoteBinding.numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Snackbar.make(addNoteBinding.getRoot(),
                    "Please insert a title and a description", Snackbar.LENGTH_SHORT
            ).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}