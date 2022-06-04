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

public class NewsWallFragment extends Fragment {
    private static final String TAG = "NewsWallFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_wall,container,false);
        Log.d(TAG, "onCreateView: news");
        return view;
    }
}
