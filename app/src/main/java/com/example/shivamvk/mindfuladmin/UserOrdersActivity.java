package com.example.shivamvk.mindfuladmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserOrdersActivity extends AppCompatActivity {

    private String userTtpe,userEmail,userName;
    List<Order> orderList;
    private RecyclerView rvOrders;
    private ProgressBar pbOrders;
    private TextView tvNoOrders;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_orders);
        
        userTtpe = getIntent().getStringExtra("usertype");
        userEmail = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("name");

        rvOrders = findViewById(R.id.rv_orders);
        pbOrders = findViewById(R.id.pb_orders);
        tvNoOrders = findViewById(R.id.tv_fragment_no_orders);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setHasFixedSize(true);

        orderList = new ArrayList<>();

        setTitle("Orders by "+userName);
        
        if (userTtpe.equals("users")){
            setTitle("Orders by "+userName);
            loadUserOrders(); 
        } else {
            setTitle("Loads applied by " + userName);
            loadSupplierOrders();
        }
    }

    private void loadSupplierOrders() {

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("suppliers").child(generateHash(userEmail))
                .child("appliedfor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        orderList.add(snapshot.getValue(Order.class));
                }
                OrderAdapter adapter = new OrderAdapter(orderList,UserOrdersActivity.this,"supplier");
                rvOrders.setAdapter(adapter);
                pbOrders.setVisibility(View.GONE);
                if(orderList.isEmpty()){
                    tvNoOrders.setVisibility(View.VISIBLE);
                    tvNoOrders.setText("No orders by this user yet!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadUserOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users").child(generateHash(userEmail))
                .child("orders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    orderList.add(snapshot.getValue(Order.class));
                }
                OrderAdapter adapter = new OrderAdapter(orderList,UserOrdersActivity.this,"user");
                rvOrders.setAdapter(adapter);
                pbOrders.setVisibility(View.GONE);
                if(orderList.isEmpty()){
                    tvNoOrders.setVisibility(View.VISIBLE);
                    tvNoOrders.setText("No orders by this user yet!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String generateHash(String s) {
        int hash = 21;
        String main = s;
        for (int i = 0; i < main.length(); i++) {
            hash = hash*31 + main.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeAsUp){
            onBackPressed();
        }

        return true;
    }
}
