package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer;

import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;

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

public class AdminCustomerAddActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String customerName, customerPhone, customerAddress, customerEmail, customerPassword, customerAvatar, customerDOB;

    private EditText edtAdCusAddName, edtAdCusAddEmail, edtAdCusAddPassword, edtAdCusAddAddress, edtAdCusAddDOB, edtAdCusAddClass, edtAdCusAddPhone;
    private Button btnAdCusAddSave, btnAdCusAddExit, btnAdCusAddTakePhoto, btnAdCusAddChoosePhoto;
    private ImageButton imBtnAdCusAddDelDOB;
    private ImageView ivAdCusAddAvt, ivAdCusAddExit;

    private RadioGroup rgAdCusAddGender, rgAdCusAddVip;
    private RadioButton rbAdCusAddMale, rbAdCusAddFemale, rbAdCusAddActiveVip, rbAdCusAddInactiveVip;
    String updateGender = "1", isVip = "0";

    //for date of birth
    final Calendar calendar = Calendar.getInstance();

    ArrayList<Customer> customerArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_add);

        //Connect layout
        initUI();

        //Button Delete Date of birth
        imBtnAdCusAddDelDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAdCusAddDOB.setText("");
            }
        });

        //Set click text view Date of birth
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        edtAdCusAddDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AdminCustomerAddActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //RadioGroup Gender
        rgAdCusAddGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_cus_add_male) {
                    updateGender = "1";
                } else {
                    updateGender = "0";
                }
            }
        });

        //RadioGroup Customer Vip
        rgAdCusAddVip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_cus_add_active_vip) {
                    isVip = "1";
                } else {
                    isVip = "0";
                }
            }
        });

        //Button Choose Photo
        btnAdCusAddChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdCusAddTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdCusAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ImageView Exit
        ivAdCusAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button Save
        btnAdCusAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdCusAddName)) {
                    edtAdCusAddName.setError("Please enter student's name");
                }
                if (isEmptyEditText(edtAdCusAddPassword)) {
                    edtAdCusAddPassword.setError("Please enter student's password");
                }
                if (isEmptyEditText(edtAdCusAddEmail)) {
                    edtAdCusAddEmail.setError("Please enter student's email");
                }

                if (isEmailValid(edtAdCusAddEmail)) {
                    customerName = edtAdCusAddName.getText().toString();
                    customerEmail = edtAdCusAddEmail.getText().toString();
                    customerPassword = edtAdCusAddPassword.getText().toString();
                    customerAddress = edtAdCusAddAddress.getText().toString();
                    customerDOB = edtAdCusAddDOB.getText().toString();
                    customerPhone = edtAdCusAddPhone.getText().toString();
                    if (customerName.length() > 0 && customerPassword.length() > 0 && customerEmail.length() > 0) {
                        if (!realPath.equals("")) {
                            uploadInfoWithPhoto();
                        } else {
                            uploadInfo();
                        }
                    }
                } else {
                    edtAdCusAddEmail.setError("Email address not valid");
                }

            }
        });
    }

    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        if (email.equals("")) return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
            callback = insertData.AdminAddCustomerData(customerName, customerPhone, customerAddress, customerEmail, isVip, customerPassword, APIUtils.BASE_URL + "images/" + customerAvatar, customerDOB, updateGender);
        } else {
            callback = insertData.AdminAddCustomerData(customerName, customerPhone, customerAddress, customerEmail, isVip, customerPassword, "NO_IMAGE_ADD_STUDENT", customerDOB, updateGender);
        }
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Ad Stu Add Info", result);
                if (result.trim().equals("ADD_STUDENT_SUCCESSFUL")) {
                    Toast.makeText(AdminCustomerAddActivity.this, "Customer " + customerName + " Added Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Ad Stu Add Info", t.getMessage());
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
        retrofit2.Call<String> callback = dataClient.UploadCustomerPhoto(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    customerAvatar = response.body();
                    uploadInfo();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Photo", t.getMessage());
            }
        });
    }


    //Label for date of birth
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtAdCusAddDOB.setText(sdf.format(calendar.getTime()));
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
                    ivAdCusAddAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdCusAddAvt.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivAdCusAddAvt.getDrawable()).getBitmap();
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
        ivAdCusAddAvt = findViewById(R.id.iv_ad_cus_add_avt);
        ivAdCusAddExit = findViewById(R.id.iv_ad_cus_add_exit);

        edtAdCusAddName = findViewById(R.id.edt_ad_cus_add_name);
        edtAdCusAddAddress = findViewById(R.id.edt_ad_cus_add_address);
        edtAdCusAddDOB = findViewById(R.id.edt_ad_cus_add_dob);
        edtAdCusAddPhone = findViewById(R.id.edt_ad_cus_add_phone);
        edtAdCusAddEmail = findViewById(R.id.edt_ad_cus_add_email);
        edtAdCusAddPassword = findViewById(R.id.edt_ad_cus_add_password);

        
        
        

        rgAdCusAddGender = findViewById(R.id.rg_ad_cus_add_gender);
        rgAdCusAddVip = findViewById(R.id.rg_ad_cus_add_vip);

        rbAdCusAddFemale = findViewById(R.id.rb_ad_cus_add_female);
        rbAdCusAddMale = findViewById(R.id.rb_ad_cus_add_male);
        rbAdCusAddActiveVip = findViewById(R.id.rb_ad_cus_add_active_vip);
        rbAdCusAddInactiveVip = findViewById(R.id.rb_ad_cus_add_inactive_vip);

        btnAdCusAddSave = findViewById(R.id.btn_ad_cus_add_save);
        btnAdCusAddExit = findViewById(R.id.btn_ad_cus_add_exit);
        btnAdCusAddTakePhoto = findViewById(R.id.btn_ad_cus_add_take_photo);
        btnAdCusAddChoosePhoto = findViewById(R.id.btn_ad_cus_add_choose_photo);
        imBtnAdCusAddDelDOB = findViewById(R.id.im_btn_ad_cus_add_del_dob);
    }
}