package com.f.schoolsintouchteachers.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.f.schoolsintouchteachers.Homefragment;
import com.f.schoolsintouchteachers.R;
import com.f.schoolsintouchteachers.models.DBHelper;
import com.f.schoolsintouchteachers.models.Post;
import com.f.schoolsintouchteachers.models.realmObjects.Pst;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressImageButton;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.helper.ToroPlayerHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by admin on 2/9/2018.
 */

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.MyViewholder>  {
    private Context context;

    private ArrayList<Post> videos;
    private FragmentManager fragmentManager;
private SimpleExoPlayer simpleExoPlayer;
    private ProgressDialog mProgressDialog;
   // private Downloadasnyc downloadTask;
    private Post post;
    private StorageReference refrence;
   // private ArrayList<ArrayList<Uri>> uris;

    private Realm realm;
    private String selection;
    Homefragment homefragment;
    private File file;
    int f=0;
   // private int k;


    public WallAdapter(Context context, ArrayList<Post> vids, FragmentManager childFragmentManager, Realm realm, Homefragment homefrag) {
        this.context = context;
        this.videos=vids;
        this.fragmentManager=childFragmentManager;

        this.realm=realm;
        this.homefragment=homefrag;
       // this.uris=uris;


    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1= LayoutInflater.from(context).inflate(R.layout.layout_wall,parent,false);


        return new MyViewholder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewholder holder, final int position) {
//uris.size();
        if (realm!=null){
            RealmResults<Pst> realmResults=realm.where(Pst.class).findAllAsync();
            realmResults.size();
        }

        if (videos.get(position).getImages()!=null && videos.get(position).getImages().size()>0){
            holder.contaoner.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(mLayoutManager);
            holder.recyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());

            holder.recyclerView.setOnFlingListener(null);
            SnapHelper snapHelper=new PagerSnapHelper();
            snapHelper.attachToRecyclerView(holder.recyclerView);
            holder.recyclerView.setAdapter(new Myfadapter(context, videos.get(position).getImages()));
//            if (uris.size()>=f+1) {
//                holder.recyclerView.setAdapter(new Myfadapter(context, videos.get(position).getImages().get(f)));
//                f++;
//            }else {
//                f--;
//                holder.recyclerView.setAdapter(new Myfadapter(context, videos.get(position).getImages().get(f)));
//
//            }




        }else {
            holder.contaoner.setVisibility(View.VISIBLE);
            holder.recyclerView.setVisibility(View.GONE);
           if (TextUtils.isEmpty(videos.get(position).getPost1().getId()) && !TextUtils.isEmpty(videos.get(position).getPost1().getVideo())){
               holder.setmedia(videos.get(position).getPost1().getVideo());
               holder.imageButton.setVisibility(View.GONE);
           }else if(!TextUtils.isEmpty(videos.get(position).getPost1().getId())){
              // int num=position;
//               final Cursor cursor=dbHelper.getData(videos.get(position).getPost1().getId());
//               if (cursor.getCount()>0){
//                   cursor.moveToFirst();
//                   selection=cursor.getString(cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_VIDURL));
//                   holder.setmedia(selection);
//                   holder.imageButton.setVisibility(View.GONE);
//               }
//               cursor.close();
           }


            holder.contaoner.setId(position+1);
            holder.setImage(videos.get(position).getPost1().getThumbnail());


        }


        holder.setPost(videos.get(position).getPost1().getPost());
        holder.setDate(videos.get(position).getPost1().getPostedtime());






//make frame visible,make recy inv,make itemcount to videos.size

//check for permissions i.e storage

   holder.imageButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           holder.imageButton.startAnimation();
          // holder.thumbnail.setVisibility(View.GONE);
           post =videos.get(position);
           int num=position;
      //    final Cursor cursor=dbHelper.getData(post.getPost1().getId());

               final File SDCardRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"intouch/myvideos");
               file = new File(SDCardRoot,  post.getPost1().getName() +".mp4");
               if (!SDCardRoot.exists()){
                   try {
                       SDCardRoot.mkdirs();
                       file.createNewFile();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
// execute this when the downloader must be fired

               refrence= FirebaseStorage.getInstance().getReferenceFromUrl(post.getPost1().getVideo());


           final int finalNum = num;
           refrence.getFile(file).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                 //  @SuppressWarnings("VisibleForTests")
                   @Override
                   public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                       long ts=taskSnapshot.getBytesTransferred();
                       long ttb=taskSnapshot.getTotalByteCount();
                      // (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100)
                       double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                       holder.imageButton.setProgress((int) progress);
                   }
               }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                       context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {


                               long vidid =getIdForFile(file.getAbsolutePath());

                               Log.d("VIDID", " " + vidid);
                               String urivid = "content://media/external/video/media/" + vidid;
                               //  it's right uri  for videofile
                             //  dbHelper.insertContact("gsgrrry",urivid, post.getPost1().getId());

//                               if(cursor.getCount() >0){
//                                   cursor.moveToFirst();
//                                   selection = cursor.getString(cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_VIDURL));
//
//                                   Toast.makeText(context, selection, Toast.LENGTH_SHORT).show();
//                               }
//                               cursor.close();
                               assert selection!=null;
                               holder.setmedia(selection);
                               holder.imageButton.stopAnimation();
                               holder.imageButton.dispose();
                               holder.imageButton.setVisibility(View.GONE);

                           }
                       },3000);

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       holder.imageButton.stopAnimation();
                       holder.imageButton.resetProgress();
                       Toast.makeText(context, "failed: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                   }
               });
           }




//dbHelper.getData(position);
          // holder.setmedia(selection);
//           holder.playerView.setPlayer(simpleExoPlayer);
//         //  MediaSource source=new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context,"mi")).createMediaSource(Uri.parse(file.getAbsolutePath()));
//
////           new ExtractorMediaSource(Uri.parse(selection),
////                   new DefaultDataSourceFactory(context,"ua"),
////                   new DefaultExtractorsFactory(), null, null);
//           MediaSource source=new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context,"mimi")).createMediaSource(Uri.parse(selection));
////simpleExoPlayer.release();
////simpleExoPlayer.
//
//           simpleExoPlayer.prepare(source,true,false);
//simpleExoPlayer.setPlayWhenReady(true);


   });





holder.button.setEventListener(new SparkEventListener(){
    @Override
    public void onEvent(ImageView button, boolean buttonState) {
        if (buttonState) {
            // Button is active
        } else {
            // Button is inactive
        }
    }

    @Override
    public void onEventAnimationEnd(ImageView button, boolean buttonState) {

    }

    @Override
    public void onEventAnimationStart(ImageView button, boolean buttonState) {

    }
});

    }
    public long getIdForFile(String filePath) {
        Cursor c = null;
        try {
            c =context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID},
                    MediaStore.Video.Media.DATA + " = ?",
                    new String[]{filePath},
                    null              // Default sort order
            );
            if (c.moveToNext()) {
                return c.getInt(0); // MediaStore.Video.Media._ID
            }
        } catch (Exception ex) {
            Log.e("VIDURI", ".... some error message ....", ex);
        } finally {
            if (c != null) c.close();
        }
        return 0;
    }


    @Override
    public int getItemCount() {
        return videos.size();
    }



    public class MyViewholder extends RecyclerView.ViewHolder implements ToroPlayer{
RecyclerView recyclerView;
SimpleExoPlayerView playerView;
SparkButton button;
CircularProgressImageButton imageButton;
LinearLayout linearLayout;
FrameLayout contaoner;
ImageView thumbnail;
Uri mediauri;
TextView posts,dates;
        ToroPlayerHelper helper;
        public MyViewholder(View itemView) {
            super(itemView);
            posts=itemView.findViewById(R.id.txtpost);
            dates=itemView.findViewById(R.id.textdate);
            thumbnail=itemView.findViewById(R.id.thumbnailpic);
            button=itemView.findViewById(R.id.btnlike);
            recyclerView=itemView.findViewById(R.id.recyimages);
           linearLayout=itemView.findViewById(R.id.linrlyt);
           playerView=itemView.findViewById(R.id.ytb);
           imageButton=itemView.findViewById(R.id.progressButton);
           contaoner=itemView.findViewById(R.id.framecontainer);



        }

        public void setImage(String s) {
            if (!TextUtils.isEmpty(s)){
                contaoner.setVisibility(View.VISIBLE);
                thumbnail.setVisibility(View.VISIBLE);

          //  String url = "https://img.youtube.com/vi/"+s+"/0.jpg";
            Glide.with(context).load(s).into(thumbnail);}
            else {
                contaoner.setVisibility(View.GONE);

            }
        }

        @NonNull
        @Override
        public View getPlayerView() {
            return playerView;
        }

        @NonNull
        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        @Override
        public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
            if (mediauri==null){
                return;
            }
            if (helper == null) {
                helper = new ExoPlayerViewHelper(this, mediauri);
            }
            helper.initialize(container, playbackInfo);
        }

        @Override
        public void play() {
            if (mediauri==null){
                return;
            }
            if (helper != null){
                thumbnail.setVisibility(View.GONE);
                helper.play();}
        }

        @Override
        public void pause() {
            if (helper != null) helper.pause();
        }

        @Override
        public boolean isPlaying() {
            return helper != null && helper.isPlaying();
        }

        @Override
        public void release() {
            if (helper != null) {
                helper.release();
                helper = null;
            }
        }

        @Override
        public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 1.0;
        }

        @Override
        public int getPlayerOrder() {
            return getAdapterPosition();
        }

        public void setmedia(String selection) {
if (selection!=null){
    mediauri=Uri.parse(selection);
    playerView.setVisibility(View.VISIBLE);
}
        }

        public void setPost(String post) {
            if (TextUtils.isEmpty(post)){
                return;
            }
            posts.setText(post);
        }

        public void setDate(String postedtime) {
            if (TextUtils.isEmpty(postedtime)){
                return;
            }

            long curTime = System.currentTimeMillis();
            long curTimesec = curTime / 1000;  //you can continue to do this if you want to get minutes, hours, days, etc
            long diff = curTimesec - Long.valueOf(postedtime);
            long minut,hour;
            if (diff>60){
              minut=diff/60;
              if (minut>60){
                  hour=minut/60;
                  if (hour>24){
                      long days=hour/24;
                      dates.setText("posted on: "+days+" days ago");
                  }else {
                      dates.setText("posted on: "+hour+" hours ago");
                  }

              }else{
                  dates.setText("posted on: "+minut+" minutes ago");
              }

            }else {
                dates.setText("posted on: "+diff+" sec ago");

            }

        }
    }
    public static class Myfadapter extends RecyclerView.Adapter<WallAdapter.Innerfragmet> {
        Context context;
        ArrayList<String> images;
      //  int pos;

        public Myfadapter(Context context, ArrayList<String> images) {
            this.context = context;
            this.images=images;
        }

        @Override
        public Innerfragmet onCreateViewHolder(ViewGroup parent, int viewType) {
           View v=LayoutInflater.from(context).inflate(R.layout.image_layot,parent,false);
           return new Innerfragmet(v);
        }

        @Override
        public void onBindViewHolder(Innerfragmet holder, int position) {
           // pos=position;

                holder.setimages(images.get(position),context);


        }

        @Override
        public int getItemCount() {

            return images.size();
        }
    }
    public static class Innerfragmet extends RecyclerView.ViewHolder{
ImageView imageView;
        public Innerfragmet(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgviewrecy);
        }

        public void setimages(String uri,Context context) {
            Glide.with(context).load(uri).into(imageView);
        }
    }
}
