package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.fooditem;

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
import com.xuancanhit.hotelmanagementsystem.presentation.model.FoodItem;


import java.util.ArrayList;

public class AdminFoodItemViewDetailsActivity extends AppCompatActivity {

    private ImageView ivAdFoodItemViewDetailsImage, ivAdFoodItemViewDetailsExit;
    private TextView tvAdFoodItemViewDetailsName, tvAdFoodItemViewDetailsPrice, tvAdFoodItemViewDetailsDes;
    private Button btnAdFoodItemViewDetailsUpdate, btnAdFoodItemViewDetailsExit;

    ArrayList<FoodItem>foodItemArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_item_view_details);

        //Connect layout
        initUI();

        //Receive Data from View All
        receiveDataFromFoodItemAdapter();

        //Set on View
        initView();

        //Button Exit
        btnAdFoodItemViewDetailsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivAdFoodItemViewDetailsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnAdFoodItemViewDetailsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminFoodItemViewDetailsActivity.this, AdminFoodItemUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("FOOD_ITEM_DATA_ARRAY",foodItemArr);
                bundle.putInt("FOOD_ITEM_DATA_POSITION", position);
                intent.putExtra("FOOD_ITEM_DATA_FROM_AD_FOOD_ITEM_VIEW_DETAILS_TO_UPDATE", bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvAdFoodItemViewDetailsName.setText(foodItemArr.get(position).getFoodItemName());
        tvAdFoodItemViewDetailsPrice.setText(foodItemArr.get(position).getFoodItemPrice());
        tvAdFoodItemViewDetailsDes.setText(foodItemArr.get(position).getFoodItemDes());

        if (!foodItemArr.get(position).getFoodItemImage().equals("")) {
            Picasso.get()
                    .load(foodItemArr.get(position).getFoodItemImage())
                    .placeholder(R.drawable.diet)
                    .error(R.drawable.diet)
                    .into(ivAdFoodItemViewDetailsImage);
        } else {
            ivAdFoodItemViewDetailsImage.setImageResource(R.drawable.diet);
        }
    }

    private void receiveDataFromFoodItemAdapter() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("FOOD_ITEM_DATA_FROM_ROOM_ADAPTER_TO_AD_FOOD_ITEM_VIEW_DETAILS");
        if (bundle != null) {
           foodItemArr = bundle.getParcelableArrayList("FOOD_ITEM_DATA_ARRAY");
            position = bundle.getInt("FOOD_ITEM_DATA_POSITION");
        }
    }

    private void initUI() {
        ivAdFoodItemViewDetailsImage = findViewById(R.id.iv_ad_food_item_view_details_avt);
        ivAdFoodItemViewDetailsExit = findViewById(R.id.iv_ad_food_item_view_details_exit);
        tvAdFoodItemViewDetailsName = findViewById(R.id.tv_ad_food_item_view_details_name);
        tvAdFoodItemViewDetailsPrice = findViewById(R.id.tv_ad_food_item_view_details_price);
        tvAdFoodItemViewDetailsDes = findViewById(R.id.tv_ad_food_item_view_details_des);
        btnAdFoodItemViewDetailsExit = findViewById(R.id.btn_ad_food_item_view_details_exit);
        btnAdFoodItemViewDetailsUpdate = findViewById(R.id.btn_ad_food_item_view_details_update);
    }

    private void backToMenu() {
        Intent intent = new Intent(AdminFoodItemViewDetailsActivity.this, AdminFoodItemViewAllActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}