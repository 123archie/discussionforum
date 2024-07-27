package com.hemant.askagain;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarnava.textwriter.TextWriter;
public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private Button signInBtn;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private UserModel userModel;
    TextWriter textWriter, textwriter;
    boolean alreadyExist=true;
    ConstraintLayout constraintLayout;
    DataSnapshot snapshot;
    DatabaseReference databaseReference;
    private ImageView user;
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
        googleSignInConfigure();
        checkPreviousSignIn();
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signInBtn.setBackgroundColor(Color.rgb(51, 153, 255));
               signInBtn.setTextColor(Color.rgb(251, 250, 250));
               signIn();
                 }
        });
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInBtn.setBackgroundColor(Color.rgb(251, 250, 250));
                signInBtn.setTextColor(Color.rgb(0,0,0));
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
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d("GoogleSignInAccount", "GoogleSignINACcount:"+requestCode);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d("AccountGoogle", "AccountGoogle: "+account);
            if(dataExist()){
                getProfileInfo();
                            }
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("TaskResultFailed", "signInResult:failed code=" + e);
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
        return alreadyExist;
    }
    private void getProfileInfo() {
        // Getting the profile info of signed in userModel
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        assert  acct!=null;
            try {
                userModel = new UserModel(acct.getDisplayName(), acct.getPhotoUrl().toString(), acct.getEmail());
            }
            catch(Exception e){
                Log.e("TAG", "Exception: "+e);
            }
              FirebaseDatabase.getInstance()
                    .getReference()
                    .child("User")
                    .child(acct.getId())
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists() || snapshot.exists()){
                        FirebaseDatabase.getInstance().getReference().child("User").child(acct.getId()).child("Name").setValue(acct.getDisplayName());
                            Uri photoUrl=acct.getPhotoUrl();
                            try {
                                FirebaseDatabase.getInstance().getReference().child("User").child(acct.getId()).child("ProfilePic").setValue(acct.getPhotoUrl().toString());
                                FirebaseDatabase.getInstance().getReference().child("User").child(acct.getId()).child("ProfilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String photoUrl=(String) snapshot.getValue();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        }catch(Exception e){
                            FirebaseDatabase.getInstance().getReference().child("User").child(acct.getId()).child("ProfilePic").setValue(user);
                        }
                    FirebaseDatabase.getInstance().getReference().child("User").child(acct.getId()).child("Email").setValue(acct.getEmail());}
                    openHomePage();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
        user=findViewById(R.id.profilePic);
        textWriter=findViewById(R.id.textwriter);
        signInBtn = findViewById(R.id.signInBtn);
        textwriter=findViewById(R.id.text);
        constraintLayout=findViewById(R.id.constraint);
    }
    }