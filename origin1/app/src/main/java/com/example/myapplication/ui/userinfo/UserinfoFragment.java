package com.example.myapplication.ui.userinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.activity.LogActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.api_tools;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

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

        final SharedPreferences sharedPreferences=getContext().getSharedPreferences("User",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
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


        final Button userInfoUpdateBT = root.findViewById(R.id.userInfoUpdateBT);
        userInfoUpdateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_tools apiTools=new api_tools();
                Map<String,String> map = new HashMap<>();
                String params="{\""+userInfoUsernameET.getTag().toString()+"\":\""+userInfoUsernameET.getText().toString()+"\","
                        + "\""+userInfoNameET.getTag().toString()+"\":\""+userInfoNameET.getText().toString()+"\","
                        + "\""+userInfoTelET.getTag().toString()+"\":\""+userInfoTelET.getText().toString()+"\","
                        + "\""+userInfoEmailET.getTag().toString()+"\":\""+userInfoEmailET.getText().toString()+"\","
                        + "\""+userInfoRelative1ET.getTag().toString()+"\":\""+userInfoRelative1ET.getText().toString()+"\","
                        + "\""+userInfoRelative2ET.getTag().toString()+"\":\""+userInfoRelative2ET.getText().toString()+"\"}";
                String filter="{\"_id\":\""+sharedPreferences.getString("_id","")+"\"}";
                map.put("filter",filter);
                map.put("params",params);

                apiTools.updateUser(getActivity(),map);

                editor.putString(userInfoUsernameET.getTag().toString(),userInfoUsernameET.getText().toString());
                editor.putString(userInfoNameET.getTag().toString(),userInfoNameET.getText().toString());
                editor.putString(userInfoTelET.getTag().toString(),userInfoTelET.getText().toString());
                editor.putString(userInfoEmailET.getTag().toString(),userInfoEmailET.getText().toString());
                editor.putString(userInfoRelative1ET.getTag().toString(),userInfoRelative1ET.getText().toString());
                editor.putString(userInfoRelative2ET.getTag().toString(),userInfoRelative2ET.getText().toString());
                editor.apply();
            }
        });

//        userInfoUsernameET.addTextChangedListener(new myTextWatcher(this.getActivity(),sharedPreferences.getString("_id",""),userInfoUsernameET));
//        userInfoNameET.addTextChangedListener(new myTextWatcher(this.getActivity(),sharedPreferences.getString("_id",""),userInfoNameET));
//        userInfoTelET.addTextChangedListener(new myTextWatcher(this.getActivity(),sharedPreferences.getString("_id",""),userInfoTelET));
//        userInfoEmailET.addTextChangedListener(new myTextWatcher(this.getActivity(),sharedPreferences.getString("_id",""),userInfoEmailET));
//        userInfoRelative1ET.addTextChangedListener(new myTextWatcher(this.getActivity(),sharedPreferences.getString("_id",""),userInfoRelative1ET));
//        userInfoRelative2ET.addTextChangedListener(new myTextWatcher(this.getActivity(),sharedPreferences.getString("_id",""),userInfoRelative2ET));


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