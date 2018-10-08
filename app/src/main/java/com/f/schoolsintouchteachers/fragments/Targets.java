package com.f.schoolsintouchteachers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.f.schoolsintouchteachers.BlueView;
import com.f.schoolsintouchteachers.R;
import com.f.schoolsintouchteachers.Reports;
import com.f.schoolsintouchteachers.adapters.TargetsAdapter;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by admin on 3/15/2018.
 */

public class Targets extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        Reports.fab.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_targets, container, false);

        Reports.fab.setVisibility(View.GONE);
        RelativeLayout relativeLayout=rootView.findViewById(R.id.targetback);
        relativeLayout.addView(new BlueView(getContext()));
        WaveLoadingView waveLoadingView=rootView.findViewById(R.id.waveLoadingView);
        waveLoadingView.setAnimDuration(5000);

        RecyclerView recyclerView=rootView.findViewById(R.id.recytargets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TargetsAdapter(getContext()));
        return rootView;
    }
}
