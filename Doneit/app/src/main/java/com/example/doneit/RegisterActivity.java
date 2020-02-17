package com.example.doneit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doneit.service.RegisterService;

import org.json.JSONException;
import org.json.JSONObject;
import static com.example.doneit.constants.MessageCode.*;

public class RegisterActivity extends AppCompatActivity {

    private TextView redirectToLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        redirectToLogIn = findViewById(R.id.register_redirect);
        redirectToLogIn.setClickable(true);
        redirectToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"prova",Toast.LENGTH_LONG).show();
                goToLogin();
            }
        });
    }

    public void handleRegister(View view){
        TextView emailView = findViewById(R.id.email);
        TextView nameView = findViewById(R.id.name);
        TextView surnameView = findViewById(R.id.surname);
        TextView passwordView = findViewById(R.id.password);
        String password = passwordView.getText().toString();
        String name = nameView.getText().toString();
        String surname = surnameView.getText().toString();
        String email = emailView.getText().toString();
        String username = name + "." + surname;
        RegisterTask registerTask = new RegisterTask(username, name, surname, email, password);
        registerTask.execute();
    }


    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private class RegisterTask extends AsyncTask<Void, Void, String> {
        private String username;
        private String password;
        private String name;
        private String surname;
        private String email;

        public RegisterTask(String username, String name, String surname, String email, String password){
            this.username = username;
            this.password = password;
            this.name = name;
            this.surname = surname;
            this.email = email;
        }
        @Override
        protected String doInBackground(Void... voids) {
            RegisterService registerService = new RegisterService();
            JSONObject jsonResponse = registerService.makeRegisterRequest(username, name, surname, email, password);
            if(jsonResponse != null){
                try {
                    if(jsonResponse.get("messageCode").equals(SUCCESSFUL_REGISTER)) {
                        return "REGISTRAZIONE AVVENUTA CON SUCCESSO";
                    }else{
                        return "ALTRO TIPO DI ERRORE";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                return "ERRORE NEL CONTATTARE IL SERVER";
            }
            return "ERRORE";
        }

        @Override
        protected void onPostExecute(String result) {
            showToast(result);
            if(result.equals("REGISTRAZIONE AVVENUTA CON SUCCESSO")){
                goToLogin();
                finish();
            }

        }

    }

    public void goToLogin(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

}
