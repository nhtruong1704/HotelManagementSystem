package com.xuancanhit.hotelmanagementsystem.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Room;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room.AdminRoomViewDetailsActivity;
import com.xuancanhit.hotelmanagementsystem.ui.interfaces.ItemClickListener;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> {
    //Form for adapter
    Context context;
    ArrayList<Room> roomArr;

    public RoomListAdapter(Context context, ArrayList<Room> roomArr) {
        this.context = context;
        this.roomArr = roomArr;
    }

    @NonNull
    @Override
    public RoomListAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_room, parent, false);
        return new RoomListAdapter.RoomViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RoomListAdapter.RoomViewHolder holder, int position) {
        Room room = roomArr.get(position);

        String roomName = room.getRoomName();
        String roomPrice = room.getRoomPrice();


        if (!room.getRoomImage().equals("")) {
            Picasso.get()
                    .load(room.getRoomImage())
                    .placeholder(R.drawable.bed)
                    .error(R.drawable.bed)
                    .into(holder.ivRoomImage);
        } else {
            holder.ivRoomImage.setImageResource(R.drawable.bed);
        }
        
        
        holder.tvRoomName.setText(roomName);
        holder.tvRoomPrice.setText(roomPrice);

        //Click for RecycleView
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Room " + room.getRoomName(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), AdminRoomViewDetailsActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("ROOM_DATA_ARRAY", roomArr);
                    bundle.putInt("ROOM_DATA_POSITION", position);
                    intent.putExtra("ROOM_DATA_FROM_ROOM_ADAPTER_TO_AD_ROOM_VIEW_DETAILS", bundle);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return roomArr == null ? 0 : roomArr.size();
    }


    //Data ViewHolder class
    public static class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivRoomImage;
        TextView tvRoomName, tvRoomPrice;

        ItemClickListener itemClickListener;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tv_list_room_name);
            tvRoomPrice = itemView.findViewById(R.id.tv_list_room_price);
            ivRoomImage = itemView.findViewById(R.id.iv_list_room_image);

            //Turn On Click for RecycleView
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        //onClick for RecycleView
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        //onLongClick for RecycleView
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
            //return false; // if not use
        }
    }
}
