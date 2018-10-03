package com.example.shivamvk.mindfuladmin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView rvUsers;
    private ProgressBar pbUsers;
    private TextView tvNoUsers;
    Spinner spinner;
    List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbUsers = view.findViewById(R.id.pb_users);
        tvNoUsers = view.findViewById(R.id.tv_fragment_no_users);
        rvUsers = view.findViewById(R.id.rv_users);


        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();


        loadUsers();
    }

    private void loadUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    userList.add(snapshot.getValue(User.class));
                }
                UsersSupplierAdapter adapter = new UsersSupplierAdapter(userList, getContext(),"users");
                rvUsers.setAdapter(adapter);
                pbUsers.setVisibility(View.GONE);
                if (userList.isEmpty()){
                    tvNoUsers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
