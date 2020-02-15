package com.example.doneit.convalidation;

import com.example.doneit.ConvalidationActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.SERVER_URL;

public class ConvalidationService {


    public String getConvalidationKey(String token, Long todoId) {
        String url = SERVER_URL + "/get-convalidation-key/" + todoId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .header("Content-Type", "text")
                .header("Authorization", "Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ConvalidationActivity.print(response.toString());
            if (response.isSuccessful()) {
                String textResponse = response.body().string();
                ConvalidationActivity.print(textResponse);
                return textResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public JSONObject askForConvalidation(Convalidation convalidation, String token) {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = SERVER_URL + "/convalidate-todo";
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(MEDIA_TYPE, gson.toJson(convalidation));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ConvalidationActivity.print(response.toString());
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string());
            } else {
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
