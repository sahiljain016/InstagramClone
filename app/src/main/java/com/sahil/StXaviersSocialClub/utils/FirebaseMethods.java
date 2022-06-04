package com.sahil.StXaviersSocialClub.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.sahil.StXaviersSocialClub.Models.AllUserSettings;
import com.sahil.StXaviersSocialClub.Models.User;
import com.sahil.StXaviersSocialClub.Models.UserAccountSettings;
import com.sahil.StXaviersSocialClub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods {

    private static final  String TAG  = "FirebaseMethods";

    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context){
        mAuth =FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();

}
    }
    /*public boolean CheckIfUsernameAlreadyExists(String username, DataSnapshot dataSnapshot){
        Log.d(TAG, "Checking to see if "+ username + " already exists in the database!");

        User user = new User();

        for(DataSnapshot ds:dataSnapshot.child(userID).getChildren()){
Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);

user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUsernameExists: username: " + user.getUsername());

            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkIfUsernameExists: FOUND & MATCH: " + user.getUsername());
                return true;
            }
        }
        return false;
        }*/

    public void registerNewUser(String email, String username,String password, String confirmPassword, long UID, long PhoneNumber,String FullName){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                           userID = mAuth.getCurrentUser().getUid();
                            Toast toast = Toast.makeText(mContext, R.string.auth_success_singup,
                                    Toast.LENGTH_LONG);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);

                            v.setTextSize(20);
                            toast.show();
                            // SENDING VERIFICATION EMAIL
                            sendVerificationEmail();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast toast = Toast.makeText(mContext, R.string.auth_failed_signup,
                                    Toast.LENGTH_LONG);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            v.setTextColor(Color.RED);
                            v.setTextSize(20);
                            toast.show();

                        }

                        // ...
                    }
                });


    }

    public void sendVerificationEmail(){
FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

if(user != null){

    user.sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast toast = Toast.makeText(mContext,"Please check your email for confirmation link.",
                                Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);

                        v.setTextSize(20);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(mContext,"Could'nt send Verification Email. Try again",
                                Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.RED);
                        v.setTextSize(20);
                        toast.show();
                    }


                }
            });
}

    }


    /**
     * Add details to users node in database
     * Add details to user_account_settings node in database
     * @param display_name
     * @param college_uid
     * @param phone_number
     * @param email
     * @param username
     * @param gender
     * @param course
     * @param website
     * @param description
     * @param profile_photo
     */
    public void AddNewUser(String display_name,long college_uid,long phone_number,String email,String username,
                            String gender,String course,String website,String description, String profile_photo){

        User user = new User(college_uid,course,email,gender,phone_number,userID,StringManipulation.comdenseUsername(username));

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings userAccountSettings = new UserAccountSettings(
                description,
                display_name,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.comdenseUsername(username),
                website

        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(userAccountSettings);
    }

    /**
     * Retrieves user account settings from firebase of the user currently logged in
     * Firebase: user_account_settings node
     * @param dataSnapshot
     * @return
     */
    public AllUserSettings getUserSettings(DataSnapshot dataSnapshot){
    Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase database.");

    UserAccountSettings settings  = new UserAccountSettings();
    User user = new User();

    for(DataSnapshot ds: dataSnapshot.getChildren()){
        // For user_account_settings Node
        if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))){
            Log.d(TAG, "getUserAccountSettings: datasnapshot" + ds);

            try {
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFriendly_xavierites(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFriendly_xavierites()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                Log.d(TAG, "getUserAccountSettings: user_account_settings information retrieved Success!" + settings.toString());
            }catch (NullPointerException e) {
                Log.d(TAG, "getUserAccountSettings: NullPointerException " + e.getMessage());
            }
            }
        // For user_account_settings Node
        if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
            Log.d(TAG, "getUserAccountSettings: datasnapshot" + ds);
            try {
                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setCollege_uid(
                        ds.child(userID)
                                .getValue(User.class)
                                .getCollege_uid()
                );
                user.setCourse(
                        ds.child(userID)
                                .getValue(User.class)
                                .getCourse()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setGender(
                        ds.child(userID)
                                .getValue(User.class)
                                .getGender()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );
                Log.d(TAG, "getUserAccountSettings: users information retrieved Success!" + user.toString());
            }catch (NullPointerException e){
                Log.d(TAG, "getUserAccountSettings: NullPointerException " + e.getMessage());
            }
        }
        }
return new AllUserSettings(user,settings);
    }
    public void UpdateUserAccountSettings(String Display_name, String website, String description,String course, String gender ){
        if(Display_name != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(Display_name);
        }
        if(website != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }
        if(description != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }
        if(course != null) {
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_course))
                    .setValue(course);
        }
        if(gender != null) {
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_gender))
                    .setValue(gender);
        }

    }

    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername: updating username to : " + username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }

    /**
     * updating the user's email to the database
     * @param email
     */
    public void updateEmail(String email) {
        Log.d(TAG, "updateEmail: updating email to : " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);
    }
    /**
     * updating the user's phone number to the database
     * @param phoneNumber
     */
    public void updatePhoneNumber(long phoneNumber) {
        Log.d(TAG, "updateEmail: updating email to : " + phoneNumber);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_phoneNumber))
                .setValue(phoneNumber);
    }
    /**
     * updating the user's phone number to the database
     * @param collegeUID
     */
    public void updateCollegeUID(long collegeUID) {
        Log.d(TAG, "updateEmail: updating email to : " + collegeUID);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_collegeUID))
                .setValue(collegeUID);
    }

}

