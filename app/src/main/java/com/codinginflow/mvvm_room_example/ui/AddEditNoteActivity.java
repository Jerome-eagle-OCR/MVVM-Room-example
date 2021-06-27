package com.codinginflow.mvvm_room_example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codinginflow.mvvm_room_example.R;
import com.codinginflow.mvvm_room_example.databinding.ActivityAddNoteBinding;
import com.google.android.material.snackbar.Snackbar;

public class AddEditNoteActivity extends AppCompatActivity {

    private ActivityAddNoteBinding addEditNoteBinding;

    public static final String EXTRA_ID =
            "com.codinginflow.mvvm_room_example.ui.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.codinginflow.mvvm_room_example.ui.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.codinginflow.mvvm_room_example.ui.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.codinginflow.mvvm_room_example.ui.EXTRA_PRIORITY";
    public static final  String EDIT_NOTE = "Edit note";
    public static final  String ADD_NOTE = "Add note";

    int mId;
    EditText mTitle;
    EditText mDescription;
    NumberPicker mPriority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addEditNoteBinding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(addEditNoteBinding.getRoot());

        initActivity();
    }

    private void initActivity() {
        Intent intent = getIntent();

        mTitle = addEditNoteBinding.editTextTitle;
        mDescription = addEditNoteBinding.editTextDescription;

        mPriority = addEditNoteBinding.numberPickerPriority;
        mPriority.setMinValue(1);
        mPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24);

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(EDIT_NOTE);
            mId = intent.getIntExtra(EXTRA_ID, -1);
            mTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            mDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            mPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle(ADD_NOTE);
        }
    }

    private void saveNote() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        int priority = mPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Snackbar.make(addEditNoteBinding.getRoot(),
                    "Please insert a title and a description", Snackbar.LENGTH_SHORT
            ).show();
            return;
        }

        Intent data = new Intent();
        if (mId != -1) data.putExtra(EXTRA_ID, mId);
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