package com.example.doneit.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.*;


public class RegisterService {
    public JSONObject makeRegisterRequest(String username, String password){
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = SERVER_URL + REGISTER_USER;

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username", username);
            postdata.put("password", password);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
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
