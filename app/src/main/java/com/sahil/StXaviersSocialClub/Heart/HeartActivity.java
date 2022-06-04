package com.sahil.StXaviersSocialClub.Heart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.utils.NavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HeartActivity extends AppCompatActivity {
private static final String TAG ="Heart Activity";
private static final int Activity_num = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_sign_up);
        // Bottom Navigation View
        //setUpBottomNav();
    }
    //RESPONSIBLE FOR SETTING UP THE BOTTOM NAVIGATION VIEW
    public void setUpBottomNav(){
        Log.d(TAG, "Bottom nav view Heart clicked");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationMenu);

        NavigationViewHelper.enableNavigation(HeartActivity.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }
}