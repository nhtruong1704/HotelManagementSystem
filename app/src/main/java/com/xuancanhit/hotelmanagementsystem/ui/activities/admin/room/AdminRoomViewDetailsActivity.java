package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room;

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
import com.xuancanhit.hotelmanagementsystem.presentation.model.Room;


import java.util.ArrayList;

public class AdminRoomViewDetailsActivity extends AppCompatActivity {

    private ImageView ivAdRoomViewDetailsImage, ivAdRoomViewDetailsExit;
    private TextView tvAdRoomViewDetailsName, tvAdRoomViewDetailsPrice, tvAdRoomViewDetailsDes;
    private Button btnAdRoomViewDetailsUpdate, btnAdRoomViewDetailsExit;

    ArrayList<Room> roomArr;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_view_details);

        //Connect layout
        initUI();

        //Receive Data from View All
        receiveDataFromRoomAdapter();

        //Set on View
        initView();

        //Button Exit
        btnAdRoomViewDetailsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivAdRoomViewDetailsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnAdRoomViewDetailsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRoomViewDetailsActivity.this, AdminRoomUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("ROOM_DATA_ARRAY", roomArr);
                bundle.putInt("ROOM_DATA_POSITION", position);
                intent.putExtra("ROOM_DATA_FROM_AD_ROOM_VIEW_DETAILS_TO_UPDATE", bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvAdRoomViewDetailsName.setText(roomArr.get(position).getRoomName());
        tvAdRoomViewDetailsPrice.setText(roomArr.get(position).getRoomPrice());
        tvAdRoomViewDetailsDes.setText(roomArr.get(position).getRoomDes());
        
        if (!roomArr.get(position).getRoomImage().equals("")) {
            Picasso.get()
                    .load(roomArr.get(position).getRoomImage())
                    .placeholder(R.drawable.bed)
                    .error(R.drawable.bed)
                    .into(ivAdRoomViewDetailsImage);
        } else {
            ivAdRoomViewDetailsImage.setImageResource(R.drawable.bed);
        }
    }

    private void receiveDataFromRoomAdapter() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ROOM_DATA_FROM_ROOM_ADAPTER_TO_AD_ROOM_VIEW_DETAILS");
        if (bundle != null) {
            roomArr = bundle.getParcelableArrayList("ROOM_DATA_ARRAY");
            position = bundle.getInt("ROOM_DATA_POSITION");
        }
    }

    private void initUI() {
        ivAdRoomViewDetailsImage = findViewById(R.id.iv_ad_room_view_details_avt);
        ivAdRoomViewDetailsExit = findViewById(R.id.iv_ad_room_view_details_exit);
        tvAdRoomViewDetailsName = findViewById(R.id.tv_ad_room_view_details_name);
        tvAdRoomViewDetailsPrice = findViewById(R.id.tv_ad_room_view_details_price);
        tvAdRoomViewDetailsDes = findViewById(R.id.tv_ad_room_view_details_des);
        btnAdRoomViewDetailsExit = findViewById(R.id.btn_ad_room_view_details_exit);
        btnAdRoomViewDetailsUpdate = findViewById(R.id.btn_ad_room_view_details_update);
    }

    private void backToMenu() {
        Intent intent = new Intent(AdminRoomViewDetailsActivity.this, AdminRoomViewAllActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}