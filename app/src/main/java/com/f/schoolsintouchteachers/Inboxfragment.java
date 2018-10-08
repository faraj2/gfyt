package com.f.schoolsintouchteachers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.f.schoolsintouchteachers.fragments.Messages;
import com.f.schoolsintouchteachers.fragments.Requests;

/**
 * Created by admin on 2/6/2018.
 */

public class Inboxfragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tableLayout;
    Myfragmentpager myfragmentpager;
    private FloatingActionButton fab;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.inbox_layout,container,false);
        viewPager=view.findViewById(R.id.container);
        tableLayout=view.findViewById(R.id.tabs);
        myfragmentpager=new Myfragmentpager(getChildFragmentManager());
        viewPager.setAdapter(myfragmentpager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
        tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
       // fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//
        return view;
    }
public class Myfragmentpager extends FragmentPagerAdapter{

    public Myfragmentpager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new Messages();

            case 1:
                return new Requests();

                default:
                    return new Messages();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

}
