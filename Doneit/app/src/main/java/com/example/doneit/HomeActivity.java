package com.example.doneit;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.doneit.constants.Client.*;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        String name = prefs.getString("token", "No name defined");//"No name defined" is the default value.
        Log.d("token: ", name);

    }

    public void handleLogout(View view){
        SharedPreferences preferences = getSharedPreferences(SHARED_LOGIN, 0);
        preferences.edit().remove("token").commit();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
