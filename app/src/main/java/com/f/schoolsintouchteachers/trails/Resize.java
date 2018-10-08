package com.f.schoolsintouchteachers.trails;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.f.schoolsintouchteachers.CompressImage;
import com.f.schoolsintouchteachers.R;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.codetail.animation.ViewAnimationUtils;

public class Resize extends AppCompatActivity {

    private ImageView resize,actual;
    private CompressImage image;
    DonutProgress donutProgress;
    private Animator animator;
    private View myView;
    private float finalRadius;
    int cx,cy;

    private final Runnable revealAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            int cx = myView.getRight();
            int cy = myView.getBottom();

            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
            if (myView.getVisibility()==View.VISIBLE){
                 animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        myView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }else if (myView.getVisibility()==View.INVISIBLE){
                 animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();
                myView.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myView.removeCallbacks(revealAnimationRunnable);
        animator.removeAllListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize);
        resize=findViewById(R.id.resize);
        actual=findViewById(R.id.actual);
        donutProgress=findViewById(R.id.donut);

         myView = findViewById(R.id.awesome_card);

        // get the center for the clipping circle
         cx = (myView.getLeft() + myView.getRight()) / 2;
         cy = (myView.getTop() + myView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, myView.getWidth() - cx);
        int dy = Math.max(cy, myView.getHeight() - cy);
        finalRadius = (float) Math.hypot(dx, dy);

for (int i=1;i<6;i++){
    final int j=i;
    new Handler().post(new Runnable() {
        @Override
        public void run() {
         int h=j;
         Log.d("FROM RUNNAMBLE "+String.valueOf(j),String.valueOf(h));
            try {
                Thread.sleep(200);
                Log.d("NEW VALUE",String.valueOf(h));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
}

        donutProgress.setVisibility(View.GONE);
       // startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),12);
       //  image=new CompressImage(this);
       // setresize();
    }
public void plyy(final View view){
        myView.post(revealAnimationRunnable);
//    animator =
//            ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
//    animator.setInterpolator(new AnticipateOvershootInterpolator());
//    animator.setDuration(1000);
//animator.start();
        view.setVisibility(View.GONE);
        donutProgress.setVisibility(View.VISIBLE);
        donutProgress.setProgress(10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                donutProgress.setProgress(20);
            }
        },4000);
        donutProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donutProgress.setProgress(0);
                donutProgress.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);

            }
        });

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==12 && resultCode==RESULT_OK){
            Uri g=data.getData();
            Uri newuri=g;
          Glide.with(Resize.this).load(g).into(actual);
            Glide.with(Resize.this).asBitmap().load("https://firebasestorage.googleapis.com/v0/b/schoolsintouch-d84bb.appspot.com/o/Schools%2FAdanans%20school%2F20180913235247848%2F1536871964109.jpg.png?alt=media&token=4af72307-97b5-4c28-bf43-12900e6c9f54")
                   .apply(new RequestOptions().override((int)(250*getResources().getDisplayMetrics().density), (int) (250*getResources().getDisplayMetrics().density))) .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            resize.setImageBitmap(resource);
                            try {
                                File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mine.png");
                                if (!file.exists()){
                                    file.createNewFile();
                                }
                                FileOutputStream out = new FileOutputStream(file);
                                resource.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                                out.close();
                                Toast.makeText(Resize.this, "DONE", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
//            try {
//                String comp=image.compressImage(newuri.toString());
//                String path= image.getRealPathFromURI(g.toString());
//                File file=new File(path);
//                Log.d("ACTUAKSIZE",String.valueOf(file.length()/1024));
//                Bitmap bitmap=new Compressor(Resize.this).setMaxWidth(50).setMaxHeight(50)
//                        .setQuality(100).setCompressFormat(Bitmap.CompressFormat.PNG).compressToBitmap(file);
//                Glide.with(Resize.this).asBitmap().load(comp).into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                       resize.setImageBitmap(resource);
//                        Log.d("BITMAPCOUNT", String.valueOf( BitmapCompat.getAllocationByteCount(resource)/1024));
//
//                    }
//                });
////          Glide.with(Resize.this).asBitmap().load(newuri).apply(new RequestOptions().override((int) (20*getResources().getDisplayMetrics().density),(int) (20*getResources().getDisplayMetrics().density))).into(new SimpleTarget<Bitmap>() {
////              @Override
////              public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
////                  resize.setImageBitmap(resource);
////
////              }
////          });
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void setresize() {
       final File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mine.png");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     StorageReference reference= FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/schoolsintouch-d84bb.appspot.com/o/Schools%2FAdanans%20school%2F20180913235247848%2F1536871964109.jpg.png?alt=media&token=4af72307-97b5-4c28-bf43-12900e6c9f54");
reference.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
        if (task.isSuccessful()){
            Toast.makeText(Resize.this, "done", Toast.LENGTH_SHORT).show();
            Log.d("BITMAPS",String.valueOf(file.length()/1024));
        }
    }
});
        Glide.with(Resize.this).asBitmap().load("https://firebasestorage.googleapis.com/v0/b/schoolsintouch-d84bb.appspot.com/o/Schools%2FAdanans%20school%2F20180913235247848%2F1536871964109.jpg.png?alt=media&token=4af72307-97b5-4c28-bf43-12900e6c9f54")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        float width=resource.getWidth();
//                        float height=resource.getHeight();
                        actual.setImageBitmap(resource);
                        Log.d("BITMAPS",String.valueOf(resource.getByteCount()/1024));
                        float widthsc=250 * getResources().getDisplayMetrics().density;
                        Bitmap bitmap=Bitmap.createScaledBitmap(resource, Math.round(widthsc),  Math.round(widthsc), false);
                        resize.setImageBitmap(bitmap);
                        Log.d("BITMAPS",String.valueOf(bitmap.getByteCount()/1024));
                    }
                });
    }
}
