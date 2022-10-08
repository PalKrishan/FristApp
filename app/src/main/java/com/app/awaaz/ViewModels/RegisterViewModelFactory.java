package com.app.awaaz.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.awaaz.Models.RegisterModel;
import com.app.awaaz.RegisterViewModel;

public class RegisterViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private Context context;
    private RegisterModel registerViewModel;


    public RegisterViewModelFactory(Context context, RegisterModel registerViewModel) {
        this.context = context;
        this.registerViewModel = registerViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RegisterViewModel(registerViewModel,context);
    }
}
