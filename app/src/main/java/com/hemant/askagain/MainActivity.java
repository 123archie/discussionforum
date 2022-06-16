package com.hemant.askagain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private SignInButton signInBtn;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        googleSignInConfigure();
        checkPreviousSignIn();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void checkPreviousSignIn() {
        // Check for any previous signIn User after launch
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
           openHomePage();
        }
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
   }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            getProfileInfo();

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void getProfileInfo() {
        // Getting the profile info of signed in user
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            user = new User(personName,personPhoto.toString(),personEmail);

            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(personId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        Log.d("TAG", "onDataChange: new user");
                        FirebaseDatabase.getInstance().getReference().child("User").child(personId).setValue(user);
                    }
                    openHomePage();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void openHomePage(){
        // Opening the next activity
        Intent intent=new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }

    private void googleSignInConfigure() {
        // Building google SignInOptions and SignInClient
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void initViews() {
        // Initializing the Views
        signInBtn = findViewById(R.id.signInBtn);
    }
}