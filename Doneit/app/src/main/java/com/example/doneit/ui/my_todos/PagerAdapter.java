package com.example.doneit.ui.my_todos;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doneit.ui.creation.event_creation.CreateEventFragment;
import com.example.doneit.ui.creation.todo_creation.CreateTodoFragment;
import com.example.doneit.ui.my_todos.accepted.AcceptedTodoFragment;
import com.example.doneit.ui.my_todos.completed.CompletedTodoFragment;
import com.example.doneit.ui.my_todos.pending.PendingTodoFragment;
import com.example.doneit.ui.my_todos.published.PublishedTodoFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                PublishedTodoFragment tab1 = new PublishedTodoFragment();
                return  tab1;

            case 1:
                AcceptedTodoFragment tab2 = new AcceptedTodoFragment();
                return  tab2;
            case 2:
                CompletedTodoFragment tab3 = new CompletedTodoFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
