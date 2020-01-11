package com.example.doneit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doneit.model.Todo;
import com.example.doneit.service.CreateTodoService;

import static com.example.doneit.constants.Client.SHARED_LOGIN;
import static com.example.doneit.constants.MessageCode.*;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateTodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
    }

    public void handleConfirmButton(View view){
        TextView titleText = findViewById(R.id.titleText);
        TextView descriptionText = findViewById(R.id.descriptionText);

        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();

        SharedPreferences prefs = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        String token = prefs.getString("token", "No name defined");

        CreateTodoTask createTodoTask = new CreateTodoTask(title,description,token);
        createTodoTask.execute();

    }

    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public class CreateTodoTask extends AsyncTask<Void,Void,JSONObject> {

        private String title;
        private String description;
        private String token;

        public CreateTodoTask(String title, String description, String token){
            this.title = title;
            this.description = description;
            this.token = token;
        }

        @Override
        protected JSONObject doInBackground(Void... voids){
            Todo todo = new Todo(title,description);
            CreateTodoService createTodoService = new CreateTodoService(token);
            JSONObject jsonResponse = createTodoService.makeCreateTodoRequest(todo);

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result != null){
                try {
                    if(result.get("messageCode").equals(TODO_CREATED)){
                        showToast("TODO CREATO CORRETTAMENTE");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("ERRORE NEL CREARE TODO");
                }

            }
        }
    }
}
