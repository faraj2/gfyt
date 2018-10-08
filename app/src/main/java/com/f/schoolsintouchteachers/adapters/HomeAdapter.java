package com.f.schoolsintouchteachers.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.f.schoolsintouchteachers.CompressImage;
import com.f.schoolsintouchteachers.R;
import com.f.schoolsintouchteachers.Util;
import com.f.schoolsintouchteachers.firebase.Config;
import com.f.schoolsintouchteachers.models.Downloadholders;
import com.f.schoolsintouchteachers.models.Post;
import com.f.schoolsintouchteachers.models.Post1;
import com.f.schoolsintouchteachers.models.realmObjects.Pst;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.builder.LoadBuilder;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.vincent.videocompressor.VideoCompress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressImageButton;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.helper.ToroPlayerHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

import static android.content.Context.MODE_PRIVATE;

public class HomeAdapter extends RealmRecyclerViewAdapter<Pst, HomeAdapter.Myviewholder> {
    Context context;
    private FragmentManager childFragmentManager;
    private File file;
    private StorageReference refrence;
    private String selection;
    public static Realm realm;
    private Map<String, Object> clp = new HashMap<>();
    private SparseArray<StorageTask> tsks = new SparseArray<>();
    private static Pst pst;
    int pos = 0;
    private SparseArray<RealmList<String>> imm;
    RecyclerView recyclerView;
    private ArrayList<String> imagess;
    private ArrayList<String> vidid = new ArrayList<>();
    private ArrayList<Integer> postin = new ArrayList<>();
    private SparseArray<File> files = new SparseArray<>();
    private String outputDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String input;
    private Bitmap thumbnl;
    private Uri viduri;
    // int c = 0;
    private Handler handler;
    private String imge, download, jina;
    ArrayList<Downloadholders> downloadholders = new ArrayList<>();


    public HomeAdapter(Realm realm, Context context, @Nullable OrderedRealmCollection<Pst> data, boolean autoUpdate, FragmentManager childFragmentManager) {
        super(data, autoUpdate);
        imm = new SparseArray<>();
        this.context = context;
        this.childFragmentManager = childFragmentManager;
        this.realm = realm;


    }

    private void compressingVid(Uri uri, final String pic) {

        try {
            input = Util.getFilePath(context, uri);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final String f = outputDir + File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".mp4";

//                String[] command = {"-y", "-i", input, "-s", "640x480", "-r", "24", "-vcodec", "h264", "-b:v", "110k", "-b:a", "48000", "-ac", "2", "-ar", "22050", f};
//                ffmeg.execFFmpegBinary(command);


        VideoCompress.compressVideoLow(input, f, new VideoCompress.CompressListener() {
            public byte[] d;

            @Override
            public void onStart() {
//                        startTime = System.currentTimeMillis();
//                        if (alertDialog != null && alertDialog.isShowing()) {
//                            alertDialog.dismiss();
//                        }
//                        mProgressDialog.show();
                Util.writeFile(context, "Start at: " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()) + "\n");

                // Toast.makeText(getContext(), "Started task", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(f))));
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long vidid = getIdForFile(f);
                        Log.d("VIDID", " " + vidid);
                        String urivid = "content://media/external/video/media/" + vidid;
                        viduri = Uri.parse(urivid);//  it's right uri  for videofile
//                        MediaMetadataRetriever g = new MediaMetadataRetriever();
//                        g.setDataSource(context,viduri);
//                        thumbnl = g.getFrameAtTime(1000);
//                                                long minutes=Long.parseLong(g.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//                                                int minute=(int) ((minutes/1000)/60);
//                                                Toast.makeText(getActivity(),String.valueOf(minute), Toast.LENGTH_SHORT).show();
                        // getResultsFromApi();
//                        File file = new File(f);
//                        long length = file.length();
//                        length = (length / 1024) / 1024;
                        if (!TextUtils.isEmpty(pic)) {
                            Uri u = Uri.parse(pic);
//                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                            thumbnl.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                            d = byteArrayOutputStream.toByteArray();
                            final StorageReference reference = Config.storageReference.child(Config.auth.getCurrentUser().getDisplayName()).child("thumbnail/" + System.currentTimeMillis() + ".png");
                            UploadTask task = reference.putFile(u);
                            task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return reference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        imge = task.getResult().toString();
                                        final StorageReference storageReference = Config.storageReference.child(Config.auth.getCurrentUser().getDisplayName()).child("videos/" + viduri.getLastPathSegment());
                                        final UploadTask taskk = storageReference.putFile(viduri);
                                        taskk.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                                if (!task.isSuccessful()) {
                                                    // mProgressDialog.dismiss();
                                                    throw task.getException();
                                                } else {
                                                    jina = task.getResult().getMetadata().getName();
                                                }
                                                return storageReference.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    //  mProgressDialog.dismiss();
                                                    download = task.getResult().toString();
                                                    Message message = handler.obtainMessage();
                                                    message.obj = "video";
                                                    handler.sendMessage(message);
                                                    //   posting();

                                                }
                                            }
                                        });
                                        taskk.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                int p = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                                                Message m = handler.obtainMessage();
                                                m.arg1 = (p / 2) + 50;
                                                handler.sendMessage(m);
                                                // mProgressDialog.setMessage("Uploading:" + taskSnapshot.getBytesTransferred());
                                            }
                                        });
                                    } else {
                                        // Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        // Toast.makeText(getContext(), "video sixe " + length, Toast.LENGTH_SHORT).show();


                    }
                }, 3000);
                //   mProgressDialog.dismiss();
            }

            @Override
            public void onFail() {
                //mProgressDialog.dismiss();

            }

            @Override
            public void onProgress(float percent) {
                Message message = handler.obtainMessage();
                message.arg1 = (int) percent / 2;
                handler.sendMessage(message);
                if ((int) percent == 100) {
                    message.obj = "compressed";
                }
                //   mProgressDialog.setProgress((int) percent);
            }
        });

    }

    public void sendImage(final RealmList<String> uri) {

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.US);
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        StorageReference reference = Config.storageReference.child(Config.auth.getCurrentUser().getDisplayName()).child(filename);

        imagess = new ArrayList<>();

        for (final String uri2 : uri) {

            File file = new File(new CompressImage(context).compressImage(uri2));
            Uri uri1 = Uri.fromFile(file);
            reference.child(uri1.getLastPathSegment() + ".png").putFile(uri1).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return task.getResult().getStorage().getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        imagess.add(task.getResult().toString());
                        if (imagess.size() == uri.size()) {

                            //   posting();
                            Message message = handler.obtainMessage();
                            message.obj = "image";
                            handler.sendMessage(message);
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wall, parent, false);


        return new Myviewholder(view1);
    }

    @Override
    public void onViewRecycled(@NonNull Myviewholder holder) {
        super.onViewRecycled(holder);
        holder.getAdapterPosition();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.getLayoutManager().scrollToPosition(getItemCount() - 1);
        this.recyclerView = recyclerView;

    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onBindViewHolder(@NonNull final Myviewholder holder, final int position) {

        pst = getData().get(position);
//holder.setIsRecyclable(false);
        if (pst.isPosting()) {
            if (postin != null) {
                if (!postin.contains(position)) {
                    postin.add(position);
                    final Pst pst1 = getData().get(position);
                    final String id = pst1.getId();

                    holder.donut2.setVisibility(View.VISIBLE);
                    final Post post = new Post();
                    final Post1 post1 = new Post1();
                    post1.setId(pst1.getId());
                    post1.setClaps(pst1.getPost1().getClaps());
                    post1.setName(pst1.getPost1().getName());
                    post1.setPost(pst1.getPost1().getPost());
                    post1.setPostedtime(pst1.getPost1().getPostedtime());
                    post1.setClap(pst1.getPost1().isClap());
                    post.setId(pst1.getId());
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);

                            int p = msg.arg1;

                            holder.donut2.setProgress(p);

                            if (msg.obj != null) {
                                String pass = (String) msg.obj;
                                if (pass.equals("image")) {
                                    holder.donut2.setProgress(99);
                                    post.setImages(imagess);
                                    post.setPost1(post1);
                                    Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("posts").document(id)
                                            .set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                holder.donut2.setVisibility(View.GONE);
                                                SharedPreferences.Editor editor = context.getSharedPreferences("id", MODE_PRIVATE).edit();
                                                editor.putString("ids", id);
                                                editor.apply();
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        Pst ps = realm.where(Pst.class).equalTo("id", id).findFirst();
                                                        assert ps != null;
                                                        ps.setPosting(false);
                                                        postin.remove(Integer.valueOf(position));

                                                    }
                                                });

                                            }
                                        }
                                    });
                                    assert imge != null;
                                } else if (pass.equals("video")) {
                                    holder.donut2.setProgress(100);
                                    post1.setThumbnail(imge);
                                    assert download != null;
                                    post1.setVideo(download);
                                    assert jina != null;
                                    post1.setName(jina);
                                    post.setPost1(post1);
                                    Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("posts").document(id)
                                            .set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                holder.donut2.setVisibility(View.GONE);
                                                SharedPreferences.Editor editor = context.getSharedPreferences("id", MODE_PRIVATE).edit();
                                                editor.putString("ids", id);
                                                editor.apply();
                                                realm.executeTransactionAsync(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        Pst ps = realm.where(Pst.class).equalTo("id", id).findFirst();
                                                        assert ps != null;
                                                        ps.setPosting(false);
                                                        postin.remove(Integer.valueOf(position));

                                                    }
                                                });

                                            }
                                        }
                                    });
                                } else if (pass.equals("compressed")) {
                                    //  c = 50;
                                }

                            }


                        }
                    };

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            if (pst1.getImages() != null && pst1.getImages().size() > 0) {
                                sendImage(pst1.getImages());
                                Message message = handler.obtainMessage();
                                message.arg1 = 90;
                                handler.sendMessage(message);

                            } else if (pst1.getPost1().getVideo() != null && !pst1.getPost1().getVideo().isEmpty()) {
                                String pic = pst1.getPost1().getThumbnail();
                                if (!TextUtils.isEmpty(pic)) {
                                    holder.setImage(pic);
                                    Uri uri = Uri.parse(pst1.getPost1().getVideo());
                                    compressingVid(uri, pic);
                                } else {
                                    holder.donut2.setProgress(0);
                                    Toast.makeText(context, "ERROR PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                                }


                            }


                        }
                    });
                }
            }

        }
        if (pst.getImages() != null && pst.getImages().size() > 0) {
            String string = getData().get(position).getId();
            holder.contaoner.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            mLayoutManager.setInitialPrefetchItemCount(pst.getImages().size());
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(mLayoutManager);
            holder.recyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());

            holder.recyclerView.setOnFlingListener(null);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(holder.recyclerView);
            HomeAdapter.Myfadapter myfadapter = new HomeAdapter.Myfadapter(context, string, position, imm);

            holder.recyclerView.setAdapter(myfadapter);
            myfadapter.updateList(pst.getImages());

            //   holder.recyclerView.setRecycledViewPool(viewPool);

        } else {
            holder.contaoner.setVisibility(View.VISIBLE);
            holder.recyclerView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(pst.getPost1().getVideo()) && pst.getPost1().getVideo().startsWith("https")) {
                if (files != null && files.get(position) != null) {
                    holder.ply.setVisibility(View.GONE);
                } else {
                    holder.ply.setVisibility(View.VISIBLE);
                }

            } else {
                holder.ply.setVisibility(View.GONE);
                holder.donutProgress.setVisibility(View.GONE);
                holder.setmedia(pst.getPost1().getVideo());
            }


            holder.contaoner.setId(position + 1);
            holder.setImage(pst.getPost1().getThumbnail());


        }


        holder.setPost(pst.getPost1().getPost());
        holder.setDate(pst.getPost1().getPostedtime());

        holder.button.setChecked(pst.getPost1().isClap());
        if (pst.getPost1().getClaps() != null) {
            holder.claps.setText(pst.getPost1().getClaps() + " claps");
        }


//        holder.imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Pst pst1 = getData().get(position);
//                holder.imageButton.startAnimation();
//                //  holder.imageButton.setI
//                // holder.thumbnail.setVisibility(View.GONE);
//
//                //  int num=position;
//                //    final Cursor cursor=dbHelper.getData(post.getPost1().getId());
//
//                final File SDCardRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "intouch/myvideos");
//                file = new File(SDCardRoot, pst1.getPost1().getName() + ".mp4");
//                if (!SDCardRoot.exists()) {
//                    try {
//                        SDCardRoot.mkdirs();
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    if (!file.exists()) {
//                        try {
//                            file.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//// execute this when the downloader must be fired
//                pst1.getPost1().getVideo();
//
//                refrence = FirebaseStorage.getInstance().getReferenceFromUrl(pst1.getPost1().getVideo());
//
//
//                //      final int finalNum = num;
//                refrence.getFile(file).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
//                    //  @SuppressWarnings("VisibleForTests")
//                    @Override
//                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        long ts = taskSnapshot.getBytesTransferred();
//                        long ttb = taskSnapshot.getTotalByteCount();
//                        // (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100)
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        holder.imageButton.setProgress((int) progress);
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                long vidid = getIdForFile(file.getAbsolutePath());
//
//                                Log.d("VIDID", " " + vidid);
//                                final String urivid = "content://media/external/video/media/" + vidid;
//                                if (realm != null) {
//                                    realm.executeTransaction(new Realm.Transaction() {
//                                        @Override
//                                        public void execute(Realm realm) {
//                                            Pst ps = realm.where(Pst.class).equalTo("id", pst1.getId()).findFirst();
//                                            ps.getPost1().setVideo(urivid);
//
//                                        }
//                                    });
//                                    selection = urivid;
//                                }
//
//
//                                assert selection != null;
//                                holder.setmedia(selection);
//                                holder.imageButton.stopAnimation();
//                                holder.imageButton.dispose();
//                                holder.imageButton.setVisibility(View.GONE);
//
//                            }
//                        }, 3000);
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        holder.imageButton.stopAnimation();
//                        holder.imageButton.resetProgress();
//                        Toast.makeText(context, "failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        holder.button.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, final boolean buttonState) {
                final Pst ps = getData().get(position);
                if (!buttonState) {
                    String[] strings = holder.claps.getText().toString().split(" ");

                    holder.claps.setText(String.valueOf(Integer.parseInt(strings[0]) - 1) + " claps");
                    if (realm != null) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Pst pst1 = realm.where(Pst.class).equalTo("id", ps.getId()).findFirst();
                                pst1.getPost1().setClap(false);
                                pst1.getPost1().setClaps(pst1.getPost1().getClaps() - 1);
                                // realm.removeAllChangeListeners();
                            }
                        });
                    }
                }
                clp.put("id", ps.getId());
                Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("likes").document(ps.getId()).set(clp).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                clp.clear();
                                Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("likes").document(ps.getId()).collection("claps")
                                        .document(Config.auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (buttonState) {
                                            if (!documentSnapshot.exists()) {
                                                clp.put("clap", true);
                                                Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("likes").document(ps.getId()).collection("claps")
                                                        .document(Config.auth.getCurrentUser().getUid()).set(clp);
                                            }
                                        } else {
                                            if (documentSnapshot.exists()) {
                                                Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("likes").document(ps.getId()).collection("claps")
                                                        .document(Config.auth.getCurrentUser().getUid()).delete();
                                            }
                                        }

                                    }
                                });
                            }
                        }
                );

            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                if (buttonState) {
                    String[] strings = holder.claps.getText().toString().split(" ");
                    holder.claps.setText(String.valueOf(Integer.parseInt(strings[0]) + 1) + " claps");
                    if (realm != null) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Pst pst1 = realm.where(Pst.class).equalTo("id", getData().get(position).getId()).findFirst();
                                pst1.getPost1().setClap(true);
                                pst1.getPost1().setClaps(pst1.getPost1().getClaps() + 1);
                                //  realm.removeAllChangeListeners();
                            }
                        });
                    }
                }
            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
        holder.ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                holder.donutProgress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.ply.setVisibility(View.VISIBLE);
                        v.setVisibility(View.GONE);
                        if (downloadholders != null && downloadholders.size()>0) {
                            for (Downloadholders downloadholders1:downloadholders){
                                if (downloadholders1.getPosition()==holder.getAdapterPosition() && !downloadholders1.getFuture().isDone()){
                                    downloadholders1.getFuture().cancel();
                                    if (downloadholders1.getFuture().isCancelled()){
                                        downloadholders.remove(downloadholders1);
                                        break;
                                    }
                                }
                            }
                            if (file != null && file.exists()) {
                                file.delete();
                            }
                            // refrence.getActiveDownloadTasks().get(0).cancel();
                            Toast.makeText(context, "canceled " + position, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "task is completed " + position, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final DonutProgress progress = holder.donutProgress;

                final Pst p = getData().get(position);
                final File SDCardRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "intouch/myvideos");
                file = new File(SDCardRoot, p.getPost1().getName() + ".mp4");
                if (!SDCardRoot.exists()) {
                    try {
                        SDCardRoot.mkdirs();
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (realm != null) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Pst ps = realm.where(Pst.class).equalTo("id", p.getId()).findFirst();
                                    ps.getPost1().setVideo(Uri.fromFile(file).toString());
                                    selection = Uri.fromFile(file).toString();

                                    assert selection != null;
                                    holder.setmedia(selection);
                                    progress.setVisibility(View.GONE);

                                }
                            });

                        }
                        return;

                    }

                }
                holder.setIsRecyclable(false);
                progress.setVisibility(View.VISIBLE);
                Future builder =
                        Ion.with(context)
                                .load(p.getPost1().getVideo())
// have a ProgressBar get updated automatically with the percent
                                //.progressBar(progress)
// and a ProgressDialog
                                //  .progressDialog(progressDialog)
// can also use a custom callback
                                .progressHandler(new ProgressCallback() {
                                    @Override
                                    public void onProgress(long downloaded, long total) {
                                        progress.setProgress((int) ((100 * downloaded) / total));
                                        System.out.println("" + downloaded + " / " + total);
                                    }
                                })

                                .progress(new ProgressCallback() {
                                    @Override
                                    public void onProgress(long downloaded, long total) {
                                        //
                                        System.out.println("" + downloaded + " / " + total);
                                    }
                                })
                                .write(file)
                                .setCallback(new FutureCallback<File>() {
                                    @Override
                                    public void onCompleted(Exception e, final File file1) {
                                        if (e != null) {
                                            Log.d("ERROR", e.getMessage());
                                            return;
                                        }
                                        for (final Downloadholders downloadholders1 : downloadholders) {
                                            if (downloadholders1.getFile() == file1) {
                                                if (realm != null) {
                                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            Pst ps = realm.where(Pst.class).equalTo("id", downloadholders1.getIdd()).findFirst();
                                                            ps.getPost1().setVideo(Uri.fromFile(file1).toString());
                                                            selection = Uri.fromFile(file1).toString();


                                                        }
                                                    }, new Realm.Transaction.OnSuccess() {
                                                        @Override
                                                        public void onSuccess() {
                                                            downloadholders1.getProgress().donutProgress.setVisibility(View.GONE);
                                                            //   progress.setVisibility(View.GONE);
                                                            downloadholders1.getProgress().setIsRecyclable(true);
                                                            downloadholders.remove(downloadholders1);

                                                        }
                                                    });

                                                }
                                            }
                                        }

                                        // download done...
                                        // do stuff with the File or error
                                    }
                                });
                downloadholders.add(new Downloadholders(p.getId(), file, file.getPath(), holder.getAdapterPosition(), holder,builder));

            }
        });
//        holder.ply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                //  int f=0;
//                v.setVisibility(View.GONE);
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                      final Myviewholder myviewholder=holder;
//
//               //       myviewholder.setIsRecyclable(false);
//                      myviewholder.donutProgress.setVisibility(View.VISIBLE);
//                        final Pst pst1 = getData().get(position);
//
//
//                        final File SDCardRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "intouch/myvideos");
//                        file = new File(SDCardRoot, pst1.getPost1().getName() + ".mp4");
//
//                        if (!SDCardRoot.exists()) {
//                            try {
//                                SDCardRoot.mkdirs();
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            if (!file.exists()) {
//                                try {
//                                    file.createNewFile();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }else {
//                                if (realm != null) {
//                                    realm.executeTransaction(new Realm.Transaction() {
//                                        @Override
//                                        public void execute(Realm realm) {
//                                            Pst ps = realm.where(Pst.class).equalTo("id", pst1.getId()).findFirst();
//                                            ps.getPost1().setVideo(Uri.fromFile(file).toString());
//                                            selection = Uri.fromFile(file).toString();
//
//                                                assert selection != null;
//                                                holder.setmedia(selection);
//                                                holder.donutProgress.setVisibility(View.GONE);
//
//                                        }
//                                    });
//
//                                }
//                                return;
//
//                            }
//
//                        }
//                        vidid.add(pst1.getId());
//                        files.put(position, file);
//// execute this when the downloader must be fired
//                        pst1.getPost1().getVideo();
//
//                        refrence = FirebaseStorage.getInstance().getReferenceFromUrl(pst1.getPost1().getVideo());
//
//                        //      final int finalNum = num;
//                        final StorageTask taskSnapshot = refrence.getFile(files.get(position)).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
//                            //  @SuppressWarnings("VisibleForTests")
//                            @Override
//                            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                long ts = taskSnapshot.getBytesTransferred();
//                                long ttb = taskSnapshot.getTotalByteCount();
//
//                                // (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100)
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                                myviewholder.donutProgress.setProgress((int) progress);
//                                if ((int)progress>=70){
//                                  int sze=  taskSnapshot.getStorage().getActiveDownloadTasks().size();
//                               int gh=sze;
//                                }
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
////                                pos++;
////                                if (taskSnapshot.getTotalByteCount() != taskSnapshot.getBytesTransferred()) {
////                                    return;
////                                }
////
////                                if (pos == files.size()) {
////                                    final int g=files.size();
////                                    if (files != null && files.size() > 0) {
////                                        for (int i = 0; i <g; i++) {
////                                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(files.valueAt(i))));
////                                        }
////                                    }
////
////                                    new Handler().postDelayed(new Runnable() {
////                                        @Override
////                                        public void run() {
////                                            if (files != null && files.size() > 0) {
////                                                int ho=g;
////                                                for (int i = 0; i < ho; i++) {
////                                                    final long vidid1 = getIdForFile(files.valueAt(i).getAbsolutePath());
////
////                                                    Log.d("VIDID", " " + vidid1);
////                                                    final String urivid = "content://media/external/video/media/" + vidid1;
////                                                    if (realm != null) {
////                                                        final int finalI = i;
////                                                        realm.executeTransaction(new Realm.Transaction() {
////                                                            @Override
////                                                            public void execute(Realm realm) {
////                                                                Pst ps = realm.where(Pst.class).equalTo("id", vidid.get(finalI)).findFirst();
////                                                                ps.getPost1().setVideo(urivid);
////                                                                selection = urivid;
////                                                                 if (holder.getLayoutPosition() == files.keyAt(finalI)) {
////                                                                     assert selection != null;
////                                                                     holder.setmedia(selection);
////                                                                     holder.donutProgress.setVisibility(View.GONE);
////                                                                 }
////
////
////                                                        files.removeAt(finalI);
////                                                        vidid.remove(finalI);
////                                                        tsks.removeAt(finalI);
////                                                       pos--;
////
////                                                            }
////                                                        });
////
////                                                    }
////
////                                                }
////                                                ho=0;
////                                            }
////
////
//////
////
////
////                                        }
////                                    }, 3000);
////                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                tsks.remove(position);
//                                if (file.exists() && file != null) {
//                                    file.delete();
//                                }
//                                myviewholder.donutProgress.setVisibility(View.GONE);
//                                v.setVisibility(View.VISIBLE);
//                                Toast.makeText(context, "failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        tsks.put(position, taskSnapshot);
//
//                    }
//                });
////holder.setIsRecyclable(false);
////                holder.donutProgress.setVisibility(View.VISIBLE);
//
//            }
//        });

    }

    private long getIdForFile(String filePath) {
        Cursor c = null;
        try {
            c = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
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

    public class Myviewholder extends RecyclerView.ViewHolder implements ToroPlayer {
        private final DonutProgress donutProgress;
        RecyclerView recyclerView;
        ImageButton ply;
        SimpleExoPlayerView playerView;
        SparkButton button;
        CircularProgressImageButton imageButton;
        LinearLayout linearLayout;
        FrameLayout contaoner;
        ImageView thumbnail;
        Uri mediauri;
        TextView posts, dates;
        ToroPlayerHelper helper;
        TextView claps;
        DonutProgress donut2;

        public Myviewholder(View itemView) {
            super(itemView);

            ply = itemView.findViewById(R.id.ply);
            donutProgress = itemView.findViewById(R.id.donut);
            donut2 = itemView.findViewById(R.id.donut1);
            donut2.setVisibility(View.GONE);

            donutProgress.setVisibility(View.GONE);
            claps = itemView.findViewById(R.id.claps);
            posts = itemView.findViewById(R.id.txtpost);
            dates = itemView.findViewById(R.id.textdate);
            thumbnail = itemView.findViewById(R.id.thumbnailpic);
            button = itemView.findViewById(R.id.btnlike);
            recyclerView = itemView.findViewById(R.id.recyimages);
            linearLayout = itemView.findViewById(R.id.linrlyt);
            playerView = itemView.findViewById(R.id.ytb);
            imageButton = itemView.findViewById(R.id.progressButton);
            imageButton.setVisibility(View.GONE);
            contaoner = itemView.findViewById(R.id.framecontainer);
        }

        public void setImage(String s) {
            if (!TextUtils.isEmpty(s)) {
                contaoner.setVisibility(View.VISIBLE);
                thumbnail.setVisibility(View.VISIBLE);

                //  String url = "https://img.youtube.com/vi/"+s+"/0.jpg";
                GlideApp.with(context).load(s).into(thumbnail);
            } else {
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
            if (mediauri == null) {
                return;
            }
            if (helper == null) {
                helper = new ExoPlayerViewHelper(this, mediauri);
            }
            helper.initialize(container, playbackInfo);
        }

        @Override
        public void play() {
            if (mediauri == null) {
                return;
            }
            if (helper != null) {
                thumbnail.setVisibility(View.GONE);
                helper.play();
            }
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
            if (selection != null) {
                if (donutProgress.getVisibility() == View.VISIBLE) {
                    donutProgress.setVisibility(View.GONE);
                }
                mediauri = Uri.parse(selection);
                playerView.setVisibility(View.VISIBLE);
            }
        }

        public void setPost(String post) {
            if (TextUtils.isEmpty(post)) {
                return;
            }
            posts.setText(post);
        }

        public void setDate(String postedtime) {
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
                        dates.setText("posted on: " + days + " days ago");
                    } else {
                        dates.setText("posted on: " + hour + " hours ago");
                    }

                } else {
                    dates.setText("posted on: " + minut + " minutes ago");
                }

            } else {
                dates.setText("posted on: " + diff + " sec ago");

            }

        }

    }

    public static class Myfadapter extends RecyclerView.Adapter<HomeAdapter.Innerfragmet> {
        Context context;
        // public static RealmList<String> images;
        SparseArray<RealmList<String>> first;
        int post;
        String id;


        //  int pos;

        public Myfadapter(Context context, String id, int position, SparseArray<RealmList<String>> imm) {
            this.context = context;
            this.first = imm;
            this.post = position;
            this.id = id;

        }

        public void updateList(RealmList<String> imagess) {

            if (first != null) {
                if (imagess.size() > 0) {
                    first.put(post, imagess);
                }
            }

        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            Log.d("DETACHED", "DETACHED");
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            Log.d("ATTACHED", "ATACHED");

        }

        @Override
        public HomeAdapter.Innerfragmet onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layot, parent, false);
            //  Innerfragmet innerfragmet=new Innerfragmet(v);
            return new HomeAdapter.Innerfragmet(v);
        }

        @Override
        public void onBindViewHolder(HomeAdapter.Innerfragmet holder, int position) {
            // pos=position;
//holder.setIsRecyclable(false);
            holder.setimages(first.get(post).get(position), context, position, id);
            //   notifyDataSetChanged();


        }

        @Override
        public void onViewRecycled(@NonNull Innerfragmet holder) {
            super.onViewRecycled(holder);
            holder.getAdapterPosition();
        }

        @Override
        public int getItemCount() {
            int num = post;
            return first.get(post).size();
        }
    }

    public static class Innerfragmet extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Innerfragmet(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgviewrecy);
        }

        public void setimages(final String uri, final Context context, final int position, final String id) {

            GlideApp.with(context).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    int u = resource.getHeight();
                    int y = resource.getWidth();
                    if (u == 350 && y == 350) {
                        imageView.setImageBitmap(resource);
                        return;
                    }
                    Bitmap drawable = scaleImage(resource);
                    imageView.setImageBitmap(drawable);
                    if (uri.startsWith("https")) {
                        final File SDCardRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "intouch/images");
                        final File file = new File(SDCardRoot, String.valueOf(System.currentTimeMillis()) + ".png");
                        if (!SDCardRoot.exists()) {
                            try {

                                SDCardRoot.mkdirs();
                                file.createNewFile();
                                FileOutputStream out = new FileOutputStream(file);
                                drawable.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                                out.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                file.createNewFile();
                                FileOutputStream out = new FileOutputStream(file);
                                drawable.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Cursor c = null;
                                try {
                                    c = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            new String[]{MediaStore.Images.Media._ID},
                                            MediaStore.Images.Media.DATA + " = ?",
                                            new String[]{file.getAbsolutePath()},
                                            null              // Default sort order
                                    );
                                    if (c.moveToNext()) {
                                        long vidid = c.getInt(0); // MediaStore.Video.Media._ID
                                        Log.d("VIDID", " " + vidid);
                                        final String urivid = "content://media/external/images/media/" + vidid;
                                        if (realm != null) {
                                            realm.executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
//                                                            Myfadapter.images.set(position,urivid);
//                                                            pos=position;
                                                    Pst pst1 = realm.where(Pst.class).equalTo("id", id).findFirst();
                                                    pst1.getImages().set(position, urivid);
//                                                pst1.setImages(Myfadapter.images);
                                                    //  pst1..setVideo(urivid);

                                                }
                                            });
                                        }
                                    }
                                } catch (Exception ex) {
                                    Log.e("VIDURI", ".... some error message ....", ex);
                                } finally {
                                    if (c != null) c.close();
                                }
                            }
                        }, 3000);
                        //   Log.d("BITMAPS",String.valueOf( BitmapCompat.getAllocationByteCount(resource)/1024));
                    }

                }
            });
        }

        private Bitmap scaleImage(Bitmap bitmap) {
//

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int xBounding = 350; //((View) view.getParent()).getWidth();//EXPECTED WIDTH
            int yBounding = 350; //((View) view.getParent()).getHeight();//EXPECTED HEIGHT

            float xScale = ((float) xBounding) / width;
            float yScale = ((float) yBounding) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(xScale, yScale);

            Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            width = scaledBitmap.getWidth();
            height = scaledBitmap.getHeight();
            Log.d("NEW MEASUREMENTS ", String.valueOf(width) + " :" + String.valueOf(height));

            return scaledBitmap;


        }
    }
}
