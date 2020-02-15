package com.example.doneit.service;


import android.util.JsonReader;
import android.util.Log;

import com.example.doneit.ConvalidationActivity;
import com.example.doneit.model.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.SERVER_URL;
import static com.example.doneit.constants.ServerService.USER_IMAGE;

public class ImageService {

    public String getImage(String username, String token) throws IOException {
        String url = SERVER_URL + USER_IMAGE + "/" + username+ "/get-image-profile";

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
            ConvalidationActivity.print(response.toString());
            if (response.isSuccessful()) {
                String textResponse = response.body().string();
                Log.d("provapls", textResponse);
                return textResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return  null;
    }

}
