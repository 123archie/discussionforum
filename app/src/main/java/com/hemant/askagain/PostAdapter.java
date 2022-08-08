package com.hemant.askagain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LongDef;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemant.askagain.databinding.PostDashboardBinding;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    ArrayList<PostModel> postList;

    public PostAdapter(ArrayList<PostModel> postList,Context context ){
        this.context =context;
        this.postList = postList;
    }


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_dashboard,parent,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        PostModel postData = postList.get(position);
        // binding question
        holder.postDashboardBinding.textQuestion.setText(postData.getTextQuestion());
        Log.d("TAG",postData.getPostedBy());
        Log.d("TAG", "onBindViewHolder: " + postData.getImageQuestion());

        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(postData.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Log.d("Adapter", "onDataChange: exists");
                    // binding profile pic
                    Glide.with(context)
                            .load(snapshot.child("profilePic").getValue())
                            .placeholder(R.drawable.siu)
                            .into(holder.postDashboardBinding.postedByProfilePic);
                    // binding name
                    holder.postDashboardBinding.postedByName.setText(snapshot.child("name").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.postDashboardBinding.commentCount.setText(postData.getCommentCount() + "");
        holder.postDashboardBinding.likeCount.setText(postData.getLikeCount() + "");

        FirebaseDatabase.getInstance().getReference().child("Posts").child(GoogleSignIn.getLastSignedInAccount(context).getId());

        if(postData.getImageQuestion() != null){
            Log.d("TAG", "onBindViewHolder: " + postData.getImageQuestion());
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
                // if like clicked
                FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId()).child("likedBy").child(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // already liked now remove like
                            holder.postDashboardBinding.like.setColorFilter(R.color.black);
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
                                    .child("likedBy").child(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
                                    .child("likeCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int likeCount =0;
                                            if(snapshot.exists()){
                                                likeCount = snapshot.getValue(Integer.class);
                                            }
                                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
                                                    .child("likeCount").setValue(likeCount -1);
                                            holder.postDashboardBinding.likeCount.setText(likeCount - 1  + "");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }else{
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
                                    .child("likedBy").child(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId())
                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Posts")
                                                    .child(postData.getPostId())
                                                    .child("likeCount")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            int likeCount = 0;
                                                            if(snapshot.exists()){
                                                                likeCount = snapshot.getValue(Integer.class);
                                                            }
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("Posts")
                                                                    .child(postData.getPostId())
                                                                    .child("likeCount")
                                                                    .setValue(likeCount+1);
                                                            holder.postDashboardBinding.likeCount.setText(likeCount + 1 + "");
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
