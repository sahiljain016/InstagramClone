package com.sahil.StXaviersSocialClub.SignUp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sahil.StXaviersSocialClub.LogIn.LogInActivity;
import com.sahil.StXaviersSocialClub.Models.User;
import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
private static final String TAG ="SignUp Activity";

    private Context mContext;
    private EditText mFullName, mEmail, mPassword, mConfirmPassword, mUsername, mPhoneNumber, mUID;
    private String FullName, Email, Password, ConfirmPassword, Username;
    //String check = "";
    Boolean checkIfEmpty;
    private long PhoneNumber,UID;
    private TextView loadingSignUpTV, LoginLinkOnSignUp;
    private ProgressBar mProgressBar;
    private Button btn_signup;

    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;

    private String append = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_sign_up);
        mContext = SignUpActivity.this;
        //INITIALISING ALL WIDGETS
        initWidgets();
        // FIREBASE
        setupFirebaseAuth();
        firebaseMethods = new FirebaseMethods(mContext);
        //Initialising Authentication
        init();

        PasswordFocusGained();
        ConfirmPasswordFocusGained();
        /*ChangeEmailETColour();
        ChangeFullNameColour();
        ChangeUserNameColour();*/
}
/*
INITIALISE ALL MY WIDGETS HERE
 */
    private void initWidgets(){
        mProgressBar = (ProgressBar) findViewById(R.id.SignUpButtonProgressBar);
        mEmail = (EditText) findViewById(R.id.SignUpEmail);
        mPassword = (EditText) findViewById(R.id.SignUpPassword);
        mConfirmPassword = (EditText) findViewById(R.id.SignUpCPassword);
        loadingSignUpTV = (TextView)  findViewById(R.id.LoadingSignUpText);
        btn_signup = (Button) findViewById(R.id.btn_signUp);
        mFullName = (EditText) findViewById(R.id.SignUpFullName);
        mUID = (EditText) findViewById(R.id.SignUpUID);
        mPhoneNumber= (EditText) findViewById(R.id.SignUpPhoneNumber);
        mContext = SignUpActivity.this;
        mUsername = (EditText) findViewById(R.id.SignUpUsername);

        LoginLinkOnSignUp = (TextView) findViewById(R.id.linkLogInOnSignUp);
        mProgressBar.setVisibility(View.GONE);
        loadingSignUpTV.setVisibility(View.GONE);
    }
    /*
    IF PASSWORD GAINS FOCUS AFTER EMPTY STRING
     */
    private void PasswordFocusGained(){
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mPassword.hasFocus()) {
                   // mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPassword.setText("");
                    //mPassword.setTextColor(Color.BLACK);
                }
            }
        });

    }
    /*
    IF Confirm PASSWORD GAINS FOCUS AFTER EMPTY STRING
     */
    private void ConfirmPasswordFocusGained(){
        mConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mConfirmPassword.hasFocus()) {
                    mConfirmPassword.setText("");
                }
            }
        });


    }
    /*
   IF Email ET HAS FOCUS CHANGE COLOUR TO BLACK
    */
    /*private void ChangeEmailETColour(){
        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mEmail.hasFocus()) {
                    if(mEmail.getText().toString().equals("Email Required")){
                        mEmail.setText("");
                    }
                    mEmail.setTextColor(Color.BLACK);
                }
            }
        });

    }*/
    /*
   IF FULL NAME ET HAS FOCUS CHANGE COLOUR TO BLACK
   */
   /* private void ChangeFullNameColour(){
        mFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mFullName.hasFocus()) {
                    if(mFullName.getText().toString().equals("Full Name Required")){
                        mFullName.setText("");
                    }
                    mFullName.setTextColor(Color.BLACK);
                }
            }
        });

    }*/
    /*
 IF USERNAME ET HAS FOCUS CHANGE COLOUR TO BLACK
 */
    /*private void ChangeUserNameColour(){
        mUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mUsername.hasFocus()) {
                    if(mUsername.getText().toString().equals("Username Required")){
                        mUsername.setText("");
                    }
                    mUsername.setTextColor(Color.BLACK);
                }
            }
        });

    }*/


    /*
    ON SIGNUP CLICK AUTHENTICATE
 */
    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
private void init(){
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = mEmail.getText().toString();
                Username = mUsername.getText().toString();
                FullName = mFullName.getText().toString();
                Password = mPassword.getText().toString();
                ConfirmPassword = mConfirmPassword.getText().toString();
                if(isEmpty(mUID) && !isEmpty(mPhoneNumber)){
                    UID = 0;
                    PhoneNumber = Long.parseLong(String.valueOf(mPhoneNumber.getText()));
                }
                else if(isEmpty(mPhoneNumber) && !isEmpty(mUID) ){
                    UID = Long.parseLong(String.valueOf(mUID.getText()));
                    PhoneNumber = 0;
                }else if(isEmpty(mUID) && isEmpty(mPhoneNumber)){
                    UID = 0;
                    PhoneNumber = 0;
                }
                else{
                    PhoneNumber = Long.parseLong(String.valueOf(mPhoneNumber.getText()));
                    UID = Long.parseLong(String.valueOf(mUID.getText()));
                }

                if(checkInputStrings(Email,Username,FullName,Password,ConfirmPassword) )
                {
// <------------------------------------------------------ passsword check if equal start ------------------------------>
                    if(Password.equals(ConfirmPassword) /*&& CheckIfPhoneNoExists(PhoneNumber).equals("true")*/) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        loadingSignUpTV.setVisibility(View.VISIBLE);
                        btn_signup.setText("");
                        firebaseMethods.registerNewUser(Email, Username, Password, ConfirmPassword, UID, PhoneNumber, FullName);
                    }
 // <------------------------------------------------------ password check else start ------------------------------>
                   /* else if (CheckIfPhoneNoExists(PhoneNumber).equals("false")  ){
                        mProgressBar.setVisibility(View.GONE);
                        loadingSignUpTV.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(mContext, "Phone number is already linked!",
                                Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.RED);
                        v.setTextSize(18);
                        toast.show();
                    }*/
                else {
                    mProgressBar.setVisibility(View.GONE);
                    loadingSignUpTV.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(mContext, "Check Your Passwords again! They don't match.",
                            Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }
// <------------------------------------------------------ password check else end ------------------------------>
                }
// <------------------------------------------------------ this else is the else of main if ------------------------------>
                else  {
                    mProgressBar.setVisibility(View.GONE);
                    loadingSignUpTV.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(mContext, "Sign Up failed! Please check your credentials",
                            Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }
            }
        });
/*
  NAVIGATION TO LOGIN FROM SIGN UP
 */
    LoginLinkOnSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
    });


}
    /*
    CHECKING IF ALL DETAILS ARE FILLED
     */
private boolean checkInputStrings(String Email, String Username, String FullName, String Password, String ConfirmPassword){
        if(Email.equals("")){
            /*mEmail.setText("Email Required");
            mEmail.setTextColor(Color.RED);*/
            mEmail.setError("Email Required");

            checkIfEmpty = false;
        }
        else{
            checkIfEmpty = true;
        }
        if(Username.equals("")){
            /*mUsername.setText("Username Required");
            mUsername.setTextColor(Color.RED);*/
            mUsername.setError("Username Required");
            checkIfEmpty = false;

        }
        else{
            checkIfEmpty = true;
        }
         if(FullName.equals("")){
             /* mFullName.setText("Full Name Required");
              mFullName.setTextColor(Color.RED);*/
              mFullName.setError("Full Name Required");
             checkIfEmpty = false;

         }
         else{
             checkIfEmpty = true;
         }
         if(Password.equals("")){
           /* *//* mPassword.setText("Password Required");
             mPassword.setTextColor(Color.RED);*//*
             mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/
             mPassword.setError("Password Required");
             checkIfEmpty = false;
         }
         else{
             checkIfEmpty = true;
         }
         if(ConfirmPassword.equals("")){
             /*mConfirmPassword.setText("Please Confirm Your Password");
             mConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
             mConfirmPassword.setTextColor(Color.RED);*/
             mConfirmPassword.setError("Please Confirm Your Password");
             checkIfEmpty = false;
         }
         else{
             checkIfEmpty = true;
         }
return checkIfEmpty;

}

/*
    ------------------------------------ Firebase ---------------------------------------------
     */
    /**
     * checks if user already exists
     * @param username
     */
    private void CheckIfUsernameExists(final String username) {
        Log.d(TAG, "CheckIfUsernameExists: " + username);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot SingleSnapshot : snapshot.getChildren()){
                    if(SingleSnapshot.exists()){
                        Log.d(TAG, "CheckIfUsernameExists: Found A Match " + SingleSnapshot.getValue(User.class).getUsername());
                        append = myRef.push().getKey().substring(6,10);
                        Log.d(TAG, "Username already exists, appending random string to name" + append);
                    }
                }

                    String mUsername = "";
                    mUsername = username + append;

                    firebaseMethods.AddNewUser(FullName, UID, PhoneNumber, Email, mUsername, "", "F.Y BSC IT",
                            "", "", "");
                    mAuth.signOut();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
   /* *//**
     * checks if user already exists
     * @param phoneNumber
     *//*
    private String CheckIfPhoneNoExists(final long phoneNumber) {
        Log.d(TAG, "CheckIfUsernameExists: " + phoneNumber );


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_phoneNumber))
                .equalTo(phoneNumber);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot SingleSnapshot : snapshot.getChildren()){
                    if(SingleSnapshot.exists()){
                        Log.d(TAG, "CheckIfPhoneNoExists: FoundMatch " + SingleSnapshot.getValue(User.class).getPhone_number());
                        check = "false";
                    }
                    else{
                        check="true";
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ffffff");
            }
        });
        Log.d(TAG, "CheckIfPhoneNoExists: otuside " + check);

        return check;
    }
*/
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CheckIfUsernameExists(Username);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    finish();
                    btn_signup.setText("Sign Up");
                    
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
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}