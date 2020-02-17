package com.example.doneit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doneit.R;
import com.example.doneit.model.Todo;
import com.example.doneit.service.AddPartecipationService;
import com.example.doneit.service.ImageService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.doneit.constants.MessageCode.PROPOSAL_CREATED;
import static com.example.doneit.constants.MessageCode.TODO_CREATED;
import static com.facebook.FacebookSdk.getApplicationContext;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<Todo> mTodos;
    private LayoutInflater mInflater;
    private SharedPreferences prefs;
    private View upperView;

    // potrei ottimizzare il tutto qua, dataè una lista di titoli dei todo
    // quindi tecnicamente mi basta solo todos
    public TodoListAdapter(Context context, List<Todo> todos, SharedPreferences prefs) {
        this.mInflater = LayoutInflater.from(context);
        this.mTodos = todos;
        this.prefs = prefs;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.todo_list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String todo_title = mTodos.get(position).getTitle();
        holder.titleTextView.setText(todo_title);
        holder.descriptionTextView.setText(mTodos.get(position).getDescription());
        String username = prefs.getString("username", "No name defined");
        if(isOwner(position, username)){
            holder.partecipateTodo.setText("Non puoi proporti al tuo stesso todo");
            holder.partecipateTodo.setTextSize(10);
            holder.partecipateTodo.setClickable(false);
            holder.partecipateTodo.setTextColor(R.color.disableButton);
        }
        holder.ownerTextView.setText(mTodos.get(position).getCategory().getCfuPrice() + " CFU");
        //holder.profileImage.setImageBitmap(getProfilePicture(mTodos.get(position).getUser().getUsername()));
        holder.partecipateTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOwner(position, username)){
                    //Toast.makeText(v.getContext(), mTodos.get(position).toString(),  Toast.LENGTH_SHORT).show();
                    String token = prefs.getString("token", "No name defined");
                    checkAsyncStatus(position, token);

                }
            }
        });

    }

    private void checkAsyncStatus(int position, String token) {
        AddPartecipationTodo addPartecipationTodo = new AddPartecipationTodo(mTodos.get(position), token);
        if(addPartecipationTodo.getStatus() == AsyncTask.Status.RUNNING){
            Toast.makeText(getApplicationContext(), "async is running", Toast.LENGTH_SHORT).show();
        }

        if(addPartecipationTodo.getStatus() == AsyncTask.Status.FINISHED ||
                addPartecipationTodo.getStatus() == AsyncTask.Status.PENDING){
            addPartecipationTodo.execute();
        }
    }

    private boolean isOwner(int position, String username) {
        return mTodos.get(position).getUser().getUsername().equals(username);
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mTodos.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button partecipateTodo;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView ownerTextView;
        CircleImageView profileImage;


        ViewHolder(View itemView) {
            super(itemView);
            upperView = itemView;
            titleTextView = itemView.findViewById(R.id.card_title);
            descriptionTextView = itemView.findViewById(R.id.card_description);
            partecipateTodo = itemView.findViewById(R.id.partecipate);
            ownerTextView = itemView.findViewById(R.id.owner);
            profileImage = itemView.findViewById(R.id.profile_image_todo);
        }

    }

    private class ImageTask extends AsyncTask<Void,Void, String> {

        private String token;
        private String username;

        ImageTask(String username, String token) {
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
    }

    public class AddPartecipationTodo extends AsyncTask<Void,Void, JSONObject> {

        private String token;
        private Todo todo;

        public AddPartecipationTodo(Todo todo, String token){
            this.todo = todo;
            this.token = token;
        }

        @Override
        protected JSONObject doInBackground(Void... voids){
            AddPartecipationService addPartecipationService = new AddPartecipationService(token);
            JSONObject jsonResponseTodo = null;
            try {
                jsonResponseTodo = addPartecipationService.makeRequestTodo(todo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponseTodo;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result != null) {
                try {
                    if(result.get("messageCode").equals(PROPOSAL_CREATED)){
                        showFragmentToast("proposta creata", true);
                    }else if(result.get("messageCode").equals(TODO_CREATED)){
                        showFragmentToast("non puoi proporti sul tuo stesso Todo", false);
                    }else{
                        showFragmentToast("qualcosa è andato storto",false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{
                showFragmentToast("ti sei già proposto per questo Todo", false);
            }
           showFragmentToast("ti sei già proposto per questo Todo", false);
        }
    }


    public void showFragmentToast(String text, boolean positive){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast,  upperView.findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        //ImageView toastImage = layout.findViewById(R.id.toast_image);
        if(!positive)
            layout.setBackgroundResource(R.drawable.toast_background_negative);

        toastText.setText(text);
        //toastImage.setImageResource(R.drawable.baldons);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 176);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
    public void showToast() {

    }

}