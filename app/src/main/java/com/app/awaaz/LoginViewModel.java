package com.app.awaaz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.app.awaaz.Models.LoginModel;
import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Constance;
import com.app.awaaz.Utils.Message;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> userEmail = new MutableLiveData<String>();
    public MutableLiveData<String> userPassword = new MutableLiveData<String>();
    public MutableLiveData<Boolean> load = new MutableLiveData<>(false);


    public LoginModel user;
    public Context context;

    public LoginViewModel(LoginModel user, Context context) {
        this.user = user;
        this.context = context;
    }


    public void login() {
        user.setUserEmail(userEmail.getValue());
        user.setPassword(userPassword.getValue());

        if (user.getUserEmail() == null || user.getPassword() == null || user.getUserEmail().equals("") || user.getPassword().equals("")) {
            Message.makeToasts(context, "Please Enter All Details", false);
        } else {
            loginUser(user.getUserEmail(), user.getPassword());
        }
    }

    private void loginUser(String userEmail, String password) {
        if (load.getValue().equals(true)) {
            return;
        }
        load.setValue(true);
        Call<Object> callApi = RetrofitObject.getInstance().getApi().loginUserData(userEmail, password);
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String, Object> userData = (Map<String, Object>) response.body();
                if (userData.get("response").equals("done")) {
                    Message.makeToasts(context, "Login Success", true);
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constance.USER_ID, userData.get("id").toString());
                    editor.putString(Constance.USER_UI, userData.get("user_name").toString());
                    editor.putString(Constance.USER_PS, userData.get("password").toString());
                    editor.putString(Constance.USER_FN, userData.get("first_name").toString());
                    editor.putString(Constance.USER_LN, userData.get("last_name").toString());
                    editor.putString(Constance.USER_EM, userData.get("email").toString());
                    editor.putString(Constance.USER_PP, userData.get("profile_pic").toString());
                    editor.commit();

                    Intent gotoDashboard = new Intent(context, MainActivity.class);
                    context.startActivity(gotoDashboard);
                    load.setValue(false);
                    ((Activity) context).finish();
                } else if (userData.get("response").equals("error")) {
                    Message.makeToasts(context, "Either Username Or Password Is Wrong", false);
                    load.setValue(false);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(context, "Something Went Wrong Please Check Internet Connection", false);
                load.setValue(false);
            }
        });
    }

}
