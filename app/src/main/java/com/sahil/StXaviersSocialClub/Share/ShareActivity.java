package com.sahil.StXaviersSocialClub.Share;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;



import com.google.android.material.tabs.TabLayout;

import com.sahil.StXaviersSocialClub.R;

import com.sahil.StXaviersSocialClub.utils.Permissions;

import com.sahil.StXaviersSocialClub.utils.SectionViewPagerAdapter;

import java.util.Objects;

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "Add Activity";
    private ViewPager mViewPager;
    TabLayout tabLayout;
    //constants
    private static final int Activity_num = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if(CheckPermissionsArray(Permissions.PERMISSIONS)){
            //View Pager For Fragments
            SetUpViewPager();
        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }




    }


public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();

}
    /**
     * verifying array of permissions
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions array");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST_CODE
        );
        
    }

    /**
     * Checking all permissions in the array
     * @param permissions
     * @return
     */
    public Boolean CheckPermissionsArray(String[] permissions){
        Log.d(TAG, "CheckPermissionsArray: checking permissions array");
        for (int i =0 ; i < permissions.length;i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * checking permissions one by one if it has been verified
     * @param permission
     * @return
     */
    public Boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking single permission");

        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this,permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: permission denied by user" + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: permission grated by user" + permission);
            return true;
        }
    }

    private void SetUpViewPager() {
        Log.d(TAG, "ONCREATE STARTED");
        SectionViewPagerAdapter adapter = new SectionViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new GalleryFragment() ); //index 0
        adapter.AddFragment(new CameraFragment()); //index 1

        mViewPager= findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

       tabLayout = findViewById(R.id.tabBottomShare);

        tabLayout.setupWithViewPager(mViewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(getString(R.string.gallery));
        Log.d(TAG, "SetUpViewPager: " + tabLayout.getTabAt(0).getClass());
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(getString(R.string.camera));


    }
   /* //RESPONSIBLE FOR SETTING UP THE BOTTOM NAVIGATION VIEW
    public void setUpBottomNav(){
        Log.d(TAG, "Bottom nav view Share clicked");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationMenu);

        NavigationViewHelper.enableNavigation(ShareActivity.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }*/


}