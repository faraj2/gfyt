package com.f.schoolsintouchteachers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.f.schoolsintouchteachers.trails.Search;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Rectangle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 12;
    private int id;
    MenuItem menuItem;
    private int IMAGE_CODE = 101;
    private Uri imageUri;
    EditText Profile, stream;
    AutoCompleteTextView Name;
    Menu men;
    Editable nam, prof;
    DatabaseReference databaseReference;
    List<EditText> myedits = new ArrayList<>();
    private LinearLayout linearLayout;
    StorageReference firebaseStorage;
    private ProgressDialog progressDialog;
    private ImageButton imageView;
    private Button signup;
    private EditText phone, classfrom, classto;
    private Uri sizedimage;
    private RadioGroup rdgroup;
    private String type;
    private String download;
private boolean isnetworkavail(){
    ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    assert connectivityManager != null;
    NetworkInfo info=connectivityManager.getActiveNetworkInfo();
    return info!=null && info.isConnected();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        imageView = findViewById(R.id.imageView5);
        databaseReference =  FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        RelativeLayout back = findViewById(R.id.emiter_top_left);
        back.addView(new BlueView(this));

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bottombar)));
        classfrom = findViewById(R.id.etClassnumber);
        classto = findViewById(R.id.etToclass);
        signup = findViewById(R.id.save);
        signup.setText("SIGNUP");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(phone.getText().toString()) && !TextUtils.isEmpty(Name.getText().toString()) && !TextUtils.isEmpty(type)) {
                   databaseReference.child("numbers")
                            .child("used").addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if (dataSnapshot.child(phone.getText().toString()).exists()) {
                               Toast.makeText(Main3Activity.this, "The phone number is already registered", Toast.LENGTH_LONG).show();
                               Log.d("EXISTS", "EXZ");
                           } else {
                               if (sizedimage != null) {
                                   imageStore(sizedimage);
                               } else {
                                   otp(download);
                               }
//                                databaseReference.document("numbers")
//                                        .collection("used").document("names").collection("schools").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        task.getResult().getDocuments().contains("al")
//                                    }
//                                })
                               // Rectangle rectangle=new Rectangle(4,8,2)

                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
                } else {
                    Toast.makeText(Main3Activity.this, "School name,Phone Number and either Primary or secondary are must", Toast.LENGTH_LONG).show();
                    if (TextUtils.isEmpty(Name.getText().toString())) {
                        Name.setError("");
                    } else if (TextUtils.isEmpty(phone.getText().toString())) {
                        phone.setError("");
                    } else if (TextUtils.isEmpty(type)) {
                        AppCompatRadioButton radioButton = findViewById(R.id.rbsecondry);
                        radioButton.setError("");
                    }
                }


            }
        });
        linearLayout = findViewById(R.id.linearedit);
        phone = findViewById(R.id.etPhone);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", s.toString() + " " + start + " " + before + " " + count);
                if (s.length() != 0 && s.charAt(0) == '0') {
                    phone.setError("PLEASE DONT INCLUDE THE ZERO FROM YOUR MOBILENUMBER");
                    phone.requestFocus();
                    phone.setText("");
                    Log.d("ZERO", s.toString());


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rdgroup = findViewById(R.id.radioGroup);
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
        Name = findViewById(R.id.etSchoolname);
        final ProgressDialog dialog=new ProgressDialog(this);
        final ArrayList<String> names=new ArrayList<>();
if (isnetworkavail()){
    dialog.setMessage("loading please wait");
    dialog.show();

    databaseReference.child("Schools").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot:
                    dataSnapshot.getChildren()) {
                names.add((snapshot.child("name").getValue(String.class)).toLowerCase());
            }
            getSharedPreferences("schoolnames",MODE_PRIVATE).edit().putStringSet("names", new HashSet<String>(names)).apply();
            dialog.dismiss();
            names.clear();
            ArrayList<String> me=new ArrayList<>(getSharedPreferences("schoolnames",MODE_PRIVATE).getStringSet("names",new HashSet<String>()));
            Name.setAdapter(new ArrayAdapter<String>(Main3Activity.this,android.R.layout.simple_list_item_1,me));

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}else {
    ArrayList<String> me=new ArrayList<>(getSharedPreferences("schoolnames",MODE_PRIVATE).getStringSet("names",new HashSet<String>()));
    Name.setAdapter(new ArrayAdapter<String>(Main3Activity.this,android.R.layout.simple_list_item_1,me));
}



        Name.setCursorVisible(true);
        Name.setCompletionHint("registered schools");
        Name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if (!parent.getItemAtPosition(position).toString().isEmpty())
                  Name.setError("school already registered");

                Toast.makeText(Main3Activity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
//                switch (parent.getItemAtPosition(position).toString()){
//                    case "afraha secondary":
//                        Name.setError("school already registered");
//                        break;
//                }
            }
        });

        Profile = findViewById(R.id.etProfile);
        stream = findViewById(R.id.etStreamNo);
        // EditText stream = view.findViewById(R.id.etStreamNo);
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


                        EditText editText = new EditText(getApplicationContext());

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


    }

    private void otp(final String download) {
        final String mobile = phone.getText().toString();
        final String name = Name.getText().toString();
        final String profile = Profile.getText().toString();
        final String classnum = classfrom.getText().toString();
        final String clasto = classto.getText().toString();

        AlertDialog.Builder a = new AlertDialog.Builder(Main3Activity.this);
        a.setMessage("an SMS will be sent to the number +254" + mobile + " with the otp code.Please confirm the number.\n\n*standard sms charges may apply");
        a.setTitle("VERIFICATION");
        a.setNegativeButton("edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        a.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Main3Activity.this, Otp.class);
                i.putExtra("number", mobile);
                i.putExtra("schoolname", name);
                i.putExtra("type", type);
                i.putExtra("profile", profile);
                i.putExtra("clasnum", classnum);
                i.putExtra("clasto", clasto);
                i.putExtra("image", download);
                if (!myedits.isEmpty()) {
                    // Bundle bundle=new Bundle();
                    ArrayList<String> edits = new ArrayList<>();
                    for (EditText editText : myedits) {
                        edits.add(editText.getText().toString());
                    }
                    //  bundle.putStringArrayList("streams",edits);
                    i.putStringArrayListExtra("streams", edits);
                    //  i.putExtras(bundle);
                }
                //  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        AlertDialog alertDialog = a.create();
        alertDialog.show();


    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//                Profile.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                prof = editable;
//                if (nam != null)
//                    if (nam.length() >= 3 && editable.length() > 250) {
//                        men.getItem(0).setEnabled(true);
//                    } else {
//                        men.getItem(0).setEnabled(false);
//                    }
//
//
//            }
//        });
//        Name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                nam = editable;
//                if (prof != null)
//                    if (editable.length() >= 3 && prof.length() >250) {
//                        men.getItem(0).setEnabled(true);
//                    } else {
//                        men.getItem(0).setEnabled(false);
//                    }
//
//
//            }
//        });
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.men = menu;
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main3, menu);
//        menu.getItem(0).setEnabled(false);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.next) {
//            startActivity(new Intent(Main3Activity.this, Main4Activity.class));
//            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void browsepic(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            browseImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    browseImage();
                } else {
                    Toast.makeText(Main3Activity.this, "Please accept the permission to attach the image", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
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
                CompressImage compressImagee = new CompressImage(getApplicationContext());

                String image = compressImagee.compressImage(imageUri.toString());
                File file = new File(image);
                long sz = file.length() / 1024;
                Toast.makeText(Main3Activity.this, String.valueOf(sz), Toast.LENGTH_SHORT).show();
                Log.d("IMAGE SUZE", String.valueOf(sz));
                sizedimage = Uri.fromFile(file);


                Glide.with(Main3Activity.this).load(sizedimage).into(imageView);
            }


        }
    }

    private void imageStore(Uri sizedimage) {
        progressDialog = new ProgressDialog(Main3Activity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        final StorageReference reference = firebaseStorage.child(String.valueOf(System.currentTimeMillis())).child("upload.jpg");
        final UploadTask uploadTask = reference.putFile(sizedimage);
        uploadTask.addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                Toast.makeText(Main3Activity.this, String.valueOf(progress), Toast.LENGTH_SHORT).show();
//
//                progressDialog.setProgress((int) progress);

//        progressDialog.setButton("pause", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (uploadTask.isInProgress()){
//                    uploadTask.pause();
//                }else if(uploadTask.isPaused()){
//                    uploadTask.resume();
//                }
//            }
//        });

            }
        });
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                download = taskSnapshot.getDownloadUrl().toString();
//                // taskSnapshot.getDownloadUrl();
//                otp(download);
//                Toast.makeText(Main3Activity.this, download, Toast.LENGTH_SHORT).show();
//                Log.d("DOWNLOADURL", download);
//                progressDialog.dismiss();
//            }
//        });
    }


}
