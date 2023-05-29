package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.employee;

import static android.app.ProgressDialog.show;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Employee;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanhit.hotelmanagementsystem.ui.adapters.EmployeeListAdapter;
import com.xuancanhit.hotelmanagementsystem.ui.tools.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminEmployeeViewAllActivity extends AppCompatActivity {

    private ArrayList<Employee> employeeArr, employeeArrSearch;
    RecyclerView rvItems;
    SwipeRefreshLayout srlAdEmpViewAll;
    private EmployeeListAdapter employeeListAdapter;

    ImageButton ibEmpAdd;
    EditText edtEmpViewAllSearch;

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_view_all);

        //Search
        edtEmpViewAllSearch = findViewById(R.id.edt_emp_view_all_search);
        edtEmpViewAllSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textSearch = charSequence.toString();
                FilterData(textSearch);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //SwipeRefreshLayout
        srlAdEmpViewAll = findViewById(R.id.srl_ad_emp_view_all);
        srlAdEmpViewAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                readData();
                employeeListAdapter.notifyDataSetChanged();
                srlAdEmpViewAll.setRefreshing(false);
            }
        });

        //Circle Button Add
        ibEmpAdd = findViewById(R.id.ib_emp_add);
        ibEmpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminEmployeeViewAllActivity.this, AdminEmployeeAddActivity.class));
            }
        });

        addControls();
        readData();
    }

    //Filter data
    @SuppressLint("NotifyDataSetChanged")
    public void FilterData(String textSearch) {
        textSearch = textSearch.toLowerCase(Locale.getDefault());
        Log.d("filter", textSearch);
        employeeArr.clear();
        if (textSearch.length() == 0) {
            employeeArr.addAll(employeeArrSearch);
            Log.d("load data", "all");
        } else {
            Log.d("load data", "filtered");
            for (int i = 0; i < employeeArrSearch.size(); i++) {
                if (employeeArrSearch.get(i).getEmpName().toLowerCase(Locale.getDefault()).contains(textSearch) ||
                        employeeArrSearch.get(i).getEmpPosition().toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    employeeArr.add(employeeArrSearch.get(i));
                }
            }
        }
        employeeListAdapter.notifyDataSetChanged();
    }

    private void readData() {
        employeeArr.clear();
        employeeArrSearch.clear();
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Employee>> callback = dataClient.AdminViewAllEmployeeData();
        callback.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                employeeArr = (ArrayList<Employee>) response.body();

                if (employeeArr.size() > 0) {
                    employeeArrSearch.addAll(employeeArr);
                    employeeListAdapter = new EmployeeListAdapter(getApplicationContext(), employeeArr);
                    //employeeListAdapter.notifyDataSetChanged();
                    rvItems.setAdapter(employeeListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.d("Error load all Emp", t.getMessage());
            }
        });
    }

    private void addControls() {
        employeeArr = new ArrayList<>();
        employeeArrSearch = new ArrayList<>();
        rvItems = findViewById(R.id.rv_ad_emp_view_all_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(AdminEmployeeViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Fix: No adapter attached; skipping layout
        //Set adapter first after show
        employeeListAdapter = new EmployeeListAdapter(getApplicationContext(), employeeArr); // this
        rvItems.setAdapter(employeeListAdapter);
    }
}
