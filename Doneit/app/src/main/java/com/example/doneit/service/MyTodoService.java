package com.example.doneit.service;

import android.util.Log;

import com.example.doneit.model.Todo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.*;


public class MyTodoService {

    private String token;
    private String state;

    public MyTodoService(String token, String state){
        this.token = token;
        this.state = state;
    }
    public List<Todo> getMyTodo(){
        String url = SERVER_URL + MY_TODO_LIST + "/" + state;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                Log.d("response-diocane", "successo " + response.toString());
                String jsonResponse = response.body().string();
                Type listType = new TypeToken<List<Todo>>() {}.getType();
                List<Todo> todoList = new Gson().fromJson(jsonResponse, listType);
                if(todoList!=null){
                    Log.d("response-diocane", "successo ");
                    Log.d("response-diocane", todoList.toString());
                    for( Todo todo : todoList){
                        Log.d("response-diocane", todo.toString());
                    }

                }else{
                    Log.d("response-diocane", "fallimento  ");
                }
                return todoList;
            }else{
                Log.d("response-diocane", "fallimento " +response.body().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}

