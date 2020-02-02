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

import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class LogoutFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*
        logoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        logoutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
*/
        /* bottone usato per provare la logout

        logoutButton = root.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(onClickListener);

         */
        handleLogout();

        /*
        TodoListTask todoListTask = new TodoListTask(token);
        todoListTask.execute();*/
        return null;
    }

    public void handleLogout(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_LOGIN, 0);
        preferences.edit().remove("token").commit();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void handleCreateTodo(View view){
        Intent intent = new Intent(getActivity(), CreateTodoActivity.class);
        startActivity(intent);
    }




}