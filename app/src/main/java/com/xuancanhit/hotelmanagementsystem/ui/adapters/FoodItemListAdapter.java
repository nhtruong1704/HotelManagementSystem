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
import com.xuancanhit.hotelmanagementsystem.presentation.model.FoodItem;

import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.fooditem.AdminFoodItemViewDetailsActivity;
import com.xuancanhit.hotelmanagementsystem.ui.interfaces.ItemClickListener;

import java.util.ArrayList;

public class FoodItemListAdapter extends RecyclerView.Adapter<FoodItemListAdapter.FoodItemViewHolder> {
    //Form for adapter
    Context context;
    ArrayList<FoodItem> foodItemArr;

    public FoodItemListAdapter(Context context, ArrayList<FoodItem> foodItemArr) {
        this.context = context;
        this.foodItemArr = foodItemArr;
    }

    @NonNull
    @Override
    public FoodItemListAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food_item, parent, false);
        return new FoodItemListAdapter.FoodItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodItemListAdapter.FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItemArr.get(position);

        String foodItemName = foodItem.getFoodItemName();
        String foodItemPrice = foodItem.getFoodItemPrice();


        if (!foodItem.getFoodItemImage().equals("")) {
            Picasso.get()
                    .load(foodItem.getFoodItemImage())
                    .placeholder(R.drawable.diet)
                    .error(R.drawable.diet)
                    .into(holder.ivFoodItemImage);
        } else {
            holder.ivFoodItemImage.setImageResource(R.drawable.diet);
        }
        
        
        holder.tvFoodItemName.setText(foodItemName);
        holder.tvFoodItemPrice.setText(foodItemPrice);

        //Click for RecycleView
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "FoodItem " + foodItem.getFoodItemName(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), AdminFoodItemViewDetailsActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("FOOD_ITEM_DATA_ARRAY", foodItemArr);
                    bundle.putInt("FOOD_ITEM_DATA_POSITION", position);
                    intent.putExtra("FOOD_ITEM_DATA_FROM_ROOM_ADAPTER_TO_AD_FOOD_ITEM_VIEW_DETAILS", bundle);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodItemArr == null ? 0 : foodItemArr.size();
    }


    //Data ViewHolder class
    public static class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivFoodItemImage;
        TextView tvFoodItemName, tvFoodItemPrice;

        ItemClickListener itemClickListener;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodItemName = itemView.findViewById(R.id.tv_list_food_item_name);
            tvFoodItemPrice = itemView.findViewById(R.id.tv_list_food_item_price);
            ivFoodItemImage = itemView.findViewById(R.id.iv_list_food_item_image);

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
