package com.hemant.askagain;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

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

        if(postData.getImageQuestion() != null){
            Glide.with(context)
                    .load(postData.getImageQuestion())
                    .into(holder.postDashboardBinding.imageQuestion);
            holder.postDashboardBinding.imageQuestion.setVisibility(View.VISIBLE);
        }

//        holder.postDashboardBinding.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // if like clicked
//                FirebaseDatabase.getInstance().getReference().child("Posts").child("likes").child(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            // already liked
//                            holder.postDashboardBinding.like.setColorFilter(R.color.orange);
//                        }else{
//                            // already liked
//                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postData.getPostId())
//                                    .child("likedBy").child(GoogleSignIn.getLastSignedInAccount(view.getContext()).getId())
//                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            FirebaseDatabase.getInstance().getReference()
//                                                    .child("Posts")
//                                                    .child(postData.getPostId())
//                                                    .child("like")
//                                                    .setValue(postData.getLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void unused) {
//                                                            holder.postDashboardBinding.like.setColorFilter(R.color.orange);
//                                                        }
//                                                    });
//                                        }
//                                    });
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });

        holder.postDashboardBinding.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = new AddCommentFragment();
                FragmentTransaction fragmentTransaction= activity.getSupportFragmentManager().beginTransaction();
                // send posted by in bundle
                Bundle bundle = new Bundle();
                bundle.putString("PostedBy", postData.getPostedBy());
                bundle.putString("PostId", postData.getPostId());

                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment,fragment).commit();
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
