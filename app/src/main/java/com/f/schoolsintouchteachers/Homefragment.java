package com.f.schoolsintouchteachers;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.f.schoolsintouchteachers.adapters.AudioViewholder;
import com.f.schoolsintouchteachers.adapters.HomeAdapter;
import com.f.schoolsintouchteachers.adapters.PreviewpicsAdapter;
import com.f.schoolsintouchteachers.firebase.Config;
import com.f.schoolsintouchteachers.firebase.HttpAsync;
import com.f.schoolsintouchteachers.models.Post;
import com.f.schoolsintouchteachers.models.realmObjects.Pst;
import com.f.schoolsintouchteachers.trails.TestRecord;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vincent.videocompressor.VideoCompress;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import im.ene.toro.PlayerSelector;
import im.ene.toro.widget.Container;
import io.codetail.animation.ViewAnimationUtils;
import io.realm.Realm;
import io.realm.RealmList;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

/**
 * Created by admin on 2/6/2018.
 */

public class Homefragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private static final int RC_WRITESTORAGE = 121;
    private static final int RC_AUDIO = 122;
    String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    private Container recyclerView;
    private String outputDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final int IMAGE_PICKER_SELECT = 1;
    RecyclerView recycpics;
    ImageButton sendd, play;
    private ProgressDialog mProgressDialog;
    private String input;
    private long startTime;
    private AlertDialog alertDialog;

    private Uri viduri;
    HomeAdapter homeAdapter;
    private StorageReference storage;
    private Uri download;
    private Bitmap thumbnl;
    private String imge;

    private Homefragment homefrag;
    private EditText posttext;
    private ArrayList<Post> snapshotlist;
    private CardView cardpics;
    private VideoView videoView;
    private ArrayList<Uri> imglist;
    SwipeRefreshLayout refreshLayout;
    private FirebaseAuth.AuthStateListener authlistner;
    private String jina;
    private ArrayList<String> images;
    private byte[] d;
    private Map<Integer, ArrayList<Uri>> uris;
    private Realm realm;
    View layview;
    ImageButton pic, vid;
    private Animator animator;
    private RelativeLayout reltv;
    private RecordButton recordButton;
    private RecordView recordView;
    private CountDownTimer countdon;
    private MediaRecorder mediaRecorder;
    private String AudioSavePathInDevice;


    //   private MyFfmeg ffmeg;

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
        // attachmentDialog();
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
        EasyPermissions.requestPermissions(getActivity(), "Please accept the permissions for reading media files", requestCode);
    }


    private final Runnable revealAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            int cx = layview.getRight();
            int cy = layview.getBottom();

            int finalRadius = Math.max(layview.getWidth(), layview.getHeight());
            if (layview.getVisibility() == View.VISIBLE) {
                animator = ViewAnimationUtils.createCircularReveal(layview, cx, cy, finalRadius, 0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layview.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            } else if (layview.getVisibility() == View.GONE) {
                layview.setVisibility(View.VISIBLE);
                animator = ViewAnimationUtils.createCircularReveal(layview, cx, cy, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();

            }

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        getActivity().getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        FirebaseDatabase.getInstance().getReference().child("token").setValue(getActivity().getSharedPreferences("Token", MODE_PRIVATE).getString("mytoken", ""));
        //Realm
        realm = Realm.getDefaultInstance();
        layview = view.findViewById(R.id.attmntshow);
        pic = view.findViewById(R.id.picture);
        vid = view.findViewById(R.id.video);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getActivity().getPackageManager();
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                List<ResolveInfo> listGallery = pm.queryIntentActivities(galleryIntent, 0);
                // List<Pair<String, Intent>> result = new ArrayList<>();
                try {
                    if (pm.getApplicationInfo("com.google.android.apps.photos", 0) == null) {


                    } else {
                        for (ResolveInfo res : listGallery) {
                            ActivityInfo info = res.activityInfo;

                            if (info.packageName.equals("com.google.android.apps.photos")) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setComponent(new ComponentName(info.packageName, res.activityInfo.name));
                                intent.setPackage(res.activityInfo.packageName);
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                startActivityForResult(intent, IMAGE_PICKER_SELECT);
                                break;
                            }

                        }
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "no photos app,redirecting to playstore", Toast.LENGTH_SHORT).show();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.photos")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.photos")));
                    }
                }
            }
        });
        vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
            }
        });
        refreshLayout = view.findViewById(R.id.swipetoref);
        play = view.findViewById(R.id.videoply);
        recyclerView = view.findViewById(R.id.recywall);
        cardpics = view.findViewById(R.id.cardattchmnt);
        videoView = view.findViewById(R.id.videoview);
        cardpics.setVisibility(View.GONE);
        recycpics = view.findViewById(R.id.recypics);
        sendd = view.findViewById(R.id.send);
        sendd.setVisibility(View.GONE);
        posttext = view.findViewById(R.id.messagepost);
        recordView = view.findViewById(R.id.record_view);
        recordButton = view.findViewById(R.id.record_button);
        recordButton.setRecordView(recordView);
        recordButton.setListenForRecord(false);
        recordView.setLessThanSecondAllowed(false);
        reltv = view.findViewById(R.id.reltv);
        if (checkaudioperm()) {
            recordButton.setListenForRecord(true);
        }
        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(perms, RC_AUDIO);
            }
        });
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                Main4Activity.viewPager.setPagingEnabled(false);
                reltv.setVisibility(View.GONE);
                countdon = new CountDownTimer(300000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        //   Toast.makeText(TestRecord.this, String.valueOf(millisUntilFinished/1000), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
                MediaRecorderReady();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancel() {
                countdon.cancel();
                mediaRecorder.reset();
                //  MediaRecorderReady();
            }

            @Override
            public void onFinish(long recordTime) {
                countdon.cancel();
                mediaRecorder.stop();
                // mediaPlayer.release();
                //  MediaRecorderReady();

                //  Toast.makeText(TestRecord.this, String.valueOf(System.currentTimeMillis()/1000), Toast.LENGTH_SHORT).show();
                reltv.setVisibility(View.VISIBLE);
                Main4Activity.viewPager.setPagingEnabled(true);
                if (AudioSavePathInDevice != null && !TextUtils.isEmpty(AudioSavePathInDevice)) {
posting(null,null,AudioSavePathInDevice);
                }
            }

            @Override
            public void onLessThanSecond() {
                countdon.cancel();
                // recordButton.setListenForRecord(false);
                reltv.setVisibility(View.VISIBLE);
            }
        });
        posttext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recordButton.setClickable(false);
                recordButton.setVisibility(View.INVISIBLE);
                sendd.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {

                    recordButton.setClickable(true);
                    recordButton.setVisibility(View.VISIBLE);
                    sendd.setVisibility(View.GONE);
                }
            }
        });
        homefrag = new Homefragment();
        snapshotlist = new ArrayList<>();
        //snapshotimages=new ArrayList<>();
        uris = new HashMap<>();

        storage = Config.storageReference;
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                    gettingdata();
                }
            }
        });

        authlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    getActivity().finish();
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }
        };

        sendd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viduri != null) {
                    posting(viduri, null,null);
                    //   getResultsFromApi();
                } else if (imglist != null && imglist.size() > 0) {
                    posting(null, imglist,null);
                    //  sendImage(imglist);
                } else if (!TextUtils.isEmpty(posttext.getText().toString())) {
                    posting(null, null,null);
                } else {
                    posttext.setError("");
                }

            }
        });
        ImageButton attachment = view.findViewById(R.id.attachmnt);
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
//recyclerView.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));
            }
        };

        // linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        homeAdapter = new HomeAdapter(realm, getActivity(), realm.where(Pst.class).findAll(), true, getActivity().getSupportFragmentManager(),getActivity());
//
        recyclerView.setAdapter(homeAdapter);
        recyclerView.getLayoutManager().scrollToPosition(homeAdapter.getItemCount() - 1);
        recyclerView.setPlayerSelector(PlayerSelector.BY_AREA);

        gettingdata();
//         ffmeg=new MyFfmeg(getContext());
//        ffmeg.loadFFMpegBinary();
        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                reltv.setVisibility(View.VISIBLE);
                Main4Activity.viewPager.setPagingEnabled(true);
            }
        });
        return view;


    }

    @AfterPermissionGranted(RC_AUDIO)
    private boolean checkaudioperm() {

        boolean acpted;
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            // Already have permission, do the thing
            // ...
            acpted = true;
            recordButton.setListenForRecord(true);
            // attachmentDialog();

        } else {
            // Do not have permissions, request them now
            acpted = false;
            //

        }
        return acpted;
    }

    private void MediaRecorderReady() {
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/intouch/audio" +
                        String.valueOf(System.currentTimeMillis()) + "AudioRecording.3gp";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authlistner != null) {
            Config.auth.removeAuthStateListener(authlistner);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Config.auth.addAuthStateListener(authlistner);
    }

    private void gettingdata() {
        final ArrayList<Pst> pt = new ArrayList<>();
        if (getActivity().getSharedPreferences("id", MODE_PRIVATE).getString("ids", "").isEmpty()) {
            Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("posts").limit(20).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    int last = documentSnapshots.getDocuments().size();
                    for (int i = 0; i < documentSnapshots.getDocuments().size(); i++) {
                        Pst pst = new Pst();
                        Post post = documentSnapshots.getDocuments().get(i).toObject(Post.class);
                        assert post != null;
                        if (realm.where(Pst.class).equalTo("id", post.getId()).findFirst() == null) {
                            if (post.getImages() != null && post.getImages().size() > 0) {
                                RealmList<String> img = new RealmList<>();
                                img.addAll(post.getImages());
                                pst.setImages(img);
                            }
                            if (post.getPost1() != null) {
                                pst.setPost1(new com.f.schoolsintouchteachers.models.realmObjects.Post1(
                                        post.getPost1().getThumbnail(), post.getPost1().getVideo(), post.getPost1().getName(),
                                        post.getPost1().getPostedtime(), post.getPost1().getPost(), post.getPost1().getId(), post.getPost1().getType()
                                        , post.getPost1().getAudio()
                                ));
                            }
                            pst.setId(post.getId());
                            pt.add(pst);
                        }
                        if (i == (last - 1)) {
                            //    tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("id", MODE_PRIVATE).edit();
                            editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
                            editor.apply();

                        }
//                        if (documentSnapshots.getDocuments().get(i).contains("images")){
//                            Post1 imag= (Post1) documentSnapshots.getDocuments().get(i).get("post1");
//                            RealmList<String> img= (RealmList<String>) documentSnapshots.getDocuments().get(i).get("images");
//                            String id=documentSnapshots.getDocuments().get(i).getId();
//                            Pst pst=new Pst(img,imag,id);
//                            pt.add(pst);
//
//                            if (i == (last - 1)) {
//                                //    tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("id", MODE_PRIVATE).edit();
//                                editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
//                                editor.apply();
//
//                            }
//                        }else {
//                            Post1 imag= (Post1) documentSnapshots.getDocuments().get(i).get("post1");
//                            String id=documentSnapshots.getDocuments().get(i).getId();
//                            Pst pst=new Pst(imag,id);
//                            pt.add(pst);
//
//                            if (i == (last - 1)) {
//                                //    tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("id", MODE_PRIVATE).edit();
//                                editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
//                                editor.apply();
//
//                            }
//                        }
                        // ids.add(documentSnapshots.getDocuments().get(i).getId());
                        //  tv.append(documentSnapshots.getDocuments().get(i).getString("name"));

                    }
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(pt);


                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("likes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot documentSnapshots) {

                                    int d = documentSnapshots.getDocuments().size();
                                    for (final DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                                        documentSnapshot.getReference().collection("claps").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                                final int size = documentSnapshots.getDocuments().size();
                                                if (realm != null) {
                                                    realm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            Pst pst1 = realm.where(Pst.class).equalTo("id", documentSnapshot.getId()).findFirst();
                                                            assert pst1 != null;
                                                            if (pst1.isValid()) {
                                                                pst1.getPost1().setClaps(size);
                                                            }


                                                        }
                                                    });
                                                }

                                            }
                                        });
                                    }
                                }
                            });
                            // Transaction was a success.
                            //wallAdapter.notifyDataSetChanged();
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.setRefreshing(false);
                            }
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            // Transaction failed and was automatically canceled.
                            Log.d("REALMERROR", error.getMessage());
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Query query = Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("posts").orderBy("id").startAfter(getActivity().getSharedPreferences("id", MODE_PRIVATE).getString("ids", ""));
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    int last = documentSnapshots.getDocuments().size();

                    for (int i = 0; i < documentSnapshots.getDocuments().size(); i++) {
                        Pst pst = new Pst();
                        Post post = documentSnapshots.getDocuments().get(i).toObject(Post.class);
                        assert post != null;
                        if (realm.where(Pst.class).equalTo("id", post.getId()).findFirst() == null) {
                            if (post.getImages() != null && post.getImages().size() > 0) {
                                RealmList<String> img = new RealmList<>();
                                img.addAll(post.getImages());
                                pst.setImages(img);
                            }
                            if (post.getPost1() != null) {
                                pst.setPost1(new com.f.schoolsintouchteachers.models.realmObjects.Post1(
                                        post.getPost1().getThumbnail(), post.getPost1().getVideo(), post.getPost1().getName(),
                                        post.getPost1().getPostedtime(), post.getPost1().getPost(), post.getPost1().getId(),
                                        post.getPost1().getType(), post.getPost1().getAudio()
                                ));
                            }

                            pst.setId(post.getId());
                            pt.add(pst);
                        }

                        if (i == (last - 1)) {
                            //    tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("id", MODE_PRIVATE).edit();
                            editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
                            editor.apply();

                        }
//                        if (documentSnapshots.getDocuments().get(i).contains("images")){
//                            Post1 imag= (Post1) documentSnapshots.getDocuments().get(i).get("post1");
//                            RealmList<String> img= (RealmList<String>) documentSnapshots.getDocuments().get(i).get("images");
//                            String id=documentSnapshots.getDocuments().get(i).getId();
//                            Pst pst=new Pst(img,imag,id);
//                            pt.add(pst);
//
//                            if (i == (last - 1)) {
//                                //    tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("id", MODE_PRIVATE).edit();
//                                editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
//                                editor.apply();
//
//                            }
//                        }else {
//                            Post1 imag= (Post1) documentSnapshots.getDocuments().get(i).get("post1");
//                            String id=documentSnapshots.getDocuments().get(i).getId();
//                            Pst pst=new Pst(imag,id);
//                            pt.add(pst);
//
//                            if (i == (last - 1)) {
//                                //    tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("id", MODE_PRIVATE).edit();
//                                editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
//                                editor.apply();
//
//                            }
//                        }
                        // ids.add(documentSnapshots.getDocuments().get(i).getId());
                        //  tv.append(documentSnapshots.getDocuments().get(i).getString("name"));

                    }
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (pt.size() > 0) {
                                realm.insertOrUpdate(pt);
//                                String id = pt.get(0).getId();
//                                String post = pt.get(0).getPost1().getPost();
                                //wallAdapter.notifyDataSetChanged();

                            }

                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("likes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot documentSnapshots) {

                                    int d = documentSnapshots.getDocuments().size();
                                    for (final DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                                        documentSnapshot.getReference().collection("claps").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                                final int size = documentSnapshots.getDocuments().size();
                                                if (realm != null) {
                                                    realm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            Pst pst1 = realm.where(Pst.class).equalTo("id", documentSnapshot.getId()).findFirst();
                                                            if (pst1!=null && pst1.isValid()) {
                                                                if (pst1.getPost1() != null) {
                                                                    pst1.getPost1().setClaps(size);
                                                                }
                                                            }


                                                        }
                                                    });
                                                }

                                            }
                                        });
                                    }
                                }
                            });
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.setRefreshing(false);
                            }
                        }
                    });


                }
            });
        }

//        Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("likes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot documentSnapshots) {
//
//                int d = documentSnapshots.getDocuments().size();
//                for (final DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
//                    documentSnapshot.getReference().collection("claps").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot documentSnapshots) {
//                            final int size = documentSnapshots.getDocuments().size();
//                            if (realm != null) {
//                                realm.executeTransaction(new Realm.Transaction() {
//                                    @Override
//                                    public void execute(Realm realm) {
//                                        Pst pst1 = realm.where(Pst.class).equalTo("id", documentSnapshot.getId()).findFirst();
//                                        pst1.getPost1().setClaps(size);
//
//                                    }
//                                });
//                            }
//
//                        }
//                    });
//                }
//            }
//        });
//        databaseref=FirebaseDatabase.getInstance().getReference().child("posts");
//        databaseref.keepSynced(true);
//
//        databaseref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChildren()){
////                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
////                        if (snapshot.child("images").exists()){
////                            for (int i=0;i<snapshot.child("images").getChildrenCount();i++){
////                                img.add(Uri.parse(snapshot.child("images").child(String.valueOf(i)).getValue(String.class)));
////                            }
////                            uris.put(snapshotlist.size(),new ArrayList<Uri>(img));
////                            snapshotlist.add(new Post(snapshot.child("postedtime").getValue(String.class),uris,snapshot.child("post").getValue(String.class),snapshot.getKey()));
////                       img.clear();
////                        }else if (snapshot.child("video").exists()){
////                            snapshotlist.add(new Post(snapshot.child("thumbnail").getValue(String.class),snapshot.child("video").getValue(String.class),snapshot.child("name").getValue(String.class),
////                                    snapshot.child("postedtime").getValue(String.class),snapshot.child("post").getValue(String.class),snapshot.getKey()));
////                        }else {
////                            snapshotlist.add(snapshot.getValue(Post.class));
////                        }
////
////                    }
//wallAdapter.notifyDataSetChanged();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//Log.d("Error",databaseError.getMessage());
//            }
//        });

    }

    private void posting(@Nullable Uri uri, @Nullable ArrayList<Uri> arrayList,@Nullable String audio) {
        String post = posttext.getText().toString();
        DateTime dateTime = new DateTime();
        DateTime msa = dateTime.withZone(DateTimeZone.forID("Africa/Nairobi"));
        // DocumentReference collectionReference = Config.firestore.document(Config.auth.getCurrentUser().getDisplayName()).collection("posts").document(msa.toString());
        final Pst p = new Pst();
        com.f.schoolsintouchteachers.models.realmObjects.Post1 post1 = new com.f.schoolsintouchteachers.models.realmObjects.Post1();
        if (!TextUtils.isEmpty(post)) {
            @SuppressLint("StaticFieldLeak")
            HttpAsync httpAsync = new HttpAsync(post, getActivity().getSharedPreferences("Token", MODE_PRIVATE).getString("mytoken", "")) {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                }
            };
            // httpAsync.execute();

            if (uri != null && arrayList == null&& TextUtils.isEmpty(audio)) {
                MediaMetadataRetriever g = new MediaMetadataRetriever();
                g.setDataSource(getContext(), viduri);
                thumbnl = g.getFrameAtTime(1000);
                File SDCardRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "intouch/myvideos/thumbnails");
                File file = new File(SDCardRoot, String.valueOf(System.currentTimeMillis()) + ".png");
                if (!SDCardRoot.exists()) {
                    try {
                        SDCardRoot.mkdirs();
                        file.createNewFile();
                        thumbnl.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                            thumbnl.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                imge = Uri.fromFile(file).toString();
                post1.setPost(post);
                post1.setId(msa.toString());
                post1.setThumbnail(imge);
                post1.setPostedtime(String.valueOf(System.currentTimeMillis() / 1000));
                post1.setName(jina);
                post1.setVideo(viduri.toString());
                post1.setClaps(0);
                p.setId(msa.toString());
                p.setPost1(post1);
                p.setPosting(true);
                realm.beginTransaction();
                realm.insert(p);
                realm.commitTransaction();
                if (images != null) {
                    images.clear();
                }
                download = null;
                imge = "";
                jina = "";
                thumbnl = null;
                posttext.setText("");
                cardpics.setVisibility(View.GONE);
                recycpics.setVisibility(View.GONE);
                if (imglist != null) {
                    imglist.clear();
                }
                viduri = null;
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Hide:
                assert imm != null;
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            } else if (uri == null && arrayList != null && TextUtils.isEmpty(audio)) {
                post1.setPost(post);
                post1.setId(msa.toString());
                post1.setThumbnail(imge);
                post1.setPostedtime(String.valueOf(System.currentTimeMillis() / 1000));
                post1.setName(jina);
                RealmList<String> realmList = new RealmList<>();
                for (Uri uri1 : arrayList) {
                    realmList.add(uri1.toString());
                }
                p.setImages(realmList);
                post1.setClaps(0);
                p.setId(msa.toString());
                p.setPost1(post1);
                p.setPosting(true);
                realm.beginTransaction();
                realm.insert(p);
                realm.commitTransaction();
                if (images != null) {
                    images.clear();
                }
                download = null;
                imge = "";
                jina = "";
                thumbnl = null;
                posttext.setText("");
                recycpics.setVisibility(View.GONE);
                if (imglist != null) {
                    imglist.clear();
                }
                viduri = null;
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Hide:
                assert imm != null;
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                realm.executeTransactionAsync(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.insertOrUpdate(p);
//                    }
//                },new Realm.Transaction.OnSuccess(){
//
//                    @Override
//                    public void onSuccess() {
//                        if (images!=null){
//                        images.clear();
//                    }
//                    download = null;
//                    imge = "";
//                    jina = "";
//                    posttext.setText("");
//                    recycpics.setVisibility(View.GONE);
//                    if (imglist!=null){
//                        imglist.clear();
//                    }
//                    viduri = null;
//                    }
//                });

            }
        }else {
            if (uri == null && arrayList == null && !TextUtils.isEmpty(audio)){
                post1.setPost(post);
                post1.setId(msa.toString());
                post1.setThumbnail(imge);
                post1.setPostedtime(String.valueOf(System.currentTimeMillis() / 1000));
                post1.setName(jina);
                post1.setAudio(audio);
                post1.setType(1);
                post1.setClaps(0);
                p.setId(msa.toString());
                p.setPost1(post1);
                p.setPosting(true);
                realm.beginTransaction();
                realm.insert(p);
                realm.commitTransaction();
                if (images != null) {
                    images.clear();
                }
                download = null;
                imge = "";
                jina = "";
                thumbnl = null;
                AudioSavePathInDevice=null;
                posttext.setText("");
                recycpics.setVisibility(View.GONE);
                if (imglist != null) {
                    imglist.clear();
                }
                viduri = null;
            }

        }

    }


//    private void gettingdata() {
//        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Schools");
//        Query query=databaseReference.orderByChild("maths").equalTo("60");
//
//


    private void getResultsFromApi() {
//add to realm first
        if (viduri == null) {
            choose();

        } else {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            //  mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
            if (thumbnl != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbnl.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                d = byteArrayOutputStream.toByteArray();
                final StorageReference reference = storage.child(Config.auth.getCurrentUser().getDisplayName()).child("thumbnail/" + System.currentTimeMillis() + ".png");
                UploadTask task = reference.putBytes(d);
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
                            final StorageReference storageReference = storage.child(Config.auth.getCurrentUser().getDisplayName()).child("videos/" + viduri.getLastPathSegment());
                            final UploadTask taskk = storageReference.putFile(viduri);
                            taskk.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                    if (!task.isSuccessful()) {
                                        mProgressDialog.dismiss();
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
                                        mProgressDialog.dismiss();
                                        download = task.getResult();
                                        //   posting();

                                    }
                                }
                            });
                            taskk.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    mProgressDialog.setMessage("Uploading:" + taskSnapshot.getBytesTransferred());
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "thumbnail is null from getresultapi", Toast.LENGTH_SHORT).show();
            }
//                assert input != null;
//                File file=new File(yomyuri);
//                Toast.makeText(getActivity(), String.valueOf(file.length()/1024), Toast.LENGTH_SHORT).show();

            // UploadActvty.MakeRequestTask makeRequestTask=new UploadActvty.MakeRequestTask(mCredential)

//            taskk.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    mProgressDialog.dismiss();
//                    //  sendImage(task.getResult().getDownloadUrl());
//                    jina = taskSnapshot.getMetadata().getName();
//                    download = taskSnapshot.getTask().getDownloadUrl();
//                    posting();
//                }
//            });
            String pst = "";
            if (!TextUtils.isEmpty(posttext.getText().toString())) {
                pst = posttext.getText().toString();
            }

            //   snapshotlist.add(new Post(Base64.encodeToString(d, Base64.DEFAULT),viduri.toString(),viduri.getLastPathSegment(),String.valueOf(System.currentTimeMillis()/1000),pst,""));
//wallAdapter.notifyDataSetChanged();
        }


    }


    @AfterPermissionGranted(RC_WRITESTORAGE)
    private void choose() {
//attachmentDialog();
        String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            // Already have permission, do the thing
            // ...
            layview.post(revealAnimationRunnable);
            // attachmentDialog();

        } else {
            // Do not have permissions, request them now
            requestPermissions(perms, RC_WRITESTORAGE);

        }

    }

    private void attachmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Choose type of media file");
        builder.setPositiveButton("Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PackageManager pm = getActivity().getPackageManager();
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                List<ResolveInfo> listGallery = pm.queryIntentActivities(galleryIntent, 0);
                // List<Pair<String, Intent>> result = new ArrayList<>();
                try {
                    if (pm.getApplicationInfo("com.google.android.apps.photos", 0) == null) {


                    } else {
                        for (ResolveInfo res : listGallery) {
                            ActivityInfo info = res.activityInfo;

                            if (info.packageName.equals("com.google.android.apps.photos")) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setComponent(new ComponentName(info.packageName, res.activityInfo.name));
                                intent.setPackage(res.activityInfo.packageName);
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                startActivityForResult(intent, IMAGE_PICKER_SELECT);
                                break;
                            }

                        }
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "no photos app,redirecting to playstore", Toast.LENGTH_SHORT).show();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.photos")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.photos")));
                    }
                }


            }
        });
        builder.setNegativeButton("Video", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public long getIdForFile(String filePath) {

        Cursor c = null;
        try {
            c = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_PICKER_SELECT:
                if (resultCode == RESULT_OK) {
                    ArrayList<Uri> mydata = new ArrayList<>();
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            mydata.add(imageUri);

                            //do something with the image (save it to some directory or whatever you need to do with it here)
                        }
                        compressing(mydata);
                    } else if (data.getData() != null) {
                        mydata.add(data.getData());
                        compressing(mydata);
                        // String imagePath = data.getData().getPath();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }

                }
                break;

        }
    }

    private void compressing(ArrayList<Uri> data) {
        imglist = new ArrayList<>();

        for (Uri uri : data) {
            if (uri.toString().contains("image")) {
                imglist.add(uri);
                Toast.makeText(getContext(), "its an image", Toast.LENGTH_SHORT).show();
                layview.post(revealAnimationRunnable);
                //handle image
            } else if (uri.toString().contains("video")) {
                recycpics.setVisibility(View.GONE);
                cardpics.setVisibility(View.VISIBLE);
                layview.post(revealAnimationRunnable);
                final MediaController mediaController = new MediaController(getContext());
                videoView.setMediaController(mediaController);
                // mediaController.setAnchorView(videoView);

                videoView.setVideoURI(uri);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaController.show(3000);
                        if (mediaController.isShowing()) {
                            mediaController.hide();
                        }
                    }
                });
                viduri = uri;
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.start();
                        v.setVisibility(View.GONE);

                    }
                });

            }
        }
        if (!imglist.isEmpty()) {
            cardpics.setVisibility(View.GONE);
            recycpics.setVisibility(View.VISIBLE);
            recycpics.setHasFixedSize(true);
            recycpics.setLayoutManager(new GridLayoutManager(getContext(), 3));
            recycpics.setAdapter(new PreviewpicsAdapter(getContext(), imglist));
        }

    }

    private void compressingVid(Uri uri) {
        try {
            input = Util.getFilePath(getContext(), uri);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final String f = outputDir + File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("waiting 1 minutes..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
//                String[] command = {"-y", "-i", input, "-s", "640x480", "-r", "24", "-vcodec", "h264", "-b:v", "110k", "-b:a", "48000", "-ac", "2", "-ar", "22050", f};
//                ffmeg.execFFmpegBinary(command);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Choose compression..");
        builder.setPositiveButton("Medium", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    //  mProgressDialog.show();
                } else {
                    //  mProgressDialog.show();
                }
                VideoCompress.compressVideoMedium(input, f, new VideoCompress.CompressListener() {
                    @Override
                    public void onStart() {
                        startTime = System.currentTimeMillis();


                        Util.writeFile(getContext(), "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");

                    }

                    @Override
                    public void onSuccess() {
                        //  mProgressDialog.dismiss();
                        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(f))));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                long vidid = getIdForFile(f);
                                Log.d("VIDID", " " + vidid);
                                String urivid = "content://media/external/video/media/" + vidid;
                                viduri = Uri.parse(urivid);//  it's right uri  for videofile
                                MediaMetadataRetriever g = new MediaMetadataRetriever();
                                g.setDataSource(getActivity(), viduri);
                                thumbnl = g.getFrameAtTime(1000);
//                                                long minutes=Long.parseLong(g.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//                                                int minute=(int) ((minutes/1000)/60);
//                                                Toast.makeText(getActivity(),String.valueOf(minute), Toast.LENGTH_SHORT).show();
                                // getResultsFromApi();
                                File file = new File(f);
                                long length = file.length();
                                length = (length / 1024) / 1024;
                                Toast.makeText(getContext(), "video sixe " + length, Toast.LENGTH_SHORT).show();

                            }
                        }, 3000);
//

                    }

                    @Override
                    public void onFail() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onProgress(float percent) {
                        mProgressDialog.setProgress((int) percent);
                    }
                });
            }
        });
        builder.setNegativeButton("Low", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                VideoCompress.compressVideoLow(input, f, new VideoCompress.CompressListener() {
                    @Override
                    public void onStart() {
                        startTime = System.currentTimeMillis();
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        mProgressDialog.show();
                        Util.writeFile(getContext(), "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");

                        // Toast.makeText(getContext(), "Started task", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {

                        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(f))));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                long vidid = getIdForFile(f);
                                Log.d("VIDID", " " + vidid);
                                String urivid = "content://media/external/video/media/" + vidid;
                                viduri = Uri.parse(urivid);//  it's right uri  for videofile
                                MediaMetadataRetriever g = new MediaMetadataRetriever();
                                g.setDataSource(getActivity(), viduri);
                                thumbnl = g.getFrameAtTime(1000);
//                                                long minutes=Long.parseLong(g.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//                                                int minute=(int) ((minutes/1000)/60);
//                                                Toast.makeText(getActivity(),String.valueOf(minute), Toast.LENGTH_SHORT).show();
                                // getResultsFromApi();
                                File file = new File(f);
                                long length = file.length();
                                length = (length / 1024) / 1024;
                                Toast.makeText(getContext(), "video sixe " + length, Toast.LENGTH_SHORT).show();

                            }
                        }, 3000);
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFail() {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onProgress(float percent) {
                        mProgressDialog.setProgress((int) percent);
                    }
                });
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

    }


    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    /**
     * An asynchronous task that handles the YouTube Data API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
        if (animator != null) {
            if (animator.getListeners() != null && animator.getListeners().size() > 0) {
                animator.removeAllListeners();
            }
        }

        layview.removeCallbacks(revealAnimationRunnable);
        try {
            File dir = getContext().getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
