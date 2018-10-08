package com.f.schoolsintouchteachers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.f.schoolsintouchteachers.adapters.ReminderAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by admin on 2/6/2018.
 */

public class Reminderfragment extends Fragment {
    RecyclerView recyclerView;
   FloatingActionButton floatingActionButton;
    private MaterialCalendarView calndr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.reminder_layout,container,false);
        recyclerView=view.findViewById(R.id.recyreminder);
        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        calndr=view.findViewById(R.id.calendarView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ReminderAdapter(getActivity()));
//        floatingActionButton.bringToFront();
        Calendar calendar = Calendar.getInstance();
       // calendar.add(Calendar.MONTH, -2);
        ArrayList<CalendarDay> dates = new ArrayList<>();
      //  for (int i = 0; i < 30; i++) {
            CalendarDay day = CalendarDay.from(Calendar.YEAR,Calendar.JUNE,Calendar.FRIDAY);
            dates.add(CalendarDay.from(2018,5,20));
        dates.add(CalendarDay.from(2018,5,25));
        dates.add(CalendarDay.from(2018,5,21));

            calendar.add(Calendar.DATE, 5);
        //}
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH,0);
//        ArrayList<CalendarDay> days=new ArrayList<>();
//        CalendarDay day = CalendarDay.from(Calendar.YEAR,Calendar.JUNE,15);
//        CalendarDay day2 = CalendarDay.from(Calendar.YEAR,Calendar.JUNE,25);
//        days.add(day);
//        days.add(day2);
//        days.add(new CalendarDay(2018,06,20));
//        days.add(new CalendarDay(2018,06,25));
//        days.add(new CalendarDay(2018,06,28));

        calndr.addDecorator(new MyDecoco(Color.BLACK,dates));
        calndr.invalidateDecorators();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
              //  Log.d("NEWSTATE", String.valueOf(newState));

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("NEWSTATE", String.valueOf(dy));
                if (dy>0){
                    floatingActionButton.setVisibility(View.GONE);
                }else {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "pressed", Toast.LENGTH_SHORT).show();
               MyDialog dialog=new MyDialog();

                dialog.show(getFragmentManager(),"me");
//                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
//
//                PopupWindow popupWindow = new PopupWindow(popupView,
//                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                popupWindow.setBackgroundDrawable(new ColorDrawable());
//                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
            }
        });
        return view;
    }
    public class MyDecoco implements DayViewDecorator {
        private int color;
        private HashSet<CalendarDay> dates;

        public MyDecoco(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(15, color));
        }
    }
}
