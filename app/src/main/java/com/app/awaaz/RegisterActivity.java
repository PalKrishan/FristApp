package com.app.awaaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.awaaz.Models.LoginModel;
import com.app.awaaz.Models.RegisterModel;
import com.app.awaaz.ViewModels.LoginViewModelFactory;
import com.app.awaaz.ViewModels.RegisterViewModelFactory;
import com.app.awaaz.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        //Initilizing ViewModel Class
        registerViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new RegisterViewModelFactory(RegisterActivity.this, new RegisterModel())).get(RegisterViewModel.class);
        binding.setRegister(registerViewModel);

        registerViewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true) {
                    binding.registerVerifyText.setVisibility(View.INVISIBLE);
                    binding.registerVerifyLoding.setVisibility(View.VISIBLE);
                } else if (aBoolean == false) {
                    binding.registerVerifyText.setVisibility(View.VISIBLE);
                    binding.registerVerifyLoding.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void Back(View v) {
        finish();
    }
}