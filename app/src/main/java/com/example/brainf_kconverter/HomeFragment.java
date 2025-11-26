package com.example.brainf_kconverter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.*;


public class HomeFragment extends Fragment {

    EditText inputEdit;
    TextView outputView;
    RadioButton radioTextToBF, radioBFToText;
    Button convertBtn, clearBtn;
    AppCompatButton copyBtn, btnShare;
    @SuppressLint("UseSwitchCompatOrMaterialCode")

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEdit = view.findViewById(R.id.inputEdit);
        outputView = view.findViewById(R.id.outputView);
        radioTextToBF = view.findViewById(R.id.radioTextToBF);
        radioBFToText = view.findViewById(R.id.radioBFToText);
        convertBtn = view.findViewById(R.id.convertBtn);
        copyBtn = view.findViewById(R.id.copyBtn);
        btnShare = view.findViewById(R.id.btnShare);
        clearBtn = view.findViewById(R.id.clearBtn);

        clearBtn.setVisibility(View.GONE);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Exit app")
                                .setMessage("Do you really want to exit?")
                                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .setPositiveButton("Exit", (dialog, which) -> requireActivity().finish())
                                .setCancelable(true)
                                .show();
                    }
                }
        );

        convertBtn.setOnClickListener(view1 -> {
            String input = inputEdit.getText().toString();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(requireContext(), "Enter input first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (radioTextToBF.isChecked()) {
                String bf;
                bf = UltraBFEncoderV2.encode(input);
                outputView.setText(bf);
            } else {
                String out;
                try {
                    out = interpretBrainfuck(input);
                } catch (Exception e) {
                    out = "Error interpreting Brainfuck: " + e.getMessage();
                }
                outputView.setText(out);
            }
            clearBtn.setVisibility(View.VISIBLE);
        });

        copyBtn.setOnClickListener(view12 -> {
            String out = outputView.getText().toString();
            if (!TextUtils.isEmpty(out)) {
                ClipboardManager cm = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("output", out);
                if (cm != null) cm.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Nothing to copy", Toast.LENGTH_SHORT).show();
            }
        });

        btnShare.setOnClickListener(v -> {
            String outPUt = outputView.getText().toString();
            if (!outPUt.isEmpty()) {
                Intent iShara = new Intent(Intent.ACTION_SEND);
                iShara.setType("text/plain");
                iShara.putExtra(Intent.EXTRA_TEXT, outPUt);
                startActivity(Intent.createChooser(iShara, "SHARE With"));
            } else {
                Toast.makeText(requireContext(), "No Text Present To Share", Toast.LENGTH_SHORT).show();
            }
        });

        clearBtn.setOnClickListener(view13 -> {
            inputEdit.setText("");
            outputView.setText("");
        });
    }
    /* ----------------- ULTRA OPTIMIZED BF ENCODER V2 ----------------- */

    public static class UltraBFEncoderV2 {

        private static class Factor {
            int a,b,r,cost;
            Factor(int a,int b,int r,int cost){this.a=a;this.b=b;this.r=r;this.cost=cost;}
        }

        public static String encode(String text){
            StringBuilder bf=new StringBuilder();
            int cur=0;

            for(char ch:text.toCharArray()){
                int target=ch&0xFF;
                int diff=(target-cur+256)%256;

                if(Math.abs(diff)<=20||Math.abs(256-diff)<=20){
                    applyDelta(diff,bf);
                    bf.append('.');
                    cur=target;
                    continue;
                }

                Factor f=bestFactor(diff);

                if(Math.abs(diff)>f.cost)
                    bf.append("[-]");

                repeat(bf,'+',f.a);
                bf.append("[>");
                repeat(bf,'+',f.b);
                bf.append("<-]>");

                if(f.r>0) repeat(bf,'+',f.r);

                bf.append('.');
                cur=target;
            }
            return optimize(bf.toString());
        }

        private static void applyDelta(int diff,StringBuilder bf){
            if(diff<=128) repeat(bf,'+',diff);
            else repeat(bf,'-',256-diff);
        }

        private static Factor bestFactor(int v){
            int best=Integer.MAX_VALUE;
            Factor bestF=new Factor(v,1,0,v);

            for(int a=2;a<=Math.sqrt(v)+20;a++){
                for(int b=2;b<=Math.sqrt(v)+20;b++){
                    int prod=a*b;
                    int r=v-prod;
                    if(r<0) continue;
                    int cost=a+b+r+6;
                    if(cost<best){
                        best=cost;
                        bestF=new Factor(a,b,r,cost);
                    }
                }
            }
            return bestF;
        }

        private static String optimize(String code){
            return code.replace("+-","")
                    .replace("-+","")
                    .replace("<>","")
                    .replace("><","");
        }

        private static void repeat(StringBuilder sb,char c,int n){
            for(int i=0;i<n;i++) sb.append(c);
        }
    }


    /* ---------------- Brainfuck Interpreter (unchanged) ---------------- */

    private String interpretBrainfuck(String code) throws Exception {
        Map<Integer, Integer> bracketMap = buildBracketMap(code);
        final int CELL_SIZE = 30000;
        int[] cells = new int[CELL_SIZE];
        int ptr = 0;
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            switch (c) {
                case '>': ptr++; if (ptr >= CELL_SIZE) ptr = 0; break;
                case '<': ptr--; if (ptr < 0) ptr = CELL_SIZE - 1; break;
                case '+': cells[ptr] = (cells[ptr] + 1) & 0xFF; break;
                case '-': cells[ptr] = (cells[ptr] - 1) & 0xFF; break;
                case '.': output.append((char) (cells[ptr] & 0xFF)); break;
                case '[':
                    if (cells[ptr] == 0) i = bracketMap.get(i);
                    break;
                case ']':
                    if (cells[ptr] != 0) i = bracketMap.get(i);
                    break;
            }
            if (output.length() > 20000) {
                output.append("\n[Output truncated]");
                break;
            }
        }
        return output.toString();
    }

    private Map<Integer, Integer> buildBracketMap(String code) throws Exception {
        Map<Integer, Integer> map = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '[') stack.push(i);
            else if (c == ']') {
                if (stack.isEmpty()) throw new Exception("Unmatched ] at position " + i);
                int openIndex = stack.pop();
                map.put(openIndex, i);
                map.put(i, openIndex);
            }
        }
        if (!stack.isEmpty()) throw new Exception("Unmatched [ at position " + stack.pop());
        return map;
    }
}
