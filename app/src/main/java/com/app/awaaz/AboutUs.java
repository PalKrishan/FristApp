package com.app.awaaz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutUs extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_about_us, container, false);
       v.findViewById(R.id.aboutus_backbutton).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if ( getFragmentManager().getBackStackEntryCount() > 0)
               {
                   getFragmentManager().popBackStack();
                   return;
               }
           }
       });

        return v;
    }
}