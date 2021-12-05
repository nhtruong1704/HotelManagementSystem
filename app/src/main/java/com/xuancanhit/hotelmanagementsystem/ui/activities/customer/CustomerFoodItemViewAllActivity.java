package com.xuancanhit.hotelmanagementsystem.ui.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.FoodItem;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.fooditem.AdminFoodItemAddActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.fooditem.AdminFoodItemViewAllActivity;
import com.xuancanhit.hotelmanagementsystem.ui.adapters.FoodItemListAdapter;
import com.xuancanhit.hotelmanagementsystem.ui.tools.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerFoodItemViewAllActivity extends AppCompatActivity {

    private ArrayList<FoodItem> foodItemArr, foodItemArrSearch;
    RecyclerView rvItems;
    SwipeRefreshLayout srlAdFoodItemViewAll;
    private FoodItemListAdapter foodItemListAdapter;

    EditText edtFoodItemViewAllSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_item_view_all);

        //Search
        edtFoodItemViewAllSearch = findViewById(R.id.edt_cus_food_item_view_all_search);
        edtFoodItemViewAllSearch.addTextChangedListener(new TextWatcher() {
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
        srlAdFoodItemViewAll = findViewById(R.id.srl_cus_food_item_view_all);
        srlAdFoodItemViewAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                readData();
                foodItemListAdapter.notifyDataSetChanged();
                srlAdFoodItemViewAll.setRefreshing(false);
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
        foodItemArr.clear();
        if(textSearch.length() == 0) {
            foodItemArr.addAll(foodItemArrSearch);
            Log.d("load data", "all");
        }
        else {
            Log.d("load data", "filtered");
            for (int i=0; i<foodItemArrSearch.size(); i++) {
                if(foodItemArrSearch.get(i).getFoodItemName().toLowerCase(Locale.getDefault()).contains(textSearch) ||
                        foodItemArrSearch.get(i).getFoodItemPrice().toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    foodItemArr.add(foodItemArrSearch.get(i));
                }
            }
        }
        foodItemListAdapter.notifyDataSetChanged();
    }

    private void readData() {
        foodItemArr.clear();
        foodItemArrSearch.clear();
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<FoodItem>> callback = dataClient.AdminViewAllFoodItemData();
        callback.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                foodItemArr = (ArrayList<FoodItem>) response.body();

                if (foodItemArr.size() > 0) {
                    foodItemArrSearch.addAll(foodItemArr);
                    foodItemListAdapter = new FoodItemListAdapter(getApplicationContext(), foodItemArr);
                    //foodItemListAdapter.notifyDataSetChanged();
                    rvItems.setAdapter(foodItemListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.d("Error load all foodItem", t.getMessage());
            }
        });
    }

    private void addControls() {
        foodItemArr = new ArrayList<>();
        foodItemArrSearch = new ArrayList<>();
        rvItems = findViewById(R.id.rv_cus_food_item_view_all_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(CustomerFoodItemViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Fix: No adapter attached; skipping layout
        //Set adapter first after show
        foodItemListAdapter = new FoodItemListAdapter(getApplicationContext(), foodItemArr); // this
        rvItems.setAdapter(foodItemListAdapter);
    }
}