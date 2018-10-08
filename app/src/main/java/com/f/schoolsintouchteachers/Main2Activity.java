package com.f.schoolsintouchteachers;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.ViewAnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.f.schoolsintouchteachers.firebase.Config;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {
    private Runnable runnable;
    private Handler handler;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final FrameLayout layout=findViewById(R.id.consta);
        RelativeLayout m=findViewById(R.id.emiter_top_leftt);

       BlueView myvi=new BlueView(this);


        m.addView(myvi);
        //  m.setBackground(getResources().getDrawable(R.drawable.books));
        float height = getWindowManager().getDefaultDisplay().getHeight();
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -height, 0f);
        translateAnimation.setDuration(5000);
        translateAnimation.setStartOffset(1000);


        ImageView h=findViewById(R.id.imageView2);
        ImageView t=findViewById(R.id.imageView3);
        t.setAnimation(translateAnimation);
        //   button.performClick();
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Main2Activity.this,MainActivity.class);
                if(user!=null){
                     i=new Intent(Main2Activity.this,Main4Activity.class);
                }

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        };
        handler.postDelayed(runnable,8000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    @Override
    protected void onStart() {
        super.onStart();
      user=  Config.auth.getCurrentUser();
    }
}
