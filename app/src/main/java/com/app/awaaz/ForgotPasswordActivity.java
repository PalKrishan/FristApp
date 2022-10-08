package com.app.awaaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Message;
import com.app.awaaz.databinding.ActivityForgotPasswordBinding;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityForgotPasswordBinding forgotPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);

        forgotPasswordBinding.forgotpasswordSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forgotPasswordBinding.forgotpasswordInputEmail.getText().toString().equals("")){
                    Message.makeToasts(ForgotPasswordActivity.this,"Please Enter Email",false);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(forgotPasswordBinding.forgotpasswordInputEmail.getText().toString()).matches()){
                    Message.makeToasts(ForgotPasswordActivity.this,"Please Enter Valid Email",false);
                }else{
                    sendPassword(forgotPasswordBinding.forgotpasswordInputEmail.getText().toString(),forgotPasswordBinding);
                }
            }
        });
    }

    public void sendPassword(String email,ActivityForgotPasswordBinding v){
        v.forgotpasswordSentText.setVisibility(View.INVISIBLE);
        v.forgotpasswordSentLoding.setVisibility(View.VISIBLE);

        Call<Object> callApi = RetrofitObject.getInstance().getApi().sentPassword(email);
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String,Object> responseData = (Map<String, Object>) response.body();
                if(responseData.get("done").equals("success")){
                    Message.makeToasts(ForgotPasswordActivity.this,"Username & Password Has Been Sent To The Given Email",true);
                    v.forgotpasswordSentText.setVisibility(View.VISIBLE);
                    v.forgotpasswordSentLoding.setVisibility(View.INVISIBLE);
                    finish();
                }else if(responseData.get("done").equals("NotPresent")){
                    Message.makeToasts(ForgotPasswordActivity.this,"Given Email Is Not Registered",false);
                    v.forgotpasswordSentText.setVisibility(View.VISIBLE);
                    v.forgotpasswordSentLoding.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(ForgotPasswordActivity.this,"Something Went Wrong",false);
                v.forgotpasswordSentText.setVisibility(View.VISIBLE);
                v.forgotpasswordSentLoding.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void Back(View v){
        finish();
    }
}