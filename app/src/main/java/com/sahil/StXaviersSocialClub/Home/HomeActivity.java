package com.sahil.StXaviersSocialClub.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sahil.StXaviersSocialClub.LogIn.LogInActivity;
import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.utils.NavigationViewHelper;
import com.sahil.StXaviersSocialClub.utils.SectionViewPagerAdapter;
import com.sahil.StXaviersSocialClub.utils.UniversalImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Home Activity";
    private static final int Activity_num = 0;
    private Context mContext = HomeActivity.this;
    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // RELATED TO UNIVERSAL IMAGE LOADER FIND METHOD BELOW
        initImageLoader();
        //Bottom navigation view
        setUpBottomNav();
        // View pager
        SetUpViewPager();
         // FIREBASE AUTHENTICATION
        setupFirebaseAuth();

       // mAuth.signOut();

    }

    //RESPONSIBLE FOR SETTING UP THE BOTTOM NAVIGATION VIEW
    public void setUpBottomNav() {
        Log.d(TAG, "Bottom nav view clicked");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationMenu);

        NavigationViewHelper.enableNavigation(HomeActivity.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }

    //RESPONSIBLE FOR ADDING THE THREE FRAGMENTS TO THE LIST BY ADAPTER
    private void SetUpViewPager() {
        Log.d(TAG, "ONCREATE STARTED");
        SectionViewPagerAdapter adapter = new SectionViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new NewsWallFragment()); //index 0
        adapter.AddFragment(new HomeFragment()); //index 1
        adapter.AddFragment(new ClassroomBSCITFragment()); //index 2
        ViewPager viewPager =  findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout =  findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("News Wall");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Social Feed");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText("BSC IT ONLY");

    }

    private void initImageLoader() {

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }


     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * checks to see if the @param 'user' is logged in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(mContext, LogInActivity.class);
            startActivity(intent);
        }
    }
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
