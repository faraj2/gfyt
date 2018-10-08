package com.f.schoolsintouchteachers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Location extends AppCompatActivity {

    private Spinner spinner;
    private int newhit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        RelativeLayout relativeLayout = findViewById(R.id.reltvlocation);
        relativeLayout.addView(new BlueView(this));
        ArrayList<String> mylist = new ArrayList<>();
        mylist.add("Coast");
        mylist.add("Nairobi");
        mylist.add("Central");
        mylist.add("Nyanza");
        mylist.add("Rift Valley");
        mylist.add("Eastern");
        mylist.add("North Eastern");
        mylist.add("Western");

        spinner = findViewById(R.id.splocation);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mylist));
    }

    public void nextloctn(View view) {
        final ProgressDialog progressDialog=new ProgressDialog(Location.this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (spinner.getSelectedItem().equals("Coast")) {
            progressDialog.dismiss();
            Toast.makeText(this, " coast", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Location.this, Main3Activity.class);
            startActivity(i);
        } else {
//            FirebaseFirestore.getInstance().collection("requests").addSnapshotListener(Location.this, new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                    Log.d("Done", String.valueOf(documentSnapshots.size()));
//                }
//            });
            Map<String,Object> map=new HashMap<>();
            map.put("prov",spinner.getSelectedItem().toString());
                FirebaseDatabase.getInstance().getReference().child("requests").child("location").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                   public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(Location.this,"Our services are currently available only in COAST." +spinner.getSelectedItem().toString()+" will be coming soon.", Toast.LENGTH_LONG).show();
                     //   Log.d("TASK",task.getResult().toString());
                    }
                });


        }
    }
}
