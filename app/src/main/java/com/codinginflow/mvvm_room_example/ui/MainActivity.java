package com.codinginflow.mvvm_room_example.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.codinginflow.mvvm_room_example.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);

        mainViewModel.getAllNotes().observe(this, notes -> Toast.makeText(MainActivity.this, notes.get(0).toString(), Toast.LENGTH_LONG).show());
    }
}