package com.hemant.askagain;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

        Log.d("Adapter", "onBindViewHolder: started binding");
        PostModel postData = postList.get(position);
        holder.postDashboardBinding.postedByName.setText(postData.getPostedByName());
        holder.postDashboardBinding.textQuestion.setText(postData.getTextQuestion());
        Log.d("Adapter", "onBindViewHolder: text binded");

        Glide.with(context)
                .load(postData.getPostedByProfilePic())
                .placeholder(R.drawable.siu)
                .into(holder.postDashboardBinding.postedByProfilePic);

        holder.postDashboardBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if like clicked
            }
        });

        holder.postDashboardBinding.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCommentActivity.class);
                context.startActivity(intent);
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
