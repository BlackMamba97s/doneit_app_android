package com.example.doneit.ui.creation;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doneit.ui.creation.event_creation.CreateEventFragment;
import com.example.doneit.ui.creation.todo_creation.CreateTodoFragment;


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
                CreateTodoFragment tab1 = new CreateTodoFragment();
                return tab1;
            case 1:
                CreateEventFragment tab2 = new CreateEventFragment();
                return  tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
