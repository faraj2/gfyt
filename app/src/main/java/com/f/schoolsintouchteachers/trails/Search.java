package com.f.schoolsintouchteachers.trails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.f.schoolsintouchteachers.Location;
import com.f.schoolsintouchteachers.MainActivity;
import com.f.schoolsintouchteachers.R;
import com.f.schoolsintouchteachers.models.DBHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Search extends AppCompatActivity {

    private AutoCompleteTextView textView;
    int k=10;
    int f=0;
    boolean add=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final ArrayList<String> names=new ArrayList<>();

      //  for (int i=0;i<k;i++)


      textView=findViewById(R.id.autoCompleteTextView);
//        DBHelper dbHelper=new DBHelper(this);
//        dbHelper.insertContact("mimi","rjhnurruuri","LISvrpzXNfZLOerCJyo");
//
//        Cursor cursor=dbHelper.getData("");
//        if (cursor.getCount()>0){
//            cursor.moveToFirst();
//           String selection = cursor.getString(cursor.getColumnIndex(DBHelper.CONTACTS_COLUMN_VIDURL));
//            textView.setText(selection);
//        }
//cursor.close();


//                FirebaseDatabase.getInstance().getReference().child("Schools").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot:
//                    dataSnapshot.getChildren()) {
//                    names.add((snapshot.child("name").getValue(String.class)).toLowerCase());
//                }
//                getSharedPreferences("schoolnames",MODE_PRIVATE).edit().putStringSet("names", new HashSet<String>(names)).apply();
//                dialog.dismiss();
//                names.clear();
//                ArrayList<String> me=new ArrayList<>(getSharedPreferences("schoolnames",MODE_PRIVATE).getStringSet("names",new HashSet<String>()));
//                textView.setAdapter(new ArrayAdapter<String>(Search.this,android.R.layout.simple_list_item_1,me));
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//        textView.setCursorVisible(true);
//        textView.setCompletionHint("registered schools");
//        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Search.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
//                switch (parent.getItemAtPosition(position).toString()){
//                    case "afraha secondary":
//                        textView.setError("school already registered");
//                        break;
//                }
//            }
//        });
//
//

    }

    public void logout(View view) {
      //  FirebaseAuth.getInstance().signOut();
        if (k>=f && add){
            f++;
        }else {
            add=false;
            f--;
            if (f<=0){
                add=true;
            }
        }
    }
    /**
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String g = account.getIdToken();
                String h = account.getEmail();
//                pref.putString("gmail", h);
//                pref.putString("gtoken", g);
//                pref.apply();

                // GoogleSignInAccount h= GoogleSignInAccount.zzeu(g);
                // assert h != null;
                Log.d("H", g + " " + h);
             //   startActivity(new Intent(MainActivity.this, Location.class));
                //   firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
                // ...
            }
        }
    put in oncreate

    pref = getSharedPreferences("googlet", MODE_PRIVATE).edit();
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(getString(R.string.default_web_client_id))
    .requestEmail()
    .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }**/

    /**@param view storing data in json file
     *
     *    File jsons = new File(SDCardRoot,"intouch/json/");
     //                File data = null;
     //                if(!jsons.exists()){
     //                    try {
     //                        jsons.mkdirs();
     //                        jsons.setWritable(true);
     //                        jsons.setReadable(true);
     //                        data=new File(jsons,"mydata.json");
     //                        if (!data.exists()){
     //                            data.createNewFile();
     //                        }
     //
     //
     //                    } catch (IOException e) {
     //                        e.printStackTrace();
     //                    }
     //                }else {
     //                    data=new File(jsons,"mydata.json");
     //                    if (!data.exists()){
     //                        try {
     //                            data.createNewFile();
     //                        } catch (IOException e) {
     //                            e.printStackTrace();
     //                        }
     //                    }
     //                }
     //
     //                JSONObject json = new JSONObject();
     //                try {
     //
     //                    json.put(videoss.getName(),file.getAbsolutePath());
     //
     //
     //                 //   OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.json", Context.MODE_PRIVATE));
     //                    if (new File(context.getFilesDir(),"conf.json").exists()){
     //                        Reader r=new InputStreamReader(context.openFileInput("conf.json"));
     //                        Jsondata jsondata=new Jsondata().load(new JacksonFactory(),r);
     //                        r.close();
     //                        JSONObject jsonObject=new JSONObject(jsondata.getName());
     //                        JSONArray jsonArray=jsonObject.getJSONArray("data");
     //                        jsonArray.put(json);
     //                        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(context.getFilesDir(),"conf.json")));
     //
     //                        writer.write(jsonObject.toString());
     //                        writer.close();
     //
     //                    }else {
     //                        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(context.getFilesDir(),"conf.json")));
     ////                        JSONObject jo = Json.createObjectBuilder()
     ////                                .add("employees", Json.createArrayBuilder()
     ////                                        .add(Json.createObjectBuilder()
     ////                                                .add("firstName", "John")
     ////                                                .add("lastName", "Doe")))
     ////                                .build();
     //
     //                    }
     //
     //
     //                } catch (JSONException e) {
     //                    e.printStackTrace();
     //                } catch (IOException e) {
     //                    e.printStackTrace();
     //                }
     //                try {
     //                    Reader r=new InputStreamReader(context.openFileInput("conf.json"));
     //                    Jsondata jsondata=new Jsondata().load(new JacksonFactory(),r);
     //                  r.close();
     //
     //                    String kj= String.valueOf(jsondata.getName().get(videoss.getName()));
     //                    Log.d("MYKEYIS",kj);
     //                } catch (FileNotFoundException e) {
     //                    e.printStackTrace();
     //                } catch (IOException e) {
     //                    e.printStackTrace();
     //                }
     //                Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();

     //         downloadTask = new Downloadasnyc(context,videoss);
     //        downloadTask.execute();
     *
     */
    public void getdata(View view) {
        FirebaseDatabase.getInstance().getReference().child("requests").child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap :
                        dataSnapshot.getChildren()) {
                   String me= snap.child("prov").getValue(String.class);
                    Toast.makeText(Search.this, me, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
Log.d("DATABASEERROR",databaseError.getMessage());
            }
        });
    }

    public void login(View view) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword("me@me.com","mimi123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   Toast.makeText(Search.this, "loged in success", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(Search.this, "login failed", Toast.LENGTH_SHORT).show();
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Search.this, "onfail "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
