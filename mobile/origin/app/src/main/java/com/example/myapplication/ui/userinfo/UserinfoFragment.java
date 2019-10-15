package com.example.myapplication.ui.userinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.activity.LogActivity;

import static android.content.Context.MODE_PRIVATE;

public class UserinfoFragment extends Fragment {

    private UserinfoViewModel userinfoViewModel;

    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userinfoViewModel =
                ViewModelProviders.of(this).get(UserinfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_userinfo, container, false);
//        final TextView textView = root.findViewById(R.id.text_userinfo);
//        userinfoViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
//            @Override
//            public void onChanged(@androidx.annotation.Nullable String s) {
//                textView.setText(s);
//            }
//        });

        final SharedPreferences sharedPreferences=this.getContext().getSharedPreferences("User",MODE_PRIVATE);

        final EditText userInfoUsernameET = root.findViewById(R.id.userInfoUsername);
        userinfoViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable String s) {
                userInfoUsernameET.setText(sharedPreferences.getString("username",""));
            }
        });
        final EditText userInfoNameET = root.findViewById(R.id.userInfoName);
        userinfoViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable String s) {
                userInfoNameET.setText(sharedPreferences.getString("name",""));
            }
        });
        final EditText userInfoTelET = root.findViewById(R.id.userInfoTel);
        userinfoViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable String s) {
                userInfoTelET.setText(sharedPreferences.getString("tel",""));
            }
        });
        final EditText userInfoEmailET = root.findViewById(R.id.userInfoEmail);
        userinfoViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable String s) {
                userInfoEmailET.setText(sharedPreferences.getString("email",""));
            }
        });


//        Array Relative=sharedPreferences.getString("relavice","")
        final EditText userInfoRelative1ET = root.findViewById(R.id.userInfoRelative1);
        userinfoViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable String s) {

                userInfoRelative1ET.setText("小黑");
            }
        });
        final EditText userInfoRelative2ET = root.findViewById(R.id.userInfoRelative2);
        userinfoViewModel.getText().observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable String s) {
                userInfoRelative2ET.setText("小白");
            }
        });

        final Button logoutBT=root.findViewById(R.id.logout);
        logoutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();//清除登录信息
                Intent intent=new Intent(getActivity(), LogActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

        });



//        userInfoUsernameET.setText(sharedPreferences.getString("username",""));
//
//        final EditText userInfoTelET = root.findViewById(R.id.userInfoTel);
//        userInfoTelET.setText(sharedPreferences.getString("tel",""));
//
//        final EditText userInfoEmailET = root.findViewById(R.id.userInfoEmail);
//        userInfoEmailET.setText(sharedPreferences.getString("email",""));

        return root;
    }

    public void ChangeUserinfo(String sort,String data){

    }
}