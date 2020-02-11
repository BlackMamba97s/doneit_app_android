package com.example.doneit.ui.creation.todo_creation;

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

import com.example.doneit.R;
import com.example.doneit.model.Category;
import com.example.doneit.model.Todo;
import com.example.doneit.service.CreateTodoService;
import com.example.doneit.service.GetAllCategoriesService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;
import static com.example.doneit.constants.MessageCode.CFU_INSUFFICIENT;
import static com.example.doneit.constants.MessageCode.TODO_CREATED;

public class CreateTodoFragment extends Fragment {

    private SharedPreferences prefs;
    private View root;
    private String formattedDate;
    Button addTodo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.todo_creation, container, false);
        prefs = getActivity().getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
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
        Date date = new Date();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(df.format(c));
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
            GetAllCategoriesService getAllCategoriesService = new GetAllCategoriesService(token);
            List<Category> categories= getAllCategoriesService.getAllCategories();


            Todo todo = new Todo(title,description,categories.get(0));
            CreateTodoService createTodoService = new CreateTodoService(token);
            JSONObject jsonResponseTodo = createTodoService.makeCreateTodoRequest(todo);

            return jsonResponseTodo;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result != null){
                try {
                    if(result.get("messageCode").equals(TODO_CREATED)){
                        showFragmentToast("Todo creato correttamente");
                    }else if (result.get("messageCode").equals(CFU_INSUFFICIENT)){
                        showFragmentToast(("credito insufficiente"));
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