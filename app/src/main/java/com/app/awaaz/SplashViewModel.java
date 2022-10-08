package com.app.awaaz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.window.SplashScreenView;

import androidx.lifecycle.ViewModel;

import com.app.awaaz.LoginActivity;
import com.app.awaaz.Utils.Constance;

public class SplashViewModel extends ViewModel {
    private Activity activity;
    SharedPreferences sharedPreferences;
    SharedPreferences alreadyLogin;

    public SplashViewModel(Activity activity) {
        this.activity = activity;
    }

    public void SplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = activity.getSharedPreferences(Constance.FIRST_TIME, Context.MODE_PRIVATE);
                alreadyLogin = activity.getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, Context.MODE_PRIVATE);

                if (sharedPreferences.contains(Constance.FIRST_TIME_KEY) && !alreadyLogin.contains(Constance.USER_UI)) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                } else if (alreadyLogin.contains(Constance.USER_UI)) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    Intent intent = new Intent(activity, OnboardingScreens.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

            }
        }, 2000);
    }

}
