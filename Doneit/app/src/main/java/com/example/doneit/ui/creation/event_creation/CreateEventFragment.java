package com.example.doneit.ui.creation.event_creation;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.doneit.R;
import com.example.doneit.model.Event;
import com.example.doneit.service.CreateEventService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;
import static com.example.doneit.constants.MessageCode.TODO_CREATED;

public class CreateEventFragment extends Fragment {

    private SharedPreferences prefs;
    private View root;
    private String formattedDate;
    Button addEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.event_creation, container, false);
        prefs = getActivity().getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);

        addEvent = root.findViewById(R.id.create_event);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventCreationRequest();

            }
        });
        return root;
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return formattedDate = df.format(c);
    }
    private Date getFutureDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date c= null;
        try {
            c = sdf.parse("2020-05-26");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //String date=sdf.format(c);
        return c;
    }

    private void sendEventCreationRequest() {

        // get all information i need from form, like title description and the token from shared memory
        String token = prefs.getString("token", "No name defined");
        TextView todoTitle = root.findViewById(R.id.titleText);
        TextView descriptionText = root.findViewById(R.id.descriptionText);
        String title = todoTitle.getText().toString();
        String description = descriptionText.getText().toString();

        // start asynctask
        CreateEventTask createEventTask = new CreateEventTask(title,description,token);
        createEventTask.execute();
    }

    public class CreateEventTask extends AsyncTask<Void,Void, JSONObject> {

        private String title;
        private String description;
        private String token;

        public CreateEventTask(String title, String description, String token){
            this.title = title;
            this.description = description;
            this.token = token;
        }

        @Override
        protected JSONObject doInBackground(Void... voids){
            Event event = new Event(title,description,"Via Dio cane 16",getFutureDay());
            CreateEventService createEventService = new CreateEventService(token);
            JSONObject jsonResponse = createEventService.makeCreateEventRequest(event);

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result != null){
                try {
                    if(result.get("messageCode").equals(TODO_CREATED)){
                        showFragmentToast("Todo creato correttamente");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showFragmentToast("Errore nella creazione del todo");
                }

            }
        }
    }

    public void showFragmentToast(String toast){
        Toast.makeText(getActivity().getApplicationContext(), toast, Toast.LENGTH_LONG).show();

    }
}