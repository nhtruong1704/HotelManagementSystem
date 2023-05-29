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
import com.xuancanhit.hotelmanagementsystem.presentation.model.Employee;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.employee.AdminEmployeeViewProfileActivity;
import com.xuancanhit.hotelmanagementsystem.ui.interfaces.ItemClickListener;

import java.util.ArrayList;

//public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {
//    //Form for adapter
//    Context context;
//    ArrayList<Employee> employeeArr;
//
//    public EmployeeListAdapter(Context context, ArrayList<Employee> employeeArr) {
//        this.context = context;
//        this.employeeArr = employeeArr;
//    }
//
//    @NonNull
//    @Override
//    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_employee, parent, false);
//        return new EmployeeViewHolder(itemView);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
//        Employee employee = employeeArr.get(position);
//
//        String employeeName = employee.getEmpName();
//        String employeePosition = employee.getEmpPosition();
//
//
//
//
//
//if (employee.getEmpAvatar() == null || employee.getEmpAvatar().equals("")) {
//        holder.ivEmpAvt.setImageResource(R.drawable.admin);
//        } else {
//        Picasso.get()
//        .load(employee.getEmpAvatar())
//        .placeholder(R.drawable.admin)
//        .error(R.drawable.admin)
//        .into(holder.ivEmpAvt);
//        }
//
//       // holder.ivEmpAvt.setImageResource(R.drawable.admin);
//
//        holder.tvEmpName.setText(employeeName);
//        holder.tvEmpPosition.setText(employeePosition);
//
//        //Click for RecycleView
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                if (isLongClick) {
//                    Toast.makeText(context, "Employee " + employee.getEmpName(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(view.getContext(), AdminEmployeeViewProfileActivity.class);
//
//                    Bundle bundle = new Bundle();
//
//                    bundle.putParcelableArrayList("EMPLOYEE_DATA_ARRAY", employeeArr);
//                    bundle.putInt("EMPLOYEE_DATA_POSITION", position);
//                    intent.putExtra("EMPLOYEE_DATA_FROM_EMPLOYEE_ADAPTER_TO_AD_EMPLOYEE_VIEW_PROFILE", bundle);
//                    view.getContext().startActivity(intent);
//                    ((Activity) view.getContext()).finish();
//                }
//            }
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return employeeArr == null ? 0 : employeeArr.size();
//    }
//
//
//    //Data ViewHolder class
//    public static class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
//        ImageView ivEmpAvt;
//        TextView tvEmpName, tvEmpPosition;
//
//        ItemClickListener itemClickListener;
//
//        public EmployeeViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvEmpName = itemView.findViewById(R.id.tv_emp_name);
//            tvEmpPosition = itemView.findViewById(R.id.tv_emp_position);
//            ivEmpAvt = itemView.findViewById(R.id.iv_emp_avt);
//
//
//            //Turn On Click for RecycleView
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
//        }
//
//        public void setItemClickListener(ItemClickListener itemClickListener) {
//            this.itemClickListener = itemClickListener;
//        }
//
//        //onClick for RecycleView
//        @Override
//        public void onClick(View v) {
//            itemClickListener.onClick(v, getAdapterPosition(), false);
//        }
//
//        //onLongClick for RecycleView
//        @Override
//        public boolean onLongClick(View v) {
//            itemClickListener.onClick(v, getAdapterPosition(), true);
//            return true;
//            //return false; // if not use
//        }
//    }
//}
//






public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {
    Context context;
    ArrayList<Employee> employeeArr;

    public EmployeeListAdapter(Context context, ArrayList<Employee> employeeArr) {
        this.context = context;
        this.employeeArr = employeeArr;
    }

    @NonNull
    @Override
    public EmployeeListAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_employee, parent, false);
        return new EmployeeListAdapter.EmployeeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListAdapter.EmployeeViewHolder holder, int position) {
        Employee employee = employeeArr.get(position);

        String employeeName = employee.getEmpName();
        String employeePosition = employee.getEmpPosition();

        if (employee.getEmpAvatar() == null || employee.getEmpAvatar().equals("")) {
            holder.ivEmpAvt.setImageResource(R.drawable.employees);
        } else {
            Picasso.get()
                    .load(employee.getEmpAvatar())
                    .placeholder(R.drawable.employees)
                    .error(R.drawable.employees)
                    .into(holder.ivEmpAvt);
        }




        holder.tvEmpName.setText(employeeName);
        holder.tvEmpPosition.setText(employeePosition);




        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Employee " + employee.getEmpName(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), AdminEmployeeViewProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("EMPLOYEE_DATA_ARRAY", employeeArr);
                    bundle.putInt("EMPLOYEE_DATA_POSITION", position);
                    intent.putExtra("EMPLOYEE_DATA_FROM_EMPLOYEE_ADAPTER_TO_AD_EMPLOYEE_VIEW_PROFILE", bundle);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeArr == null ? 0 : employeeArr.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivEmpAvt;
        TextView tvEmpName, tvEmpPosition;

        ItemClickListener itemClickListener;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName = itemView.findViewById(R.id.tv_emp_name);
            tvEmpPosition = itemView.findViewById(R.id.tv_emp_position);
            ivEmpAvt = itemView.findViewById(R.id.iv_emp_avt);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }
}
