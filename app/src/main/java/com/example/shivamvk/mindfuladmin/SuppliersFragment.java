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

public class SuppliersFragment extends Fragment {

    private RecyclerView rvSuppliers;
    private ProgressBar pbSuppliers;
    private TextView tvNoSuppliers;
    Spinner spinner;
    List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suppliers, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbSuppliers = view.findViewById(R.id.pb_suppliers);
        tvNoSuppliers = view.findViewById(R.id.tv_fragment_no_suppliers);
        rvSuppliers = view.findViewById(R.id.rv_suppliers);


        rvSuppliers.setHasFixedSize(true);
        rvSuppliers.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();


        loadSuppliers();
    }

    private void loadSuppliers() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("suppliers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    userList.add(snapshot.getValue(User.class));
                }
                UsersSupplierAdapter adapter = new UsersSupplierAdapter(userList, getContext(),"suppliers");
                rvSuppliers.setAdapter(adapter);
                pbSuppliers.setVisibility(View.GONE);
                if (userList.isEmpty()){
                    tvNoSuppliers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
