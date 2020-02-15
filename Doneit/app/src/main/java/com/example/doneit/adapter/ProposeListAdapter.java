package com.example.doneit.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doneit.ConvalidationActivity;
import com.example.doneit.R;
import com.example.doneit.model.Todo;
import com.example.doneit.service.AddPartecipationService;
import com.facebook.share.Share;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;
import static com.example.doneit.constants.MessageCode.PROPOSAL_CREATED;
import static com.example.doneit.constants.MessageCode.TODO_CREATED;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ProposeListAdapter extends RecyclerView.Adapter<ProposeListAdapter.ViewHolder> {

    private List<Todo> mTodos;
    private LayoutInflater mInflater;
    private SharedPreferences prefs;

    // potrei ottimizzare il tutto qua, dataè una lista di titoli dei todo
    // quindi tecnicamente mi basta solo todos
    public ProposeListAdapter(Context context, List<Todo> todos, SharedPreferences prefs) {
        this.mInflater = LayoutInflater.from(context);
        this.mTodos = todos;
        this.prefs = prefs;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.proposal_card_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String todo_title = mTodos.get(position).getTitle();
        holder.titleTextView.setText(todo_title);
        holder.descriptionTextView.setText(mTodos.get(position).getDescription());
        holder.cfuTextView.setText(Integer.toString(mTodos.get(position).getCategory().getCfuPrice())+ " CFU");
        holder.convalidateTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long id = mTodos.get(position).getId();
                String title = mTodos.get(position).getTitle();
                Intent intent = new Intent(getApplicationContext(), ConvalidationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("todoName", title);
                intent.putExtra("todoId", id);
                intent.putExtra("Owner", false);


                getApplicationContext().startActivity(intent);
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mTodos.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button convalidateTodo;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView cfuTextView;


        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.card_title);
            descriptionTextView = itemView.findViewById(R.id.card_description);
            convalidateTodo = itemView.findViewById(R.id.validate_todo);
            cfuTextView = itemView.findViewById(R.id.owner);
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
                        showFragmentToast("proposta creata");
                    }else if(result.get("messageCode").equals(TODO_CREATED)){
                        showFragmentToast("non puoi proporti sul tuo stesso Todo");
                    }else{
                        showFragmentToast("qualcosa è andato storto");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showFragmentToast(String toast){
        Log.d("prova", " sono entrato qua");
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();

    }

}