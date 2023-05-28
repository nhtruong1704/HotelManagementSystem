package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Employee;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.employee.AdminEmployeeUpdateActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.employee.AdminEmployeeViewAllActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.employee.AdminEmployeeViewProfileActivity;

import java.util.ArrayList;

public class AdminEmployeeViewProfileActivity extends AppCompatActivity {

    private ImageView ivAdEmpViewProfileAvatar, ivAdEmpViewProfileExit;
    private TextView tvAdEmpViewProfileName, tvAdEmpViewProfilePosition, tvAdEmpViewProfileSalary, tvAdEmpViewProfilePhone;
    private Button btnAdEmpViewProfileUpdate, btnAdEmpViewProfileExit;

    ArrayList<Employee> employeeArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_view_profile);


        //Connect layout
        initUI();

        //Receive Data from View All
        receiveDataFromEmployeeAdapter();

        //Set on View
        initView();

        //Button Exit
        btnAdEmpViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivAdEmpViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnAdEmpViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEmployeeViewProfileActivity.this, AdminEmployeeUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("EMPLOYEE_DATA_ARRAY", employeeArr);
                bundle.putInt("EMPLOYEE_DATA_POSITION", position);
                intent.putExtra("EMPLOYEE_DATA_FROM_AD_EMPLOYEE_VIEW_PROFILE_TO_UPDATE", bundle);
                startActivity(intent);
                finish();
            }
        });
    }

//    @SuppressLint("SetTextI18n")
//    private void initView() {
//        if (employeeArr != null && position < employeeArr.size()) {
//            tvAdEmpViewProfileName.setText(employeeArr.get(position).getEmpName());
//            tvAdEmpViewProfilePosition.setText(employeeArr.get(position).getEmpPosition());
//            tvAdEmpViewProfileSalary.setText(employeeArr.get(position).getEmpSalary());
//            tvAdEmpViewProfilePhone.setText(employeeArr.get(position).getEmpPhone());
//
//            //String empAvatar = employeeArr.get(position).getEmpAvatar();
//            //if (empAvatar != null && !empAvatar.equals("")) {
//            if (!employeeArr.get(position).getEmpAvatar().equals("")) {
//                Picasso.get()
//                        .load(employeeArr.get(position).getEmpAvatar())
//                        .placeholder(R.drawable.admin)
//                        .error(R.drawable.admin)
//                        .into(ivAdEmpViewProfileAvatar);
//            } else {
//                ivAdEmpViewProfileAvatar.setImageResource(R.drawable.admin);
//            }
//        }
//    }


    @SuppressLint("SetTextI18n")
    private void initView() {
        if (employeeArr != null && position < employeeArr.size()) {
            tvAdEmpViewProfileName.setText(employeeArr.get(position).getEmpName());
            tvAdEmpViewProfilePosition.setText(employeeArr.get(position).getEmpPosition());
            tvAdEmpViewProfileSalary.setText(employeeArr.get(position).getEmpSalary());
            tvAdEmpViewProfilePhone.setText(employeeArr.get(position).getEmpPhone());

            String empAvatar = employeeArr.get(position).getEmpAvatar();
            if (empAvatar != null && !empAvatar.equals("")) {
                Picasso.get()
                        .load(empAvatar)
                        .placeholder(R.drawable.employees)
                        .error(R.drawable.employees)
                        .into(ivAdEmpViewProfileAvatar);
            } else {
                ivAdEmpViewProfileAvatar.setImageResource(R.drawable.employees);
            }
        }
    }




    private void receiveDataFromEmployeeAdapter() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("EMPLOYEE_DATA_FROM_EMPLOYEE_ADAPTER_TO_AD_EMPLOYEE_VIEW_PROFILE");
        if (bundle != null) {
            employeeArr = bundle.getParcelableArrayList("EMPLOYEE_DATA_ARRAY");
            position = bundle.getInt("EMPLOYEE_DATA_POSITION");
        }
    }

    private void initUI() {
        ivAdEmpViewProfileAvatar = findViewById(R.id.iv_ad_emp_view_profile_avt);
        ivAdEmpViewProfileExit = findViewById(R.id.iv_ad_emp_view_profile_exit);
        tvAdEmpViewProfileName = findViewById(R.id.tv_ad_emp_view_profile_name);
        tvAdEmpViewProfilePosition = findViewById(R.id.tv_ad_emp_view_profile_position);
        tvAdEmpViewProfileSalary = findViewById(R.id.tv_ad_emp_view_profile_salary);
        tvAdEmpViewProfilePhone = findViewById(R.id.tv_ad_emp_view_profile_phone);
        btnAdEmpViewProfileExit = findViewById(R.id.btn_ad_emp_view_profile_exit);
        btnAdEmpViewProfileUpdate = findViewById(R.id.btn_ad_emp_view_profile_update);
    }

    private void backToMenu() {
        Intent intent = new Intent(AdminEmployeeViewProfileActivity.this, AdminEmployeeViewAllActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}
