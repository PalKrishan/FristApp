package com.app.awaaz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.awaaz.Adapters.RecyclearViewEventAdapter;
import com.app.awaaz.Models.EventsModel;
import com.app.awaaz.RetrofitController.RetrofitObject;
import com.app.awaaz.Utils.Message;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event_fragnment extends Fragment {

    List<EventsModel> data;
    RecyclearViewEventAdapter eventAdapter;
    RecyclerView eventRecyclearView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_fragnment, container, false);
        //Taking Global Instance
        eventRecyclearView = v.findViewById(R.id.eventRecyclearView);
        progressBar = v.findViewById(R.id.event_progressbar);
        eventRecyclearView.setLayoutManager(new LinearLayoutManager(getContext()));
        data = new ArrayList<EventsModel>();

        fetchEvents();


        return v;
    }

    private void fetchEvents(){
        Call<Object> callApi = RetrofitObject.getInstance().getApi().fetchEvents();
        callApi.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println(response.body());
                Map<String,Object> data = (Map<String, Object>) response.body();
                if(data.get("response").equals(true)){
                    ArrayList<Object> allEvents = new ArrayList<Object>();
                    List<EventsModel> adapterdata = new ArrayList<>();
                    allEvents = (ArrayList<Object>) data.get("0");
                    for(int i = 0;i < allEvents.size(); i++){
                        Map<String,Object> singleEvent = (Map<String, Object>) allEvents.get(i);
                        adapterdata.add(new EventsModel(singleEvent.get("id").toString(),singleEvent.get("title_event").toString(),singleEvent.get("description_event").toString(),singleEvent.get("image_event").toString(),singleEvent.get("eventer_name").toString(),singleEvent.get("eventer_photo").toString(),singleEvent.get("date").toString()));
                    }

                    eventAdapter = new RecyclearViewEventAdapter(adapterdata);
                    eventAdapter.notifyDataSetChanged();
                    eventRecyclearView.setAdapter(eventAdapter);

                    eventRecyclearView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                   // System.out.println(allEvents);


                }else if(data.get("response").equals(false)){
                    Message.makeToasts(getContext(),"Something Went Wrong",false);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Message.makeToasts(getContext(),"Something Went Wrong "+t.getMessage(),false);
            }
        });
    }
}