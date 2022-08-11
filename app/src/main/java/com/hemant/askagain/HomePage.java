package com.hemant.askagain;

import android.os.Bundle;
import android.util.Log;

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
        getUserInfo();
        replaceFragment(new DashboardFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dashboard:
                    replaceFragment(new DashboardFragment());
                    break;
                case R.id.my_profile:
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
        if(userModel.getName()!=null){
            bundle.putString("Name", userModel.getName() );
        }
        if(userModel.getEmail()!=null){
            bundle.putString("Email", userModel.getEmail());
        }
        if(userModel.getProfilePic()!=null){
            bundle.putString("ProfilePic", userModel.getProfilePic());
        }
        if(userModel.getContactNumber()!=null){
            bundle.putString("Contact", userModel.getContactNumber());
        }
        if(userModel.getGender()!=null){
            bundle.putString("Gender", userModel.getGender());
        }
        if(userModel.getProfession()!=null){
            bundle.putString("Profession", userModel.getProfession());
        }
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment,fragment).addToBackStack(null).commit();
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
        fragmentTransaction.addToBackStack(null).commit();
    }

}