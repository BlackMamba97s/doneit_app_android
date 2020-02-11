package com.example.doneit.service;

import com.example.doneit.model.Category;
import com.example.doneit.model.Todo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.ALL_CATEGORIES;
import static com.example.doneit.constants.ServerService.SERVER_URL;

public class GetAllCategoriesService {
    private String token;

    public GetAllCategoriesService(String token){
        this.token = token;
    }


    public List<Category> getAllCategories(){
        String url = SERVER_URL + ALL_CATEGORIES;

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
                Type listType = new TypeToken<List<Category>>() {}.getType();
                List<Category> categoryList = new Gson().fromJson(jsonResponse, listType);
                return categoryList;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
