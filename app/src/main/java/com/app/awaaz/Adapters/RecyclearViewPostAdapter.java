package com.app.awaaz.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.awaaz.CommentsFragment;
import com.app.awaaz.MakePostFragment;
import com.app.awaaz.Models.PostsModel;
import com.app.awaaz.R;
import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Constance;
import com.app.awaaz.Utils.Message;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclearViewPostAdapter extends RecyclerView.Adapter<RecyclearViewPostAdapter.RecyclearViewPostViewHolder>{

    List<PostsModel> items;
    FragmentActivity context;
    boolean buttonClick = false;
    Bundle bundle = new Bundle();


    public RecyclearViewPostAdapter(List<PostsModel> items, FragmentActivity context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclearViewPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycleview_layout,parent,false);
        return new RecyclearViewPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclearViewPostViewHolder holder, int position) {
        int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
        Picasso.get().load(Constance.LOGIN_API+items.get(position).getUserprofilepic()).into(holder.userphoto);
        Picasso.get().load(Constance.LOGIN_API+items.get(position).getPostimage()).into(holder.postImage);
        holder.userFullname.setText(items.get(position).getUserfname()+" "+items.get(position).getUserlname());
        holder.totalLikes.setText(items.get(position).getTotallikes());
        holder.totalComments.setText(items.get(position).getTotalcomment());
        holder.postdesc.setText(items.get(position).getPostdesc());

        for(int i = 0;i<items.size();i++){
            int random = new Random().nextInt(7);
            holder.cardView.setBackgroundColor(rainbow[random]);
            holder.constraintLayout.setBackgroundColor(rainbow[random]);
        }

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = items.get(holder.getAdapterPosition()).getId();
                bundle.putString("id",s);
                Fragment fragments = new CommentsFragment();
                fragments.setArguments(bundle);
                FragmentManager fragmentManagers = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransactions = fragmentManagers.beginTransaction();
                fragmentTransactions.add(R.id.full_frame, fragments);
                fragmentTransactions.addToBackStack(null);
                fragmentTransactions.commit();
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likePost(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    public class RecyclearViewPostViewHolder extends RecyclerView.ViewHolder{
        TextView totalLikes,totalComments,userFullname,postdesc;
        CircleImageView userphoto;
        ImageView postImage;
        LinearLayout commentButton,likeButton;
        CardView cardView;
        ConstraintLayout constraintLayout;

        public RecyclearViewPostViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.post_image);
            userphoto = itemView.findViewById(R.id.post_user_photo);
            userFullname = itemView.findViewById(R.id.post_user_name);
            postdesc = itemView.findViewById(R.id.post_description);
            totalLikes = itemView.findViewById(R.id.post_numberlike);
            totalComments = itemView.findViewById(R.id.post_numbercomments);
            commentButton = itemView.findViewById(R.id.comment_container);
            likeButton = itemView.findViewById(R.id.like_container);
            cardView = itemView.findViewById(R.id.cardview);
            constraintLayout = itemView.findViewById(R.id.consraint_parent);
        }
    }

    private void likePost(int posiionss){
        if(buttonClick == true){
            return;
        }

        buttonClick = true;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constance.SHAREDPREFRENCE_USERDATA,Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString(Constance.USER_ID,"");
        Call<Object> callApi = RetrofitObject.getInstance().getApi().likePost(userid,items.get(posiionss).getId());
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String,Object> alldata = (Map<String, Object>) response.body();
                if(alldata.get("response").equals(true)){
                    int tlikes = (int) Math.round((Double)alldata.get("message"));
                    items.get(posiionss).setTotallikes(String.valueOf(tlikes));
                    notifyDataSetChanged();
                    buttonClick = false;
                }else if(alldata.get("response").equals(true)){
                    buttonClick = false;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(context,"Something Went Wrong Please Check Internet Connection",false);
                buttonClick = false;
            }
        });
    }
}
