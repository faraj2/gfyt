package com.f.schoolsintouchteachers.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.chibde.visualizer.LineBarVisualizer;
import com.f.schoolsintouchteachers.R;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.varunest.sparkbutton.SparkButton;

public  class AudioViewholder extends RecyclerView.ViewHolder {
    LineBarVisualizer lineBarVisualizer;
    SparkButton button;
    TextView clap,datee;
    ImageButton play;
    DonutProgress donutProgress;
    ProgressBar progressBar;
    public AudioViewholder(View itemView) {
        super(itemView);
        lineBarVisualizer = itemView.findViewById(R.id.visualizer);
        button=itemView.findViewById(R.id.btnlike);
        clap=itemView.findViewById(R.id.claps);
        play=itemView.findViewById(R.id.ply);
        donutProgress=itemView.findViewById(R.id.donut);
        progressBar=itemView.findViewById(R.id.dont);
        datee=itemView.findViewById(R.id.textdate);

    }

    public void setdate(String postedtime) {
        if (TextUtils.isEmpty(postedtime)) {
            return;
        }

        long curTime = System.currentTimeMillis();
        long curTimesec = curTime / 1000;  //you can continue to do this if you want to get minutes, hours, days, etc
        long diff = curTimesec - Long.valueOf(postedtime);
        long minut, hour;
        if (diff > 60) {
            minut = diff / 60;
            if (minut > 60) {
                hour = minut / 60;
                if (hour > 24) {
                    long days = hour / 24;
                    datee.setText("posted on: " + days + " days ago");
                } else {
                    datee.setText("posted on: " + hour + " hours ago");
                }

            } else {
                datee.setText("posted on: " + minut + " minutes ago");
            }

        } else {
            datee.setText("posted on: " + diff + " sec ago");

        }

    }

    public void setclap(Integer claps) {
        clap.setText(claps + " claps");
    }

    public void setplay(String audio, Activity context) {
        lineBarVisualizer.setColor(ContextCompat.getColor(context, R.color.cardview_light_background));
        lineBarVisualizer.setDensity(70f);
        if (true){
            donutProgress.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

           // DisplayMetrics dm = itemView.getContext().getResources().getDisplayMetrics();

            final MediaPlayer mp=new MediaPlayer();
            try{
                //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                mp.setDataSource(audio);


                mp.prepare();

            }catch(Exception e){e.printStackTrace();}
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    lineBarVisualizer.setPlayer(mp.getAudioSessionId());

                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mp.isPlaying()){
                        mp.start();

                    }else {
                        mp.pause();


                    }
                }
            });
mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mp) {

       // mWaveView.stop();
    }
});
        }
    }
}
