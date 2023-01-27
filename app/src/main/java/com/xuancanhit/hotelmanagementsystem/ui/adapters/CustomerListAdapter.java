package com.xuancanhit.hotelmanagementsystem.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuancanhit.hotelmanagementsystem.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer.AdminCustomerViewProfileActivity;
import com.xuancanhit.hotelmanagementsystem.ui.interfaces.ItemClickListener;

import java.util.ArrayList;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder> {
    //Form for adapter
    Context context;
    ArrayList<Customer> customerArr;

    public CustomerListAdapter(Context context, ArrayList<Customer> customerArr) {
        this.context = context;
        this.customerArr = customerArr;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false);
        return new CustomerViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerArr.get(position);

        String customerName = customer.getCusName();
        String customerAddress = customer.getCusAddress();


        if (!customer.getCusAvatar().equals("")) {
            Picasso.get()
                    .load(customer.getCusAvatar())
                    .placeholder(R.drawable.review)
                    .error(R.drawable.review)
                    .into(holder.ivCusAvt);
        } else {
            if (!customer.getCusGender().equals("-1")) {
                if (customer.getCusGender().equals("1")) {
                    holder.ivCusAvt.setImageResource(R.drawable.male);
                } else {
                    holder.ivCusAvt.setImageResource(R.drawable.female);
                }
            } else {
                holder.ivCusAvt.setImageResource(R.drawable.review);
            }
        }

        if(customer.getCusIsVip().equals("1")) {
            holder.ivCusVip.setImageResource(R.drawable.vip_card);
        }
        else {
            holder.ivCusVip.setImageResource(R.drawable.transparent);
        }
        


        holder.tvCusName.setText(customerName);
        holder.tvCusAddress.setText(customerAddress);

        //Click for RecycleView
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Customer " + customer.getCusName(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), AdminCustomerViewProfileActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", customerArr);
                    bundle.putInt("STUDENT_DATA_POSITION", position);
                    intent.putExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE", bundle);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return customerArr == null ? 0 : customerArr.size();
    }


    //Data ViewHolder class
    public static class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivCusAvt, ivCusVip;
        TextView tvCusName, tvCusAddress;

        ItemClickListener itemClickListener;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCusName = itemView.findViewById(R.id.tv_stu_name);
            tvCusAddress = itemView.findViewById(R.id.tv_cus_address);
            ivCusAvt = itemView.findViewById(R.id.iv_cus_avt);
            ivCusVip = itemView.findViewById(R.id.iv_cus_vip);

            //Turn On Click for RecycleView
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        //onClick for RecycleView
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        //onLongClick for RecycleView
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
            //return false; // if not use
        }
    }
}
