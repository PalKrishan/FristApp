package com.app.awaaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.text.method.CharacterPickerDialog;
import android.view.View;

import com.app.awaaz.Adapters.ViewPageAdapter;
import com.app.awaaz.Models.ViewPageItems;
import com.app.awaaz.Utils.Constance;
import com.app.awaaz.databinding.ActivityOnboardingScreensBinding;

import java.util.ArrayList;
import java.util.List;

public class OnboardingScreens extends AppCompatActivity {

    ViewPageAdapter pageAdapter;
    List<ViewPageItems> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOnboardingScreensBinding onboardingScreensBinding = DataBindingUtil.setContentView(this,R.layout.activity_onboarding_screens);

        int[] imagesId = {R.drawable.first_image,R.drawable.second_image,R.drawable.third_image,R.drawable.fourth_image};
        String[] heading = {getString(R.string.first_heading),getString(R.string.second_heading),getString(R.string.third_heading),getString(R.string.fourth_heading)};
        String[] description = {getString(R.string.first_description),getString(R.string.second_description),getString(R.string.third_description),getString(R.string.fourth_description)};

        //Setting List
        data = new ArrayList<ViewPageItems>();
        for(int i = 0;i < imagesId.length;i++){
            ViewPageItems item = new ViewPageItems(imagesId[i],heading[i],description[i]);
            data.add(item);
        }

        //Setting View Page Adapter
        pageAdapter = new ViewPageAdapter(data,OnboardingScreens.this, onboardingScreensBinding.onboardingPageviewer);

        //Setting adapter onto Viewpager
        onboardingScreensBinding.onboardingPageviewer.setAdapter(pageAdapter);

    }
}