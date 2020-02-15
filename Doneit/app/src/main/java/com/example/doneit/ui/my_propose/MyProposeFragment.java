package com.example.doneit.ui.my_propose;

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
import com.example.doneit.adapter.ProposeListAdapter;
import com.example.doneit.model.Todo;
import com.example.doneit.service.ProposeListService;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class MyProposeFragment extends Fragment {


    private View root;
    private ProposeListAdapter proposeListAdapter;
    ArrayList<Todo> todoMemory;
    SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.mypropose_fragment, container, false);
        prefs = this.getActivity().getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        getProposeList(); // get Todo list form server




        return root;
    }


    private void getProposeList() {
        String token = prefs.getString("token", "No name defined");
        ProposeListTask proposeListTask = new ProposeListTask(token);
        proposeListTask.execute();
    }

    private class ProposeListTask extends AsyncTask<Void,Void, List<Todo>> {

        private String token;

        ProposeListTask(String token){
            this.token = token;
        }
        @Override
        protected List<Todo> doInBackground(Void... voids) {
            ProposeListService proposeListService = new ProposeListService(token);
            List<Todo> response = proposeListService.getAllPropose();
            return response;
        }

        @Override
        protected void onPostExecute(List<Todo> result){
            //Toast.makeText(getActivity().getApplicationContext(),"provaaaaa",Toast.LENGTH_LONG).show();
            todoMemory = (ArrayList<Todo>) result;
            setRecycleView(result);

        }
    }


    private void setRecycleView(List<Todo> result) {
        // data to populate the RecyclerView with
        if(result!=null){

            // set up the RecyclerView
            RecyclerView recyclerView = root.findViewById(R.id.my_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            proposeListAdapter = new ProposeListAdapter(getActivity(), result, prefs);

            recyclerView.setAdapter(proposeListAdapter);
        }
    }

}