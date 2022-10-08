package com.app.awaaz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Constance;
import com.app.awaaz.Utils.Message;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends Fragment {

    EditText newPass,confPass;
    TextView changeButtonText;
    ProgressBar progressBar;


    boolean buttonClick = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        //Taking Instances
        newPass = v.findViewById(R.id.changepassword_inputPassword);
        confPass = v.findViewById(R.id.changepassword_inputConformPassword);
        progressBar = v.findViewById(R.id.changepasswoed_sent_loding);
        changeButtonText = v.findViewById(R.id.changepassword_sent_text);


        v.findViewById(R.id.changepassword_sentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPass.getText().toString().equals("") || confPass.getText().toString().equals("")){
                    Message.makeToasts(getContext(),"Please Enter All Details",false);
                }else if (!newPass.getText().toString().equals(confPass.getText().toString())){
                    Message.makeToasts(getContext(),"Please Conform Password",false);
                }else{
                    changePassword(newPass.getText().toString());
                }
            }
        });

        v.findViewById(R.id.changepassword_backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( getFragmentManager().getBackStackEntryCount() > 0)
                {
                    getFragmentManager().popBackStack();
                    return;
                }
            }
        });
        return v;
    }


    private void changePassword(String password){
        if(buttonClick == true){
            return;
        }

        buttonClick = true;
        progressBar.setVisibility(View.VISIBLE);
        changeButtonText.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(Constance.USER_ID,"");

        Call<Object> callApi = RetrofitObject.getInstance().getApi().changePassword(id,password);
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String,Object> data = (Map<String, Object>) response.body();
                if(data.get("response").equals(true)){
                    Message.makeToasts(getContext(),"Password Changed Successfully",true);
                    newPass.setText("");
                    confPass.setText("");
                    buttonClick = false;
                    progressBar.setVisibility(View.INVISIBLE);
                    changeButtonText.setVisibility(View.VISIBLE);
                    if ( getFragmentManager().getBackStackEntryCount() > 0)
                    {
                        getFragmentManager().popBackStack();
                        return;
                    }
                }else{
                    Message.makeToasts(getContext(),"Something Went Wrong Check Internet Connection",false);
                    buttonClick = false;
                    progressBar.setVisibility(View.INVISIBLE);
                    changeButtonText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(getContext(),"Something Went Wrong "+t.getMessage(),false);
                buttonClick = false;
                progressBar.setVisibility(View.INVISIBLE);
                changeButtonText.setVisibility(View.VISIBLE);
            }
        });



    }


}