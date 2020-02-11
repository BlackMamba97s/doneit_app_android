package com.example.doneit.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doneit.R;
import com.example.doneit.model.Event;
import com.example.doneit.model.Todo;
import com.example.doneit.service.AddPartecipationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.example.doneit.constants.MessageCode.EVENT_CREATED;
import static com.example.doneit.constants.MessageCode.TODO_CREATED;
import static com.facebook.FacebookSdk.getApplicationContext;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<Event> mEvents;
    private LayoutInflater mInflater;
    private SharedPreferences prefs;

    // data is passed into the constructor
    public EventListAdapter(Context context, List<Event> events, SharedPreferences prefs) {
        this.mInflater = LayoutInflater.from(context);
        this.mEvents = events;
        this.prefs = prefs;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.todo_list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String event = mEvents.get(position).getTitle();
        holder.titleTextView.setText(event);
        holder.partecipateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), mTodos.get(position).toString(),  Toast.LENGTH_SHORT).show();
                String token = prefs.getString("token", "No name defined");
                AddPartecipationEvent addPartecipationEvent = new AddPartecipationEvent(mEvents.get(position), token);
                addPartecipationEvent.execute();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        Button partecipateEvent;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.card_title);
            partecipateEvent = itemView.findViewById(R.id.partecipate);
        }


    }

    public class AddPartecipationEvent extends AsyncTask<Void,Void, JSONObject> {

        private String token;
        private Event event;

        public AddPartecipationEvent(Event event, String token){
            this.event = event;
            this.token = token;
        }

        @Override
        protected JSONObject doInBackground(Void... voids){
            AddPartecipationService addPartecipationService = new AddPartecipationService(token);
            JSONObject jsonResponseTodo = null;
            try {
                jsonResponseTodo = addPartecipationService.makeRequestEvent(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponseTodo;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result != null){
                try {
                    if(result.get("messageCode").equals(EVENT_CREATED)){
                        showFragmentToast("Partecipazione all'evento avvenuta");
                    }else if(result.get("messageCode").equals(TODO_CREATED)){
                        showFragmentToast("Non puoi partecipare all'evento");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showFragmentToast("Errore nella partecipazione dell'evento");
                }

            } else {
                showFragmentToast("qualcosa è andato storto");
            }
        }
    }

    public void showFragmentToast(String toast){
        Log.d("prova", " sono entrato qua");
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();

    }
}