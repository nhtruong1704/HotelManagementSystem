package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Room;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.room.AdminRoomAddActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRoomAddActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String roomName, roomPrice, roomDes, roomImage;

    private EditText edtAdRoomAddName, edtAdRoomAddPrice, edtAdRoomAddDes;
    private Button btnAdRoomAddSave, btnAdRoomAddExit, btnAdRoomAddTakePhoto, btnAdRoomAddChoosePhoto;

    private ImageView ivAdRoomAddImage, ivAdRoomAddExit;

    

    ArrayList<Room> roomArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_add);

        //Connect layout
        initUI();



        //Button Choose Photo
        btnAdRoomAddChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdRoomAddTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdRoomAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ImageView Exit
        ivAdRoomAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button Save
        btnAdRoomAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdRoomAddName)) {
                    edtAdRoomAddName.setError("Please enter room's name");
                }
                if (isEmptyEditText(edtAdRoomAddPrice)) {
                    edtAdRoomAddPrice.setError("Please enter room's price");
                }
                if (isEmptyEditText(edtAdRoomAddDes)) {
                    edtAdRoomAddDes.setError("Please enter room's description");
                }

                    roomName = edtAdRoomAddName.getText().toString();
                    roomPrice = edtAdRoomAddPrice.getText().toString();
                    roomDes = edtAdRoomAddDes.getText().toString();
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

    private void uploadInfo() {
        DataClient insertData = APIUtils.getData();
        Call<String> callback;
        if (!realPath.equals("")) {
            callback = insertData.AdminAddRoomData(roomName, roomPrice, roomDes, APIUtils.BASE_URL + "admin/room/images/" + roomImage);
        } else {
            callback = insertData.AdminAddRoomData(roomName, roomPrice, roomDes, "NO_IMAGE_ADD_ROOM");
        }
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Ad Room Add Info", result);
                if (result.trim().equals("ADD_ROOM_SUCCESSFUL")) {
                    Toast.makeText(AdminRoomAddActivity.this, "Room " + roomName + " Added Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Ad Room Add Info", t.getMessage());
            }
        });
    }

    private void uploadInfoWithPhoto() {
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arrayNamePhoto = file_path.split("\\.");
        file_path = arrayNamePhoto[0] + "_" + System.currentTimeMillis() + "." + arrayNamePhoto[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file_path, requestBody);
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.UploadRoomPhoto(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    roomImage = response.body();
                    uploadInfo();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Err Updated Room Photo", t.getMessage());
            }
        });
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
                    ivAdRoomAddImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdRoomAddImage.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivAdRoomAddImage.getDrawable()).getBitmap();
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
        ivAdRoomAddImage = findViewById(R.id.iv_ad_room_add_image);
        ivAdRoomAddExit = findViewById(R.id.iv_ad_room_add_exit);

        edtAdRoomAddName = findViewById(R.id.edt_ad_room_add_name);
        edtAdRoomAddPrice = findViewById(R.id.edt_ad_room_add_price);
        edtAdRoomAddDes = findViewById(R.id.edt_ad_room_add_des);


        btnAdRoomAddSave = findViewById(R.id.btn_ad_room_add_save);
        btnAdRoomAddExit = findViewById(R.id.btn_ad_room_add_exit);
        btnAdRoomAddTakePhoto = findViewById(R.id.btn_ad_room_add_take_photo);
        btnAdRoomAddChoosePhoto = findViewById(R.id.btn_ad_room_add_choose_photo);
    }
}