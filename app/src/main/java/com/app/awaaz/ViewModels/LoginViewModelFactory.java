package com.app.awaaz.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.awaaz.LoginActivity;
import com.app.awaaz.LoginViewModel;
import com.app.awaaz.Models.LoginModel;

public class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Context context;
    private LoginModel user;

    public LoginViewModelFactory(LoginActivity context,LoginModel user){
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(user,context);
    }
}
