package com.example.doneit;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.doneit.constants.Client.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.doneit.model.Todo;
import com.example.doneit.service.TodoService;

import java.util.List;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        String token = prefs.getString("token", "No name defined");

        TodoListTask todoListTask = new TodoListTask(token);
        todoListTask.execute();
    }

    public void handleLogout(View view){
        SharedPreferences preferences = getSharedPreferences(SHARED_LOGIN, 0);
        preferences.edit().remove("token").commit();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    public void showResponse(List<Todo> response){
        for(Todo todo : response){
            Log.d("response",""+response);
        }

    }

    private class TodoListTask extends AsyncTask<Void,Void,List<Todo>>{

        private String token;

        TodoListTask(String token){
            this.token = token;
        }
        @Override
        protected List<Todo> doInBackground(Void... voids) {
            TodoService todoService = new TodoService(token);
            List<Todo> response = todoService.getAllTodo();
            return response;
        }

        @Override
        protected void onPostExecute(List<Todo> result){
            showResponse(result);
        }
    }

}
