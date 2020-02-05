package com.example.doneit.ui.todos;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doneit.R;
import com.example.doneit.adapter.TodoListAdapter;
import com.example.doneit.model.Todo;
import com.example.doneit.service.TodoService;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class TodosFragment extends Fragment implements TodoListAdapter.ItemClickListener{


    private View root;
    private TodoListAdapter todoListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.todos_fragment, container, false);

        getTodoList(); // get Todo list form server

        return root;
    }

    private void getTodoList() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        String token = prefs.getString("token", "No name defined");
        TodoListTask todoListTask = new TodoListTask(token);
        todoListTask.execute();
    }

    private class TodoListTask extends AsyncTask<Void,Void, List<Todo>> {

        private String token;

        TodoListTask(String token){
            this.token = token;
        }
        @Override
        protected List<Todo> doInBackground(Void... voids) {
            TodoService todoService = new TodoService(token);
            List<Todo> response = todoService.getAllTodo();
            return response;
        }

        @Override
        protected void onPostExecute(List<Todo> result){
            //Toast.makeText(getActivity().getApplicationContext(),"provaaaaa",Toast.LENGTH_LONG).show();
            showResponse(result);
            setRecycleView(result);

        }
    }
    public void showResponse(List<Todo> response){
        if(response != null) {
            for (Todo todo : response) {
                Log.d("response", "" + response);
            }
        }

    }

    private void setRecycleView(List<Todo> result) {
        // data to populate the RecyclerView with
        ArrayList<String> todos = new ArrayList<>();
        for(Todo todo : result){
            todos.add(todo.getTitle());
        }

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        todoListAdapter = new TodoListAdapter(getActivity(), todos);
        todoListAdapter.setClickListener(this);
        recyclerView.setAdapter(todoListAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "You clicked " + todoListAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
