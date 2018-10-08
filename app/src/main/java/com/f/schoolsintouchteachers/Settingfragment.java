package com.f.schoolsintouchteachers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by admin on 2/6/2018.
 */

public class Settingfragment extends Fragment implements View.OnClickListener {
    ExpandableLayout expandableLayout, expandableLayout1,expandableLayout2;
    private TextView expand, expand1, newuser;
    private Button logout, savedata,create;
    private EditText usernme,passwrd,cnfrmpasswrd;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String emal;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout, container, false);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    emal=firebaseAuth.getCurrentUser().getEmail();
                    Log.d("EMAIL",emal);


                }else {
                    startActivity(new Intent(getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    //getActivity().overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                    getActivity().finish();
                }
            }
        };
        usernme=view.findViewById(R.id.username);
        passwrd=view.findViewById(R.id.password);
        cnfrmpasswrd=view.findViewById(R.id.cnfrmpass);

        expandableLayout = view.findViewById(R.id.expandable_layout_0);
        expandableLayout2 = view.findViewById(R.id.expandable_layout_1);
        expand = view.findViewById(R.id.expand_button_0);
        logout = view.findViewById(R.id.button2);
        create = view.findViewById(R.id.create);
        savedata = view.findViewById(R.id.savepass);
        expand1 = view.findViewById(R.id.expand_button_2);
        expandableLayout1 = view.findViewById(R.id.expandable_layout_2);
        newuser = view.findViewById(R.id.expand_button_1);
        logout.setOnClickListener(this);
        savedata.setOnClickListener(this);
        expand1.setOnClickListener(this);
        newuser.setOnClickListener(this);
        expand.setOnClickListener(this);
        create.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                if (auth.getCurrentUser() != null) {
                    auth.signOut();
//                    startActivity(new Intent(getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                    //getActivity().overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
//                    getActivity().finish();
                }

                break;
            case R.id.create:
                startActivity(new Intent(getActivity(), NewUsers.class));
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.savepass:
                Toast.makeText(getContext(), "Saved from onclick", Toast.LENGTH_SHORT).show();
                break;
            case R.id.expand_button_1:
                if (expandableLayout2.isExpanded()) {
                    expandableLayout2.collapse(true);
                    newuser.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    expandableLayout2.expand(true);
                    newuser.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);

                }

                break;
            case R.id.expand_button_0:
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse(true);
                    expand.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    usernme.setText(emal);
                    usernme.setEnabled(false);
                    expandableLayout.expand(true);
                    expand.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);

                }
                break;
            case R.id.expand_button_2:
                if (expandableLayout1.isExpanded()) {
                    expandableLayout1.collapse(true);
                    expand1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                } else {
                    expandableLayout1.expand(true);
                    expand1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);

                }
                break;

            default:
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
