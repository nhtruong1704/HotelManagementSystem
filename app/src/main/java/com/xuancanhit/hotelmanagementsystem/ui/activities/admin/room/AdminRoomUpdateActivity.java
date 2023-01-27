package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room;

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
import com.xuancanhit.hotelmanagementsystem.presentation.model.Room;
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

public class AdminRoomUpdateActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String roomName, roomPrice, roomDes, roomImage;

    private EditText edtAdRoomUpdateName, edtAdRoomUpdatePrice, edtAdRoomUpdateDes;
    private Button btnAdRoomUpdateSave, btnAdRoomUpdateDelete, btnAdRoomUpdateExit, btnAdRoomUpdateTakePhoto, btnAdRoomUpdateChoosePhoto;

    private ImageView ivAdRoomUpdateImage, ivAdRoomUpdateExit;


    ArrayList<Room> roomArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_update);
        //Connect layout
        initUI();

        //Receive Data from AdStuViewProfile
        receiveDataFromAdRoomViewDetails();

        //Set on View
        initView();


        //Button Choose Photo
        btnAdRoomUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdRoomUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdRoomUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //ImageView Exit
        ivAdRoomUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //Button Delete
        btnAdRoomUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminRoomUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this student account");
                builder.setMessage("Are you sure want to delete room " + roomArr.get(position).getRoomName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccStudent();
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
        btnAdRoomUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdRoomUpdateName)) {
                    edtAdRoomUpdateName.setError("Please enter room's name");
                }
                if (isEmptyEditText(edtAdRoomUpdateDes)) {
                    edtAdRoomUpdateDes.setError("Please enter room's description");
                }
                if (isEmptyEditText(edtAdRoomUpdatePrice)) {
                    edtAdRoomUpdatePrice.setError("Please enter room's price");
                }


                roomName = edtAdRoomUpdateName.getText().toString();
                roomPrice = edtAdRoomUpdatePrice.getText().toString();
                roomDes = edtAdRoomUpdateDes.getText().toString();

                if (roomName.length() > 0 && roomPrice.length() > 0 && roomDes.length() > 0) {
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
    }

    private void deleteAccStudent() {
        String currentImage;
        if (!roomArr.get(position).getRoomImage().equals("")) {
            currentImage = roomArr.get(position).getRoomImage();
            currentImage = currentImage.substring(currentImage.lastIndexOf("/"));
        } else {
            currentImage = "NO_IMAGE_ROOM_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteRoomData(roomArr.get(position).getRoomId(), currentImage);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("ROOM_DELETED_SUCCESSFUL")) {
                    Toast.makeText(AdminRoomUpdateActivity.this, "Deleted Room " + roomArr.get(position).getRoomName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminRoomUpdateActivity.this, AdminRoomViewAllActivity.class);
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


    private void receiveDataFromAdRoomViewDetails() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ROOM_DATA_FROM_AD_ROOM_VIEW_DETAILS_TO_UPDATE");
        if (bundle != null) {
            roomArr = bundle.getParcelableArrayList("ROOM_DATA_ARRAY");
            position = bundle.getInt("ROOM_DATA_POSITION");
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
        retrofit2.Call<String> callbackPhoto = dataClient.UploadRoomPhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    roomImage = response.body();
                    Log.d("Updated Photo", roomImage);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Photo", t.getMessage());
            }
        });
    }

    private void uploadInfo() {
        String currentImage, newImage;
        if (roomArr.get(position).getRoomImage().equals("")) {
            //curAva = "", newAva=""
            currentImage = "NO_CURRENT_IMAGE_ROOM_UPDATE";
            if (realPath.equals("")) {
                newImage = "";
            } else {
                newImage = APIUtils.BASE_URL + "admin/room/images/" + roomImage;
            }
        } else {
            if (realPath.equals("")) {
                currentImage = "NO_CURRENT_IMAGE_ROOM_UPDATE";
                newImage = roomArr.get(position).getRoomImage();
            } else {
                currentImage = roomArr.get(position).getRoomImage();
                currentImage = currentImage.substring(currentImage.lastIndexOf("/"));
                newImage = APIUtils.BASE_URL + "admin/room/images/" + roomImage;
            }
        }

        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.AdminUpdateRoomData(roomArr.get(position).getRoomId(),
                roomName, roomPrice, roomDes, newImage, currentImage);
        roomArr.get(position).setRoomName(roomName);
        roomArr.get(position).setRoomPrice(roomPrice);
        roomArr.get(position).setRoomDes(roomDes);
        roomArr.get(position).setRoomImage(newImage);

        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Room Info", result);
                if (result.trim().equals("ROOM_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(AdminRoomUpdateActivity.this, "Successfully Updated Room Information " + roomName, Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Err Updated Room Info", t.getMessage());
            }
        });
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = new Intent(AdminRoomUpdateActivity.this, AdminRoomViewDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ROOM_DATA_ARRAY", roomArr);
        bundle.putInt("ROOM_DATA_POSITION", position);
        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
        intent.putExtra("ROOM_DATA_FROM_ROOM_ADAPTER_TO_AD_ROOM_VIEW_DETAILS", bundle);
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
                    ivAdRoomUpdateImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdRoomUpdateImage.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivAdRoomUpdateImage.getDrawable()).getBitmap();
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
        ivAdRoomUpdateImage = findViewById(R.id.iv_ad_room_update_image);
        ivAdRoomUpdateExit = findViewById(R.id.iv_ad_room_update_exit);

        edtAdRoomUpdateName = findViewById(R.id.edt_ad_room_update_name);
        edtAdRoomUpdatePrice = findViewById(R.id.edt_ad_room_update_price);
        edtAdRoomUpdateDes = findViewById(R.id.edt_ad_room_update_des);

        btnAdRoomUpdateSave = findViewById(R.id.btn_ad_room_update_save);
        btnAdRoomUpdateDelete = findViewById(R.id.btn_ad_room_update_delete);
        btnAdRoomUpdateExit = findViewById(R.id.btn_ad_room_update_exit);
        btnAdRoomUpdateTakePhoto = findViewById(R.id.btn_ad_room_update_take_photo);
        btnAdRoomUpdateChoosePhoto = findViewById(R.id.btn_ad_room_update_choose_photo);
    }

    private void initView() {
        edtAdRoomUpdateName.setText(roomArr.get(position).getRoomName());
        edtAdRoomUpdatePrice.setText(roomArr.get(position).getRoomPrice());
        edtAdRoomUpdateDes.setText(roomArr.get(position).getRoomDes());


        if (!roomArr.get(position).getRoomImage().equals("")) {
            Picasso.get()
                    .load(roomArr.get(position).getRoomImage())
                    .placeholder(R.drawable.bed)
                    .error(R.drawable.bed)
                    .into(ivAdRoomUpdateImage);
        } else {
            ivAdRoomUpdateImage.setImageResource(R.drawable.bed);
        }

    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}