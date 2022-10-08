package com.app.awaaz;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.awaaz.Models.ServerResponse;
import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Constance;
import com.app.awaaz.Utils.Message;
import com.app.awaaz.Utils.RealPathUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonateThingFragment extends Fragment {

    String imagePath="",id,email;
    ImageView imageView;
    EditText thingname,mobilenumber,aboutthing,address;
    TextView buttonText;
    ProgressBar progressBar;
    boolean buttonClick = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_donate_thing, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, Context.MODE_PRIVATE);

        //Taking Refrence
        imageView = v.findViewById(R.id.thing_image);
        thingname = v.findViewById(R.id.donate_thingname);
        mobilenumber = v.findViewById(R.id.donate_mobileno);
        aboutthing = v.findViewById(R.id.donate_aboutthing);
        address = v.findViewById(R.id.donate_fulladdress);
        id = sharedPreferences.getString(Constance.USER_ID,"");
        email = sharedPreferences.getString(Constance.USER_EM,"");
        buttonText = v.findViewById(R.id.donate_donate_text);
        progressBar = v.findViewById(R.id.donate_donate_loding);


        v.findViewById(R.id.thing_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickPhoto.setType("image/*");
                        startActivityForResult(pickPhoto, 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Message.makeToasts(getContext(),"Please Accept Permission",false);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
            }
        });


        v.findViewById(R.id.donate_DonateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thingname.getText().toString().equals("") || mobilenumber.getText().toString().equals("") || aboutthing.getText().toString().equals("") || address.getText().toString().equals("")){
                    Message.makeToasts(getContext(),"Please Fill All Details",false);
                }else if(!mobilenumber.getText().toString().matches(Constance.MOBILE_PATTERN)){
                    Message.makeToasts(getContext(),"Please Enter Valid Phone Number",false);
                }else if(imagePath.equals("")){
                    Message.makeToasts(getContext(),"Please Select Image",false);
                }
                else{
                    donateSend(thingname.getText().toString(),mobilenumber.getText().toString(),aboutthing.getText().toString(),address.getText().toString());
                }
            }
        });


        //On BackButton Pressed
        v.findViewById(R.id.donation_backbutton).setOnClickListener(new View.OnClickListener() {
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


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = imageReturnedIntent.getData();
            imagePath = RealPathUtil.getRealPath(getContext(),uri);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void donateSend(String thing_name,String mobile_no,String thing_info,String full_address){

        if(buttonClick == true){
            return;
        }
        buttonClick = true;
        progressBar.setVisibility(View.VISIBLE);
        buttonText.setVisibility(View.INVISIBLE);

        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RequestBody ide = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody donoremail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody thingnames = RequestBody.create(MediaType.parse("text/plain"), thing_name);
        RequestBody donorphoneno = RequestBody.create(MediaType.parse("text/plain"), mobile_no);
        RequestBody thingdescrr = RequestBody.create(MediaType.parse("text/plain"), thing_info);
        RequestBody addresssss = RequestBody.create(MediaType.parse("text/plain"), full_address);

        Call<ServerResponse> callApi = RetrofitObject.getInstance().getApi().donateDetailSend(fileToUpload,filename,ide,thingnames,donorphoneno,donoremail,thingdescrr,addresssss);
        callApi.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = (ServerResponse) response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Message.makeToasts(getContext(),"Thank You For Donation\n We Will Call You Soon",true);
                        if ( getFragmentManager().getBackStackEntryCount() > 0)
                        {
                            getFragmentManager().popBackStack();
                            return;
                        }
                    } else {
                       Message.makeToasts(getContext(),"Something Went Wrong Please Check Internet Connection",false);

                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                    buttonClick = false;
                    progressBar.setVisibility(View.INVISIBLE);
                    buttonText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Message.makeToasts(getContext(),"Something Went Wrong Please Check Internet Connection",false);
                buttonClick = false;
                progressBar.setVisibility(View.INVISIBLE);
                buttonText.setVisibility(View.VISIBLE);
            }
        });
    }
}