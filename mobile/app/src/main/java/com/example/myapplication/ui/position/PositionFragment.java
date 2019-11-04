package com.example.myapplication.ui.position;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.FallDetection.FallActivity;
import com.example.myapplication.R;


public class PositionFragment extends Fragment {

    private Switch mswitch;
    private static final String TAG = "!!!!!!dujuke:FallActivity!!!!!!";
    private PositionViewModel positionViewModel;
    private ServiceConnection conn;
    private Button mbtbeginfall;

    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        positionViewModel = ViewModelProviders.of(this).get(PositionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_position, container, false);
        final Button btloc = root.findViewById(R.id.btn_locate);
        mbtbeginfall=root.findViewById(R.id.bt_beginfall);
        btloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationMarkerActivity.class);
                startActivityForResult(intent, 0x11);
            }
        });

        mbtbeginfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FallActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }
}