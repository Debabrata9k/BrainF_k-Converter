package com.example.brainf_kconverter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brainf_kconverter.R;
import com.example.notesapp.data.Note;
import com.example.notesapp.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class NotesListFragment extends Fragment implements NotesAdapter.OnNoteClickListener {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        fab = view.findViewById(R.id.fabAddNote);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> adapter.setNotes(notes));

        fab.setOnClickListener(v -> {
            // if you're using navigation component, use NavHostFragment.
            // If you're using FragmentManager replace with container id:
            AddNoteFragment fragment = new AddNoteFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)   // use your container ID
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onNoteClick(Note note) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", note.getId());
        bundle.putString("title", note.getTitle());
        bundle.putString("content", note.getContent());
        bundle.putLong("timestamp", note.getTimestamp());

        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNoteLongClick(Note note) {
        // show a confirmation dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete note")
                .setMessage("Are you sure you want to delete this note?")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Delete", (dialog, which) -> {
                    noteViewModel.delete(note);
                    Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }
}