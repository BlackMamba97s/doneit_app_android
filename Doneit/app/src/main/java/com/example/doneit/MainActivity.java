package com.example.doneit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doneit.service.LoginService;
import com.example.doneit.service.RegisterService;
import com.facebook.FacebookSdk;

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
    private TextView redirectToRegister;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        if (preferences.contains("token")) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        redirectToRegister = findViewById(R.id.register_redirect);
        redirectToRegister.setClickable(true);
        redirectToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "prova", Toast.LENGTH_LONG).show();
                gotoRegisterActivity(v);
            }
        });

    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        3);
            }
        } else {
        }

    }


    public void handleLogin(View view) {
        Toast.makeText(getApplicationContext(), "bottone login funge", Toast.LENGTH_LONG).show();
        TextView usernameView = findViewById(R.id.username);
        TextView passwordView = findViewById(R.id.password);
        username = usernameView.getText().toString();
        final String password = passwordView.getText().toString();

        LoginTask loginTask = new LoginTask(username, password);
        loginTask.execute();

    }

    public void gotoRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void gotoHomeActivity(String token) {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.putString("username", username);
        editor.apply();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    public void showToast(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }


    private class LoginTask extends AsyncTask<Void, Void, JSONObject> {
        private String username;
        private String password;

        public LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            LoginService loginService = new LoginService();
            try {
                return loginService.makeAuthenticationRequest(username, password);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {

                    if (result.get("messageCode").equals(SUCCESSFUL_LOGIN) || result.get("messageCode").equals(FIRST_LOGIN)) {

                        gotoHomeActivity(result.get("token").toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("ERRORE NEL SERVER");
                }
            } else {
                showToast("CREDENZIALI ERRATE");
            }
        }


    }


}
