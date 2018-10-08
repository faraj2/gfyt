package com.f.schoolsintouchteachers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private EditText mEditText, passcode;
    private ProgressBar pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout m = findViewById(R.id.emiter_top_left);

//        MyViews myvi=new MyViews(this);

        m.addView(new BlueView(this));
         pw =  findViewById(R.id.pw_spinner);
         pw.setVisibility(View.INVISIBLE);
        CardView cardView = findViewById(R.id.cardview);
        TextView textViewe = findViewById(R.id.txtemail);
        TextView textViewp = findViewById(R.id.txtpasswrd);
        TextView textViewex = findViewById(R.id.txtexplantion);
        if (getSharedPreferences("details", MODE_PRIVATE).contains("txtemail")) {
            cardView.setVisibility(View.GONE);
            textViewe.setText("username: " + getSharedPreferences("details", MODE_PRIVATE).getString("txtemail", ""));
            textViewp.setText("password: " + Math.abs(getSharedPreferences("details", MODE_PRIVATE).getInt("txtpass", 0)));
            textViewex.setText("\n**The above are your login details,you will be able to log in when your signup request has been processed.Please dont" +
                    " forget to change your password once you log in successfully.");
        } else {
            cardView.setVisibility(View.GONE);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                    UserProfileChangeRequest.Builder creator = new UserProfileChangeRequest.Builder().setDisplayName("Adanans school");


                    firebaseAuth.getCurrentUser().updateProfile(creator.build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("COMPLETE", "donee");
                            if (task.isSuccessful()){
                                FirebaseMessaging.getInstance().subscribeToTopic(firebaseAuth.getCurrentUser().getDisplayName().replace(" ","").toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d("SUCCESS","done");
                                        }else{
                                            Log.d("ERROR",task.getResult().toString());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                                startActivity(new Intent(MainActivity.this, Main4Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            }


                        }
                    });

                }
            }
        };

        mEditText = findViewById(R.id.etEmail);
        passcode = findViewById(R.id.etPassword);

//        mEditText.addTextChangedListener(new TextWatcher(){
//            public void afterTextChanged(Editable s) {}
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//               // mEditText.setText(" ");
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //do stuff
//                String mStr = mEditText.getText().toString();
//                if(mStr.startsWith(" ")){
//                    mEditText.setText(" " + mStr);
//                }
//            }
//        });
    }

    public void login(View view) {
        pw.setVisibility(View.VISIBLE);
findViewById(R.id.btLogin).setAlpha(0.2f);

     //  progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        progressDialog.setMessage("Please wait..");

        String email = mEditText.getText().toString();
        String password = passcode.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

//                    if (task.isSuccessful()) {
////pw.stopSpinning();
////pw.setVisibility(View.GONE);
//                        pw.setVisibility(View.GONE);
//                        startActivity(new Intent(MainActivity.this, Main4Activity.class));
//                        finish();
//                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                    } else {
////                        pw.stopSpinning();
////                        pw.setVisibility(View.GONE);
//                        pw.setVisibility(View.GONE);
//                        findViewById(R.id.btLogin).setAlpha(1f);
//                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                }
            });
        } else {

            pw.setVisibility(View.GONE);
            findViewById(R.id.btLogin).setAlpha(1f);
           if (email.isEmpty()){
               mEditText.setError("");
           }else if (password.isEmpty()){
               passcode.setError("");
           }
        }


    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        finish();
//        System.exit(0);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void signup(View view) {
        startActivity(new Intent(MainActivity.this, Location.class));

//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RGN_IN);

    }
}
