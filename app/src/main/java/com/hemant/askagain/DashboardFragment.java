package com.hemant.askagain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
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
//        createPost();



        getAllPost();

        return view;
    }

//    private void createPost() {
//        PostModel post;
//        post = new PostModel("https://lh3.googleusercontent.com/a-/AOh14Gilzax1J-gyorswryPpi1UL6Lt5fO-vD7ZVrFjs",
//                "Hemant Upadhyay",
//                "fuck time",
//                "What is your question",
//                "2");
//        FirebaseDatabase.getInstance().getReference().child("Posts").child("first").setValue(post);
//
//    }

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
                Log.d("TAG", "onDataChange: iteration");
                if(snapshot.exists()){
                    Log.d("TAG", "onDataChange: snapshot exist");
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Log.d("TAG", "onDataChange: " + ds.getValue());
                        PostModel post = ds.getValue(PostModel.class);
                        Log.d("TAG", "onDataChange: adding in post list");
                        postList.add(post);
                        Log.d("TAG", "onDataChange: " + postList.size());
                    }
                    postAdapter.notifyDataSetChanged();
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
    }
}