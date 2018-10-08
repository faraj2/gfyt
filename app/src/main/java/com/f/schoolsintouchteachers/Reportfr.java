package com.f.schoolsintouchteachers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.f.schoolsintouchteachers.adapters.IconTreeItem;
import com.f.schoolsintouchteachers.adapters.Myholdertree;
import com.f.schoolsintouchteachers.interfaces.MySnapshot;
import com.f.schoolsintouchteachers.models.Models;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by admin on 2/6/2018.
 */

public class Reportfr extends Fragment {
    private LinearLayout linearLayout;
    DatabaseReference databaseReference;
    private AndroidTreeView tView;
    private View view;
    private DataSnapshot mydata;
    MyySnapshot mySnapshot;
    private SharedPreferences sharedPreferences;

    public interface MyySnapshot {
        void snapsht(DataSnapshot dataSnapshot);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.report_layout, container, false);

        linearLayout = view.findViewById(R.id.linearreport);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("School");
        treedata(savedInstanceState, inflater, container);
        sharedPreferences=getContext().getSharedPreferences("PATH",Context.MODE_PRIVATE);

        return view;
    }

    private void treedata(final Bundle savedInstanceState, final LayoutInflater inflater, final ViewGroup container) {




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mydata=dataSnapshot;

                final ArrayDeque<TreeNode> first = new ArrayDeque<>();

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ArrayDeque<TreeNode> second = new ArrayDeque<>();
                        first.add(new TreeNode(new IconTreeItem(snapshot.getKey())).setViewHolder(new Myholdertree(getContext())));
                        DataSnapshot snapshot1 = dataSnapshot.child(snapshot.getKey());
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            ArrayList<TreeNode> third = new ArrayList<>();
                            second.add(new TreeNode(new IconTreeItem(snapshot2.getKey())).setViewHolder(new Myholdertree(getContext())));
                            DataSnapshot snapshot3 = snapshot1.child(snapshot2.getKey());
                            if (snapshot3.hasChildren()) {
                                for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                    third.add(new TreeNode(new IconTreeItem(snapshot4.getKey())).setViewHolder(new Myholdertree(getContext())));
                                }
                                second.getLast().addChildren(third);
                            }

                        }
                        first.getLast().addChildren(second);
                    }

                }
                TreeNode root = TreeNode.root();


                root.addChildren(first);

                tView = new AndroidTreeView(getActivity(), root);
                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                tView.setDefaultNodeClickListener(nodeClickListener);

                linearLayout.removeAllViews();


                linearLayout.addView(tView.getView());
                tView.setSelectionModeEnabled(true);
//                if (savedInstanceState != null) {
//                    String state = savedInstanceState.getString("tState");
//                    if (!TextUtils.isEmpty(state)) {
//                        tView.restoreState(state);
//                    }
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR","ERR");
            }
        });

    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItem item = (IconTreeItem) value;
            if(node.getLevel()==3)
            {
                node.setSelectable(false);
              //  Toast.makeText(getContext(), item.mytext, Toast.LENGTH_SHORT).show();
              //  Toast.makeText(getContext(), node.getParent().getValue().toString()+node.getParent().getParent().getValue().toString(), Toast.LENGTH_LONG).show();
              if (mydata!=null){
                  DataSnapshot snapshot=mydata.child(node.getParent().getParent().getValue().toString()).child(node.getParent().getValue().toString()).child(item.mytext);
                  Models models=Models.getInstance();
                  models.setSnapshot(snapshot);
                  SharedPreferences.Editor editor=sharedPreferences.edit();
                  editor.putString("firstpath",node.getParent().getParent().getValue().toString());
                  editor.putString("secondpath",node.getParent().getValue().toString());
                  editor.putString("thirdpath",item.mytext);
                  editor.apply();
                  if (snapshot.hasChildren()){


                      for (DataSnapshot snapshot1:snapshot.getChildren()){
                          Toast.makeText(getContext(), snapshot1.getValue().toString(), Toast.LENGTH_SHORT).show();
                      }
                      startActivity(new Intent(getActivity(),Reports.class));
                  }

               //    mySnapshot.snapsht(snapshot);



              }

//
//                DatabaseReference reference=databaseReference.child(node.getParent().getParent().getValue().toString()).child(node.getParent().getValue().toString()).child(item.mytext);
//
//           reference.addListenerForSingleValueEvent(new ValueEventListener() {
//               @Override
//               public void onDataChange(DataSnapshot dataSnapshot) {
//
//               }
//
//               @Override
//               public void onCancelled(DatabaseError databaseError) {
//
//               }
//           });
            }


        }
    };
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("tState", tView.getSaveState());
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

            mySnapshot= new Reports.PlaceholderFragment();




    }
}
