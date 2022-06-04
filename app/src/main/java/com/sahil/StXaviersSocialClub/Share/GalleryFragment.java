package com.sahil.StXaviersSocialClub.Share;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sahil.StXaviersSocialClub.R;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);
       // Log.d(TAG, "onCreateView: Gallery Fragment");
        int currenttab = ((ShareActivity)getActivity()).getCurrentTabNumber();
        Log.d(TAG, "onCreateView: GAllllerryy Fragment" + currenttab );
        return view;
    }
}
