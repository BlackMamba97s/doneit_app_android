package com.example.doneit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.doneit.constants.ServerService.*;
import static com.example.doneit.constants.Client.*;
import static com.example.doneit.constants.MessageCode.*;


public class MainActivity extends AppCompatActivity {

    private JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         SharedPreferences preferences = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
         if(preferences.contains("token")){
             Intent intent = new Intent(this, HomeActivity.class);
             startActivity(intent);
         }

    }


    public void handleLogin(View view){
        TextView usernameView = findViewById(R.id.username);
        TextView passwordView = findViewById(R.id.password);
        final String username = usernameView.getText().toString();
        final String password = passwordView.getText().toString();

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    makeAuthenticationRequest(username,password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkResponseFromServer();

    }

    public void gotoRegisterActivity(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void makeAuthenticationRequest(String username, String password) throws IOException{
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = SERVER_URL + AUTHENTICATE;

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

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()){
            try {
                jsonResponse = new JSONObject(response.body().string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            jsonResponse = null;
        }

    }

    public void checkResponseFromServer(){
        if(jsonResponse != null) {
            try {
                if (jsonResponse.get("messageCode").equals(SUCCESSFUL_LOGIN)) {
                    SharedPreferences.Editor editor = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE).edit();
                    editor.putString("token",  jsonResponse.get("token").toString());
                    editor.apply();

                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "CREDENZIALI ERRATE", Toast.LENGTH_SHORT).show();
        }
    }


}
