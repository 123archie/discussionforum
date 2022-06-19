package com.hemant.askagain;

import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.logging.Logger;

public class AddCommentFragment extends Fragment {
    ImageView profilePic,imageAnswer,addAnswerPhoto;
    TextView commentedByName;
    TextInputEditText answerText;
    AppCompatButton addCommentBtn;
    String PostedBy, PostId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_comment, container, false);

        initViews(view);
        setProfileName();
        getBundle();


        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentModel commentModel = new CommentModel();
                commentModel.setCommentedBy(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId());
                commentModel.setTextAnswer(answerText.getText().toString());

                FirebaseDatabase.getInstance().getReference()
                        .child("Posts")
                        .child(PostId)
                        .child("comments")
                        .push()
                        .setValue(commentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Posts")
                                        .child(PostId)
                                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commnetCount = 0;
                                                if(snapshot.exists()){
                                                    commnetCount = snapshot.getValue(Integer.class);
                                                }
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Posts")
                                                        .child(PostId)
                                                        .child("commentCount")
                                                        .setValue(commnetCount +1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("TAG", "Succesful added");
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });

            }
        });







        return view;
    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        PostedBy = bundle.getString("PostedBy");
        PostId = bundle.getString("PostId");
        Log.d("TAG",PostedBy);
        Log.d("TAG",PostId);
    }

    private void setProfileName() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        commentedByName.setText(acct.getDisplayName());
        Glide.with(getContext())
                .load(acct.getPhotoUrl())
                .into(profilePic);
    }

    private void initViews(View view) {
        profilePic = view.findViewById(R.id.profilePic);
        imageAnswer = view.findViewById(R.id.imageAnswer);
        addAnswerPhoto = view.findViewById(R.id.addAnswerPhoto);
        commentedByName = view.findViewById(R.id.commentedByName);
        answerText = view.findViewById(R.id.answerText);
        addCommentBtn = view.findViewById(R.id.addCommentBtn);
    }
}