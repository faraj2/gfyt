package com.f.schoolsintouchteachers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by admin on 4/1/2018.
 */

public class Individualchat extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText text;
    ArrayList<String> data=new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indivdual_layout);
        recyclerView=findViewById(R.id.recyindividual);
        RelativeLayout relativeLayout=findViewById(R.id.reltvindvdual);
        relativeLayout.addView(new BlueView(this));
        text=findViewById(R.id.writemessage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.scrollToPosition(data.size()-1);
            }
        });

//
//        textView.setText(

    }
    public void sendmessage(View view){
        if (!TextUtils.isEmpty(text.getText().toString())){
         data.add(text.getText().toString());
            recyclerView.setAdapter(new Adapter<MyHolder>() {
                @Override
                public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view=LayoutInflater.from(getBaseContext()).inflate(R.layout.message_layout,parent,false);
                    return new MyHolder(view);
                }

                @Override
                public void onBindViewHolder(MyHolder holder, int position) {
                    holder.textView.setText(data.get(position) + " \u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0"); // 10 spaces
                }

                @Override
                public int getItemCount() {
                    return data.size();
                }
            });

        }

    }
    private class MyHolder extends ViewHolder{

       public final TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
             textView = itemView.findViewById(R.id.txtMsgFrom);
        }
    }
}
