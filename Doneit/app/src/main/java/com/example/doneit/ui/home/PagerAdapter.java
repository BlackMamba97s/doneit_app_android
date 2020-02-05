package com.example.doneit.ui.home;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doneit.ui.share.ShareFragment;
import com.example.doneit.ui.todolist.TodoListFragment;
import com.example.doneit.ui.todos.TodosFragment;


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
                TodoListFragment tab2 = new TodoListFragment();
                return  tab2;
            case 2:
                ShareFragment tab3 = new ShareFragment();
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
