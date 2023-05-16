package com.xuancanhit.hotelmanagementsystem.ui.activities.customer;

import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Admin;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerChangePasswordActivity extends AppCompatActivity {

    EditText edtCustomerChangePasswordCurrentPassword, edtCustomerChangePasswordNewPassword, edtCustomerChangePasswordRetypeNewPassword;
    Button btnCustomerChangePasswordSave, btnCustomerChangePasswordExit;
    ImageView ivCustomerChangePasswordExit;

    ArrayList<Customer> customerArr;
    String currentPassword, newPassword, retypeNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_change_password);

        //Connect layout
        initUI();

        // Receive data from AdminUpdateActivity
        receiveDataFromUpdate();

        //Button Exit
        btnCustomerChangePasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToUpdate();
            }
        });

        //ImageView Back
        ivCustomerChangePasswordExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToUpdate();
            }
        });

        //Button Save
        btnCustomerChangePasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = edtCustomerChangePasswordCurrentPassword.getText().toString();
                newPassword = edtCustomerChangePasswordNewPassword.getText().toString();
                retypeNewPassword = edtCustomerChangePasswordRetypeNewPassword.getText().toString();
                if (currentPassword.length() > 0 && newPassword.length() > 0 && retypeNewPassword.length() > 0) {
                    if (currentPassword.equals(customerArr.get(0).getCusPassword()) && newPassword.equals(retypeNewPassword)) {

                        DataClient checkData = APIUtils.getData();
                        Call<String> callback = checkData.ChangePasswordAdminData(customerArr.get(0).getCusId(), newPassword);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String res = response.body();
                                if (res.trim().equals("CUSTOMER_CHANGE_PASSWORD_SUCCESSFUL")) {
                                    Toast.makeText(CustomerChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                    //Update password and Sent Data to Update
                                    customerArr.get(0).setCusPassword(newPassword);
                                    backToUpdate();
                                } else if (res.trim().equals("CUSTOMER_CHANGE_PASSWORD_FAILED")) {
                                    Toast.makeText(CustomerChangePasswordActivity.this, "Customer Email Or ID Is Incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CustomerChangePasswordActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("Wrong:", t.getMessage());
                            }
                        });
                    } else if (currentPassword.equals(customerArr.get(0).getCusPassword()) && !newPassword.equals(retypeNewPassword)) {
                        Toast.makeText(CustomerChangePasswordActivity.this, "New passwords and retype new passwords are not the same", Toast.LENGTH_SHORT).show();
                    } else if (!currentPassword.equals(customerArr.get(0).getCusPassword()) && newPassword.equals(retypeNewPassword)) {
                        Toast.makeText(CustomerChangePasswordActivity.this, "The current password Incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CustomerChangePasswordActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CustomerChangePasswordActivity.this, "Please enter complete information!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //Receive data from ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD
    private void receiveDataFromUpdate() {
        Intent intent = getIntent();
        customerArr = intent.getParcelableArrayListExtra("ADMIN_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD");
    }

    private void initUI() {
        edtCustomerChangePasswordCurrentPassword = findViewById(R.id.edt_customer_change_password_current_password);
        edtCustomerChangePasswordNewPassword = findViewById(R.id.edt_customer_change_password_new_password);
        edtCustomerChangePasswordRetypeNewPassword = findViewById(R.id.edt_customer_change_password_retype_new_password);
        btnCustomerChangePasswordSave = findViewById(R.id.btn_customer_change_password_save);
        btnCustomerChangePasswordExit = findViewById(R.id.btn_customer_change_password_exit);
        ivCustomerChangePasswordExit = findViewById(R.id.iv_customer_change_password_exit);
    }

    @Override
    public void onBackPressed() {
        backToUpdate();
    }

    private void backToUpdate() {
        Intent intent = getIntent();
        intent.putExtra("ADMIN_DATA_FROM_CHANGE_PASSWORD_TO_UPDATE", customerArr);
        setResult(CustomerUpdateActivity.RESULT_CHANGE_PASSWORD_OK, intent);
        finish();
    }
}