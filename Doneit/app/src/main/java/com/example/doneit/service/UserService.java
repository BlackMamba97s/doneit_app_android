package com.example.doneit.service;

import com.example.doneit.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.*;


public class UserService {

    private String token;

    public UserService(String token){
        this.token = token;
    }
    public User getUser(){
        String url = SERVER_URL + GET_USER;

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
                String jsonResponse = response.body().string();
                Type listType = new TypeToken<User>() {}.getType();
                User user = new Gson().fromJson(jsonResponse, listType);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
