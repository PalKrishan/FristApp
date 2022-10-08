package com.app.awaaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.awaaz.ViewModels.SplashViewModelFactory;
import com.app.awaaz.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    SplashViewModel splashViewModel;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashScreenBinding splashScreenBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen);

        splashViewModel = new ViewModelProvider(this,(ViewModelProvider.Factory) new SplashViewModelFactory(SplashScreen.this)).get(SplashViewModel.class);
        splashScreenBinding.setSplash(splashViewModel);

        //Setting Animation on Splash Text
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        splashScreenBinding.splashTextView.setAnimation(animation);

        //Splash Waiting
        splashViewModel.SplashScreen();
    }
}