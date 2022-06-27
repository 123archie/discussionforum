package com.hemant.askagain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddCommentFragment extends Fragment {

    ImageView profilePic,imageAnswer,addAnswerPhoto;
    TextView commentedByName;
    TextInputEditText answerText;
    AppCompatButton addCommentBtn;
    String PostId;
    FirebaseStorage firebaseStorage;
    Uri uri;
    GoogleSignInAccount acct;
    CommentModel commentModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_comment, container, false);

        firebaseStorage = FirebaseStorage.getInstance();

        initViews(view);
        setProfileName();
        getBundle();
        commentModel = new CommentModel();


        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                                int commentCount = 0;
                                                if(snapshot.exists()){
                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Posts")
                                                        .child(PostId)
                                                        .child("commentCount")
                                                        .setValue(commentCount +1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("TAG", "Succesful added");
                                                                openDashBoard();
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

        addAnswerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return view;
    }

    private void openDashBoard() {

        Fragment fragment = new DashboardFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,11);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData() != null){
            uri = data.getData();
            imageAnswer.setImageURI(uri);
            imageAnswer.setVisibility(View.VISIBLE);
            Log.d("TAG", "onActivityResult: start");

            final StorageReference storageReference = firebaseStorage.getReference()
                    .child("ImageCommentAnswer").child(acct.getId()).child(new Date().getTime() + "");
            Log.d("TAG", "onActivityResult: " + storageReference);
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("TAG", "onSuccess: uadated to storage");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            setCommentPhoto(uri);
                        }
                    });
                }
            });
        }
    }

    private void setCommentPhoto(Uri uri) {
        Log.d("TAG", "setCommentPhoto: " + uri.toString());
        commentModel.setImageAnswer(uri.toString());
        Toast.makeText(getView().getContext(), "Image is all set", Toast.LENGTH_LONG ).show();

    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        PostId = bundle.getString("PostId");
        Log.d("TAG",PostId);
    }

    private void setProfileName() {
        acct = GoogleSignIn.getLastSignedInAccount(getContext());
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