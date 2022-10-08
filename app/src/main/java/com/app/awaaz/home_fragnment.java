package com.app.awaaz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.awaaz.Adapters.RecyclearViewPostAdapter;
import com.app.awaaz.Models.PostsModel;
import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class home_fragnment extends Fragment {

    RecyclerView recyclerView;
    RecyclearViewPostAdapter postAdapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_home_fragnment, container, false);
       recyclerView = v.findViewById(R.id.post_reycycleview);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       progressBar = v.findViewById(R.id.homefragment_progress);

        fetchPosts();



       v.findViewById(R.id.post_floatingbutton).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Fragment fragments = new MakePostFragment();
               FragmentManager fragmentManagers = getActivity().getSupportFragmentManager();
               FragmentTransaction fragmentTransactions = fragmentManagers.beginTransaction();
               fragmentTransactions.add(R.id.full_frame, fragments);
               fragmentTransactions.addToBackStack(null);
               fragmentTransactions.commit();
           }
       });

        return v;
    }



    public void fetchPosts(){
        Call<Object> callApi = RetrofitObject.getInstance().getApi().fetchPosts();
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                List<PostsModel> postdataa = new ArrayList<PostsModel>();
                Map<String,Object> fulldata = (Map<String, Object>) response.body();
                if(fulldata.get("response").equals(true)){
                    List<Object> allpost = (List<Object>) fulldata.get("data");
                    for(int i = 0;i<allpost.size();i++){
                        Map<String,Object> eachData = (Map<String, Object>) allpost.get(i);
                        int tcomments = (int) Math.round((Double)eachData.get("post_totalcomments"));
                        int tlikes = (int) Math.round((Double)eachData.get("post_totalLike"));
                        postdataa.add(new PostsModel(eachData.get("id").toString(),eachData.get("post_user_fname").toString(),eachData.get("post_user_lname").toString(),eachData.get("post_user_pic").toString(),eachData.get("posted_picture").toString(),eachData.get("posted_description").toString(),String.valueOf(tcomments),String.valueOf(tlikes)));
                    }

                    postAdapter = new RecyclearViewPostAdapter(postdataa,getActivity());
                    postAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(postAdapter);

                }else{
                    System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnoooooooooooooooooooooooooooooooo");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(getContext(),"Please Check Network Connection",false);
            }
        });
    }

}