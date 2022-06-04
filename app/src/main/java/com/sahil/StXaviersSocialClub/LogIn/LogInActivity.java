package com.sahil.StXaviersSocialClub.LogIn;

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

import com.sahil.StXaviersSocialClub.Home.HomeActivity;
import com.sahil.StXaviersSocialClub.R;
import com.sahil.StXaviersSocialClub.SignUp.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
private static final String TAG ="LogInActivity";
    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private TextView loadingLoginTV;
    private EditText mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_login);
        mProgressBar = (ProgressBar) findViewById(R.id.LogInButtonProgressBar);
        mEmail = (EditText) findViewById(R.id.LogInEmail);
        mPassword = (EditText) findViewById(R.id.LogInPassword);
        loadingLoginTV = (TextView)  findViewById(R.id.LoadingLoginText);
        mContext = LogInActivity.this;

        Log.d(TAG,"ON CREATE : STARTED");

        loadingLoginTV.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        //FIREBASE AUTHENTICATION
        setupFirebaseAuth();
        init();
    }

    private boolean isStringNull(String string)
    {
        Log.d(TAG, "CHECKING IF STRING IS NULL");

        if(string.equals("")){
            return true;
        }
        else{

            return false;
        }

    }  /*
    ------------------------------------ Firebase ---------------------------------------------
     */

  private void init(){

      final Button LoginButton = (Button) findViewById(R.id.btn_login);
      LoginButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Log.d(TAG, "ON CLICK WORKS");

              String email = mEmail.getText().toString();
              String password  = mPassword.getText().toString();
              LoginButton.setText("");
              if(isStringNull(email) ){
                  /*Toast toast = Toast.makeText(mContext,"Add email & password to login!",Toast.LENGTH_LONG);
                  TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                  v.setTextColor(Color.RED);
                  toast.show();*/
                  mEmail.setError("Enter your email to log in.");
                  LoginButton.setText("Log In");
              }
              if(isStringNull(password)){
                  mPassword.setError("Enter your password to log in.");
                  LoginButton.setText("Log In");
              }
              else{
                  mProgressBar.setVisibility(View.VISIBLE);
                  loadingLoginTV.setVisibility(View.VISIBLE);

                  mAuth.signInWithEmailAndPassword(email, password)
                          .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {

                                  FirebaseUser user = mAuth.getCurrentUser();

                                  if (task.isSuccessful()) {
                                      // Sign in success, update UI with the signed-in user's information
                                      try{
                                          //CHECKING IF EMAIL IS VERIFIED
                                          if(Objects.requireNonNull(user).isEmailVerified()){
                                              Log.d(TAG,"OnComplete success : email is verfied");
                                              Intent intent = new Intent(LogInActivity.this,HomeActivity.class);
                                              startActivity(intent);
                                              finish();
                                              Toast toast = Toast.makeText(LogInActivity.this, "     WELCOME TO THE \n St Xavier's Social Club!",
                                                      Toast.LENGTH_LONG);
                                              TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                              v.setTextSize(20);
                                              toast.show();

                                          }
                                          else{
                                              Toast toast = Toast.makeText(LogInActivity.this, "Your email is not verified.\n Check your email Inbox",
                                                      Toast.LENGTH_LONG);
                                              TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                              v.setTextColor(Color.RED);
                                              v.setTextSize(20);
                                              toast.show();
                                              mProgressBar.setVisibility(View.GONE);
                                              loadingLoginTV.setVisibility(View.GONE);
                                              mAuth.signOut();
                                          }
                                      }catch(NullPointerException e){
                                          Log.e(TAG,"onComplete: NullPointerException: "+ e.getMessage());

                                      }
                                  } else {
                                      // If sign in fails, display a message to the user.
                                      Log.w(TAG, "signInWithEmail:failure",task.getException());
                                      Toast toast = Toast.makeText(LogInActivity.this, getString(R.string.auth_failed_login),
                                              Toast.LENGTH_LONG);
                                      TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                      v.setTextColor(Color.RED);
                                      v.setTextSize(15);
                                      toast.show();
                                  }
                                  LoginButton.setText("Log In");
                                  loadingLoginTV.setVisibility(View.GONE);
                                  mProgressBar.setVisibility(View.GONE);

                                  // ...
                              }
                          });

              }
          }
      });

      TextView linkSignUp = (TextView) findViewById(R.id.linkSignUpOnLogin);
      linkSignUp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Log.d(TAG, "SING UP LINK CLICKED ON LOGIN");
              Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
              startActivity(intent);
          }
      });
/*
 NAVIGATING TO HOME ACTIVIYT IF USER IS LOGGED IN & FINISHING LOG IN
*/
      if(mAuth.getCurrentUser() != null){
         Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
         startActivity(intent);
         finish();

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
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}