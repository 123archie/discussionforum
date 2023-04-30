package com.hemant.askagain;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class ShowCommentFragment extends Fragment {
    private RecyclerView recyclerView;
    private AppCompatButton commentBtn;
    private String postID;
    private CommentModel commentData;
    static ArrayList<CommentModel> commentList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_comment,container,false);
        initViews(view);
        getBundle();
        getSetAllComment();
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddCommentFragment();
                FragmentTransaction fragmentTransaction= getActivity().getSupportFragmentManager().beginTransaction();
                // send posted by in bundle
                Bundle bundle = new Bundle();
                bundle.putString("PostId", postID);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment,fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
    private void getBundle() {
        Bundle bundle = this.getArguments();
        postID = bundle.getString("PostId");
        }
    private void getSetAllComment() {
        CommentAdapter commentAdapter = new CommentAdapter(commentList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentAdapter);
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postID).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        commentData = ds.getValue(CommentModel.class);
                        commentList.add(commentData);
                    }
                    commentAdapter.notifyItemInserted(1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.commentRecyclerView);
        commentBtn = view.findViewById(R.id.commentBtn);
        commentList.clear();
    }
}