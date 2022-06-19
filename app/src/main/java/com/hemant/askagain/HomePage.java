package com.hemant.askagain;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemant.askagain.databinding.ActivityHomePageBinding;

public class HomePage extends AppCompatActivity {

    ActivityHomePageBinding binding;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new DashboardFragment());
        getUserInfo();

        binding.fabAddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fabAddPostBtn.setVisibility(GONE);
                sendUserInfoReplaceFragment(new AddPostFragment());
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dashboard:
                    if(binding.fabAddPostBtn.getVisibility() == GONE){
                        binding.fabAddPostBtn.setVisibility(View.VISIBLE);
                    }
                    replaceFragment(new DashboardFragment());
                    break;
                case R.id.my_profile:
                    if(binding.fabAddPostBtn.getVisibility() == VISIBLE){
                        binding.fabAddPostBtn.setVisibility(GONE);
                    }
                    sendUserInfoReplaceFragment(new MyProfileFragment());
                    break;
            }
            return true;
        });

    }

    private void sendUserInfoReplaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        Log.d("TAG", "sendUserInfoReplaceFragment: " + userModel);

        Bundle bundle = new Bundle();
        bundle.putString("Name", userModel.getName() );
        bundle.putString("Email", userModel.getEmail());
        bundle.putString("ProfilePic", userModel.getProfilePic());
        bundle.putString("Contact", userModel.getContactNumber());
        bundle.putString("Gender", userModel.getGender());
        bundle.putString("Profession", userModel.getProfession());

        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment,fragment).commit();
    }


    private void getUserInfo() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            databaseReference.child("User").child(acct.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        userModel = snapshot.getValue(UserModel.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }

}