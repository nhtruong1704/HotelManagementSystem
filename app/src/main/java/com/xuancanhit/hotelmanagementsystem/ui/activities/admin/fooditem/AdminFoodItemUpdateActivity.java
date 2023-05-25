//package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.fooditem;
//
//import android.content.ContentValues;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.squareup.picasso.Picasso;
//import com.xuancanhit.hotelmanagementsystem.R;
//import com.xuancanhit.hotelmanagementsystem.presentation.model.FoodItem;
//import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
//import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
//import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer.AdminCustomerUpdateActivity;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class AdminFoodItemUpdateActivity extends AppCompatActivity {
//
//    final int REQUEST_TAKE_PHOTO = 123;
//    final int REQUEST_CHOOSE_PHOTO = 321;
//
//    String realPath = "";
//    Uri imageUri;
//    String foodItemName, foodItemPrice, foodItemDes, foodItemImage;
//
//    private EditText edtAdFoodItemUpdateName, edtAdFoodItemUpdatePrice, edtAdFoodItemUpdateDes;
//    private Button btnAdFoodItemUpdateSave, btnAdFoodItemUpdateDelete, btnAdFoodItemUpdateExit, btnAdFoodItemUpdateTakePhoto, btnAdFoodItemUpdateChoosePhoto;
//
//    private ImageView ivAdFoodItemUpdateImage, ivAdFoodItemUpdateExit;
//
//
//    ArrayList<FoodItem> foodItemArr;
//    int position;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_food_item_update);
//        //Connect layout
//        initUI();
//
//        //Receive Data from AdStuViewProfile
//        receiveDataFromAdFoodItemViewDetails();
//
//        //Set on View
//        initView();
//
//
//        //Button Choose Photo
//        btnAdFoodItemUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choosePhoto();
//            }
//        });
//
//        //Button Take Photo
//        btnAdFoodItemUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takePhoto();
//            }
//        });
//
//        //Button Exit
//        btnAdFoodItemUpdateExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                backToMenu();
//                //Check xem co can gui data
//            }
//        });
//
//        //ImageView Exit
//        ivAdFoodItemUpdateExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                backToMenu();
//                //Check xem co can gui data
//            }
//        });
//
//        //Button Delete
//        btnAdFoodItemUpdateDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(AdminFoodItemUpdateActivity.this);
//                builder.setIcon(R.drawable.ic_baseline_delete_24);
//                builder.setTitle("Delete this food item");
//                builder.setMessage("Are you sure want to delete food item " + foodItemArr.get(position).getFoodItemName() + "?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteFood();
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });
//
//        //Button Save
//        btnAdFoodItemUpdateSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isEmptyEditText(edtAdFoodItemUpdateName)) {
//                    edtAdFoodItemUpdateName.setError("Please enter food item's name");
//                    return;
//                }
//                if (isEmptyEditText(edtAdFoodItemUpdateDes)) {
//                    edtAdFoodItemUpdateDes.setError("Please enter food item's description");
//                    return;
//                }
//                if (isEmptyEditText(edtAdFoodItemUpdatePrice)) {
//                    edtAdFoodItemUpdatePrice.setError("Please enter food item's price");
//                    return;
//                }
//
//
//                foodItemName = edtAdFoodItemUpdateName.getText().toString();
//                foodItemPrice = edtAdFoodItemUpdatePrice.getText().toString();
//                foodItemDes = edtAdFoodItemUpdateDes.getText().toString();
//
//                if (foodItemName.length() > 0 && foodItemPrice.length() > 0 && foodItemDes.length() > 0) {
//                    if (!realPath.equals("")) {
//                        uploadInfoWithPhoto();
//                    } else {
//                        uploadInfo();
//                    }
//                }
//
//            }
//        });
//
//    }
//
//
//    private boolean isEmptyEditText(EditText editText) {
//        String str = editText.getText().toString();
//        if (TextUtils.isEmpty(str)) {
//            return true;
//        }
//        return false;
////        String str = editText.getText().toString().trim();
////        return TextUtils.isEmpty(str);
//    }
//
//    private void deleteFood() {
//        String currentImage;
//        if (!foodItemArr.get(position).getFoodItemImage().equals("")) {
//            currentImage = foodItemArr.get(position).getFoodItemImage();
//            currentImage = currentImage.substring(currentImage.lastIndexOf("/")+1);
//        } else {
//            currentImage = "NO_CURRENT_IMAGE_FOOD_ITEM_UPDATE";
//        }
//        DataClient dataClient = APIUtils.getData();
//        retrofit2.Call<String> callback = dataClient.DeleteFoodItemData(foodItemArr.get(position).getFoodItemId(), currentImage);
//        callback.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String res = response.body();
//                Toast.makeText(AdminFoodItemUpdateActivity.this, res.trim(), Toast.LENGTH_SHORT).show();
//                if (res.trim().equals("FOOD_ITEM_DELETED_SUCCESSFUL")) {
//                    Toast.makeText(AdminFoodItemUpdateActivity.this, "Deleted Food Item " + foodItemArr.get(position).getFoodItemName() + " Successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AdminFoodItemUpdateActivity.this, AdminFoodItemViewAllActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Log.d("Delete Err", res.trim());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("Error Retrofit response", t.getMessage());
//            }
//        });
//    }
//
//
//    private void receiveDataFromAdFoodItemViewDetails() {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("FOOD_ITEM_DATA_FROM_AD_FOOD_ITEM_VIEW_DETAILS_TO_UPDATE");
//        if (bundle != null) {
//            foodItemArr = bundle.getParcelableArrayList("FOOD_ITEM_DATA_ARRAY");
//            position = bundle.getInt("FOOD_ITEM_DATA_POSITION");
//        }
//    }
//
//    private void uploadInfoWithPhoto() {
//        File file = new File(realPath);
//        String file_path = file.getAbsolutePath();
//        String[] arrayNamePhoto = file_path.split("\\.");
//        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
//        DataClient dataClient = APIUtils.getData();
//        retrofit2.Call<String> callbackPhoto = dataClient.UploadFoodItemPhoto(body);
//        callbackPhoto.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response != null) {
//                    foodItemImage = response.body();
//                    Log.d("Updated Food Photo", foodItemImage);
//                    uploadInfo();
//                    backToMenu();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("Err Updated Food Photo", t.getMessage());
//            }
//        });
//    }
//
//    private void uploadInfo() {
//        String currentImage, newImage;
//        if (foodItemArr != null && position < foodItemArr.size()) {
//            if (foodItemArr.get(position).getFoodItemImage().equals("")) {
//                //curAva = "", newAva=""
//                currentImage = "NO_CURRENT_IMAGE_FOOD_ITEM_UPDATE";
//                if (realPath.equals("")) {
//                    newImage = "";
//                } else {
//                    newImage = APIUtils.BASE_URL + "admin/food/images/" + foodItemImage;
//                }
//            } else {
//                if (realPath.equals("")) {
//                    currentImage = "NO_CURRENT_IMAGE_FOOD_ITEM_UPDATE";
//                    newImage = foodItemArr.get(position).getFoodItemImage();
//                } else {
//                    currentImage = foodItemArr.get(position).getFoodItemImage();
//                    currentImage = currentImage.substring(currentImage.lastIndexOf("/") + 1);
//                    newImage = APIUtils.BASE_URL + "admin/food/images/" + foodItemImage;
//                }
//            }
//
//            DataClient insertData = APIUtils.getData();
//            Call<String> callbackInfo = insertData.AdminUpdateFoodItemData(foodItemArr.get(position).getFoodItemId(),
//                    foodItemName, foodItemPrice, foodItemDes, newImage, currentImage);
//            foodItemArr.get(position).setFoodItemName(foodItemName);
//            foodItemArr.get(position).setFoodItemPrice(foodItemPrice);
//            foodItemArr.get(position).setFoodItemDes(foodItemDes);
//            foodItemArr.get(position).setFoodItemImage(newImage);
//
//            callbackInfo.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    String result = response.body();
//                    Log.d("Updated food Item Info", result);
//                    if (result.trim().equals("FOOD_ITEM_UPDATE_SUCCESSFUL")) {
//                        Toast.makeText(AdminFoodItemUpdateActivity.this, "Successfully Updated Food Item Information " + foodItemName, Toast.LENGTH_SHORT).show();
//                        backToMenu();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Log.d("Err Updated Food Info", t.getMessage());
//                }
//            });
//        }
//    }
//
//    //Send data to menu and end activity current
//    private void backToMenu() {
//        Intent intent = new Intent(AdminFoodItemUpdateActivity.this, AdminFoodItemViewDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("FOOD_ITEM_DATA_ARRAY", foodItemArr);
//        bundle.putInt("FOOD_ITEM_DATA_POSITION", position);
//        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
//        intent.putExtra("FOOD_ITEM_DATA_FROM_FOOD_ITEM_ADAPTER_TO_AD_FOOD_ITEM_VIEW_DETAILS", bundle);
//        startActivity(intent);
//        finish();
//    }
//
//
//    private void takePhoto() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
//    }
//
//    private void choosePhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_CHOOSE_PHOTO) {
//                imageUri = data.getData();
//                realPath = getRealPathFromURI(imageUri);
//                try {
//                    InputStream is = getContentResolver().openInputStream(imageUri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(is);
//                    ivAdFoodItemUpdateImage.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            } else if (requestCode == REQUEST_TAKE_PHOTO) {
//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                ivAdFoodItemUpdateImage.setImageBitmap(bitmap);
//                saveToGallery();
//                realPath = getRealPathFromURI(imageUri);
//            }
//        }
//    }
//
//    // Get Real Path when upload photo(from uri - image/mame_image)
//    public String getRealPathFromURI(Uri contentUri) {
//        String path = null;
//        String[] proj = {MediaStore.MediaColumns.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            path = cursor.getString(column_index);
//        }
//        cursor.close();
//        return path;
//    }
//
//    // Save image(from image view) when take photo
//    private void saveToGallery() {
//        Bitmap bitmap = ((BitmapDrawable) ivAdFoodItemUpdateImage.getDrawable()).getBitmap();
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "Image From Take Photo");
//        values.put(MediaStore.Images.Media.BUCKET_ID, "image");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "take photo and save to gallery");
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        OutputStream outstream;
//        try {
//            outstream = getContentResolver().openOutputStream(imageUri);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
//            outstream.close();
//            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void initUI() {
//        ivAdFoodItemUpdateImage = findViewById(R.id.iv_ad_food_item_update_image);
//        ivAdFoodItemUpdateExit = findViewById(R.id.iv_ad_food_item_update_exit);
//
//        edtAdFoodItemUpdateName = findViewById(R.id.edt_ad_food_item_update_name);
//        edtAdFoodItemUpdatePrice = findViewById(R.id.edt_ad_food_item_update_price);
//        edtAdFoodItemUpdateDes = findViewById(R.id.edt_ad_food_item_update_des);
//
//        btnAdFoodItemUpdateSave = findViewById(R.id.btn_ad_food_item_update_save);
//        btnAdFoodItemUpdateDelete = findViewById(R.id.btn_ad_food_item_update_delete);
//        btnAdFoodItemUpdateExit = findViewById(R.id.btn_ad_food_item_update_exit);
//        btnAdFoodItemUpdateTakePhoto = findViewById(R.id.btn_ad_food_item_update_take_photo);
//        btnAdFoodItemUpdateChoosePhoto = findViewById(R.id.btn_ad_food_item_update_choose_photo);
//    }
//
//    private void initView() {
//        edtAdFoodItemUpdateName.setText(foodItemArr.get(position).getFoodItemName());
//        edtAdFoodItemUpdatePrice.setText(foodItemArr.get(position).getFoodItemPrice());
//        edtAdFoodItemUpdateDes.setText(foodItemArr.get(position).getFoodItemDes());
//
//
//        if (!foodItemArr.get(position).getFoodItemImage().equals("")) {
//            Picasso.get()
//                    .load(foodItemArr.get(position).getFoodItemImage())
//                    .placeholder(R.drawable.diet)
//                    .error(R.drawable.diet)
//                    .into(ivAdFoodItemUpdateImage);
//        } else {
//            ivAdFoodItemUpdateImage.setImageResource(R.drawable.diet);
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        backToMenu();
//        super.onBackPressed();
//    }
//}










package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.fooditem;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.FoodItem;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminFoodItemUpdateActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String foodItemName, foodItemPrice, foodItemDes, foodItemImage;

    private EditText edtAdFoodItemUpdateName, edtAdFoodItemUpdatePrice, edtAdFoodItemUpdateDes;
    private Button btnAdFoodItemUpdateSave, btnAdFoodItemUpdateDelete, btnAdFoodItemUpdateExit, btnAdFoodItemUpdateTakePhoto, btnAdFoodItemUpdateChoosePhoto;

    private ImageView ivAdFoodItemUpdateImage, ivAdFoodItemUpdateExit;


    ArrayList<FoodItem> foodItemArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_item_update);
        //Connect layout
        initUI();

        //Receive Data from AdStuViewProfile
        receiveDataFromAdFoodItemViewDetails();

        //Set on View
        initView();


        //Button Choose Photo
        btnAdFoodItemUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdFoodItemUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdFoodItemUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //ImageView Exit
        ivAdFoodItemUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //Button Delete
        btnAdFoodItemUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminFoodItemUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this food");
                builder.setMessage("Are you sure want to delete food " + foodItemArr.get(position).getFoodItemName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFoodItem();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Button Save
        btnAdFoodItemUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdFoodItemUpdateName)) {
                    edtAdFoodItemUpdateName.setError("Please enter food's name");
                    return;
                }
                if (isEmptyEditText(edtAdFoodItemUpdateDes)) {
                    edtAdFoodItemUpdateDes.setError("Please enter food's description");
                    return;
                }
                if (isEmptyEditText(edtAdFoodItemUpdatePrice)) {
                    edtAdFoodItemUpdatePrice.setError("Please enter food's price");
                    return;
                }


                foodItemName = edtAdFoodItemUpdateName.getText().toString();
                foodItemPrice = edtAdFoodItemUpdatePrice.getText().toString();
                foodItemDes = edtAdFoodItemUpdateDes.getText().toString();

                if (foodItemName.length() > 0 && foodItemPrice.length() > 0 && foodItemDes.length() > 0) {
                    if (!realPath.equals("")) {
                        uploadInfoWithPhoto();
                    } else {
                        uploadInfo();
                    }
                }

            }
        });

    }


    private boolean isEmptyEditText(EditText editText) {
        String str = editText.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return false;

//        String str = editText.getText().toString().trim();
//        return TextUtils.isEmpty(str);
    }

    private void deleteFoodItem() {
        String currentImage;
        if (!foodItemArr.get(position).getFoodItemImage().equals("")) {
            currentImage = foodItemArr.get(position).getFoodItemImage();
            currentImage = currentImage.substring(currentImage.lastIndexOf("/"));
        } else {
            currentImage = "NO_CURRENT_IMAGE_FOOD_ITEM_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteFoodItemData(foodItemArr.get(position).getFoodItemId(), currentImage);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("FOOD_ITEM_DELETED_SUCCESSFUL")) {
                    Toast.makeText(AdminFoodItemUpdateActivity.this, "Deleted FoodItem " + foodItemArr.get(position).getFoodItemName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminFoodItemUpdateActivity.this, AdminFoodItemViewAllActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("Delete Err", res.trim());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Retrofit response", t.getMessage());
            }
        });
    }


    private void receiveDataFromAdFoodItemViewDetails() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("FOOD_ITEM_DATA_FROM_AD_FOOD_ITEM_VIEW_DETAILS_TO_UPDATE");
        if (bundle != null) {
            foodItemArr = bundle.getParcelableArrayList("FOOD_ITEM_DATA_ARRAY");
            position = bundle.getInt("FOOD_ITEM_DATA_POSITION");
        }
    }

    private void uploadInfoWithPhoto() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arrayNamePhoto = file_path.split("\\.");
        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callbackPhoto = dataClient.UploadFoodItemPhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    foodItemImage = response.body();
                    Log.d("Updated Photo", foodItemImage);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Err Updated Food Photo", t.getMessage());
            }
        });
    }

    private void uploadInfo() {
        String currentImage, newImage;
        if (foodItemArr != null && position < foodItemArr.size()) {
            if (foodItemArr.get(position).getFoodItemImage().equals("")) {
                //curAva = "", newAva=""
                currentImage = "NO_CURRENT_IMAGE_FOOD_ITEM_UPDATE";
                if (realPath.equals("")) {
                    newImage = "";
                } else {
                    newImage = APIUtils.BASE_URL + "admin/food/images/" + foodItemImage;
                }
            } else {
                if (realPath.equals("")) {
                    currentImage = "NO_CURRENT_IMAGE_FOOD_ITEM_UPDATE";
                    newImage = foodItemArr.get(position).getFoodItemImage();
                } else {
                    currentImage = foodItemArr.get(position).getFoodItemImage();
                    currentImage = currentImage.substring(currentImage.lastIndexOf("/" )+1);
                    newImage = APIUtils.BASE_URL + "admin/food/images/" + foodItemImage;
                }
            }

            DataClient insertData = APIUtils.getData();
            Call<String> callbackInfo = insertData.AdminUpdateFoodItemData(foodItemArr.get(position).getFoodItemId(),
                    foodItemName, foodItemPrice, foodItemDes, newImage, currentImage);
            foodItemArr.get(position).setFoodItemName(foodItemName);
            foodItemArr.get(position).setFoodItemPrice(foodItemPrice);
            foodItemArr.get(position).setFoodItemDes(foodItemDes);
            foodItemArr.get(position).setFoodItemImage(newImage);

            callbackInfo.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();
                    Log.d("Updated Food Info", result);
                    if (result.trim().equals("FOOD_ITEM_UPDATE_SUCCESSFUL")) {
                        Toast.makeText(AdminFoodItemUpdateActivity.this, "Successfully Updated Food Information " + foodItemName, Toast.LENGTH_SHORT).show();
                        backToMenu();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Err Updated Food Info", t.getMessage());
                }
            });
        }
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = new Intent(AdminFoodItemUpdateActivity.this, AdminFoodItemViewDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("FOOD_ITEM_DATA_ARRAY", foodItemArr);
        bundle.putInt("FOOD_ITEM_DATA_POSITION", position);
        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
        intent.putExtra("FOOD_ITEM_DATA_FROM_FOOD_ITEM_ADAPTER_TO_AD_FOOD_ITEM_VIEW_DETAILS", bundle);
        startActivity(intent);
        finish();
    }


    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                imageUri = data.getData();
                realPath = getRealPathFromURI(imageUri);
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ivAdFoodItemUpdateImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdFoodItemUpdateImage.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
            }
        }
    }

    // Get Real Path when upload photo(from uri - image/mame_image)
    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable) ivAdFoodItemUpdateImage.getDrawable()).getBitmap();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image From Take Photo");
        values.put(MediaStore.Images.Media.BUCKET_ID, "image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "take photo and save to gallery");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        ivAdFoodItemUpdateImage = findViewById(R.id.iv_ad_food_item_update_image);
        ivAdFoodItemUpdateExit = findViewById(R.id.iv_ad_food_item_update_exit);

        edtAdFoodItemUpdateName = findViewById(R.id.edt_ad_food_item_update_name);
        edtAdFoodItemUpdatePrice = findViewById(R.id.edt_ad_food_item_update_price);
        edtAdFoodItemUpdateDes = findViewById(R.id.edt_ad_food_item_update_des);

        btnAdFoodItemUpdateSave = findViewById(R.id.btn_ad_food_item_update_save);
        btnAdFoodItemUpdateDelete = findViewById(R.id.btn_ad_food_item_update_delete);
        btnAdFoodItemUpdateExit = findViewById(R.id.btn_ad_food_item_update_exit);
        btnAdFoodItemUpdateTakePhoto = findViewById(R.id.btn_ad_food_item_update_take_photo);
        btnAdFoodItemUpdateChoosePhoto = findViewById(R.id.btn_ad_food_item_update_choose_photo);
    }

    private void initView() {
        edtAdFoodItemUpdateName.setText(foodItemArr.get(position).getFoodItemName());
        edtAdFoodItemUpdatePrice.setText(foodItemArr.get(position).getFoodItemPrice());
        edtAdFoodItemUpdateDes.setText(foodItemArr.get(position).getFoodItemDes());


        if (!foodItemArr.get(position).getFoodItemImage().equals("")) {
            Picasso.get()
                    .load(foodItemArr.get(position).getFoodItemImage())
                    .placeholder(R.drawable.diet)
                    .error(R.drawable.diet)
                    .into(ivAdFoodItemUpdateImage);
        } else {
            ivAdFoodItemUpdateImage.setImageResource(R.drawable.diet);
        }

    }

    @Override
    public void onBackPressed() {
        backToMenu();
        super.onBackPressed();
    }
}