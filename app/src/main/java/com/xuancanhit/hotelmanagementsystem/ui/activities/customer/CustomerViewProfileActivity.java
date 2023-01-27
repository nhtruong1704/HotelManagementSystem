package com.xuancanhit.hotelmanagementsystem.ui.activities.customer;

import androidx.annotation.Nullable;
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
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;

import java.util.ArrayList;

public class CustomerViewProfileActivity extends AppCompatActivity {

    public static final int CUSTOMER_UPDATE = 1;
    public static final int RESULT_UPDATE_OK = 2;

    private ImageView ivCusViewProfileAvatar, ivCusViewProfileExit, ivCusViewProfileVip;
    private TextView tvCusViewProfileAddress, tvCusViewProfileName, tvCusViewProfileDOB, tvCusViewProfilePhone, tvCusViewProfileEmail, tvCusViewProfileGender;
    private Button btnCusViewProfileUpdate, btnCusViewProfileExit;

    ArrayList<Customer> customerArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_profile);

        initUI();

        //Receive Data from student menu
        receiveDataFromMenu();

        //Set on View
        initView();


        //Button Exit
        btnCusViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivCusViewProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnCusViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CustomerViewProfileActivity.this, CustomerUpdateActivity.class);
//                //Replace CUSTOMER_DATA_FROM_MENU_TO_UPDATE for CUSTOMER_DATA_FROM_VIEW_PROFILE_TO_UPDATE
//                //Update just receive 1 time
//                intent.putExtra("CUSTOMER_DATA_FROM_MENU_TO_UPDATE", customerArr);
//                startActivityForResult(intent, CUSTOMER_UPDATE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        //Replace CUSTOMER_DATA_FROM_MENU_TO_UPDATE for CUSTOMER_DATA_FROM_VIEW_PROFILE_TO_UPDATE
        //Update just receive 1 time
        if (requestCode == CustomerMenuActivity.CUSTOMER_UPDATE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                customerArr = data.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_UPDATE_TO_MENU");
                initView();
            }
        }
    }

    private void backToMenu() {
        Intent intent = getIntent();
        intent.putExtra("CUSTOMER_DATA_FROM_VIEW_PROFILE_TO_MENU", customerArr);
        setResult(CustomerMenuActivity.RESULT_CUSTOMER_VIEW_PROFILE_OK, intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvCusViewProfileName.setText(customerArr.get(0).getCusName());
        tvCusViewProfileAddress.setText(customerArr.get(0).getCusAddress());
        tvCusViewProfileDOB.setText(customerArr.get(0).getCusDOB());
        tvCusViewProfilePhone.setText(customerArr.get(0).getCusPhone());
        tvCusViewProfileEmail.setText(customerArr.get(0).getCusEmail());
        if (customerArr.get(0).getCusGender().equals("1")) {
            tvCusViewProfileGender.setText("Male");
        } else if (customerArr.get(0).getCusGender().equals("0")) {
            tvCusViewProfileGender.setText("Female");
        }

        if (customerArr.get(0).getCusIsVip().equals("1")) {
            ivCusViewProfileVip.setImageResource(R.drawable.vip_card);
        }
        else
            ivCusViewProfileVip.setImageResource(R.drawable.transparent);

        if (!customerArr.get(0).getCusAvatar().equals("")) {
            Picasso.get()
                    .load(customerArr.get(0).getCusAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivCusViewProfileAvatar);
        } else {
            if (!customerArr.get(0).getCusGender().equals("-1")) {
                if (customerArr.get(0).getCusGender().equals("1")) {
                    ivCusViewProfileAvatar.setImageResource(R.drawable.male);
                } else {
                    ivCusViewProfileAvatar.setImageResource(R.drawable.female);
                }
            } else {
                ivCusViewProfileAvatar.setImageResource(R.drawable.review);
            }
        }
    }

    private void receiveDataFromMenu() {
        Intent intent = getIntent();
        customerArr = intent.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_MENU_TO_VIEW_PROFILE");
    }

    private void initUI() {
        ivCusViewProfileAvatar = findViewById(R.id.iv_cus_view_profile_avt);
        ivCusViewProfileExit = findViewById(R.id.iv_cus_view_profile_exit);
        ivCusViewProfileVip = findViewById(R.id.iv_cus_view_profile_vip);
        tvCusViewProfileName = findViewById(R.id.tv_cus_view_profile_name);
        tvCusViewProfileAddress = findViewById(R.id.tv_cus_view_profile_address);
        tvCusViewProfileDOB = findViewById(R.id.tv_cus_view_profile_dob);
        tvCusViewProfilePhone = findViewById(R.id.tv_cus_view_profile_phone);
        tvCusViewProfileEmail = findViewById(R.id.tv_cus_view_profile_email);
        tvCusViewProfileGender = findViewById(R.id.tv_cus_view_profile_gender);
        btnCusViewProfileExit = findViewById(R.id.btn_cus_view_profile_exit);
        btnCusViewProfileUpdate = findViewById(R.id.btn_cus_view_profile_update);
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
    
}