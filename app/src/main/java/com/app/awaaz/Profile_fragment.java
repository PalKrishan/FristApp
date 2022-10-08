package com.app.awaaz;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile_fragment extends Fragment {
    String imagePath = "";
    CircleImageView imageView;
    ProgressBar progressBar,editprogress;
    TextView fullname,editbuttonText;
    EditText fname,lname,userName,email;
    boolean edibuttonclick = false;
    Layout mainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA,Context.MODE_PRIVATE);
        String pro_add = sharedPreferences.getString(Constance.USER_PP,"");
        String finame = sharedPreferences.getString(Constance.USER_FN,"");
        String laname = sharedPreferences.getString(Constance.USER_LN,"");
        String ussName = sharedPreferences.getString(Constance.USER_UI,"");
        String ema = sharedPreferences.getString(Constance.USER_EM,"");

        //refrence
        imageView = v.findViewById(R.id.account_image_view);
        progressBar = v.findViewById(R.id.account_image_loding);
        editprogress = v.findViewById(R.id.account_edit_loding);
        editbuttonText = v.findViewById(R.id.account_edit_text);
        fullname = v.findViewById(R.id.account_full_name);
        fname = v.findViewById(R.id.account_frist_name_edit_text);
        lname = v.findViewById(R.id.account_last_name_edit_text);
        userName = v.findViewById(R.id.account_username_edit_text);
        email = v.findViewById(R.id.account_email_edit_text);



        //Setting All values
        if(!pro_add.isEmpty()){
            Picasso.get().load(Constance.LOGIN_API+pro_add).into(imageView);
        }
        fullname.setText(finame+" "+laname);
        fname.setText(finame);
        lname.setText(laname);
        userName.setText(ussName);
        email.setText(ema);


        v.findViewById(R.id.account_editprofilepic_button).setOnClickListener(new View.OnClickListener() {
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

        v.findViewById(R.id.account_editprofile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fname.getText().toString().equals("") || lname.getText().toString().equals("") || userName.getText().toString().equals("")){
                    Message.makeToasts(getContext(),"Please Fill All details",false);
                }else{
                    UpdateDetails(fname.getText().toString(),lname.getText().toString(),userName.getText().toString());
                }
            }
        });

        v.findViewById(R.id.account_Change_Password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ChangePassword();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.full_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = imageReturnedIntent.getData();
            imagePath = RealPathUtil.getRealPath(getContext(),uri);
            upload_image(imagePath);

        }
    }

    //Upload Image
    private void upload_image(String path) {
        progressBar.setVisibility(View.VISIBLE);
        File file = new File(path);
        SharedPreferences sharedPreferences  =  getContext().getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA,Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(Constance.USER_ID,"");

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RequestBody ide = RequestBody.create(MediaType.parse("text/plain"), id);

        Call<ServerResponse> callApi = RetrofitObject.getInstance().getApi().uploadImage(fileToUpload,filename,ide);
        callApi.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = (ServerResponse) response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {

                       Call<Object> fetch_image = RetrofitObject.getInstance().getApi().fetchImage(id);
                       fetch_image.enqueue(new Callback<Object>() {
                           @Override
                           public void onResponse(Call<Object> call, Response<Object> response) {
                               System.out.println("hhhh"+response.body());
                               Map<String,Object> data = (Map<String, Object>) response.body();
                               if(data.get("success").equals(true)){
                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString(Constance.USER_PP,data.get("profile_pic").toString());
                                   editor.commit();

                                   String profile_add = sharedPreferences.getString(Constance.USER_PP,"");
                                   Picasso.get().load(Constance.LOGIN_API+profile_add).into(imageView);
                                   Message.makeToasts(getContext(),"Update Seccess",true);
                                   progressBar.setVisibility(View.GONE);
                               }else{
                                   Message.makeToasts(getContext(),"Something Went Wrong Please Check Internet",false);
                                   progressBar.setVisibility(View.GONE);
                               }
                           }

                           @Override
                           public void onFailure(Call<Object> call, Throwable t) {
                               Message.makeToasts(getContext(),"Something Went Wrong Fetching"+t.getMessage(),false);
                               progressBar.setVisibility(View.GONE);
                           }
                       });
                    } else {
                        Toast.makeText(getContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Message.makeToasts(getContext(),"Something Went Wrong "+t.getMessage(),false);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    //Update Details
    private void UpdateDetails(String feename,String leename,String username){
        if(edibuttonclick == true){
            return;
        }
        edibuttonclick = true;
        editprogress.setVisibility(View.VISIBLE);
        editbuttonText.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA,Context.MODE_PRIVATE);
        String id  = sharedPreferences.getString(Constance.USER_ID,"");

        Call<Object> callApi = RetrofitObject.getInstance().getApi().updateDetails(id,feename,leename,username);
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String,Object> data = (Map<String, Object>) response.body();
                if(data.get("response").equals("success")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constance.USER_UI,data.get("user_name").toString());
                    editor.putString(Constance.USER_FN,data.get("first_name").toString());
                    editor.putString(Constance.USER_LN,data.get("last_name").toString());
                    editor.commit();

                    fullname.setText(data.get("first_name").toString()+" "+data.get("last_name").toString());
                    userName.setText(data.get("user_name").toString());
                    fname.setText(data.get("first_name").toString());
                    lname.setText(data.get("last_name").toString());

                    Message.makeToasts(getContext(),"Update Seccess",true);
                    edibuttonclick = false;
                    editprogress.setVisibility(View.INVISIBLE);
                    editbuttonText.setVisibility(View.VISIBLE);

                }else if(data.get("response").equals("fetch_fail")){
                    Message.makeToasts(getContext(),"Something Went Wrong Check Internet Connection",false);
                    edibuttonclick = false;
                    editprogress.setVisibility(View.INVISIBLE);
                    editbuttonText.setVisibility(View.VISIBLE);
                }else if(data.get("response").equals("update_fail")){
                    Message.makeToasts(getContext(),"Something Went Wrong Check Internet Connection",false);
                    edibuttonclick = false;
                    editprogress.setVisibility(View.INVISIBLE);
                    editbuttonText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(getContext(),"Error"+t.getMessage(),false);
                edibuttonclick = false;
                editprogress.setVisibility(View.INVISIBLE);
                editbuttonText.setVisibility(View.VISIBLE);
            }
        });
    }


}