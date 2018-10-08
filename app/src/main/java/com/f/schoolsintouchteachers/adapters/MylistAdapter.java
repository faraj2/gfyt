package com.f.schoolsintouchteachers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f.schoolsintouchteachers.R;

import java.util.List;
import java.util.Random;

/**
 * Created by admin on 3/13/2018.
 */

public class MylistAdapter extends RecyclerView.Adapter<MylistAdapter.Viewholder> {
    private List<String> mylist;
    private Context context;

    Random r=new Random();

    public MylistAdapter(@NonNull Context context,List<String> mylist) {

        this.mylist=mylist;
        this.context=context;
    }



    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
       View convertView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
       Viewholder viewholder=new Viewholder(convertView);
        int i= Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
       viewholder.itemView.setBackgroundColor(i);
        return viewholder ;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
holder.textView.setText(mylist.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        TextView textView;
        public Viewholder(View view){
            super(view);
            textView=view.findViewById(R.id.textt);
        }
    }
}
