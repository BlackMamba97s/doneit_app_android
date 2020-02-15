package com.example.doneit.ui.my_todos.published;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doneit.R;
import com.example.doneit.adapter.MyTodoAdapter;
import com.example.doneit.model.Todo;
import com.example.doneit.service.MyTodoService;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class PublishedTodoFragment extends Fragment {



    private View root;
    private MyTodoAdapter myTodoAdapter;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.allstate_fragment, container, false);

        getMyTodosList(); // get Todo list form server

        return root;
    }

    private void getMyTodosList() {
        prefs = this.getActivity().getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        String token = prefs.getString("token", "No name defined");
        MyTodosTask myTodosTask = new MyTodosTask(token);
        myTodosTask.execute();
    }




    private class MyTodosTask extends AsyncTask<Void,Void, List<Todo>> {

        private String token;

        MyTodosTask(String token){
            this.token = token;
        }
        @Override
        protected List<Todo> doInBackground(Void... voids) {
            MyTodoService myTodoService = new MyTodoService(token, "accepted");
            List<Todo> response = myTodoService.getMyTodo();
            return response;
        }

        @Override
        protected void onPostExecute(List<Todo> result){
            //Toast.makeText(getActivity().getApplicationContext(),"provaaaaa",Toast.LENGTH_LONG).show();
            setRecycleView(result);
        }
    }


    private void setRecycleView(List<Todo> result) {
        // data to populate the RecyclerView with
        if(result!=null){

            // set up the RecyclerView
            RecyclerView recyclerView = root.findViewById(R.id.my_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            myTodoAdapter = new MyTodoAdapter(getActivity(), result, prefs);
            recyclerView.setAdapter(myTodoAdapter);
        }
    }
}
