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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private TextView tvNoOrders;
    private ProgressBar pbOrders;
    private RecyclerView rvOrders;

    List<Order> list_of_orders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNoOrders = view.findViewById(R.id.tv_fragment_no_orders);
        pbOrders = view.findViewById(R.id.pb_orders);
        rvOrders = view.findViewById(R.id.rv_orders);

        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        list_of_orders = new ArrayList<>();


        loadAllOrders();
    }

    private void loadAllOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_of_orders.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for (DataSnapshot snapshot1 : snapshot.child("orders").getChildren()){
                        Order order = snapshot1.getValue(Order.class);
                        list_of_orders.add(order);
                    }
                }
                OrderAdapter adapter = new OrderAdapter(list_of_orders, getContext());
                rvOrders.setAdapter(adapter);
                pbOrders.setVisibility(View.GONE);
                if (list_of_orders.isEmpty()){
                    tvNoOrders.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
