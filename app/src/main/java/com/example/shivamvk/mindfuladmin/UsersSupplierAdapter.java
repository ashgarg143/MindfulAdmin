package com.example.shivamvk.mindfuladmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UsersSupplierAdapter extends RecyclerView.Adapter<UsersSupplierAdapter.ViewHolder> {

    private List<User> list;
    private Context context;
    private String userType;

    public UsersSupplierAdapter(List<User> list, Context context, String userType) {
        this.list = list;
        this.context = context;
        this.userType = userType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final User user = list.get(i);

        viewHolder.tvUserItemName.setText(user.getName());
        viewHolder.tvUserItemEmail.setText(user.getEmail());
        viewHolder.tvUserItemNumber.setText(user.getNumber());

        if (user.getEmailverified().equals("No")){
            viewHolder.tvUserItemEmailVerified.setText("Email not veified");
            viewHolder.tvUserItemEmailVerified.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.tvUserItemEmailVerified.setTextColor(context.getResources().getColor(R.color.green));
            viewHolder.tvUserItemEmailVerified.setText("Email verified");
        }

        if (user.getNumberverified().equals("No")){
            viewHolder.tvUserItemNumberVerified.setText("Number not verified");
            viewHolder.tvUserItemNumberVerified.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.tvUserItemNumberVerified.setText("Number verified");
            viewHolder.tvUserItemNumberVerified.setTextColor(context.getResources().getColor(R.color.green));
        }

        viewHolder.cvUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context,UserOrdersActivity.class);
                intent.putExtra("usertype",userType);
                intent.putExtra("email",user.getEmail());
                intent.putExtra("name",user.getName());
                context.startActivity(intent);
                Activity activity = (Activity)context;
                activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUserItemName,tvUserItemEmail,tvUserItemNumber,tvUserItemEmailVerified,tvUserItemNumberVerified;
        private CardView cvUserItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserItemEmail = itemView.findViewById(R.id.tv_user_item_email);
            tvUserItemName = itemView.findViewById(R.id.tv_user_item_name);
            tvUserItemNumber = itemView.findViewById(R.id.tv_user_item_number);
            tvUserItemEmailVerified = itemView.findViewById(R.id.tv_user_item_email_verified);
            tvUserItemNumberVerified = itemView.findViewById(R.id.tv_user_item_number_verified);
            cvUserItem = itemView.findViewById(R.id.cv_user_item_layout);
        }
    }
}
