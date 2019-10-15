package com.example.myapplication.ui.position;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;


public class PositionFragment extends Fragment {

    private PositionViewModel positionViewModel;

    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        positionViewModel =
                ViewModelProviders.of(this).get(PositionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_position, container, false);
        final TextView textView = root.findViewById(R.id.text_position);
        positionViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}