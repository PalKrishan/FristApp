package com.app.awaaz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.awaaz.Models.EventsModel;
import com.app.awaaz.R;
import com.app.awaaz.Utils.Constance;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclearViewEventAdapter extends RecyclerView.Adapter<RecyclearViewEventAdapter.RecyclearViewEventViewHolder> {

    List<EventsModel> items;


    public RecyclearViewEventAdapter(List<EventsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclearViewEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_recyclearview_layout,parent,false);
        return new RecyclearViewEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclearViewEventViewHolder holder, int position) {
        Picasso.get().load(Constance.LOGIN_API+items.get(position).getEventer_photo()).into(holder.eventerphoto);
        Picasso.get().load(Constance.LOGIN_API+items.get(position).getEvent_image()).into(holder.eventImage);

        holder.eventerName.setText(items.get(position).getEventer_name());
        holder.eventTitle.setText(items.get(position).getEvent_title());
        holder.eventDate.setText(items.get(position).getDate());
        holder.eventDescription.setText(items.get(position).getEvent_description());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclearViewEventViewHolder extends RecyclerView.ViewHolder{
        CircleImageView eventerphoto;
        TextView eventerName,eventTitle,eventDescription,eventDate;
        ImageView eventImage;


        public RecyclearViewEventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventerphoto = itemView.findViewById(R.id.event_eventer_photo);
            eventerName = itemView.findViewById(R.id.event_eventer_name);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDate = itemView.findViewById(R.id.event_date);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventImage = itemView.findViewById(R.id.event_image);

        }
    }
}
