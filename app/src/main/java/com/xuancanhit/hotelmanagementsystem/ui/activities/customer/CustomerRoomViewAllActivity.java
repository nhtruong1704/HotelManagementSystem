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
import com.xuancanhit.hotelmanagementsystem.presentation.model.Room;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room.AdminRoomAddActivity;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room.AdminRoomViewAllActivity;
import com.xuancanhit.hotelmanagementsystem.ui.adapters.RoomListAdapter;
import com.xuancanhit.hotelmanagementsystem.ui.tools.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRoomViewAllActivity extends AppCompatActivity {

    private ArrayList<Room> roomArr, roomArrSearch;
    RecyclerView rvItems;
    SwipeRefreshLayout srlAdRoomViewAll;
    private RoomListAdapter roomListAdapter;

    EditText edtRoomViewAllSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_room_view_all);
//Search
        edtRoomViewAllSearch = findViewById(R.id.edt_cus_room_view_all_search);
        edtRoomViewAllSearch.addTextChangedListener(new TextWatcher() {
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
        srlAdRoomViewAll = findViewById(R.id.srl_cus_room_view_all);
        srlAdRoomViewAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                readData();
                roomListAdapter.notifyDataSetChanged();
                srlAdRoomViewAll.setRefreshing(false);
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
        roomArr.clear();
        if(textSearch.length() == 0) {
            roomArr.addAll(roomArrSearch);
            Log.d("load data", "all");
        }
        else {
            Log.d("load data", "filtered");
            for (int i=0; i<roomArrSearch.size(); i++) {
                if(roomArrSearch.get(i).getRoomName().toLowerCase(Locale.getDefault()).contains(textSearch) ||
                        roomArrSearch.get(i).getRoomPrice().toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    roomArr.add(roomArrSearch.get(i));
                }
            }
        }
        roomListAdapter.notifyDataSetChanged();
    }

    private void readData() {
        roomArr.clear();
        roomArrSearch.clear();
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Room>> callback = dataClient.AdminViewAllRoomData();
        callback.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                roomArr = (ArrayList<Room>) response.body();

                if (roomArr.size() > 0) {
                    roomArrSearch.addAll(roomArr);
                    roomListAdapter = new RoomListAdapter(getApplicationContext(), roomArr);
                    //roomListAdapter.notifyDataSetChanged();
                    rvItems.setAdapter(roomListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.d("Error load all room", t.getMessage());
            }
        });
    }

    private void addControls() {
        roomArr = new ArrayList<>();
        roomArrSearch = new ArrayList<>();
        rvItems = findViewById(R.id.rv_cus_room_view_all_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(CustomerRoomViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Fix: No adapter attached; skipping layout
        //Set adapter first after show
        roomListAdapter = new RoomListAdapter(getApplicationContext(), roomArr); // this
        rvItems.setAdapter(roomListAdapter);
    }
}