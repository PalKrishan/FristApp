package com.app.awaaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Constance;
import com.app.awaaz.Utils.Message;
import com.app.awaaz.databinding.ActivityVerifyCodeBinding;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCodeActivity extends AppCompatActivity {

    String fname, lname, email, userName, password, otp;
    boolean stop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerifyCodeBinding verifyCodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_code);

        fname = getIntent().getStringExtra("firstName");
        lname = getIntent().getStringExtra("lastName");
        userName = getIntent().getStringExtra("userName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        otp = getIntent().getStringExtra("otp");

        verifyCodeBinding.verifycodeTextview.setText("Please Enter Six Digit Code Send To this "+email+ " Email Address");

        verifyCodeBinding.verifycodeVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("length"+verifyCodeBinding.verifycodePinView.getText().toString().length());
                if(verifyCodeBinding.verifycodePinView.getText().toString().length() == 5){
                    Message.makeToasts(VerifyCodeActivity.this,"Please Enter Full Code",false);
                }else if(!verifyCodeBinding.verifycodePinView.getText().toString().equals(otp)){
                    Message.makeToasts(VerifyCodeActivity.this,"Please Enter Correct OTP",false);
                }else{
                    registerUser(fname,lname,userName,email,password,verifyCodeBinding);
                }
            }
        });


    }


    public void registerUser(String name, String lastName, String userName, String email, String password, ActivityVerifyCodeBinding v) {

        if(stop == true){
            return;
        }

        v.verifycodeVerifyText.setVisibility(View.INVISIBLE);
        v.verifycodeVerifyLoding.setVisibility(View.VISIBLE);

        Call<Object> callApi = RetrofitObject.getInstance().getApi().registerUser(name, lastName, email, password, userName, "Verified");
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String, Object> responseData = (Map<String, Object>) response.body();
                if (responseData.get("response").equals("success")) {
                    Message.makeToasts(VerifyCodeActivity.this, "Register Success", true);
                    SharedPreferences sharedPreferences = getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constance.USER_ID, responseData.get("id").toString());
                    editor.putString(Constance.USER_UI, responseData.get("user_name").toString());
                    editor.putString(Constance.USER_PS, responseData.get("password").toString());
                    editor.putString(Constance.USER_FN, responseData.get("first_name").toString());
                    editor.putString(Constance.USER_LN, responseData.get("last_name").toString());
                    editor.putString(Constance.USER_EM, responseData.get("email").toString());
                    editor.putString(Constance.USER_PP, responseData.get("profile_pic").toString());
                    editor.commit();

                    v.verifycodeVerifyText.setVisibility(View.VISIBLE);
                    v.verifycodeVerifyLoding.setVisibility(View.INVISIBLE);

                    Intent gotoDashboard = new Intent(VerifyCodeActivity.this, MainActivity.class);
                    startActivity(gotoDashboard);
                    finish();
                    stop = false;
                } else if (responseData.get("response").equals("error")) {
                    Message.makeToasts(VerifyCodeActivity.this, "Error Occur", true);
                    v.verifycodeVerifyText.setVisibility(View.VISIBLE);
                    v.verifycodeVerifyLoding.setVisibility(View.INVISIBLE);
                    stop = false;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(VerifyCodeActivity.this, "Something Went Wrong", false);
                v.verifycodeVerifyText.setVisibility(View.VISIBLE);
                v.verifycodeVerifyLoding.setVisibility(View.INVISIBLE);
                stop = false;
            }
        });
    }

    public void Back(View v) {
        finish();
    }
}