package com.example.brainf_kconverter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.brainf_kconverter.R;
import com.example.notesapp.data.Note;
import com.example.notesapp.viewmodel.NoteViewModel;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;


public class AddNoteFragment extends Fragment {


    private EditText editTitle, editContent;
    private Button btnSave;
    private NoteViewModel noteViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        editTitle = view.findViewById(R.id.editTitle);
        editContent = view.findViewById(R.id.editContent);
        btnSave = view.findViewById(R.id.btnSave);


        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);


        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();


            if (title.isEmpty()) {
                Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }


            Note note = new Note(title, content, System.currentTimeMillis());
            noteViewModel.insert(note);

            Toast.makeText(getContext(), "Note Saved", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();

        });
    }
}