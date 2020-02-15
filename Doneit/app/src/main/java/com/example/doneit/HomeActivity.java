package com.example.doneit;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.doneit.adapter.TodoListAdapter;
import com.example.doneit.model.User;
import com.example.doneit.service.ImageService;
import com.example.doneit.service.UserService;
import com.example.doneit.ui.creation.CreationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class HomeActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private SharedPreferences prefs;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        prefs = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        String token = prefs.getString("token", "No name defined");
        UserTask userTask = new UserTask(token);
        userTask.execute();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_creation, R.id.nav_my_todo,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private class UserTask extends AsyncTask<Void,Void, User> {

        private String token;

        UserTask(String token){
            this.token = token;
        }
        @Override
        protected User doInBackground(Void... voids) {
            UserService userService = new UserService(token);
            User user = userService.getUser();
            return user;

        }

        @Override
        protected void onPostExecute(User result){
            //Toast.makeText(getActivity().getApplicationContext(),"provaaaaa",Toast.LENGTH_LONG).show();
            username = result.getUsername();
            ImageTask imageTask = new ImageTask(username, token);
            imageTask.execute();
            setUserCredential(result);
        }
    }

    private class ImageTask extends AsyncTask<Void,Void, String> {

        private String token;
        private String username;

        ImageTask(String username, String token){
            this.username = username;
            this.token = token;
        }
        @Override
        protected String doInBackground(Void... voids) {
            ImageService imageService = new ImageService();
            String imageBase64 = null;
            try {
                imageBase64 = imageService.getImage(username, token);
            } catch (IOException e) {
                e.printStackTrace();
            }
                   Log.d("HomeActivity", imageBase64);
            return imageBase64;

        }

        @Override
        protected void onPostExecute(String result){
            insertUserImage(result);
            //Toast.makeText(getActivity().getApplicationContext(),"provaaaaa",Toast.LENGTH_LONG).show();
            //SetUserCredential(result);
        }
    }


    private void setUserCredential(User result) {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nome_cognome);
        TextView navEmail = headerView.findViewById(R.id.email);
        navUsername.setText(result.getName() + " " + result.getSurname());
        navEmail.setText(result.getEmail());
    }

    private void insertUserImage(String encodedString){
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap profileImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView imageView = headerView.findViewById(R.id.profile_image);
        imageView.setImageBitmap(profileImage);

    }

}
