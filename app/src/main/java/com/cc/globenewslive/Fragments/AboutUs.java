package com.cc.globenewslive.Fragments;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.R;

public class AboutUs extends Fragment {
    private ImageView imageView;
    private ImageButton drawerr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about_us, container, false);
        imageView=view.findViewById(R.id.backbutton);
        drawerr=view.findViewById(R.id.opendrawer);
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