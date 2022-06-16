package com.hemant.askagain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfileDialog extends AppCompatDialogFragment {
    TextInputEditText etProfession,etContact;
    RadioGroup radioGroupGender;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String profession, contactNumber,gender;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.update_my_profile,null);

        etProfession = view.findViewById(R.id.etProfession);
        etContact = view.findViewById(R.id.etContact);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);

        builder.setView(view)
                .setTitle("Update Profile")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(acct.getId());

                        profession =etProfession.getText().toString();
                        contactNumber = etContact.getText().toString();
                        switch (radioGroupGender.getCheckedRadioButtonId()){
                            case R.id.rbMale:
                                gender = "Male";
                                break;
                            case R.id.rbFemale:
                                gender = "Female";
                                break;
                            case R.id.rbOther:
                                gender = "Other";
                                break;
                        }
                        databaseReference.child("profession").setValue(profession);
                        databaseReference.child("contactNumber").setValue(contactNumber);
                        databaseReference.child("gender").setValue(gender);
                    }
                });

        return builder.create();

    }
}
