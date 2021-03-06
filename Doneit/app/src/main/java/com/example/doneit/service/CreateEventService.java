package com.example.doneit.service;

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


public class CreateEventService {

    private String token;

    public CreateEventService(String token){
        this.token = token;
    }
    public JSONObject makeCreateEventRequest(Event event){
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = SERVER_URL + CREATE_EVENT;

        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        RequestBody body = RequestBody.create(MEDIA_TYPE, gson.toJson(event));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return new JSONObject(response.body().string());
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
