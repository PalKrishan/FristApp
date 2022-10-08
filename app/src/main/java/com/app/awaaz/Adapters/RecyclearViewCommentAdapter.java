package com.app.awaaz.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.awaaz.Models.CommentModel;
import com.app.awaaz.R;
import com.app.awaaz.Utils.Constance;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclearViewCommentAdapter extends RecyclerView.Adapter<RecyclearViewCommentAdapter.RecyclearViewCommentViewHlder>{
    List<CommentModel> items;

    public RecyclearViewCommentAdapter(List<CommentModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclearViewCommentViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclearview_comment_layout,parent,false);
        return new RecyclearViewCommentViewHlder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclearViewCommentViewHlder holder, int position) {
        Picasso.get().load(Constance.LOGIN_API+items.get(position).getUser_pic()).into(holder.userphoto);
        holder.fullname.setText(items.get(position).getUsername());
        holder.comment.setText(items.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class RecyclearViewCommentViewHlder extends RecyclerView.ViewHolder{
        CircleImageView userphoto;
        TextView fullname,comment;

        public RecyclearViewCommentViewHlder(@NonNull View itemView) {
            super(itemView);

            userphoto = itemView.findViewById(R.id.commenter_photo);
            fullname = itemView.findViewById(R.id.commentername);
            comment = itemView.findViewById(R.id.commentsss);
        }
    }



}
