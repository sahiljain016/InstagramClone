package com.sahil.StXaviersSocialClub.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sahil.StXaviersSocialClub.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class GridImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mInflator;
    private int LayoutResource;
    private String mAppend;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(Context context, int LayoutResource,String append,ArrayList<String> imgURLs){
        super(context,LayoutResource,imgURLs);
        mContext = context;
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.LayoutResource = LayoutResource;
        mAppend = append;
        this.imgURLs = imgURLs;

    }

    /**
     *
     */
    private static class ViewHolder {
        SquareImageView GridImage;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
// VIEW HOLDER BUILD PATTERN (SIMILAR TO RECYCLER VIEW BUT EASY TO SETUP)
        final ViewHolder holder;

        if(convertView == null){
convertView = mInflator.inflate(LayoutResource,parent,false);
holder = new ViewHolder();
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.grid_progressBar);
            holder.GridImage = (SquareImageView) convertView.findViewById(R.id.grid_imageView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        String imgURL = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();
         imageLoader.displayImage(mAppend + imgURL, holder.GridImage, new ImageLoadingListener() {
             @Override
             public void onLoadingStarted(String imageUri, View view) {
                 if(holder.mProgressBar != null){
                     holder.mProgressBar.setVisibility(View.VISIBLE);

                 }
             }

             @Override
             public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                 if(holder.mProgressBar != null){
                     holder.mProgressBar.setVisibility(View.GONE);

                 }
             }

             @Override
             public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                 if(holder.mProgressBar != null){
                     holder.mProgressBar.setVisibility(View.GONE);

                 }
             }

             @Override
             public void onLoadingCancelled(String imageUri, View view) {
                 if(holder.mProgressBar != null){
                     holder.mProgressBar.setVisibility(View.GONE);

                 }
             }
         });

        return convertView;
    }
}
