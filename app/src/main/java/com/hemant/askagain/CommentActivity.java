package com.hemant.askagain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentActivity extends AppCompatActivity {
public String author;
public String title;

   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference mDatabase;
   private DatabaseReference messages_f;
   private Button btncomment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        btncomment=findViewById(R.id.button_cmnt);


        Log.d("TAG", "onCreate: show comment started");
        show_comments();
        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommentActivity.this, AddCommentActivity.class));
            }
        });

    }

    private void show_comments() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase=firebaseDatabase.getReference().child("Comments");
        Log.d("TAG", "show_comments: " + mDatabase);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("TAG", "onDataChange: snapshot exist");
                    comment comment = snapshot.getValue(comment.class);
                    Log.d("TAG", "onDataChange: comment initiated"+ comment.getCommentedBy() + "discription" + comment.getDescription());
                    System.out.println(comment);

                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 System.out.println("The read failed: " +error.getMessage());
                 CommentActivity.super.onStart();
                 adapter.startListening();
            }
        });
    }

}
