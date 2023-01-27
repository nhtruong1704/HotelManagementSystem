package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer;

import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminCustomerViewProfileActivity extends AppCompatActivity {
    private ImageView ivAdCusViewProfileAvatar, ivAdCusViewProfileExit, ivAdCusViewProfileVip;
    private TextView tvAdCusViewProfileAddress, tvAdCusViewProfileName, tvAdCusViewProfileDOB, tvAdCusViewProfilePhone, tvAdCusViewProfileEmail, tvAdCusViewProfileGender;
    private Button btnAdCusViewProfileUpdate, btnAdCusViewProfileExit;

    ArrayList<Customer> customerArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_view_profile);


        //Connect layout
        initUI();

        //Receive Data from View All
        receiveDataFromCustomerAdapter();

        //Set on View
        initView();

        //Button Exit
        btnAdCusViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivAdCusViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnAdCusViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCustomerViewProfileActivity.this, AdminCustomerUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", customerArr);
                bundle.putInt("STUDENT_DATA_POSITION", position);
                intent.putExtra("STUDENT_DATA_FROM_AD_STU_VIEW_PROFILE_TO_UPDATE", bundle);
                startActivity(intent);
                finish();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void initView() {
        tvAdCusViewProfileName.setText(customerArr.get(position).getCusName());
        tvAdCusViewProfileAddress.setText(customerArr.get(position).getCusAddress());
        tvAdCusViewProfileDOB.setText(customerArr.get(position).getCusDOB());
        tvAdCusViewProfilePhone.setText(customerArr.get(position).getCusPhone());
        tvAdCusViewProfileEmail.setText(customerArr.get(position).getCusEmail());
        if (customerArr.get(position).getCusGender().equals("1")) {
            tvAdCusViewProfileGender.setText("Male");
        } else if (customerArr.get(position).getCusGender().equals("0")) {
            tvAdCusViewProfileGender.setText("Female");
        }

        if (customerArr.get(position).getCusIsVip().equals("1")) {
            ivAdCusViewProfileVip.setImageResource(R.drawable.vip_card);
        }
        else
            ivAdCusViewProfileVip.setImageResource(R.drawable.transparent);

        if (!customerArr.get(position).getCusAvatar().equals("")) {
            Picasso.get()
                    .load(customerArr.get(position).getCusAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdCusViewProfileAvatar);
        } else {
            if (!customerArr.get(position).getCusGender().equals("-1")) {
                if (customerArr.get(position).getCusGender().equals("1")) {
                    ivAdCusViewProfileAvatar.setImageResource(R.drawable.male);
                } else {
                    ivAdCusViewProfileAvatar.setImageResource(R.drawable.female);
                }
            } else {
                ivAdCusViewProfileAvatar.setImageResource(R.drawable.review);
            }
        }
    }

    private void receiveDataFromCustomerAdapter() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE");
        if (bundle != null) {
            customerArr = bundle.getParcelableArrayList("STUDENT_DATA_ARRAY");
            position = bundle.getInt("STUDENT_DATA_POSITION");
        }
    }

    private void initUI() {
        ivAdCusViewProfileAvatar = findViewById(R.id.iv_ad_cus_view_profile_avt);
        ivAdCusViewProfileExit = findViewById(R.id.iv_ad_stu_view_profile_exit);
        ivAdCusViewProfileVip = findViewById(R.id.iv_ad_cus_view_profile_vip);
        tvAdCusViewProfileName = findViewById(R.id.tv_ad_cus_view_profile_name);
        tvAdCusViewProfileAddress = findViewById(R.id.tv_ad_cus_view_profile_address);
        tvAdCusViewProfileDOB = findViewById(R.id.tv_ad_cus_view_profile_dob);
        tvAdCusViewProfilePhone = findViewById(R.id.tv_ad_cus_view_profile_phone);
        tvAdCusViewProfileEmail = findViewById(R.id.tv_ad_cus_view_profile_email);
        tvAdCusViewProfileGender = findViewById(R.id.tv_ad_stu_view_profile_gender);
        btnAdCusViewProfileExit = findViewById(R.id.btn_ad_cus_view_profile_exit);
        btnAdCusViewProfileUpdate = findViewById(R.id.btn_ad_cus_view_profile_update);
    }

    private void backToMenu() {
        Intent intent = new Intent(AdminCustomerViewProfileActivity.this, AdminCustomerViewAllActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}