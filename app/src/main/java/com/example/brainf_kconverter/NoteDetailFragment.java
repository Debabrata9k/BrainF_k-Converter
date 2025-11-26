package com.example.brainf_kconverter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.data.Note;
import com.example.notesapp.viewmodel.NoteViewModel;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;


public class NoteDetailFragment extends Fragment {


    private EditText editTitle, editContent;
    Button btnUpdate;
    AppCompatButton btnShare, btnCopy;
    private NoteViewModel viewModel;
    private int noteId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        editTitle = view.findViewById(R.id.editDetailTitle);
        editContent = view.findViewById(R.id.editDetailContent);
        btnCopy = view.findViewById(R.id.btnCopy);
        btnShare = view.findViewById(R.id.btnShare);
        btnUpdate = view.findViewById(R.id.btnUpdate);


        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);


        if (getArguments() != null) {
            noteId = getArguments().getInt("id");
            String title = getArguments().getString("title");
            String content = getArguments().getString("content");


            editTitle.setText(title);
            editContent.setText(content);
        }


        btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("note", editContent.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
        });
        btnShare.setOnClickListener(v -> {
            String outPUt = editContent.getText().toString();
            if (!outPUt.isEmpty()) {
                Intent iShara = new Intent(Intent.ACTION_SEND);
                iShara.setType("text/plain");
                iShara.putExtra(Intent.EXTRA_TEXT, outPUt);
                startActivity(Intent.createChooser(iShara, "SHARE With"));
            } else {
                Toast.makeText(requireContext(), "No Text Present To Share", Toast.LENGTH_SHORT).show();
            }
        });


        btnUpdate.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString().trim();
            String newContent = editContent.getText().toString().trim();


            if (newTitle.isEmpty()) {
                Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }


            Note updatedNote = new Note(newTitle, newContent, System.currentTimeMillis());
            updatedNote.setId(noteId);
            viewModel.update(updatedNote);

            Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}