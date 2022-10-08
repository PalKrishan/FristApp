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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakePostFragment extends Fragment {

    ProgressBar progressBar;
    TextView textView;
    EditText desc;
    ImageView imageView;
    String imagePath = "";
    boolean buttonClick = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_make_post, container, false);

        progressBar = v.findViewById(R.id.makepost_post_loding);
        textView = v.findViewById(R.id.makepost_post_text);
        imageView = v.findViewById(R.id.makepost_imahe);
        desc = v.findViewById(R.id.makepost_desc);


        v.findViewById(R.id.makepost_backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( getFragmentManager().getBackStackEntryCount() > 0)
                {
                    getFragmentManager().popBackStack();
                    return;
                }
            }
        });


        v.findViewById(R.id.makepost_imahe).setOnClickListener(new View.OnClickListener() {
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

        v.findViewById(R.id.makepost_postbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePath.equals("")){
                    Message.makeToasts(getContext(),"Please Select Image",false);
                }else{
                    post(desc.getText().toString());
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
    private void post(String desc){

        if(buttonClick == true){
            return;
        }
        buttonClick = true;
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(Constance.USER_ID,"");

        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RequestBody ide = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), desc);

        Call<ServerResponse> callApi = RetrofitObject.getInstance().getApi().userPost(fileToUpload,filename,ide,description);
        callApi.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = (ServerResponse) response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Message.makeToasts(getContext(),"Posted",true);
                        if ( getFragmentManager().getBackStackEntryCount() > 0)
                        {
                            getFragmentManager().popBackStack();
                            return;
                        }
                    } else {
                        Message.makeToasts(getContext(),"Something Went Wrong Please Check Internet Connection",false);
                        buttonClick = false;
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.VISIBLE);

                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                    buttonClick = false;
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Message.makeToasts(getContext(),"Something Went Wrong Please Check Internet Connection",false);
                buttonClick = false;
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }
}