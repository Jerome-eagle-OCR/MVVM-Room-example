package com.codinginflow.mvvm_room_example.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codinginflow.mvvm_room_example.databinding.NoteFragmentBinding;
import com.codinginflow.mvvm_room_example.entity.Note;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> mNotes = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        NoteFragmentBinding noteFragmentBinding = NoteFragmentBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteHolder(noteFragmentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NoteAdapter.NoteHolder holder, int position) {
        Note currentNote = mNotes.get(position);
        holder.mTextViewTitle.setText(currentNote.getTitle());
        holder.mTextViewPriority.setText(String.valueOf(currentNote.getPriority()));
        holder.mTextViewDescription.setText(currentNote.getDescription());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAtPostion(int position) {
        return mNotes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private final TextView mTextViewTitle;
        private final TextView mTextViewPriority;
        private final TextView mTextViewDescription;

        public NoteHolder(@NonNull @NotNull NoteFragmentBinding noteFragmentBinding) {
            super(noteFragmentBinding.getRoot());

            mTextViewTitle = noteFragmentBinding.textViewTitle;
            mTextViewPriority = noteFragmentBinding.textViewPriority;
            mTextViewDescription = noteFragmentBinding.textViewDescription;
        }
    }
}
