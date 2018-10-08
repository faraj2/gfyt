package com.f.schoolsintouchteachers.trails;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.f.schoolsintouchteachers.R;
import com.f.schoolsintouchteachers.firebase.Config;
import com.f.schoolsintouchteachers.models.realmObjects.Pst;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class RealmActivity extends AppCompatActivity {

    private Realm realm;
    private ArrayList<Pst> list;
  RecyclerView recyclerView;
    private ArrayList<String> ids;
    int k = 0;
    Myadapter myadapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);
        ids=new ArrayList<>();
        refreshLayout=findViewById(R.id.refresh);
       recyclerView=findViewById(R.id.recyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
     myadapter=new Myadapter(ids,RealmActivity.this);
       recyclerView.setAdapter(myadapter);
geting();
refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        if (getSharedPreferences("id", MODE_PRIVATE).getString("deletes", "").isEmpty()) {
            if (ids!=null){
                Config.firestore.document("post").collection("deltes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        for (int i = 0; i < documentSnapshots.getDocuments().size(); i++) {
                            Log.d("REMOVED", documentSnapshots.getDocuments().get(i).getId());

                            ids.remove(documentSnapshots.getDocuments().get(i).getId());
                            Toast.makeText(RealmActivity.this, "DONE", Toast.LENGTH_SHORT).show();

                            if (i == (documentSnapshots.getDocuments().size() - 1)) {
                                getSharedPreferences("id", MODE_PRIVATE).edit().putString("deletes", documentSnapshots.getDocuments().get(i).getString("id")).apply();
                            }


                        }
myadapter.notifyDataSetChanged();
geting();

                    }
                });
            }
        }else

        {
            Query query = Config.firestore.document("post").collection("deltes").orderBy("id").startAfter(getSharedPreferences("id", MODE_PRIVATE).getString("deletes", ""));
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    for (int i = 0; i < documentSnapshots.getDocuments().size(); i++) {
                        Log.d("REMOVED", documentSnapshots.getDocuments().get(i).getId());
                        ids.remove(documentSnapshots.getDocuments().get(i).getId());
                        Toast.makeText(RealmActivity.this, "DONE", Toast.LENGTH_SHORT).show();
                        if (i == (documentSnapshots.getDocuments().size() - 1)) {
                            getSharedPreferences("id", MODE_PRIVATE).edit().putString("deletes", documentSnapshots.getDocuments().get(i).getString("id")).apply();
                        }

                    }
myadapter.notifyDataSetChanged();
geting();

                }
            });
        }
    }
});




    }

    public void getfromrealm(View view) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", "mimi");
        DateTime dateTime = new DateTime();
        DateTime msa = dateTime.withZone(DateTimeZone.forID("Africa/Nairobi"));
        //  String id=UUID.randomUUID().toString();
        map.put("id", msa.toString());
        Config.firestore.document("post").collection("school").document(msa.toString()).set(map);
        Log.d("IDS", msa.toString());


    }

    public void geting() {
      //  ids = new ArrayList<>();
        if (getSharedPreferences("id", MODE_PRIVATE).getString("ids", "").isEmpty()) {
            Config.firestore.document("post").collection("school").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    int last = documentSnapshots.getDocuments().size();
                    for (int i = 0; i < documentSnapshots.getDocuments().size(); i++) {
                        ids.add(documentSnapshots.getDocuments().get(i).getId());
                      //  tv.append(documentSnapshots.getDocuments().get(i).getString("name"));
                        if (i == (last - 1)) {
                        //    tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
                            SharedPreferences.Editor editor = getSharedPreferences("id", MODE_PRIVATE).edit();
                            editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
                            editor.apply();

                        }
                    }
                    myadapter.notifyDataSetChanged();
if (refreshLayout.isRefreshing()){
    refreshLayout.setRefreshing(false);
}
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Query query = Config.firestore.document("post").collection("school").orderBy("id").startAfter(getSharedPreferences("id", MODE_PRIVATE).getString("ids", ""));
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    int last = documentSnapshots.getDocuments().size();
                    for (int i = 0; i < documentSnapshots.getDocuments().size(); i++) {
                        ids.add(documentSnapshots.getDocuments().get(i).getId());
                        //  tv.append(documentSnapshots.getDocuments().get(i).getString("name"));
                        if (i == (last - 1)) {
                            //     tv.append(documentSnapshots.getDocuments().get(i).getString("id"));
                            SharedPreferences.Editor editor = getSharedPreferences("id", MODE_PRIVATE).edit();
                            editor.putString("ids", documentSnapshots.getDocuments().get(i).getId());
                            editor.apply();

                        }
                    }
                    myadapter.notifyDataSetChanged();
                    if (refreshLayout.isRefreshing()){
                        refreshLayout.setRefreshing(false);
                    }

                }
            });
        }
    }

public class Myadapter extends RecyclerView.Adapter<Myvieholder>{
       private ArrayList<String> ids;
       Context context;

    public Myadapter(ArrayList<String> ids,Context context) {
        this.ids = ids;
        this.context=context;
    }

    @NonNull
    @Override
    public Myvieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.trail,parent,false);
        return new Myvieholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myvieholder holder, final int position) {
holder.textView.setText(ids.get(position));
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final Map<String, Object> map = new HashMap<>();
        //  tv.setText("");



        DateTime dateTime = new DateTime();
        final DateTime msa = dateTime.withZone(DateTimeZone.forID("Africa/Nairobi"));
        //  String id=UUID.randomUUID().toString();
        map.put("id", msa.toString());
        Config.firestore.document("post").collection("school").document(ids.get(position)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Config.firestore.document("post").collection("deltes").document(ids.get(position)).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {



                    }
                });
            }
        });
    }
});
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }
}

    private class Myvieholder extends RecyclerView.ViewHolder {
        TextView textView;
        public Myvieholder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView7);

        }
    }
}
// realm = Realm.getDefaultInstance();
//        if (realm.isEmpty()) {
//
//            list = new ArrayList<>();
//            FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (final DataSnapshot s : dataSnapshot.getChildren()
//                            ) {
//                        if (s.child("images").exists()) {
//                            final RealmList<String> imag = new RealmList<>();
//                            for (DataSnapshot d : s.child("images").getChildren()
//                                    ) {
//                                imag.add(d.getValue(String.class));
//
//                            }
//                            realm.beginTransaction();
//                            Post1 imag1 = s.getValue(Post1.class);
//                            String time = UUID.randomUUID().toString();
//                            assert imag != null;
//                            Pst pst = new Pst(imag, imag1, time);
//                            realm.copyToRealm(pst);
//                            realm.commitTransaction();
////                         realm.executeTransactionAsync(new Realm.Transaction() {
////                             @Override
////                             public void execute(Realm realm) {
////                                 Pst pst=realm.createObject(Pst.class);
////                                 Post1 imag1=realm.createObject(Post1.class);
////                                 imag1.setPost1(s.getValue(Post1.class));
////                                 pst.setPost1(imag1);
////                                 pst.setImages(imag);
////                             }
////                         });
//
//                            //   list.add(new Pst(imag,s.getValue(Post1.class)));
//                        } else {
//                            realm.beginTransaction();
//                            Post1 imag = s.getValue(Post1.class);
//                            String time = UUID.randomUUID().toString();
//                            assert imag != null;
//                            Pst pst = new Pst(imag, time);
//                            realm.copyToRealm(pst);
//                            realm.commitTransaction();
////                         realm.executeTransactionAsync(new Realm.Transaction() {
////                             @Override
////                             public void execute(Realm realm1) {
////
//////                                 Post1 pst=realm.createObject(Post1.class);
//////                                 pst=s.getValue(Post1.class);
////
////                               //  pst.setPost1(s.getValue(Post1.class));
////                             }
////                         });
//
//                            //  list.add(s.getValue(Pst.class));
//                        }
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }