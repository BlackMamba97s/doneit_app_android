package com.example.doneit.ui.home;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doneit.ui.event_list.EventsFragment;
import com.example.doneit.ui.my_propose.MyProposeFragment;
import com.example.doneit.ui.todo_list.TodosFragment;


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
                TodosFragment tab1 = new TodosFragment();
                return tab1;
            case 1:
                EventsFragment tab2 = new EventsFragment();
                return  tab2;
            case 2:
                MyProposeFragment tab3 = new MyProposeFragment();
                return  tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
