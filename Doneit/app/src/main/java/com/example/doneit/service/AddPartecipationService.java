package com.example.doneit.service;

import android.util.Log;

import com.example.doneit.model.Event;
import com.example.doneit.model.Todo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.*;


public class AddPartecipationService  {

    private String token;

    public AddPartecipationService(String token){
        this.token = token;
    }

    public JSONObject makeRequestTodo(Todo todo) throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = SERVER_URL + CREATE_PROPOSAL + "/"+ todo.getId() ;

        OkHttpClient client = new OkHttpClient();



        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + token)
                .build();

            Response response = client.newCall(request).execute();
            if(response != null){
                try {
                    //Log.d("proposal-response", response.body().string());
                    return new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                return null;
            }

        return null;
    }
    public JSONObject makeRequestEvent(Event event) throws IOException {
        String url = SERVER_URL + PARTECIPATE_EVENT + "/"+ event.getId();

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + token)
                .build();

        Response response = client.newCall(request).execute();
        if(response != null){
            try {
                //Log.d("proposal-response", response.body().string());
                return new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }

        return null;
    }
}