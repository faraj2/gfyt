package com.f.schoolsintouchteachers;

import android.graphics.Color;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.f.schoolsintouchteachers.adapters.MylistAdapter;
import com.f.schoolsintouchteachers.fragments.Attendance;
import com.f.schoolsintouchteachers.fragments.Targets;
import com.f.schoolsintouchteachers.models.Models;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Reports extends AppCompatActivity {
    // Bundle bundle;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        //   bundle = new Bundle();
        RelativeLayout relativeLayout = findViewById(R.id.myreltvreprts);
        relativeLayout.addView(new BlueView(this));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void snapsht(DataSnapshot dataSnapshot) {
//
//        PlaceholderFragment placeholderFragment=new PlaceholderFragment(dataSnapshot);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack so the user can navigate back
//        transaction.replace(R.id.container, placeholderFragment);
//        transaction.addToBackStack(null);
//
//        // Commit the transaction
//        transaction.commit();
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements Reportfr.MyySnapshot {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        DataSnapshot snapshot;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
            Reports.fab.setVisibility(View.VISIBLE);
            RelativeLayout relativeLayout = rootView.findViewById(R.id.section_label);
            relativeLayout.addView(new BlueView(getContext()));
            LineChart lineChart = rootView.findViewById(R.id.chart);
            PieChart pieChart = rootView.findViewById(R.id.piechart);
            RecyclerView flabbyListView = rootView.findViewById(R.id.flabylist);
            flabbyListView.setLayoutManager(new LinearLayoutManager(getContext()));
            flabbyListView.setHasFixedSize(true);

            List<Entry> entries = new ArrayList<>();


            List<String> entries3 = new ArrayList<>();
            float g = 0;
            if (Models.getInstance().getSnapshot() != null) {
                for (DataSnapshot snapshot1 : Models.getInstance().getSnapshot().getChildren()) {
                    entries3.add(snapshot1.getValue().toString());
                    entries.add(new Entry(g, Float.parseFloat(snapshot1.getValue().toString())));
                    g++;
                    Log.d("DATASNAP", snapshot1.getValue().toString());
                    Toast.makeText(getContext(), snapshot1.getValue().toString(), Toast.LENGTH_SHORT).show();
                }
            }
//
//            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                Log.d("ONCREATEVIEW", snapshot1.getValue().toString());
////
////              //  Toast.makeText(getContext(),"from interface"+ snapshot1.getValue().toString(), Toast.LENGTH_SHORT).show();
//            }

//            entries3.add("20");
//            entries3.add("60");
//            entries3.add("80");
//            entries3.add("90");
//            entries3.add("80");
//            entries3.add("20");
//            entries3.add("60");
//            entries3.add("80");
//            entries3.add("90");
//            entries3.add("80");
//            entries3.add("20");
//            entries3.add("60");
//            entries3.add("80");
//            entries3.add("90");
//            entries3.add("80");
            MylistAdapter l = new MylistAdapter(getContext(), entries3);
            flabbyListView.setAdapter(l);


            List<PieEntry> entries2 = new ArrayList<>();

            entries2.add(new PieEntry(20f, "Maths"));
            entries2.add(new PieEntry(60f, "English"));
            entries2.add(new PieEntry(80f, "Science"));
            entries2.add(new PieEntry(90f, "Kiswahili"));
            entries2.add(new PieEntry(50f, "S.S"));

            PieDataSet set = new PieDataSet(entries2, "Second Term");
            ArrayList<Integer> myarray = new ArrayList<>();
            for (int c : ColorTemplate.JOYFUL_COLORS) {
                myarray.add(c);
            }
            set.setColors(myarray);
            PieData data = new PieData(set);
            pieChart.setData(data);
            pieChart.setHoleRadius(50f);
            pieChart.invalidate();


//            entries.add(new Entry(0f, 50f));
//            entries.add(new Entry(1f, 80f));
//            entries.add(new Entry(2f, 70f));
//            entries.add(new Entry(3f, 60f));
//            entries.add(new Entry(4f, 80f));

            List<Entry> entries1 = new ArrayList<>();
            entries1.add(new Entry(0f, 20f));
            entries1.add(new Entry(1f, 60f));
            entries1.add(new Entry(2f, 90f));
            entries1.add(new Entry(3f, 80f));
            entries1.add(new Entry(4f, 50f));

            LineDataSet dataSet = new LineDataSet(entries, "First term"); // add entries to dataset
            dataSet.setColor(Color.WHITE);
            dataSet.setValueTextColor(Color.BLACK);

            LineDataSet dataSet1 = new LineDataSet(entries1, "Second term"); // add entries to dataset
            dataSet.setColor(Color.BLACK);
            dataSet.setValueTextColor(Color.WHITE);

            // dataSet.setValueTextColor(...);
            final String[] quarters = new String[]{"Maths", "English", "Science", "Kiswahili", "S.S"};

            IAxisValueFormatter formatter = new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return quarters[(int) value];
                }

                // we don't draw numbers, so no decimal digits needed

            };

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);
            dataSets.add(dataSet1);
            LineData lineData = new LineData(dataSets);

            lineChart.setData(lineData);
            lineChart.invalidate();
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


        }

        @Override
        public void snapsht(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
                //        getActivity().getSupportFragmentManager().beginTransaction().commit();
                this.snapshot = dataSnapshot;

                stating(dataSnapshot);

            }

        }

        private void stating(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onResume() {
            super.onResume();
            fab.setVisibility(View.VISIBLE);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //  fab.setVisibility(View.GONE);
                    return new Targets();
                case 1:
                    //  fab.setVisibility(View.VISIBLE);
                    return new PlaceholderFragment();
                case 2:
                    //   fab.setVisibility(View.VISIBLE);
                    return new Attendance();
                default:
                    return new PlaceholderFragment();
            }
            // return PlaceholderFragment.newInstance(position + 1);
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
