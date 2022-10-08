package com.app.awaaz.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.awaaz.LoginActivity;
import com.app.awaaz.Models.ViewPageItems;
import com.app.awaaz.OnboardingScreens;
import com.app.awaaz.R;
import com.app.awaaz.Utils.Constance;

import java.util.List;
import java.util.zip.Inflater;

public class ViewPageAdapter extends RecyclerView.Adapter<ViewPageAdapter.ViewPageViewHolder>{

    List<ViewPageItems> items;
    ViewPageItems viewPageItems;
    Context context;
    ViewPager2 viewPager2;

    public ViewPageAdapter(List<ViewPageItems> items,Context context,ViewPager2 viewPager2) {
        this.items = items;
        this.context = context;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_layout,parent,false);
        return new ViewPageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPageViewHolder holder, int position) {
         viewPageItems = items.get(position);

        holder.imageView.setImageResource(viewPageItems.getImageId());
        holder.heading.setText(viewPageItems.getHeading());
        holder.description.setText(viewPageItems.getDescription());


        //Hiding Previous Button From First Onboarding Screen
        if(position == 0) {
            holder.previous.setVisibility(View.GONE);
        }else if(position == 3){
            holder.previous.setVisibility(View.GONE);
            holder.next.setVisibility(View.GONE);
            holder.getstartButton.setVisibility(View.VISIBLE);
        }

        //Handeling NextButton Of Onboarding Screen
        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.getOldPosition() == 3){
                    holder.previous.setVisibility(View.GONE);
                    holder.next.setVisibility(View.GONE);
                    holder.getstartButton.setVisibility(View.VISIBLE);
                }else{
                    int index = holder.getAdapterPosition();
                    index++;
                   viewPager2.setCurrentItem(index);
                }
            }
        });


        //Handeling Previous Button Of Onboarding Screen
        holder.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.getOldPosition() == 0){
                    holder.previous.setVisibility(View.GONE);
                    holder.next.setVisibility(View.VISIBLE);
                    holder.getstartButton.setVisibility(View.GONE);
                }else{
                    int index = holder.getAdapterPosition();
                    index--;
                    viewPager2.setCurrentItem(index);
                }
            }

        });

        //Handeling GetStart button Of Onboarding Screen
        holder.getstartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor onboardingPrefrences = context.getSharedPreferences(Constance.FIRST_TIME,MODE_PRIVATE).edit();
                onboardingPrefrences.putBoolean(Constance.FIRST_TIME_KEY,true);
                onboardingPrefrences.commit();
                Intent gotoLogin = new Intent(context, LoginActivity.class);
                gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(gotoLogin);
                ((Activity)context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewPageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView heading;
        TextView description;
        Button previous;
        Button next;
        LinearLayout getstartButton;

        public ViewPageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.onboardinglayout_image);
            heading = itemView.findViewById(R.id.onboardinglayout_textHeading);
            description = itemView.findViewById(R.id.onboardinglayout_textdescription);
            getstartButton = itemView.findViewById(R.id.onboarding_getStartButton);
            previous = itemView.findViewById(R.id.onboarding_previousbutton);
            next = itemView.findViewById(R.id.onboarding_nextbutton);
        }
    }
}
