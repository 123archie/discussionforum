package com.hemant.askagain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

public class MyProfileFragment extends Fragment {

    ImageView profilePic;
    TextView personName, personProfession, personEmail, personContact, personGender,personProfession2;
    MaterialButton logOutBtn,editProfileBtn;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initViews(view);

        SetUserInfo();

        Bundle bundle = this.getArguments();
        user = new User(bundle.getString("Name"),bundle.getString("ProfilePic"), bundle.getString("Email"));
        user.setGender(bundle.getString("Gender"));
        user.setProfession(bundle.getString("Profession"));
        user.setContactNumber(bundle.getString("Contact"));

        if(user.getProfession() != null){
            personProfession.setText(user.getProfession());
            personProfession2.setText(user.getProfession());
        }
        if(user.getGender() != null){
            personGender.setText(user.getGender());
        }
        if(user.getContactNumber() != null){
            personContact.setText(user.getContactNumber());
        }

        googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        googleSignInClient= GoogleSignIn.getClient(getActivity(), googleSignInOptions);


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogBox();
            }
        });

        return view;
    }

    private void openDialogBox() {
        Log.d("TAG", "openDialogBox: dialog open");
        UpdateProfileDialog updateProfileDialog = new UpdateProfileDialog();
        updateProfileDialog.show(getParentFragmentManager(),"UpdateFragment Dialog");
    }

    private void SetUserInfo() {
        personName.setText(user.getName());
        personEmail.setText(user.getEmail());
        if(user.getProfilePic() != null) {
            Glide.with(MyProfileFragment.this)
                    .load(user.getProfilePic())
                    .into(profilePic);
        }
    }


    private void logOutUser() {
        if(googleSignInClient != null){
            googleSignInClient.signOut()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            openSignIn();
                        }
                    });
        }
    }

    private void openSignIn() {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
    }

    private void initViews(View view) {
        profilePic = view.findViewById(R.id.profilePic);
        personName = view.findViewById(R.id.personName);
        personProfession = view.findViewById(R.id.personProfession);
        personEmail = view.findViewById(R.id.personEmail);
        personContact = view.findViewById(R.id.personContact);
        personGender = view.findViewById(R.id.personGender);
        personProfession2 = view.findViewById(R.id.personProfession2);
        logOutBtn = view.findViewById(R.id.logOutBtn);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
    }
}