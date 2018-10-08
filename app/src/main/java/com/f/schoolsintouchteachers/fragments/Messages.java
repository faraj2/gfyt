package com.f.schoolsintouchteachers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f.schoolsintouchteachers.Individualchat;
import com.f.schoolsintouchteachers.R;

/**
 * Created by admin on 4/1/2018.
 */

public class Messages extends Fragment {
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chats_message_layout,container,false);
recyclerView=view.findViewById(R.id.recychatedlist);
recyclerView.setHasFixedSize(true);
LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
linearLayoutManager.setStackFromEnd(true);
linearLayoutManager.setReverseLayout(true);
recyclerView.setLayoutManager(linearLayoutManager);
recyclerView.setAdapter(new MyAdapterchatedlist());

        return view;
    }
    public class MyAdapterchatedlist extends RecyclerView.Adapter<Myholder>{

        @Override
        public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_recychattedlist,parent,false);
            return new Myholder(view);
        }

        @Override
        public void onBindViewHolder(Myholder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),Individualchat.class));
                }
            });

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class Myholder extends RecyclerView.ViewHolder {
        public Myholder(View itemView) {
            super(itemView);
        }
    }
}
