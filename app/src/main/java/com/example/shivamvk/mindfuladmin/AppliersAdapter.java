package com.example.shivamvk.mindfuladmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AppliersAdapter extends RecyclerView.Adapter<AppliersAdapter.ViewHolder>{

    List<String> LIST_APPLIERS;
    List<User> LIST_ALL_SUPPLIERS;
    Context context;
    String orderid;

    public AppliersAdapter(Context context,List<String> LIST_APPLIERS, List<User> LIST_ALL_SUPPLIERS,String orderid) {
        this.context=context;
        this.LIST_APPLIERS = LIST_APPLIERS;
        this.LIST_ALL_SUPPLIERS = LIST_ALL_SUPPLIERS;
        this.orderid = orderid;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.applier_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        User user = LIST_ALL_SUPPLIERS.get(i);

        String name = user.getName();
        final String email = user.getEmail();
        final String number = user.getNumber();
        String emailverified = user.getEmailverified();
        String numberverified = user.getNumberverified();

        if(!LIST_APPLIERS.contains(number)){
            viewHolder.llApplier.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) viewHolder.cvApplier.getLayoutParams();
            layoutParams.setMargins(12, 0, 12, 0);
            viewHolder.cvApplier.requestLayout();
        } else {
            viewHolder.tvApplierName.setText(name);
            viewHolder.tvApplierEmail.setText(email);
            viewHolder.tvApplierNumber.setText(number);
            if (emailverified.equals("Yes")){
                viewHolder.tvApplierEmailVerified.setText("Email verified");
                viewHolder.tvApplierEmailVerified.setTextColor(context.getResources().getColor(R.color.green));
            } else {
                viewHolder.tvApplierEmailVerified.setText("Email not verified");
                viewHolder.tvApplierEmailVerified.setTextColor(context.getResources().getColor(R.color.red));
            }
            if (numberverified.equals("Yes")){
                viewHolder.tvApplierNumberVerified.setText("Number verified");
                viewHolder.tvApplierNumberVerified.setTextColor(context.getResources().getColor(R.color.green));
            } else {
                viewHolder.tvApplierNumberVerified.setText("Number not verified");
                viewHolder.tvApplierNumberVerified.setTextColor(context.getResources().getColor(R.color.red));
            }
        }

        viewHolder.btApplierAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setMessage("By approving order "+orderid+" for "+email+", the request for all others will be rejected.");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("users");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    for (DataSnapshot snapshot1:snapshot.child("orders").getChildren()){
                                        if (snapshot1.getKey().equals(orderid)){
                                            snapshot1.child("completed").getRef().setValue("Yes");
                                            snapshot1.child("appliedby").getRef().removeValue();
                                            snapshot1.child("supplier").getRef().setValue(number);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Please check your internet and try again!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                .getReference("suppliers");
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    for(DataSnapshot snapshot1: snapshot.child("appliedfor").getChildren()){
                                        if(snapshot1.getKey().equals(orderid)){
                                            if (snapshot.getKey().equals(number)){
                                                snapshot1.child("completed").getRef().setValue("Accepted");
                                            } else {
                                                snapshot1.child("completed").getRef().setValue("Yes");
                                            }
                                        }
                                    }
                                }
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
                            public void onCancelled(DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Please check your internet and try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        progressDialog.dismiss();
                        context.startActivity(new Intent(context, MainActivity.class));
                        Activity activity = (Activity) context;
                        activity.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                        Toast.makeText(activity, "Order approved!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return LIST_ALL_SUPPLIERS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvApplierName,tvApplierEmail,tvApplierNumber,tvApplierEmailVerified,tvApplierNumberVerified;
        private CardView cvApplier;
        private LinearLayout llApplier;
        private Button btApplierAccept;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvApplierName = itemView.findViewById(R.id.tv_applier_name);
            tvApplierEmail = itemView.findViewById(R.id.tv_applier_email);
            tvApplierNumber = itemView.findViewById(R.id.tv_applier_number);
            tvApplierEmailVerified = itemView.findViewById(R.id.tv_applier_email_verified);
            tvApplierNumberVerified = itemView.findViewById(R.id.tv_applier_number_verified);
            cvApplier = itemView.findViewById(R.id.cv_applier);
            llApplier = itemView.findViewById(R.id.ll_applier);
            btApplierAccept = itemView.findViewById(R.id.bt_applier_accept);
        }
    }
}
