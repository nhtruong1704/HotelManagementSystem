package com.xuancanhit.hotelmanagementsystem.ui.activities.customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.AdminMenuActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.fooditem.AdminFoodItemViewAllActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room.AdminRoomViewAllActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerMenuActivity extends AppCompatActivity {

    // Activity need back home menu
    public static final int CUSTOMER_UPDATE_ACTIVITY = 1;
    public static final int CUSTOMER_CHANGE_PASSWORD_ACTIVITY = 2;
    public static final int CUSTOMER_VIEW_PROFILE_ACTIVITY = 7;
    public static final int RESULT_CUSTOMER_CHANGE_PASSWORD_OK = 10;
    public static final int RESULT_CUSTOMER_VIEW_PROFILE_OK = 10;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;

    ImageView ivCusAvatar, ivCusNavHeader;
    TextView tvCusName, tvCusNavHeaderName, tvCusNavHeaderEmail, tvCusNavHeaderClass;
    Button btnCusViewProfile, btnMyBill, btnCusChangePassword, btnCusLogout, btnBookingRoom, btnOrderFood;
    ArrayList<Customer> customerArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);

        //Connect layout
        initUI();
        //Receive Data From Login
        receiveDataFromLogin();
        //Set on View
        initView();

        //Navigation Drawer
        navigationDrawer();

        //Logout Button
        btnCusLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //Button Update Profile
        btnMyBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CustomerMenuActivity.this, StudentUpdateActivity.class);
//                intent.putExtra("CUSTOMER_DATA_FROM_MENU_TO_UPDATE", customerArr);
//                startActivityForResult(intent, CUSTOMER_UPDATE_ACTIVITY);
            }
        });

        //Button Change Password
        btnCusChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CustomerMenuActivity.this, StudentChangePasswordActivity.class);
//                intent.putExtra("CUSTOMER_DATA_FROM_MENU_TO_CHANGE_PASSWORD", customerArr);
//                startActivityForResult(intent, CUSTOMER_CHANGE_PASSWORD_ACTIVITY);
            }
        });

        //Button View Profile
        btnCusViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerMenuActivity.this, CustomerViewProfileActivity.class);
                intent.putExtra("CUSTOMER_DATA_FROM_MENU_TO_VIEW_PROFILE", customerArr);
                startActivityForResult(intent, CUSTOMER_VIEW_PROFILE_ACTIVITY);
            }
        });

        //Button Booking Room
        btnBookingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(CustomerMenuActivity.this, CustomerRoomViewAllActivity.class));
//                Intent intent = new Intent(CustomerMenuActivity.this, StudentNoticeFromAdminActivity.class);
//                intent.putExtra("CUSTOMER_DATA_FROM_MENU_TO_NOTICE", customerArr);
//                startActivity(intent);
            }
        });

        //Button Order Food
        btnOrderFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerMenuActivity.this, CustomerFoodItemViewAllActivity.class));
//                Intent intent = new Intent(CustomerMenuActivity.this, StudentReportActivity.class);
//                intent.putExtra("CUSTOMER_DATA_FROM_MENU_TO_REPORT", customerArr);
//                startActivity(intent);
            }
        });

        //Nav
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Fragment fragment = null;
                switch (id) {
                    case R.id.it_stu_nav_dra_menu_dashboard:
//                        fragment = new DashBoardFragment();
//                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_update_profile:
//                        Intent intent = new Intent(CustomerMenuActivity.this, StudentUpdateActivity.class);
//                        intent.putExtra("CUSTOMER_DATA_FROM_MENU_TO_UPDATE", customerArr);
//                        startActivityForResult(intent, CUSTOMER_CHANGE_PASSWORD_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_notice:
//                        Intent intent1 = new Intent(CustomerMenuActivity.this, StudentNoticeFromAdminActivity.class);
//                        intent1.putExtra("CUSTOMER_DATA_FROM_MENU_TO_NOTICE", customerArr);
//                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_report:
//                        Intent intent2 = new Intent(CustomerMenuActivity.this, StudentReportActivity.class);
//                        intent2.putExtra("CUSTOMER_DATA_FROM_MENU_TO_REPORT", customerArr);
//                        startActivity(intent2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_stu_profile:
//                        Intent intent3 = new Intent(CustomerMenuActivity.this, StudentViewProfileActivity.class);
//                        intent3.putExtra("CUSTOMER_DATA_FROM_MENU_TO_VIEW_PROFILE", customerArr);
//                        startActivityForResult(intent3, CUSTOMER_VIEW_PROFILE_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_change_password:
//                        Intent intent4 = new Intent(CustomerMenuActivity.this, StudentChangePasswordActivity.class);
//                        intent4.putExtra("CUSTOMER_DATA_FROM_MENU_TO_CHANGE_PASSWORD", customerArr);
//                        startActivityForResult(intent4, CUSTOMER_CHANGE_PASSWORD_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_stu_nav_dra_menu_logout:
                        logout();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void navigationDrawer() {
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerMenuActivity.this);
        builder.setIcon(R.drawable.ic_baseline_logout_24);
        builder.setTitle("Logout");
        builder.setMessage(customerArr.get(0).getCusName() + ", are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CustomerMenuActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initView() {
        tvCusNavHeaderName.setText(customerArr.get(0).getCusName());
        tvCusNavHeaderEmail.setText(customerArr.get(0).getCusEmail());
        tvCusName.setText(customerArr.get(0).getCusName());

        if (!customerArr.get(0).getCusAvatar().equals("")) {
            Picasso.get()
                    .load(customerArr.get(0).getCusAvatar())
                    .placeholder(R.drawable.review)
                    .error(R.drawable.review)
                    .into(ivCusAvatar);
            Picasso.get()
                    .load(customerArr.get(0).getCusAvatar())
                    .placeholder(R.drawable.review)
                    .error(R.drawable.review)
                    .into(ivCusNavHeader);
        } else {
            if (!customerArr.get(0).getCusGender().equals("-1")) {
                if (customerArr.get(0).getCusGender().equals("1")) {
                    ivCusAvatar.setImageResource(R.drawable.male);
                    ivCusNavHeader.setImageResource(R.drawable.male);
                } else {
                    ivCusAvatar.setImageResource(R.drawable.female);
                    ivCusNavHeader.setImageResource(R.drawable.female);
                }
            } else {
                ivCusAvatar.setImageResource(R.drawable.review);
                ivCusNavHeader.setImageResource(R.drawable.review);
            }
        }

    }

    @Override
    protected void onResume() {
        navigationView.getMenu().findItem(R.id.it_stu_nav_dra_menu_dashboard).setChecked(true);
        super.onResume();
    }

    private void receiveDataFromLogin() {
        Intent intent = getIntent();
        customerArr = intent.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_LOGIN_TO_MENU");
    }

    private void initUI() {
        ivCusAvatar = findViewById(R.id.iv_cus_avt);
        tvCusName = findViewById(R.id.tv_cus_name);

        btnCusLogout = findViewById(R.id.btn_cus_logout);
        btnCusViewProfile = findViewById(R.id.btn_cus_view_profile);
        btnMyBill = findViewById(R.id.btn_cus_update_profile);
        btnCusChangePassword = findViewById(R.id.btn_cus_change_password);
        btnBookingRoom = findViewById(R.id.btn_cus_menu_notice);
        btnOrderFood = findViewById(R.id.btn_cus_menu_report);

        drawerLayout = findViewById(R.id.dl_student_drawer);
        toolbar = findViewById(R.id.tb_student_toolBar);
        navigationView = findViewById(R.id.nv_student);
        View hView = navigationView.getHeaderView(0);
        tvCusNavHeaderEmail = hView.findViewById(R.id.tv_stu_nav_header_email);
        tvCusNavHeaderName = hView.findViewById(R.id.tv_stu_nav_header_name);
        tvCusNavHeaderClass = hView.findViewById(R.id.tv_stu_nav_header_class);
        ivCusNavHeader = hView.findViewById(R.id.iv_stu_nav_header);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == CUSTOMER_UPDATE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                customerArr = data.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_UPDATE_TO_MENU");
                initView();
            }
        }
        //Change password
        if (resultCode == RESULT_CUSTOMER_CHANGE_PASSWORD_OK) {
            if (requestCode == CUSTOMER_CHANGE_PASSWORD_ACTIVITY) {
                customerArr = data.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_CHANGE_PASSWORD_TO_MENU");
            }
        }
        //View Profile(maybe don't need)
        if (resultCode == RESULT_CUSTOMER_VIEW_PROFILE_OK) {
            if (requestCode == CUSTOMER_VIEW_PROFILE_ACTIVITY) {
                customerArr = data.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_VIEW_PROFILE_TO_MENU");
                initView();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            logout();
        }

    }
}