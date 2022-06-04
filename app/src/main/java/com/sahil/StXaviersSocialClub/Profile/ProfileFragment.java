package com.sahil.StXaviersSocialClub.Profile;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.sahil.StXaviersSocialClub.Models.AllUserSettings;
import com.sahil.StXaviersSocialClub.Models.UserAccountSettings;
import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.utils.FirebaseMethods;
import com.sahil.StXaviersSocialClub.utils.NavigationViewHelper;
import com.sahil.StXaviersSocialClub.utils.SectionStateViewPagerAdapter;
import com.sahil.StXaviersSocialClub.utils.UniversalImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int Activity_num = 4;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private ViewPager viewPager;
    private SectionStateViewPagerAdapter pagerAdapter;

    private TextView mPosts, mFollowers , mFollowing, mDescription, mWebsite, mUsername,mDisplayName;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ImageButton settingsMenu;

    private Button editProfilePopUpButton;
    private Button ReportBugPopUpButton;
    private Button LogOutPopUpButton;
    private Button ClosePopUpButton;
    private Button editProfileButton;

    private RelativeLayout relativeLayout;
    private RelativeLayout relativeLayoutProfile;

    private Context mContext;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d(TAG, "onCreateView: started");

        mPosts = (TextView) view.findViewById(R.id.Posts);
        mFollowers = (TextView) view.findViewById(R.id.followers);
        mFollowing = (TextView) view.findViewById(R.id.following);
        mDescription = (TextView) view.findViewById(R.id.profile_description);
        mWebsite = (TextView) view.findViewById(R.id.profile_webiste_link);
        mUsername = (TextView) view.findViewById(R.id.username_profile);
        mDisplayName = (TextView) view.findViewById(R.id.profile_name);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_profile);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        gridView = (GridView) view.findViewById(R.id.grid_view);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.BottomNavigationMenu);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolbar);
        settingsMenu = (ImageButton) view.findViewById(R.id.menuProfileIcon);
        viewPager = (ViewPager) view.findViewById(R.id.container);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.popupRelLayout);
        relativeLayoutProfile = (RelativeLayout) view.findViewById(R.id.relLayout_dummy) ;

        editProfilePopUpButton = (Button) view.findViewById(R.id.EditProfileButtonPopup1);
        ReportBugPopUpButton = (Button) view.findViewById(R.id.ReportBugButtonPopup2);
        LogOutPopUpButton = (Button) view.findViewById(R.id.logOutButtonPopup1);
        ClosePopUpButton = (Button) view.findViewById(R.id.closePopUpButton);
        editProfileButton = (Button) view.findViewById(R.id.EditProfileButton);

        TimelineActivity.Fragment_one=true;

        mContext = getActivity();
        mFirebaseMethods = new FirebaseMethods(getActivity());

        relativeLayout.setY(2000);
        relativeLayout.setVisibility(View.VISIBLE);

        setUpProfileToolbar();
        setUpFragments();
        setUpBottomNav();
        setupFirebaseAuth();

        EditProfilePopUpButton();
        ReportBugPopUp();
        LogOutPopUp();
        ClosePopUp();
        EditProfileButton();

        return view;
    }

    /**
     * responsible for settings all the values from to the widgets
     * @param allUserSettings
     */
    private void setProfileWidgets(AllUserSettings allUserSettings){

        //User user = allUserSettings.getUser();
        UserAccountSettings settings  = allUserSettings.getUserAccountSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(),mProfilePhoto,null,"");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mFollowers.setText(String.valueOf(settings.getFriendly_xavierites()));
        mPosts.setText(String.valueOf(settings.getPosts()));
        mProgressBar.setVisibility(View.GONE);

    }


    /**
     *    RESPONSIBLE FOR PROFILE TOP TOOLBAR
     */
    public void setUpProfileToolbar(){

        ((TimelineActivity)getActivity()).setSupportActionBar(toolbar);

        settingsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.animate().translationYBy(-1300).setDuration(1000);
                settingsMenu.setEnabled(false);
                editProfileButton.setEnabled(false);
            }
        });

/**
 * RESPONSIBLE FOR SETTING UP VIEW PAGER
 */
    }
    public void setViewPager(int FragmentNumber){
        relativeLayoutProfile.setVisibility(View.GONE);
        Log.d(TAG,"setViewpager succesfull");

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(FragmentNumber);
    }

    /**
     * RESPONSIBLE FOR ADDING ALL EXTRA SETTINGS FRAGMENT
     */
    public void setUpFragments(){
        pagerAdapter = new SectionStateViewPagerAdapter(((TimelineActivity)getActivity()).getSupportFragmentManager());
        pagerAdapter.AddFragments(new EditProfileFragment(), getString(R.string.edit_profile_fragment)); //Fragment0
        pagerAdapter.AddFragments(new LogOutFragment(), getString(R.string.log_out_fragment)); //Fragment 1
        pagerAdapter.AddFragments(new ReportBugFragment(), getString(R.string.report_bug_fragment)); //Fragment 2
    }
    /**
     * RESPONSIBLE FOR SETTING UP THE BOTTOM NAVIGATION VIEW
     */
    public void setUpBottomNav(){
        Log.d(TAG, "Bottom nav view Profile clicked");
        NavigationViewHelper.enableNavigation(getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }

    /**
     *  RESPONSIBLE FOR SHOWING EDIT PROFILE FRAGMENT
     */
    public void EditProfilePopUpButton(){

        editProfilePopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"KHULAAAAA EDIT PRFILE");
                setViewPager(0);
            }
        });


    }
    /**
     *    RESPONSIBLE FOR SHOWING lOG OUT FRAGMENT
     */
    public void LogOutPopUp(){

        LogOutPopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"KHULAAAAA LOG OUT");
                setViewPager(1);
            }
        });


    }
    /**
     *  RESPONSIBLE FOR SHOWING REPORT BUG FRAGMENT
     */
    public void ReportBugPopUp(){

        ReportBugPopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"KHULAAAAA REPORT BUG");
                setViewPager(2);
            }
        });
    }
    /**
     *  //RESPONSIBLE FOR CLOSING POPUP MENU
     */
    public void ClosePopUp(){

        ClosePopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Popup Menu Closed");
                relativeLayout.animate().translationYBy(1300).setDuration(1000);
                settingsMenu.setEnabled(true);
                editProfileButton.setEnabled(true);
            }
        });
    }
    /**
     *  RESPONSIBLE FOR SHOWING EDIT PROFILE FRAGMENT FROM PROFILE Page
     */
    public void EditProfileButton(){

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"KHULAAAAA EDIT PRFILE");
                setViewPager(0);
            }
        });


    }
/*
    ------------------------------------ Firebase ---------------------------------------------
     */


    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabse.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //RETRIEVE USER INFORMATION FROM DATABASE
                setProfileWidgets(mFirebaseMethods.getUserSettings(snapshot));

                //RETRIEVE USER PHOTOS FROM DATABASE
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
