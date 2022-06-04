package com.sahil.StXaviersSocialClub.Share;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.sahil.StXaviersSocialClub.Dialogs.ConfirmPasswordDialog;
import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.utils.Permissions;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";

    //Constants
    private static final int GALLERY_FRAGMENT_NUM = 0;
    private static final int CAMERA_FRAGMENT_NUM = 1;

    private static final int PHOTO_REQUEST_CODE = 5;
    private static final int VIDEO_REQUEST_CODE = 10;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);

        ImageButton openPhoto = view.findViewById(R.id.openPhoto);
        ImageButton openVideo = view.findViewById(R.id.openVideo);


       openPhoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(((ShareActivity) Objects.requireNonNull(getActivity())).getCurrentTabNumber() == CAMERA_FRAGMENT_NUM){
                   if(((ShareActivity) Objects.requireNonNull(getActivity())).checkPermissions(Permissions.CAMERA_PERMISSIONS[0])){
                       Log.d(TAG, "onCreateView: Starting Camera For Photo");
                       Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       startActivityForResult(CameraIntent,PHOTO_REQUEST_CODE);
                   }
                   else{
                       Intent intent = new Intent(getActivity(),ShareActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);

                   }
               }

           }
       });

    openVideo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (((ShareActivity) Objects.requireNonNull(getActivity())).getCurrentTabNumber() == CAMERA_FRAGMENT_NUM) {
                Log.d(TAG, "onClick: tab no" + ((ShareActivity)getActivity()).getCurrentTabNumber());
                if (((ShareActivity) Objects.requireNonNull(getActivity())).checkPermissions(Permissions.CAMERA_PERMISSIONS[0])) {
                    Log.d(TAG, "onCreateView: Starting Camera For Video");
                    Intent Video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    Video.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
                    startActivityForResult(Video, VIDEO_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(getActivity(), ShareActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }

        }
    });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + CAMERA_FRAGMENT_NUM);
        if(requestCode ==  PHOTO_REQUEST_CODE){
            Log.d(TAG, "onActivityResult: done taking photo");
            Log.d(TAG, "onActivityResult: navigating to publish activity");
            //navigate to final publishing activity
        }

       else if(requestCode ==  VIDEO_REQUEST_CODE && resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking photo");
            Log.d(TAG, "onActivityResult: navigating to publish activity");
            //navigate to final publishing activity

            AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
          VideoView videoView = new VideoView(getActivity());
            videoView.setVideoURI(Objects.requireNonNull(data).getData());

            MediaController mc = new MediaController(getActivity());
            mc.setMediaPlayer(videoView);
            videoView.setMediaController(mc);
            videoView.start();
           builder.setView(videoView);
            builder.show();
        }
    }

}
