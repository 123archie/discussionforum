package com.hemant.askagain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
public class AddPostFragment extends Fragment {

    ImageView profilePic,imageQuestion,addQuestionPhoto;
    TextView postedByName,postedByProfession;
    TextInputEditText askQuestionTextField;
    AppCompatButton addPostBtn;
    FirebaseStorage firebaseStorage;
    String profession;
    Uri uri;
    GoogleSignInAccount acct;
    PostModel postModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post,container,false);



        firebaseStorage = FirebaseStorage.getInstance();

        initViews(view);
        setProfileName();
        getBundle();
        postModel = new PostModel();


        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postModel.setPostedBy(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId());
                postModel.setTextQuestion(askQuestionTextField.getText().toString());

                FirebaseDatabase.getInstance().getReference()
                        .child("Posts")
                        .push()
                        .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                openHomePage();
                            }
                        });

            }
        });

        addQuestionPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        
        


        return view;
    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        profession = bundle.getString("Profession");
    }


    private void openHomePage() {
        Intent intent = new Intent(getContext(), HomePage.class);
        startActivity(intent);
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
            imageQuestion.setImageURI(uri);
            imageQuestion.setVisibility(View.VISIBLE);
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
                            setPostPhoto(uri);
                        }
                    });
                }
            });
        }
    }

    private void setPostPhoto(Uri uri) {
        Log.d("TAG", "setCommentPhoto: " + uri.toString());
        postModel.setImageQuestion(uri.toString());
        Toast.makeText(getView().getContext(), "Image is all set", Toast.LENGTH_LONG ).show();
    }
    private void setProfileName() {
        acct = GoogleSignIn.getLastSignedInAccount(getContext());
        postedByName.setText(acct.getDisplayName());
        postedByName.setText(acct.getDisplayName());
        postedByProfession.setText(profession);
        Glide.with(getContext())
                .load(acct.getPhotoUrl())
                .into(profilePic);
    }
    private void initViews(View view) {
        profilePic = view.findViewById(R.id.profilePic);
        imageQuestion = view.findViewById(R.id.imageQuestion);
        addQuestionPhoto = view.findViewById(R.id.addQuestionPhoto);
        postedByName = view.findViewById(R.id.personName);
        askQuestionTextField = view.findViewById(R.id.askQuestionTextField);
        addPostBtn = view.findViewById(R.id.addPostBtn);
        postedByProfession = view.findViewById(R.id.personProfession);
    }
    
}