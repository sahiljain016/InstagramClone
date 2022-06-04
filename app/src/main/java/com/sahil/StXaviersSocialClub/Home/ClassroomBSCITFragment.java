package com.sahil.StXaviersSocialClub.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sahil.StXaviersSocialClub.R;

public class ClassroomBSCITFragment extends Fragment {
    private static final String TAG = "ClassroomBSCITFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classroom_bsc_it,container,false);
        Log.d(TAG, "onCreateView: claassBSCIT");
        return view;
    }
}
