package com.app.awaaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.awaaz.Models.LoginModel;
import com.app.awaaz.ViewModels.LoginViewModelFactory;
import com.app.awaaz.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        //Initilizing ViewModel Class
        loginViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new LoginViewModelFactory(LoginActivity.this, new LoginModel())).get(LoginViewModel.class);
        loginBinding.setLogin(loginViewModel);


        loginViewModel.load.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true) {
                    loginBinding.loginLoginText.setVisibility(View.INVISIBLE);
                    loginBinding.loginLoginLoding.setVisibility(View.VISIBLE);
                } else if (aBoolean== false) {
                    loginBinding.loginLoginText.setVisibility(View.VISIBLE);
                    loginBinding.loginLoginLoding.setVisibility(View.INVISIBLE);
                }
            }
        });


        loginBinding.loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoRegisterAvtivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(gotoRegisterAvtivity);
            }
        });

        loginBinding.loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoForgotPasswordActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(gotoForgotPasswordActivity);
            }
        });
    }
}