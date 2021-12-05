package com.xuancanhit.hotelmanagementsystem.ui.activities.admin;

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
import com.xuancanhit.hotelmanagementsystem.presentation.model.Admin;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer.AdminCustomerAddActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer.AdminCustomerViewAllActivity;
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

public class AdminMenuActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;

    Button viewListRoom, viewCustomer, noticeCustomer, reportCustomer, btnHomeMenuLogout, btnAdminEdit;
    ImageView ivAdminAvt, ivAdNavHeader;
    TextView tvAdminName, tvAdNavHeaderName, tvAdNavHeaderEmail;
    ArrayList<Admin> adminArr;

    // Activity need back home menu
    public static final int ADMIN_UPDATE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        // Connect Layout
        initUI();
        //Receive Data From Login
        receiveDataFromLogin();
        // Set on View
        initView();
        //Navigation Drawer
        navigationDrawer();


        //Logout Button
        btnHomeMenuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //View All Button
        viewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuActivity.this, AdminCustomerViewAllActivity.class));
            }
        });

        //Add Customer Button
        viewListRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuActivity.this, AdminRoomViewAllActivity.class));
            }
        });

        //Button Notice
        noticeCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuActivity.this, AdminFoodItemViewAllActivity.class));
            }
        });

        //Button Report
        reportCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AdminMenuActivity.this, AdminCustomerReportActivity.class));
            }
        });

        //Button Edit
        btnAdminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminUpdateActivity.class);
                intent.putExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE", adminArr);
                startActivityForResult(intent, ADMIN_UPDATE_ACTIVITY);
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
                    case R.id.it_ad_nav_dra_menu_dashboard:
//                        fragment = new DashBoardFragment();
//                        loadFragment(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_add_customer:
                        startActivity(new Intent(AdminMenuActivity.this, AdminCustomerAddActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_view_all_customer:
                        startActivity(new Intent(AdminMenuActivity.this, AdminCustomerViewAllActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_notice:
                        startActivity(new Intent(AdminMenuActivity.this, AdminFoodItemViewAllActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_report:
//                        startActivity(new Intent(AdminMenuActivity.this, AdminCustomerReportActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_admin_profile:
                        Intent intent = new Intent(AdminMenuActivity.this, AdminUpdateActivity.class);
                        intent.putExtra("ADMIN_DATA_FROM_MENU_TO_UPDATE", adminArr);
                        startActivityForResult(intent, ADMIN_UPDATE_ACTIVITY);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_change_password:
                        Intent intent1 = new Intent(AdminMenuActivity.this, AdminChangePasswordActivity.class);
                        //Replace
                        intent1.putExtra("ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD", adminArr);
                        startActivityForResult(intent1, RESULT_OK);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.it_ad_nav_dra_menu_logout:
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



    //Receive Data From Login
    private void receiveDataFromLogin() {
        Intent intent = getIntent();
        adminArr = intent.getParcelableArrayListExtra("ADMIN_DATA_FROM_LOGIN_TO_MENU");
    }

    // Connect Layout
    public void initUI() {
        btnAdminEdit = findViewById(R.id.btn_admin_edit);
        ivAdminAvt = findViewById(R.id.iv_admin_avt);
        tvAdminName = findViewById(R.id.tv_admin_name);
        btnHomeMenuLogout = findViewById(R.id.btn_home_menu_logout);
        viewListRoom = findViewById(R.id.btn_admin_menu_list_room);
        viewCustomer = findViewById(R.id.btn_student_view_all);
        noticeCustomer = findViewById(R.id.btn_student_notice);
        reportCustomer = findViewById(R.id.btn_student_report);
        drawerLayout = findViewById(R.id.dl_admin_drawer);
        toolbar = findViewById(R.id.tb_admin_toolBar);
        navigationView = findViewById(R.id.nv_admin);
        View hView = navigationView.getHeaderView(0);
        tvAdNavHeaderEmail = hView.findViewById(R.id.tv_admin_nav_header_email);
        tvAdNavHeaderName = hView.findViewById(R.id.tv_admin_nav_header_name);
        ivAdNavHeader = hView.findViewById(R.id.iv_ad_nav_header);
    }

    //Set on View
    private void initView() {
        tvAdminName.setText(adminArr.get(0).getAdName());
        tvAdNavHeaderName.setText(adminArr.get(0).getAdName());
        tvAdNavHeaderEmail.setText(adminArr.get(0).getAdEmail());
        if (!adminArr.get(0).getAdAvatar().equals("")) {
            Picasso.get()
                    .load(adminArr.get(0).getAdAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdminAvt);
            Picasso.get()
                    .load(adminArr.get(0).getAdAvatar())
                    .placeholder(R.drawable.admin)
                    .error(R.drawable.admin)
                    .into(ivAdNavHeader);
        }

    }

    @Override
    protected void onResume() {
        navigationView.getMenu().findItem(R.id.it_ad_nav_dra_menu_dashboard).setChecked(true);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADMIN_UPDATE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                adminArr = data.getParcelableArrayListExtra("ADMIN_DATA_FROM_UPDATE_TO_MENU");
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

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMenuActivity.this);
        builder.setIcon(R.drawable.ic_baseline_logout_24);
        builder.setTitle("Logout");
        builder.setMessage(adminArr.get(0).getAdName() + ", are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminLoginActivity.class);
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
}