package com.example.shivamvk.mindfuladmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderList;
    private Context context;
    private String string="";

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public OrderAdapter(List<Order> orderList, Context context,String string) {
        this.orderList = orderList;
        this.context = context;
        this.string = string;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Order order = orderList.get(i);

        final String orderid = generateHash(
                order.getLoadingPoint(),
                order.getTripDestination(),
                order.getTruckType(),
                order.getMaterialType(),
                order.getLoadingDate()
        );

        String loadingPoint = order.getLoadingPoint();
        String destinationPoint = order.getTripDestination();
        String trucktype = order.getTruckType();

        char chartruck = trucktype.charAt(0);

        switch (chartruck){
            case 'O':
                viewHolder.ivOrderItemTruckType.setImageResource(R.drawable.open);
                break;
            case 'C':
                viewHolder.ivOrderItemTruckType.setImageResource(R.drawable.container);
                break;
            case 'T':
                viewHolder.ivOrderItemTruckType.setImageResource(R.drawable.trailer);
        }

        if(loadingPoint.length() > 27){
            char loading[] = loadingPoint.toCharArray();
            loading[24] = '.';
            loading[25] = '.';
            loading[26] = '.';
            loadingPoint = String.valueOf(loading);
        }

        if (destinationPoint.length() >27){
            char destination[] = destinationPoint.toCharArray();
            destination[24] = '.';
            destination[25] = '.';
            destination[26] = '.';
            destinationPoint = String.valueOf(destination);
        }

        viewHolder.tvOrderItemOrderId.setText("Order id: " + orderid);

        viewHolder.tvOrderItemLoadingPoint.setText(loadingPoint);
        viewHolder.tvOrderItemTripDestination.setText(destinationPoint);
        viewHolder.tvOrderItemTruckType.setText(order.getTruckType());
        viewHolder.tvOrderItemMaterialType.setText(order.getMaterialType());
        viewHolder.tvOrderItemLoadingTime.setText(order.getLoadingDate());
        viewHolder.tvOrderItemRemarks.setText(order.getRemarks());

        if(string.equals("user")){
            if (order.getCompleted().equals("No")){
                viewHolder.tvOrderItemStatus.setText("Pending");
                viewHolder.tvOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.ivOrderItemMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(context, viewHolder.ivOrderItemMenu);
                        popupMenu.inflate(R.menu.order_item_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.menu_order_see_appliers:
                                        Intent intent = new Intent(context, AppliersListActivity.class);
                                        intent.putExtra("orderid", orderid);
                                        context.startActivity(intent);
                                        Activity activity = (Activity) context;
                                        activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                        return true;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            } else {
                viewHolder.ivOrderItemMenu.setVisibility(View.GONE);
                viewHolder.tvOrderItemStatus.setText("Completed (Click to see details)");
                viewHolder.tvOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
        } else if(string.equals("supplier")){
            if (order.getCompleted().equals("No")){
                viewHolder.tvOrderItemStatus.setText("Pending");
                viewHolder.tvOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.ivOrderItemMenu.setVisibility(View.GONE);
            } else if(order.getCompleted().equals("Accepted")) {
                viewHolder.ivOrderItemMenu.setVisibility(View.GONE);
                viewHolder.tvOrderItemStatus.setText("Completed (Click to see details)");
                viewHolder.tvOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.green));
            } else {
                viewHolder.ivOrderItemMenu.setVisibility(View.GONE);
                viewHolder.tvOrderItemStatus.setText("Rejected)");
                viewHolder.tvOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        } else {
            if (order.getCompleted().equals("No")){
                viewHolder.tvOrderItemStatus.setText("Pending");
                viewHolder.tvOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.ivOrderItemMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(context, viewHolder.ivOrderItemMenu);
                        popupMenu.inflate(R.menu.order_item_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.menu_order_see_appliers:
                                        Intent intent = new Intent(context, AppliersListActivity.class);
                                        intent.putExtra("orderid", orderid);
                                        context.startActivity(intent);
                                        Activity activity = (Activity) context;
                                        activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                        return true;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            } else {
                viewHolder.ivOrderItemMenu.setVisibility(View.GONE);
                viewHolder.tvOrderItemStatus.setText("Completed (Click to see details)");
                viewHolder.tvOrderItemStatus.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
        }

    }

    private String generateHash(String s, String s1, String s2, String s3, String s4) {
        int hash = 21;
        String main = s + s1 + s2 + s3 + s4;
        for (int i = 0; i < main.length(); i++) {
            hash = hash*31 + main.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvOrderItemLoadingPoint,tvOrderItemTripDestination,tvOrderItemTruckType,tvOrderItemMaterialType,tvOrderItemLoadingTime,
                tvOrderItemOrderId,tvOrderItemRemarks,tvOrderItemStatus,ivOrderItemMenu;

        private ImageView ivOrderItemTruckType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderItemLoadingPoint = itemView.findViewById(R.id.tv_order_item_loading_point);
            tvOrderItemTripDestination = itemView.findViewById(R.id.tv_order_item_trip_destination);
            tvOrderItemTruckType = itemView.findViewById(R.id.tv_order_item_truck_type);
            tvOrderItemMaterialType = itemView.findViewById(R.id.tv_order_item_material_type);
            tvOrderItemLoadingTime = itemView.findViewById(R.id.tv_order_item_loading_time);
            tvOrderItemOrderId = itemView.findViewById(R.id.tv_order_item_order_id);
            tvOrderItemRemarks = itemView.findViewById(R.id.tv_order_item_remarks);
            tvOrderItemStatus = itemView.findViewById(R.id.tv_order_item_status);

            ivOrderItemMenu = itemView.findViewById(R.id.iv_order_item_menu);

            ivOrderItemTruckType = itemView.findViewById(R.id.iv_order_item_truck_type);
        }
    }
}
