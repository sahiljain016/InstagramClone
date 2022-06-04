package com.sahil.StXaviersSocialClub.Search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.utils.NavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {
private static final String TAG = "Search Activity";
    private static final int Activity_num = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_login);
        //Bottom Navigation View
       // setUpBottomNav();

    }
//RESPONSIBLE FOR SETTING UP BOTTOM NAVIGATION VIEW
    public void setUpBottomNav(){
        Log.d(TAG, "Bottom nav view clicked");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationMenu);

        NavigationViewHelper.enableNavigation(SearchActivity.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }
}