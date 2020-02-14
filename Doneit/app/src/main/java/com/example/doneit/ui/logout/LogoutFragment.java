package com.example.doneit.ui.logout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.doneit.CreateTodoActivity;
import com.example.doneit.MainActivity;
import com.example.doneit.R;

import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class LogoutFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        handleLogout();
        return null;
    }

    public void handleLogout(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_LOGIN, 0);
        preferences.edit().remove("token").commit();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        //Fragment fragment = getChildFragmentManager().findFragmentById(R.id.)
    }

}