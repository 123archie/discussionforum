package com.hemant.askagain;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class MyProfileFragment extends Fragment {

    ImageView profilePic;
    TextView personName, personProfession, personEmail, personContact, personGender,personProfession2;
    MaterialButton logOutBtn;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initViews(view);

        googleSignInOptions = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        googleSignInClient= GoogleSignIn.getClient(getActivity(), googleSignInOptions);


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });



        return view;
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
    }
}