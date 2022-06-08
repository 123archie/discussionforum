package com.hemant.askagain;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.BlockingDeque;

public class MainActivity extends AppCompatActivity {

    private static int RC_SIGN_IN = 100;
    SignInButton signInBtn;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


        googleSignInConfigure();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Log.e("TAG", "account check started");
        if(account != null){
            Log.e("TAG", "onCreate: previous account signed in found out");
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
            finish();
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "onClick: sigin function called");
                signIn();
            }
        });



    }

    private void signIn() {
        Log.e("TAG", "signIn: google intent started");
        Intent intent = googleSignInClient.getSignInIntent();
        Log.e("TAG", "signIn: Start activity for result started");
        startActivityForResult(intent,RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "onActivityResult: request code check");
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.e("TAG", "onActivityResult: handler sin result");
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            Log.e("TAG", "handleSignInResult: signin completed");
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.e("TAG", "handleSignInResult: getting profile info");
            getProfileInfo();

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void getProfileInfo() {
        Log.e("TAG", "getProfileInfo: getting account");
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            Log.e("TAG", "getProfileInfo: getting info");
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            User user = new User(personName,personPhoto.toString());
            Log.e("User", "getProfileInfo: " + user.getProfilePic());
            Log.e("User", "getProfileInfo: " + user.getName());
            Log.e("User", "getProfileInfo: " + personId);


            FirebaseDatabase.getInstance().getReference().child("User").child(personId).setValue(user);
            openMyProfile();

//            // firebase realtime database user sent
//            Log.e("TAG", "getProfileInfo: sending data to firebase");
//            DatabaseReference ref = firebaseDatabase.getReference();
//            Log.e("TAG", "getProfileInfo: reference got");
//            DatabaseReference usersRef = ref.child("Users");
//            Log.e("TAG", "getProfileInfo: userref got");
//            usersRef.setValue(user);
        }
    }

    private void openMyProfile() {
        startActivity(new Intent(this, MyProfile.class));
        finish();
    }

    private void googleSignInConfigure() {
        Log.e("TAG", "googleSignInConfigure: started");
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        Log.e("TAG", "googleSignInConfigure: ended");
    }

    private void initViews() {
        Log.e("TAG", "initViews: views initialized");
        signInBtn = findViewById(R.id.signInBtn);
    }
}