package com.example.brainf_kconverter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.brainf_kconverter.R;
import com.example.notesapp.data.Note;

import org.jspecify.annotations.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes = new ArrayList<>();
    private final OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);   // <-- new method for long press
    }

    public NotesAdapter(OnNoteClickListener listener) {
        this.listener = listener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note current = notes.get(position);
        holder.textTitle.setText(current.getTitle());
        holder.textDate.setText(formatDate(current.getTimestamp()));

        holder.itemView.setOnClickListener(v -> listener.onNoteClick(current));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onNoteLongClick(current);
            return true; // indicate we consumed the long click
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private String formatDate(long time) {
        return new SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.getDefault())
                .format(new Date(time));
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textNoteTitle);
            textDate = itemView.findViewById(R.id.textNoteDate);
        }
    }
}