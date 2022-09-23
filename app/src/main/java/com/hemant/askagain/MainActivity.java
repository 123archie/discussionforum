package com.hemant.askagain;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.fonts.FontFamily;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarnava.textwriter.TextWriter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private SignInButton signInBtn;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private UserModel userModel;
    TextWriter textWriter;
    TextWriter textwriter;
    boolean alreadyExist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        textWriter
                .setText("WELCOME")
                 .setColor(Color.rgb(51, 153, 255))
                .setConfig(TextWriter.Configuration.INTERMEDIATE)
                        .setSizeFactor(32f)
                                .setDelay(40)
                .setListener(new TextWriter.Listener() {
                    @Override
                    public void WritingFinished() {
                        textwriter
                                .setText("LOGIN  HERE")
                                .setColor(Color.rgb(51, 153, 255))
                                .setConfig(TextWriter.Configuration.INTERMEDIATE)
                                .setSizeFactor(32f)
                                .setDelay(40)
                                .startAnimation();
                    }
                })
        .startAnimation();
        Log.d("TAG", "signing in: Signing in sucessfull");
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
        // Check for any previous signIn UserModel after launch
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
            if(dataExist()){
                getProfileInfo();
            }

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private boolean dataExist() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(acct.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            alreadyExist = true;
                        }else{
                            alreadyExist = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        Log.d("TAG", "dataExist: " + alreadyExist);
        return alreadyExist;
    }

    private void getProfileInfo() {
        // Getting the profile info of signed in userModel
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        Log.d("TAG","acct: "+acct);
        if (acct != null) {

            userModel = new UserModel(acct.getDisplayName(),acct.getPhotoUrl().toString(),acct.getEmail());
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("User")
                    .child(Objects.requireNonNull(acct.getId()))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(!snapshot.exists()){
                        Log.d("TAG", "onDataChange: new userModel register");
                        FirebaseDatabase.getInstance().getReference().child("User").child(acct.getId()).setValue(userModel);
                    }
                    openHomePage();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



    private void openHomePage() {

        // Opening the next activity
        Intent intent =new Intent(this, HomePage.class);
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
        textWriter=findViewById(R.id.textwriter);
        signInBtn = findViewById(R.id.signInBtn);
        textwriter=findViewById(R.id.text);
    }
    }