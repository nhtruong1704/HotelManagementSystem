package com.xuancanhit.hotelmanagementsystem.ui.activities.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.AdminLoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerLoginActivity extends AppCompatActivity {

    private EditText edtCusLoginEmail, edtCusLoginPassword;
    private Button btnCusLogin;
    private TextView tvCusLoginForgotPassword, tvCusLoginToLoginAdmin, tvCusLoginToRegister;
    private ImageView ivCusLoginClose;
    private CheckBox cbCusLoginRememberMe;
    private SharedPreferences.Editor loginPrefsEditor;

    ArrayList<Customer> customerArrayList;
    String customerEmail, customerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        //Connect layout
        initUI();

        //Login When Enter - Done typing data
        edtCusLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    customerLogin();
                    rememberMe();
                }
                return false;
            }
        });

        //Remember Me
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean rememberMeCheck = loginPreferences.getBoolean("CUSTOMER_REMEMBER_ME", false);
        if (rememberMeCheck) {
            edtCusLoginEmail.setText(loginPreferences.getString("CUSTOMER_EMAIL", ""));
            edtCusLoginPassword.setText(loginPreferences.getString("CUSTOMER_PASSWORD", ""));
            cbCusLoginRememberMe.setChecked(true);
        }

        // Close
        ivCusLoginClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //To Login Admin
        tvCusLoginToLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        //To Register
//        tvCusLoginToRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CusdentLoginActivity.this, CusdentRegisterActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        //Button Login
        btnCusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid(edtCusLoginEmail)) {
                    customerLogin();
                    rememberMe();
                } else {
                    edtCusLoginEmail.setError("Email address not valid");
                }
            }
        });
        
        
    }


    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        if (email.equals("")) return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void customerLogin() {
        customerEmail = edtCusLoginEmail.getText().toString();
        customerPassword = edtCusLoginPassword.getText().toString();
        if (customerEmail.length() > 0 && customerPassword.length() > 0) {
            DataClient dataClient = APIUtils.getData();
            retrofit2.Call<List<Customer>> callback = dataClient.LoginCustomerData(customerEmail, customerPassword);
            callback.enqueue(new Callback<List<Customer>>() {
                @Override
                public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                    customerArrayList = (ArrayList<Customer>) response.body();
                    if (customerArrayList.size() > 0) {
//                        Send Data and finish
                        Intent intent = new Intent(CustomerLoginActivity.this, CustomerMenuActivity.class);
                        intent.putExtra("CUSTOMER_DATA_FROM_LOGIN_TO_MENU", customerArrayList);
                        startActivity(intent);
                        finish();
                        Toast.makeText(CustomerLoginActivity.this, "Welcome " + customerArrayList.get(0).getCusName(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Customer>> call, Throwable t) {
                    Toast.makeText(CustomerLoginActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    //Log.d("Error", t.getMessage());
                }
            });
        }
    }

    private void rememberMe() {
        if (cbCusLoginRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("CUSTOMER_REMEMBER_ME", true);
            loginPrefsEditor.putString("CUSTOMER_EMAIL", customerEmail);
            loginPrefsEditor.putString("CUSTOMER_PASSWORD", customerPassword);
        } else {
            loginPrefsEditor.clear();
        }
        loginPrefsEditor.apply();
    }

    private void initUI() {
        edtCusLoginEmail = findViewById(R.id.edt_cus_login_email);
        edtCusLoginPassword = findViewById(R.id.edt_cus_login_password);
        btnCusLogin = findViewById(R.id.btn_cus_login);
        tvCusLoginForgotPassword = findViewById(R.id.tv_cus_login_forgot_password);
        tvCusLoginToLoginAdmin = findViewById(R.id.tv_cus_login_to_login_admin);
        tvCusLoginToRegister = findViewById(R.id.tv_cus_login_to_register);
        ivCusLoginClose = findViewById(R.id.iv_cus_login_close);
        cbCusLoginRememberMe = findViewById(R.id.cb_cus_login_remember_me);
    }
}