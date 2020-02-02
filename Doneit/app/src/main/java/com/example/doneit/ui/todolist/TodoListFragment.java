package com.example.doneit.ui.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.doneit.CreateTodoActivity;
import com.example.doneit.HomeActivity;
import com.example.doneit.R;
import com.example.doneit.model.Todo;
import com.example.doneit.service.CreateTodoService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;
import static com.example.doneit.constants.MessageCode.TODO_CREATED;

public class TodoListFragment extends Fragment {

    private TodoListViewModel todoListViewModel;
    private SharedPreferences prefs;
    private View root;
    private String formattedDate;
    Button addTodo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todoListViewModel =
                ViewModelProviders.of(this).get(TodoListViewModel.class);
        root = inflater.inflate(R.layout.fragment_todo_list, container, false);
        prefs = getActivity().getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        final TextView textView = root.findViewById(R.id.current_date);
        todoListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(getCurrentDate());
            }
        });
        addTodo = root.findViewById(R.id.create_todo);
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTodoCreationRequest();

            }
        });
        return root;
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return formattedDate = df.format(c);
    }

    private void sendTodoCreationRequest() {

        // get all information i need from form, like title description and the token from shared memory
        String token = prefs.getString("token", "No name defined");
        TextView todoTitle = root.findViewById(R.id.titleText);
        TextView descriptionText = root.findViewById(R.id.descriptionText);
        String title = todoTitle.getText().toString();
        String description = descriptionText.getText().toString();

        // start asynctask
        CreateTodoTask createTodoTask = new CreateTodoTask(title,description,token);
        createTodoTask.execute();
    }

    public class CreateTodoTask extends AsyncTask<Void,Void, JSONObject> {

        private String title;
        private String description;
        private String token;

        public CreateTodoTask(String title, String description, String token){
            this.title = title;
            this.description = description;
            this.token = token;
        }

        @Override
        protected JSONObject doInBackground(Void... voids){
            Todo todo = new Todo(title,description);
            CreateTodoService createTodoService = new CreateTodoService(token);
            JSONObject jsonResponse = createTodoService.makeCreateTodoRequest(todo);

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result != null){
                try {
                    if(result.get("messageCode").equals(TODO_CREATED)){
                        showFragmentToast("Todo creato correttamente");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showFragmentToast("Errore nella creazione del todo");
                }

            }
        }
    }

    public void showFragmentToast(String toast){
        Toast.makeText(getActivity().getApplicationContext(), toast, Toast.LENGTH_LONG).show();

    }
}