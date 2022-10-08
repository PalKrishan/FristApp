package com.app.awaaz.Interfaces;

import com.app.awaaz.Models.ServerResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiCalls {
    @FormUrlEncoded
    @POST("login.php")
    Call<Object> loginUserData(@Field("username") String username, @Field("userpassword") String userpassword);

    @FormUrlEncoded
    @POST("forgotpassword.php")
    Call<Object> sentPassword(@Field("user_email") String useremail);


    @FormUrlEncoded
    @POST("register.php")
    Call<Object> registerUser(@Field("first_name") String firstName, @Field("last_name") String lastName, @Field("email") String email, @Field("password") String password, @Field("user_name") String userName, @Field("verified") String verified);

    @Multipart
    @POST("upload_profile_pic.php")
    Call<ServerResponse> uploadImage(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("id") RequestBody id);

    @FormUrlEncoded
    @POST("fetch_profile_pic.php")
    Call<Object> fetchImage(@Field("id") String id);

    @FormUrlEncoded
    @POST("update_details.php")
    Call<Object> updateDetails(@Field("id") String id,@Field("fname") String fname,@Field("lname") String lname,@Field("username") String username);

    @FormUrlEncoded
    @POST("change_password.php")
    Call<Object> changePassword(@Field("id") String id,@Field("password") String password);

    @GET("fetch_events.php")
    Call<Object> fetchEvents();

    @Multipart
    @POST("donation.php")
    Call<ServerResponse> donateDetailSend(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("id") RequestBody id,@Part("thingname") RequestBody thingname,@Part("mobilenumber") RequestBody mobilenumber,@Part("email") RequestBody email,@Part("thingdesc") RequestBody thingdes,@Part("donoraddress") RequestBody donoraddress);

    @Multipart
    @POST("post.php")
    Call<ServerResponse> userPost(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("userid") RequestBody id,@Part("postdes") RequestBody postdesc);

    @GET("fetch_posts.php")
    Call<Object> fetchPosts();

    @FormUrlEncoded
    @POST("fetch_comments.php")
    Call<Object> fetchComments(@Field("postid") String postid);

    @FormUrlEncoded
    @POST("make_comment.php")
    Call<Object> makeComments(@Field("postid") String postid,@Field("commenterid") String id,@Field("comment") String comment);

    @FormUrlEncoded
    @POST("likes.php")
    Call<Object> likePost(@Field("userid") String userid,@Field("postid") String postid);
}
