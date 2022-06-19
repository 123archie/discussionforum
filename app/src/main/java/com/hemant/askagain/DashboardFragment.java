package com.hemant.askagain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    static ArrayList<PostModel> postList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViews(view);
        getAllPost();

        return view;
    }




    private void getAllPost() {

        Log.d("TAG", "setAllPosts: recylerview started");
        PostAdapter postAdapter = new PostAdapter(postList,getContext());
        Log.d("TAG", "getAllPost: " + postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdapter);
        Log.d("TAG", "setAllPosts: all set");


        FirebaseDatabase.getInstance().getReference().child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        PostModel post = ds.getValue(PostModel.class);
                        Log.d("TAG", "onDataChange: " + post.getPostedBy());
//                        post.setLike((Integer) ds.child("like").getValue());
                        post.setPostId(ds.getKey());
                        postList.add(post);
                    }
                    Log.d("TAG", "onDataChange: " + postList.size());
                    postAdapter.notifyItemInserted(1);
                    Log.d("TAG", "onDataChange: notify data changed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        if(postList.size()!= 0){
            postList.clear();
        }
    }
}