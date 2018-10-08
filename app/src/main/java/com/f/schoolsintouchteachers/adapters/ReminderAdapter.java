package com.f.schoolsintouchteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f.schoolsintouchteachers.R;

/**
 * Created by admin on 2/9/2018.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewholder> {
    Context context;

    public ReminderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1= LayoutInflater.from(context).inflate(R.layout.reminder_layout_view,parent,false);
        return new MyViewholder(view1);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        public MyViewholder(View itemView) {
            super(itemView);
        }
    }
}
