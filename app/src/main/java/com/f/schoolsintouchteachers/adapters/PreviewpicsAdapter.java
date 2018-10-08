package com.f.schoolsintouchteachers.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f.schoolsintouchteachers.R;

import java.util.ArrayList;

public class PreviewpicsAdapter extends RecyclerView.Adapter<PreviewpicsAdapter.Myholderpics> {
    Context context;
    ArrayList<Uri> mylist;
    RecyclerView recyclerView;

    public PreviewpicsAdapter(Context context,ArrayList<Uri> list) {
        this.context = context;
        this.mylist=list;
    }

    @NonNull
    @Override
    public Myholderpics onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.pics_recylerview,parent,false);
        return new Myholderpics(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView=recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull final Myholderpics holder, final int position) {
holder.setimage(mylist.get(position));
holder.delte.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mylist.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        if (mylist.size()==0){

           recyclerView.setVisibility(View.GONE);
        }
    }
});

    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class Myholderpics extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton delte;
        public Myholderpics(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.picspreview);
            delte=itemView.findViewById(R.id.imgdelete);
        }

        public void setimage(Uri uri) {
            Glide.with(context).load(uri).into(imageView);
        }
    }
}
