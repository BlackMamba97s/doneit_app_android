package com.example.doneit.ui.event_list;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doneit.R;
import com.example.doneit.adapter.EventListAdapter;
import com.example.doneit.model.Event;
import com.example.doneit.service.EventService;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class EventsFragment extends Fragment{


    private View root;
    private EventListAdapter eventListAdapter;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.event_fragment, container, false);

        getEventList(); // get Todo list form server

        return root;
    }

    private void getEventList() {
        prefs = this.getActivity().getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        String token = prefs.getString("token", "No name defined");
        EventListTask eventListTask = new EventListTask(token);
        eventListTask.execute();
    }




    private class EventListTask extends AsyncTask<Void,Void, List<Event>> {

        private String token;

        EventListTask(String token){
            this.token = token;
        }
        @Override
        protected List<Event> doInBackground(Void... voids) {
            EventService eventService = new EventService(token);
            List<Event> response = eventService.getAllEvent();
            return response;
        }

        @Override
        protected void onPostExecute(List<Event> result){
            //Toast.makeText(getActivity().getApplicationContext(),"provaaaaa",Toast.LENGTH_LONG).show();
            showResponse(result);
            setRecycleView(result);

        }
    }
    public void showResponse(List<Event> response){
        if(response != null) {
            for (Event event : response) {
                Log.d("response", "" + response);
            }
        }

    }

    private void setRecycleView(List<Event> result) {
        // data to populate the RecyclerView with
        if(result!=null){

            // set up the RecyclerView
            RecyclerView recyclerView = root.findViewById(R.id.my_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            eventListAdapter = new EventListAdapter(getActivity(), result, prefs);
            recyclerView.setAdapter(eventListAdapter);
        }
    }

}
