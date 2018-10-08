package com.f.schoolsintouchteachers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f.schoolsintouchteachers.R;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.Random;

/**
 * Created by admin on 3/19/2018.
 */

public class TargetsAdapter extends RecyclerView.Adapter<TargetsAdapter.MyHolder> implements IndicatorSeekBar.OnSeekBarChangeListener {
    Context context;
    Random r = new Random();
    Integer j = 0;
    String[] subs = {"MATHS", "ENGLISH", "KISWAHILI", "SCIENCE", "SOCIAL.S"};
    private View v;


    public TargetsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.myseeker, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        int i = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
        holder.indicatorSeekBar.getBuilder().setIndicatorColor(i).setTextColor(i).setProgressTrackColor(i).setThumbColor(i).apply();
        holder.textView.setText(subs[j]);

        holder.textView.setTextColor(i);
        j++;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {

    }

    @Override
    public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

    }

    @Override
    public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

    }

    @Override
    public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        IndicatorSeekBar indicatorSeekBar;
        TextView textView;

        public MyHolder(View itemView) {
            super(itemView);

            indicatorSeekBar = itemView.findViewById(R.id.myindicator);
            textView = itemView.findViewById(R.id.mytextseek);
        }
    }
}
