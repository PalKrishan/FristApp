package com.app.awaaz;

import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.awaaz.Models.RegisterModel;
import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Message;

import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> lastname = new MutableLiveData<>();
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> conform_password = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>(false);

    public RegisterModel userRegister;
    public Context context;

    public RegisterViewModel(RegisterModel userRegister, Context context) {
        this.userRegister = userRegister;
        this.context = context;
    }


    public void registerUser() {
        if (name.getValue() == null || lastname.getValue() == null || username.getValue() == null || email.getValue() == null || password.getValue() == null || conform_password.getValue() == null || name.getValue().isEmpty() || lastname.getValue().isEmpty() || email.getValue().isEmpty() || username.getValue().isEmpty() || password.getValue().isEmpty() || conform_password.getValue().isEmpty()) {
            Message.makeToasts(context, "Please Enter All Details", false);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getValue().toString()).matches()) {
            Message.makeToasts(context, "Please Enter Valid Email", false);
        } else if (!password.getValue().equals(conform_password.getValue())) {
            Message.makeToasts(context, "Please Conform Password", false);
        } else {
            verifyEmail(name.getValue().toString(), lastname.getValue().toString(), username.getValue().toString(), email.getValue().toString(), conform_password.getValue().toString());
        }
    }


    public void verifyEmail(String name, String lastName, String userName, String email, String password) {

        if (loading.getValue().equals(true)) {
            return;
        }

        loading.setValue(true);
        Call<Object> callApi = RetrofitObject.getInstance().getApi().registerUser(name, lastName, email, password, userName, "NotVerified");
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String, Object> responseData = (Map<String, Object>) response.body();
                if (responseData.get("response").equals("EmailPresent")) {
                    Message.makeToasts(context, "Email Is Alredy Registered", false);
                    loading.setValue(false);
                } else if (responseData.get("response").equals("success")) {
                    String otp = responseData.get("otp").toString();
                    Intent gotoVerify = new Intent(context, VerifyCodeActivity.class);
                    gotoVerify.putExtra("firstName", name);
                    gotoVerify.putExtra("lastName", lastName);
                    gotoVerify.putExtra("userName", userName);
                    gotoVerify.putExtra("email", email);
                    gotoVerify.putExtra("password", password);
                    gotoVerify.putExtra("otp", otp);
                    context.startActivity(gotoVerify);
                    Message.makeToasts(context, "Verification Code Send To Given Email", false);
                    loading.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(context, "Something Went Wrong", false);
                loading.setValue(false);
            }
        });
    }

}
