package com.hemant.askagain;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddPostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post,container,false);
//        UserModel userModel = new UserModel();
//        Log.d("TAG", "onCreateView: " +  userModel.getProfilePic());


        return view;
    }
}