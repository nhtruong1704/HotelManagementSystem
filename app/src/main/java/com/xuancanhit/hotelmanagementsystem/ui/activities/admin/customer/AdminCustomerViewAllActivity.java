package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanhit.hotelmanagementsystem.ui.adapters.CustomerListAdapter;
import com.xuancanhit.hotelmanagementsystem.ui.tools.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCustomerViewAllActivity extends AppCompatActivity {

    private ArrayList<Customer> customerArr, customerArrSearch;
    RecyclerView rvItems;
    SwipeRefreshLayout srlAdCusViewAll;
    private CustomerListAdapter customerListAdapter;

    ImageButton ibCusAdd;
    EditText edtCusViewAllSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_view_all);

        //Search
        edtCusViewAllSearch = findViewById(R.id.edt_cus_view_all_search);
        edtCusViewAllSearch.addTextChangedListener(new TextWatcher() {
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
        srlAdCusViewAll = findViewById(R.id.srl_ad_cus_view_all);
        srlAdCusViewAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                readData();
                customerListAdapter.notifyDataSetChanged();
                srlAdCusViewAll.setRefreshing(false);
            }
        });

        //Circle Button Add
        ibCusAdd = findViewById(R.id.ib_cus_add);
        ibCusAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminCustomerViewAllActivity.this, AdminCustomerAddActivity.class));
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
        customerArr.clear();
        if (textSearch.length() == 0) {
            customerArr.addAll(customerArrSearch);
            Log.d("load data", "all");
        } else {
            Log.d("load data", "filtered");
            for (int i = 0; i < customerArrSearch.size(); i++) {
                if (customerArrSearch.get(i).getCusName().toLowerCase(Locale.getDefault()).contains(textSearch) ||
                        customerArrSearch.get(i).getCusAddress().toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    customerArr.add(customerArrSearch.get(i));
                }
            }
        }
        customerListAdapter.notifyDataSetChanged();
    }

    private void readData() {
        customerArr.clear();
        customerArrSearch.clear();
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Customer>> callback = dataClient.AdminViewAllCustomerData();
        callback.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                customerArr = (ArrayList<Customer>) response.body();

                if (customerArr.size() > 0) {
                    customerArrSearch.addAll(customerArr);
                    customerListAdapter = new CustomerListAdapter(getApplicationContext(), customerArr);
                    //customerListAdapter.notifyDataSetChanged();
                    rvItems.setAdapter(customerListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.d("Error load all Cus", t.getMessage());
            }
        });
    }

    private void addControls() {
        customerArr = new ArrayList<>();
        customerArrSearch = new ArrayList<>();
        rvItems = findViewById(R.id.rv_ad_cus_view_all_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(AdminCustomerViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Fix: No adapter attached; skipping layout
        //Set adapter first after show
        customerListAdapter = new CustomerListAdapter(getApplicationContext(), customerArr); // this
        rvItems.setAdapter(customerListAdapter);
    }
}