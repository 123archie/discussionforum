package com.hemant.askagain;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hemant.askagain.databinding.CommentPostBinding;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ArrayList<CommentModel> commentList;

    public CommentAdapter(ArrayList<CommentModel> commentList,Context context ){
        this.context =context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_post,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        CommentModel commentData = commentList.get(position);
        holder.commentPostBinding.textAnswer.setText(commentData.getTextAnswer());


        FirebaseDatabase.getInstance().getReference().child("User").child(commentData.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Glide.with(context)
                            .load(snapshot.child("profilePic").getValue())
                            .placeholder(R.drawable.siu)
                            .into(holder.commentPostBinding.commentedByProfilePic);
                    holder.commentPostBinding.commentedByName.setText(snapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(commentData.getImageAnswer() != null){
            Glide.with(context)
                    .load(commentData.getImageAnswer())
                    .into(holder.commentPostBinding.imageAnswer);
            holder.commentPostBinding.imageAnswer.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CommentPostBinding commentPostBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentPostBinding = CommentPostBinding.bind(itemView);
        }
    }
}

