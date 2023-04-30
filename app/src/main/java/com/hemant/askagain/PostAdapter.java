package com.hemant.askagain;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemant.askagain.databinding.PostDashboardBinding;

import java.util.ArrayList;
import java.util.Objects;
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    int i=1;
    int count=0;
    final Context context;
    ArrayList<PostModel> postList;
    DatabaseReference databaseReference;
    public PostAdapter(ArrayList<PostModel> postList,Context context){
        this.context =context;
        this.postList = postList;
    }
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_dashboard,parent,false);
        return new PostAdapter.ViewHolder(view);
    };
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        PostModel postData = postList.get(position);
        // binding question
        try {
            holder.postDashboardBinding.textQuestion.setText(postData.getTextQuestion());
        }
        catch (Exception e){

        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference
                .child("User")
                .child(postData.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists()){
                    // binding profile pic
                    Glide.with(context)
                            .load(snapshot.child("ProfilePic").getValue())
                            .placeholder(R.drawable.profile)
                            .into(holder.postDashboardBinding.postedByProfilePic);
                    // binding name
                     try {
                         holder.postDashboardBinding.postedByName.setText(snapshot.child("Name").getValue().toString());
                     }
                     catch(Exception e){

                     }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       databaseReference.child("Posts").child(postData.getPostId())
                .child("likedBy").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.postDashboardBinding.likeCount.setText(snapshot.getChildrenCount() + "");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference.child("Posts").child(postData.getPostId())
                .child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.postDashboardBinding.commentCount.setText(snapshot.getChildrenCount() + "");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference.child("Posts").child(GoogleSignIn.getLastSignedInAccount(context).getId());
        if(postData.getImageQuestion() != null){
            Glide.with(context)
                    .load(postData.getImageQuestion())
                    .into(holder.postDashboardBinding.imageQuestion);
            holder.postDashboardBinding.imageQuestion.setVisibility(View.VISIBLE);
        }else{
            holder.postDashboardBinding.imageQuestion.setVisibility(View.GONE);
        }
        holder.postDashboardBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Animation animation=new RotateAnimation(
                        0,
                        -20,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                        );
                animation.setDuration(80);
                holder.postDashboardBinding.like.startAnimation(animation);
                     databaseReference.child("Posts")
                        .child(postData.getPostId())
                        .child("likedBy")
                        .child(Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.postDashboardBinding.like.setColorFilter(Color.rgb(0, 0, 0));
                            // already liked now remove like count and change color
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
                                    .child("likedBy").child(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId()).removeValue();
                           FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
                                    .child("likedBy").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            holder.postDashboardBinding.likeCount.setText(snapshot.getChildrenCount() + "");
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else{
                            holder.postDashboardBinding.like.setColorFilter(Color.rgb(51, 153, 255));
                            databaseReference.child("Posts").child(postData.getPostId())
                                    .child("likedBy").child(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId())
                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
                                                    .child("likedBy").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            holder.postDashboardBinding.likeCount.setText(snapshot.getChildrenCount() + "");
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        databaseReference.child("Posts")
                .child(postData.getPostId())
                .child("likedBy")
                .child(Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context).getId())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                        }
                        else{

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.postDashboardBinding.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = new ShowCommentFragment();
                FragmentTransaction fragmentTransaction= activity.getSupportFragmentManager().beginTransaction();
                // send posted by in bundle
                Bundle bundle = new Bundle();
                bundle.putString("PostId", postData.getPostId());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment,fragment).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        PostDashboardBinding postDashboardBinding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postDashboardBinding = PostDashboardBinding.bind(itemView);
        }
    }
}
