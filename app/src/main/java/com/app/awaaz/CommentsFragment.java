package com.app.awaaz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.awaaz.Adapters.RecyclearViewCommentAdapter;
import com.app.awaaz.Models.CommentModel;
import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Constance;
import com.app.awaaz.Utils.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclearViewCommentAdapter commentAdapter;
    String postId,userId;
    boolean buttonClick = false;
    EditText comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constance.USER_ID,"");


        recyclerView = v.findViewById(R.id.comment_recyclearview);
        comment = v.findViewById(R.id.comment_comment_edittext);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = this.getArguments();

        if(bundle == null){
            Message.makeToasts(getContext(),"No",false);
        }else{
            postId = bundle.getString("id");
            fetchComments(postId);
        }

        v.findViewById(R.id.comment_send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment.getText().toString().equals("")){
                    Message.makeToasts(getContext(),"Can't Make Empty Comment",false);
                }else{
                    makeComments(postId,comment.getText().toString());
                }
            }
        });


        v.findViewById(R.id.comments_backbutton).setOnClickListener(new View.OnClickListener() {
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

    private void fetchComments(String postid){
        Call<Object> callApi = RetrofitObject.getInstance().getApi().fetchComments(postid);
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                List<CommentModel> commentData = new ArrayList<CommentModel>();
                Map<String,Object> fulldata = (Map<String, Object>) response.body();
                if(fulldata.get("response").equals(true)){
                    List<Object> dataComments = (List<Object>) fulldata.get("data");
                    for(int i = 0; i < dataComments.size(); i++){
                        Map<String,Object> eachComments = (Map<String, Object>) dataComments.get(i);

                        String fname = eachComments.get("commenter_fname").toString();
                        String lname = eachComments.get("commenter_lname").toString();
                        String fullname = fname+" "+lname;
                        commentData.add(new CommentModel(eachComments.get("id").toString(),eachComments.get("commenter_photo").toString(),fullname,eachComments.get("comment").toString()));
                    }

                    commentAdapter = new RecyclearViewCommentAdapter(commentData);
                    commentAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(commentAdapter);

                }else{
                    System.out.println("NO DATA COME");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(getContext(),"Please Check Internet Connection",false);
            }
        });
    }

    //Make Commens
    private void makeComments(String postIde,String Comments){
        if(buttonClick == true){
            return;
        }
        buttonClick = true;

        Call<Object> callApi = RetrofitObject.getInstance().getApi().makeComments(postIde,userId,Comments);
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String,Object> data = (Map<String, Object>) response.body();
                if(data.get("response").equals(true)){
                    comment.setText("");
                    fetchComments(postId);
                    buttonClick = false;
                }else{
                    buttonClick = false;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(getContext(),"Something Went Wrong Please Check Internet Connection",false);
                buttonClick = false;
            }
        });
    }
}