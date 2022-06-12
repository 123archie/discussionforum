package com.hemant.askagain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

public class AddCommentActivity extends AppCompatActivity {

 private RelativeLayout relativeLayout;
 private TextInputEditText editText;
 private Button button;
 private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        relativeLayout=findViewById(R.id.relativeLayout);
        editText=findViewById(R.id.text);
        button=findViewById(R.id.btn);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcomment();
            }
        });

    }

    private void addcomment() {
        String post=editText.getText().toString();
        mDatabase.child("Comments").push().setValue(post);
        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("ChildAdded","onChildAdded"+snapshot.getKey());
                AddCommentActivity comment=snapshot.getValue(AddCommentActivity.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("ChildChanged","onChildChanged:"+snapshot.getKey());
                AddCommentActivity comment=snapshot.getValue(AddCommentActivity.class);
                String commentKey= snapshot.getKey();}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
               Log.d("ChildRemoved","onChildRemoved"+snapshot.getKey());
               String commentKey= snapshot.getKey();}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("ChildMoved","onChildMoved"+snapshot.getKey());
                AddCommentActivity movedComment=snapshot.getValue(AddCommentActivity.class);
                String commentKey=snapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
             Log.w("postComments:onCancelled",error.toException());
             Toast.makeText(AddCommentActivity.this, "Failed to Load Comments", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addChildEventListener(childEventListener);
        mDatabase.removeEventListener(childEventListener);



    }
}