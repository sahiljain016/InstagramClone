package com.sahil.StXaviersSocialClub.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.sahil.StXaviersSocialClub.Share.ShareActivity;
import com.sahil.StXaviersSocialClub.Heart.HeartActivity;
import com.sahil.StXaviersSocialClub.Home.HomeActivity;
import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.Search.SearchActivity;
import com.sahil.StXaviersSocialClub.Profile.TimelineActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationViewHelper {
private  static final String TAG = "BottomNavigationView";

public static void enableNavigation(final Context context, BottomNavigationView bottomNavigationView){

    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){

                case R.id.home:
                    Intent intent1 = new Intent(context, HomeActivity.class);
                    context.startActivity(intent1);
                    break;
                case R.id.search:
                    Intent intent2 = new Intent(context, SearchActivity.class);
                    context.startActivity(intent2);
                    break;
                case R.id.add:
                    Intent intent3 = new Intent(context, ShareActivity.class);
                    context.startActivity(intent3);
                    break;
                case R.id.heart:
                    Intent intent4 = new Intent(context, HeartActivity.class);
                    context.startActivity(intent4);
                    break;
                case R.id.timeline:
                    Intent intent5 = new Intent(context, TimelineActivity.class);
                    context.startActivity(intent5);
                    break;

            }

            return true;
        }
    });
}
}
