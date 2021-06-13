package com.cc.globenewslive.Fragments;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.SharedPreference;


public class Profile extends Fragment {
    TextView email,username,logout;
    private ImageView imageView;
    private ImageButton drawerr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreference sharedPreference=new SharedPreference(getActivity());
        email=view.findViewById(R.id.EmailProfileFrag);
        username=view.findViewById(R.id.usernameprofilefrag);
        logout=view.findViewById(R.id.Logout);
        imageView=view.findViewById(R.id.backbutton);
        drawerr=view.findViewById(R.id.opendrawer);
        if(sharedPreference.LoggedIN()){
            username.setText(sharedPreference.getUserName());
            email.setText(sharedPreference.getUserEmail());
            logout.setVisibility(View.VISIBLE);
            logout.setText("LOG OUT");
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                MainActivity.webviewFragment.setVisibility(View.GONE);
                sharedPreference.LOGOUT(MainActivity.navigationView);
                logout.setVisibility(View.GONE);

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                    MainActivity.webviewFragment.setVisibility(View.GONE);
            }
        });
        drawerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        return view;
    }
}