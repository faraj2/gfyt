package com.f.schoolsintouchteachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {
    private String mVerificationId = null;
    String phoneNum = "+254777777778";
    String testVerificationCode = "789100";
    Button button;
    EditText otpp;
    private PhoneAuthCredential myauth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        RelativeLayout relativeLayout = findViewById(R.id.emiter_top_left);
        relativeLayout.addView(new BlueView(this));
        final TextView textView = findViewById(R.id.textView19);
        button=findViewById(R.id.btnextotp);
        button.setAlpha(0.3f);
        button.setEnabled(false);
        otpp=findViewById(R.id.etPasswordotp);



// Whenever verification is triggered with the whitelisted number,
// provided it is not set for auto-retrieval, onCodeSent will be triggered.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Log.d("OTP",phoneAuthCredential.getSmsCode());
                        myauth=phoneAuthCredential;


                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(Otp.this, "ERROR SENDING "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        mVerificationId = s;
                        otpp.setText(testVerificationCode);
                        button.setAlpha(1);
                        button.setEnabled(true);
                        Toast.makeText(Otp.this, "CODE SENT "+s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Log.d("ERROR RETRIEVE ",s);
                    }
                });



        new CountDownTimer(300000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText("You will recieve an SMS shortly with OTP code. Resending in: "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {

            }
        }.start();
      Uri uri= getIntent().getParcelableExtra("image");
      if (uri!=null){
          Log.d("IMAGE",uri.toString());
      }
    }
    public void next(View view){
        progressDialog.show();
        if (!getIntent().getStringExtra("schoolname").isEmpty()){
          SharedPreferences.Editor preferences= getSharedPreferences("Schoolname",MODE_PRIVATE).edit();
          preferences.putString("school",getIntent().getStringExtra("schoolname"));
          preferences.putString("number",getIntent().getStringExtra("number"));
          preferences.apply();

        }
        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(mVerificationId,testVerificationCode);
        Map<String,Object> mymap=new HashMap<>();
        final Map<String,Object> mystreams=new HashMap<>();
        mymap.put("school_name",getIntent().getStringExtra("schoolname"));
        mymap.put("type",getIntent().getStringExtra("type"));
        mymap.put("profile",getIntent().getStringExtra("profile"));
        mymap.put("profile_pic",getIntent().getStringExtra("image"));
        mymap.put("classto",getIntent().getStringExtra("clasto"));
        mymap.put("classfrom",getIntent().getStringExtra("clasnum"));
        mymap.put("phoneauth",phoneAuthCredential.toString());
        mymap.put("mobile",getIntent().getStringExtra("number"));
        Map<String,Object> numbr=new HashMap<>();
        numbr.put("name",getIntent().getStringExtra("schoolname"));
        FirebaseDatabase.getInstance().getReference().child("requests").child("numbers").child("used").child(getIntent().getStringExtra("number")).setValue(numbr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("DONE","saved");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR",e.getMessage());
                progressDialog.dismiss();
                Toast.makeText(Otp.this, "ERROR PLEASE CHECK YOUR INTERNET CONNECTION AND TRY AGAIN", Toast.LENGTH_SHORT).show();
            }
        });
        if (getIntent().getStringArrayListExtra("streams")!=null){

            int i=1;
            for (String s:getIntent().getStringArrayListExtra("streams")){
                mystreams.put(String.valueOf(i),s);
                i++;
            }
        }
        FirebaseDatabase.getInstance().getReference().child("requests").child("pending").child("school").child(getIntent().getStringExtra("number"))
                .setValue(mymap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (!mystreams.isEmpty()){

                        FirebaseDatabase.getInstance().getReference().child("requests").child("pending").child("school").child(getIntent().getStringExtra("number"))
                                .child("streams").setValue(mystreams).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d("SUCCESS","suces");
                                    progressDialog.dismiss();
                                    AlertDialog.Builder a=new AlertDialog.Builder(Otp.this);
                                    a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String[] arr=getIntent().getStringExtra("schoolname").toLowerCase().split("\\s+");
                                            FirebaseFirestore.getInstance().collection("requests").document("numbers").collection("used");
                                            SharedPreferences.Editor preferences=  getSharedPreferences("details",MODE_PRIVATE).edit();
                                            if (arr.length>=2){

                                                preferences.putString("txtemail",arr[0]+arr[1]+"@intouch.com");
                                                preferences.putInt("txtpass",getIntent().getStringExtra("schoolname").hashCode());
                                                preferences.apply();
                                                Log.d("DETAILS",getSharedPreferences("details",MODE_PRIVATE).getString("txtemail","")+getSharedPreferences("details",MODE_PRIVATE).getInt("txtpass",0));
                                            }else {
                                                preferences.putString("txtemail",arr[0]+"@intouch.com");
                                                preferences.putInt("txtpass",getIntent().getStringExtra("schoolname").hashCode());
                                                preferences.apply();
                                                Log.d("DETAILS2",getSharedPreferences("details",MODE_PRIVATE).getString("txtemail","")+getSharedPreferences("details",MODE_PRIVATE).getInt("txtpass",0));

                                            }

                                            Intent intent=new Intent(Otp.this,MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    });
                                    a.setTitle("Processing");
                                    a.setMessage("Your request is being processed.Please expect a call from our representative within an hour to guide you through.\nThank you.");
                                    AlertDialog alertDialog=a.create();
                                    alertDialog.show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ERROR",e.getMessage());
                                progressDialog.dismiss();
                                Toast.makeText(Otp.this, "ERROR PLEASE CHECK YOUR INTERNET CONNECTION AND TRY AGAIN", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }else {
                        progressDialog.dismiss();
                        AlertDialog.Builder a=new AlertDialog.Builder(Otp.this);
                        a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] arr=getIntent().getStringExtra("schoolname").toLowerCase().split("\\s+");
                                FirebaseFirestore.getInstance().collection("requests").document("numbers").collection("used");
                                SharedPreferences.Editor preferences=  getSharedPreferences("details",MODE_PRIVATE).edit();
                                if (arr.length>=2){

                                    preferences.putString("txtemail",arr[0]+arr[1]+"@intouch.com");
                                    preferences.putInt("txtpass",getIntent().getStringExtra("schoolname").hashCode());
                                    preferences.apply();
                                    Log.d("DETAILS",getSharedPreferences("details",MODE_PRIVATE).getString("txtemail","")+getSharedPreferences("details",MODE_PRIVATE).getInt("txtpass",0));
                                }else {
                                    preferences.putString("txtemail",arr[0]+"@intouch.com");
                                    preferences.putInt("txtpass",getIntent().getStringExtra("schoolname").hashCode());
                                    preferences.apply();
                                    Log.d("DETAILS2",getSharedPreferences("details",MODE_PRIVATE).getString("txtemail","")+getSharedPreferences("details",MODE_PRIVATE).getInt("txtpass",0));

                                }

                                Intent intent=new Intent(Otp.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        a.setTitle("Processing");
                        a.setMessage("Your request is being processed.Please expect a call from our representative within an hour to guide you through.\nThank you.");
                        AlertDialog alertDialog=a.create();
                        alertDialog.show();
                    }

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(Otp.this, "Account wasnt created.Please try again later"+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
progressDialog.dismiss();
            }
        });

//        String mobile=getIntent().getStringExtra("number");
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+254" + mobile,
//                60,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                        Log.d("OTP",phoneAuthCredential.getSmsCode());
//                    }
//
//                    @Override
//                    public void onVerificationFailed(FirebaseException e) {
//                        Toast.makeText(Otp.this, "ERROR SENDING "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        super.onCodeSent(s, forceResendingToken);
//                        Toast.makeText(Otp.this, "CODE SENT "+s, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCodeAutoRetrievalTimeOut(String s) {
//                        super.onCodeAutoRetrievalTimeOut(s);
//                        Log.d("ERROR RETRIEVE ",s);
//                    }
//                });
    }

    public void check(View view) {
        FirebaseFirestore.getInstance().collection("requests").document("pending").collection("school").document(getIntent().getStringExtra("number"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Log.d("DONE","D");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR",e.getMessage());
            }
        });
    }
}
