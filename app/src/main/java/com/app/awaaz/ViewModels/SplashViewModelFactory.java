package com.app.awaaz.ViewModels;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.awaaz.SplashScreen;
import com.app.awaaz.SplashViewModel;

public class SplashViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private Activity context;

    public SplashViewModelFactory(SplashScreen context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SplashViewModel(context);
    }
}
