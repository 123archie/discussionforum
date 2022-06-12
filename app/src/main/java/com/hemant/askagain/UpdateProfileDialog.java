package com.hemant.askagain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class UpdateProfileDialog extends AppCompatDialogFragment {

    TextInputEditText etProfession,etContact;
    RadioGroup radioGroupGender;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.update_my_profile,null);

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

                    }
                });

        initView();

        return builder.create();

    }

    private void initView() {
        etProfession = getView().findViewById(R.id.etProfession);
        etContact = getView().findViewById(R.id.etContact);
        radioGroupGender = getView().findViewById(R.id.radioGroupGender);
    }
}
