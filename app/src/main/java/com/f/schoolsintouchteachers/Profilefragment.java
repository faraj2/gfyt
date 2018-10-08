package com.f.schoolsintouchteachers;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 2/6/2018.
 */

public class Profilefragment extends Fragment {

    Activity context;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 12;
    private int id;
    MenuItem menuItem;
    private int IMAGE_CODE = 101;
    private Uri imageUri;
    EditText Name, Profile;
    Menu men;
    Editable nam, prof;
    FirebaseFirestore databaseReference;
    List<EditText> myedits = new ArrayList<>();
    private LinearLayout linearLayout;
    StorageReference firebaseStorage;
    private ProgressDialog progressDialog;
    private ImageButton imageView;
    private FragmentActivity activity;
    private static Profilefragment instance;
    private Button savebt;
    private Uri sizedimage;
    private String download = null;
    private EditText toclass, clsnumbr;
    private RadioGroup rdgroup;
    private String type;
    private EditText streams;
    FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth auth;
    private String schoolname;
    private DocumentReference ref;
    private ListenerRegistration registration;


    public Profilefragment getInstance(Activity context) {
        this.context = context;
        if (instance == null)
            instance = new Profilefragment();

        return instance;
    }

//    @SuppressLint("ValidFragment")
//    public Profilefragment() {
//
//    }
//
//    public Profilefragment() {
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText stream = view.findViewById(R.id.etStreamNo);
        stream.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//int i=Integer.parseInt(editable.toString());
                if (editable.toString().equals("")) {
                    linearLayout.removeAllViews();
//                    if (!myedits.isEmpty()){
//                        for (EditText myedit : myedits) {
//                            linearLayout.removeAllViews();
//                        }


                } else {
                    int i = Integer.parseInt(editable.toString());
                    myedits.clear();
//                    Toast.makeText(context, stream.getText(), Toast.LENGTH_SHORT).show();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    for (int j = 0; j < i; j++) {


                        EditText editText = new EditText(activity);

                        layoutParams.setMargins(0, 34, 15, 15);

                        editText.setEms(7);
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                        editText.setPadding(15, 15, 15, 15);
                        editText.setSingleLine();
                        editText.setTextColor(getResources().getColor(R.color.cardview_dark_background));
                        editText.setBackground(getResources().getDrawable(R.drawable.edittext_white));
                        myedits.add(editText);

                    }

                    for (EditText myedit : myedits) {
                        linearLayout.addView(myedit, layoutParams);
                    }
                }


            }
        });
        Profile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                prof = editable;
//                if (nam != null)
//                    if (nam.length() >= 3 && editable.length() > 250) {
//                        men.getItem(0).setEnabled(true);
//                    } else {
//                        men.getItem(0).setEnabled(false);
//                    }
//
//
            }
        });
        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                nam = editable;
//                if (prof != null)
//                    if (editable.length() >= 3 && prof.length() >250) {
//                        men.getItem(0).setEnabled(true);
//                    } else {
//                        men.getItem(0).setEnabled(false);
//                    }


            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main3, container, false);
        databaseReference = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    if (!TextUtils.isEmpty(firebaseAuth.getCurrentUser().getDisplayName())){
                        Log.d("USER",firebaseAuth.getCurrentUser().getDisplayName());
                        schoolname=firebaseAuth.getCurrentUser().getDisplayName();
                        if (!TextUtils.isEmpty(schoolname)){
                            ref= databaseReference.collection("Schools").document(schoolname);
                    registration= ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                                    Name.setText(documentSnapshot.getString("school_name"));
                                    Glide.with(getContext()).load(documentSnapshot.getString("profile_pic")).into(imageView);
                                    clsnumbr.setText(documentSnapshot.getString("classfrom"));
                                    toclass.setText(documentSnapshot.getString("classto"));
                                    if (documentSnapshot.getString("type").equals("Primary")){
                                        rdgroup.check(R.id.rbprimary);
                                        //  view.findViewById(R.id.rbprimary).setSelected(true);
                                    }else {
                                        rdgroup.check(R.id.rbsecondry);
                                        //   view.findViewById(R.id.rbsecondry).setSelected(true);
                                    }

                                }
                            });
                        }

                    }
                }



            }
        };



        activity = getActivity();

        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bottombar)));

        savebt = view.findViewById(R.id.save);
        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizedimage != null) {
                    imageStore(sizedimage);
                } else {
                    Toast.makeText(getContext(), "An image is not selected ", Toast.LENGTH_SHORT).show();
                    saving(download);
                }
            }
        });
        clsnumbr = view.findViewById(R.id.etClassnumber);
        toclass = view.findViewById(R.id.etToclass);
        rdgroup = view.findViewById(R.id.radioGroup);
        streams = view.findViewById(R.id.etStreamNo);
//streams.setText("4");

                rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbprimary:
                        type = "Primary";
                        Log.d("PRI", type);
                        break;
                    case R.id.rbsecondry:
                        type = "Secondary";
                        Log.d("SEC", type);
                        break;
                }
            }
        });

        linearLayout = view.findViewById(R.id.linearedit);
        imageView = view.findViewById(R.id.imageView5);
        Name = view.findViewById(R.id.etSchoolname);
        Profile = view.findViewById(R.id.etProfile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                } else {
                    browseImage();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
registration.remove();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
        registration.remove();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }

    private void saving(String download) {
        String schlname = Name.getText().toString();
        String classnmbr = clsnumbr.getText().toString();
        String tooclass = toclass.getText().toString();
        String profile = Profile.getText().toString();



        if (!myedits.isEmpty()) {
            ArrayList<String> edittexts = new ArrayList<>();
            for (EditText myedit : myedits) {
                edittexts.add(myedit.getText().toString());
            }
        }
        if (!TextUtils.isEmpty(schlname) && !TextUtils.isEmpty(type)){
            Map<String,Object> values=new HashMap<>();
            values.put("school_name",schlname);
            values.put("type",type);
            values.put("profile_pic",download);
            values.put("classfrom",classnmbr);
            values.put("classto",tooclass);
            values.put("profile",profile);
            databaseReference.collection("Schools").document(schlname).set(values).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "DONE", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ERROR",e.getMessage());
                }
            });
        }
        // if (TextUtils.isEmpty(schlname))

    }

    private void storedata() {

    }


    public void myclickmethod(View view) {
//        switch (view.getId())
//        {
//            case R.id.imageView5:
//
//            //    Toast.makeText(context, "yeeeyyy", Toast.LENGTH_SHORT).show();
//
//
//                break;
//        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    browseImage();
                } else {
                    Toast.makeText(activity, "Please accept the permission to attach the image", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                }
                break;

            default:
                break;
        }
    }

    private void browseImage() {
        //  Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent galleryy = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryy, IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CODE) {
            if (data != null) {
                imageUri = data.getData();
                CompressImage compressImagee = new CompressImage(getActivity());

                String image = compressImagee.compressImage(imageUri.toString());
                File file = new File(image);
                long sz = file.length() / 1024;
                Toast.makeText(getContext(), String.valueOf(sz), Toast.LENGTH_SHORT).show();
                Log.d("IMAGE SUZE", String.valueOf(sz));
                sizedimage = Uri.fromFile(file);


                Glide.with(getContext()).load(sizedimage).into(imageView);
                //  imageStore(sizedimage);
            }


        }
    }

    private void imageStore(Uri sizedimage) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        final StorageReference reference = firebaseStorage.child("imagess/upload.jpg");
        final UploadTask uploadTask = reference.putFile(sizedimage);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Toast.makeText(getContext(), String.valueOf(progress), Toast.LENGTH_SHORT).show();

                progressDialog.setProgress((int) progress);

                progressDialog.setButton("pause", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (uploadTask.isInProgress()) {
                            uploadTask.pause();
                        } else if (uploadTask.isPaused()) {
                            uploadTask.resume();
                        }
                    }
                });

            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR", e.getMessage());
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //   taskSnapshot.getDownloadUrl();
//download=taskSnapshot.getDownloadUrl().toString();
                Log.d("DOWNLOADURL", download);
                Toast.makeText(getContext(), download, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                saving(download);
            }
        });
    }
}
