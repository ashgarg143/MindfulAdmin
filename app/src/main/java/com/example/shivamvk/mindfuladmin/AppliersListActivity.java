package com.example.shivamvk.mindfuladmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppliersListActivity extends AppCompatActivity {

    ProgressBar pbAppliers;
    TextView tvNoAppliers;
    RecyclerView rvAppliers;

    List<String> LIST_APPLIERS;
    List<User> LIST_ALL_SUPPLIERS;

    String orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliers_list);

        orderid = getIntent().getStringExtra("orderid");

        setTitle("Appliers (Order Id: " + orderid +")");

        pbAppliers = findViewById(R.id.pb_appliers);
        rvAppliers = findViewById(R.id.rv_appliers);
        tvNoAppliers = findViewById(R.id.tv_no_appliers);

        rvAppliers.setHasFixedSize(true);
        rvAppliers.setLayoutManager(new LinearLayoutManager(this));

        LIST_APPLIERS = new ArrayList<>();
        LIST_ALL_SUPPLIERS = new ArrayList<>();

        loadAllSuppliers();
        loadAppliers();
    }

    private void loadAllSuppliers() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("suppliers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LIST_ALL_SUPPLIERS.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    LIST_ALL_SUPPLIERS.add(snapshot.getValue(User.class));
                }
                AppliersAdapter appliersAdapter = new AppliersAdapter(AppliersListActivity.this,LIST_APPLIERS,LIST_ALL_SUPPLIERS,orderid);
                rvAppliers.setAdapter(appliersAdapter);
                pbAppliers.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadAppliers() {

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    for (DataSnapshot snapshot1:snapshot.child("orders").getChildren()){
                        if(snapshot1.getKey().equals(orderid)){
                            for (DataSnapshot snapshot2:snapshot1.child("appliedby").getChildren()){
                                LIST_APPLIERS.add(snapshot2.getValue().toString());
                            }
                        }
                    }
                }
                if (LIST_APPLIERS.isEmpty()){
                    tvNoAppliers.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
