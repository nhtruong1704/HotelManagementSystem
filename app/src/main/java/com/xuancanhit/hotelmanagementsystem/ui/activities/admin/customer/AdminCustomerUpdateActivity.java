package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer;

import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class AdminCustomerUpdateActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String customerName, customerPhone, customerAddress, customerEmail, customerPassword, customerDOB, customerAvatar;

    private EditText edtAdCusUpdateName, edtAdCusUpdateEmail, edtAdCusUpdatePassword, edtAdCusUpdateNo, edtAdCusUpdateDOB, edtAdCusUpdateAddress, edtAdCusUpdatePhone;
    private Button btnAdCusUpdateSave, btnAdCusUpdateDelete, btnAdCusUpdateExit, btnAdCusUpdateTakePhoto, btnAdCusUpdateChoosePhoto;
    private ImageButton imBtnAdCusUpdateDelDOB;
    private ImageView ivAdCusUpdateAvt, ivAdCusUpdateExit;

    private RadioGroup rgAdCusUpdateGender, rgAdCusUpdateStatus;
    private RadioButton rbAdCusUpdateMale, rbAdCusUpdateFemale, rbAdCusUpdateActive, rbAdCusUpdateInactive;
    String updateGender = "1", isVip = "0";

    //for date of birth
    final Calendar calendar = Calendar.getInstance();

    ArrayList<Customer> customerArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_update);

        //Connect layout
        initUI();

        //Receive Data from AdStuViewProfile
        receiveDataFromAdCusViewProfile();

        //Set on View
        initView();

        //Button Delete Date of birth
        imBtnAdCusUpdateDelDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAdCusUpdateDOB.setText("");
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
        edtAdCusUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AdminCustomerUpdateActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //RadioGroup Gender
        rgAdCusUpdateGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_cus_update_male) {
                    updateGender = "1";
                } else {
                    updateGender = "0";
                }
            }
        });

        //RadioGroup Status
        rgAdCusUpdateStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ad_cus_update_active_vip) {
                    isVip = "1";
                } else {
                    isVip = "0";
                }
            }
        });

        //Button Choose Photo
        btnAdCusUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdCusUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdCusUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //ImageView Exit
        ivAdCusUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //Button Delete
        btnAdCusUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCustomerUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this student account");
                builder.setMessage("Are you sure want to delete account customer " + customerArr.get(position).getCusName() + "?");
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
        btnAdCusUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdCusUpdateName)) {
                    edtAdCusUpdateName.setError("Please enter student's name");
                }
                if (isEmptyEditText(edtAdCusUpdatePassword)) {
                    edtAdCusUpdatePassword.setError("Please enter student's password");
                }
                if (isEmptyEditText(edtAdCusUpdateEmail)) {
                    edtAdCusUpdateEmail.setError("Please enter student's email");
                }

                if (isEmailValid(edtAdCusUpdateEmail)) {
                    customerName = edtAdCusUpdateName.getText().toString();
                    customerEmail = edtAdCusUpdateEmail.getText().toString();
                    customerPassword = edtAdCusUpdatePassword.getText().toString();

                    customerDOB = edtAdCusUpdateDOB.getText().toString();
                    customerAddress = edtAdCusUpdateAddress.getText().toString();
                    customerPhone = edtAdCusUpdatePhone.getText().toString();
                    if (customerName.length() > 0 && customerEmail.length() > 0 && customerPassword.length() > 0) {
                        if (!realPath.equals("")) {
                            uploadInfoWithPhoto();
                        } else {
                            uploadInfo();
                        }
                    }
                } else {
                    edtAdCusUpdateEmail.setError("Email address not valid");
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

    private void deleteAccStudent() {
        String currentAvatar;
        if (!customerArr.get(position).getCusAvatar().equals("")) {
            currentAvatar = customerArr.get(position).getCusAvatar();
            currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
        } else {
            currentAvatar = "NO_IMAGE_STUDENT_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteCustomerData(customerArr.get(position).getCusId(), currentAvatar);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("STUDENT_ACC_DELETED_SUCCESSFUL")) {
                    Toast.makeText(AdminCustomerUpdateActivity.this, "Deleted Customer " + customerArr.get(position).getCusName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminCustomerUpdateActivity.this, AdminCustomerViewAllActivity.class);
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


    private void receiveDataFromAdCusViewProfile() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("STUDENT_DATA_FROM_AD_STU_VIEW_PROFILE_TO_UPDATE");
        if (bundle != null) {
            customerArr = bundle.getParcelableArrayList("STUDENT_DATA_ARRAY");
            position = bundle.getInt("STUDENT_DATA_POSITION");
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
        retrofit2.Call<String> callbackPhoto = dataClient.UploadCustomerPhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    customerAvatar = response.body();
                    Log.d("Updated Stuc Photo", customerAvatar);
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
        String currentAvatar, newAvatar;
        if (customerArr.get(position).getCusAvatar().equals("")) {
            //curAva = "", newAva=""
            currentAvatar = "NO_CURRENT_IMAGE_STUDENT_UPDATE";
            if (realPath.equals("")) {
                newAvatar = "";
            } else {
                newAvatar = APIUtils.BASE_URL + "images/" + customerAvatar;
            }
        } else {
            if (realPath.equals("")) {
                currentAvatar = "NO_CURRENT_IMAGE_STUDENT_UPDATE";
                newAvatar = customerArr.get(position).getCusAvatar();
            } else {
                currentAvatar = customerArr.get(position).getCusAvatar();
                currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
                newAvatar = APIUtils.BASE_URL + "images/" + customerAvatar;
            }
        }

        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.AdminUpdateCustomerData(customerArr.get(position).getCusId(),
                customerName, customerPhone, customerAddress, customerEmail, isVip, customerPassword, newAvatar, currentAvatar, customerDOB, updateGender);
        customerArr.get(position).setCusName(customerName);
        customerArr.get(position).setCusPhone(customerPhone);
        customerArr.get(position).setCusAddress(customerAddress);
        customerArr.get(position).setCusEmail(customerEmail);
        customerArr.get(position).setCusIsVip(isVip);
        customerArr.get(position).setCusPassword(customerPassword);
        customerArr.get(position).setCusAvatar(newAvatar);
        customerArr.get(position).setCusDOB(customerDOB);
        customerArr.get(position).setCusGender(updateGender);

        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated Stu Info", result);
                if (result.trim().equals("STUDENT_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(AdminCustomerUpdateActivity.this, "Successfully Updated Customer Information " + customerName, Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Stu Info", t.getMessage());
            }
        });
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = new Intent(AdminCustomerUpdateActivity.this, AdminCustomerViewProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", customerArr);
        bundle.putInt("STUDENT_DATA_POSITION", position);
        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
        intent.putExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE", bundle);
        startActivity(intent);
        finish();
    }

    //Label for date of birth
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtAdCusUpdateDOB.setText(sdf.format(calendar.getTime()));
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
                    ivAdCusUpdateAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdCusUpdateAvt.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivAdCusUpdateAvt.getDrawable()).getBitmap();
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
        ivAdCusUpdateAvt = findViewById(R.id.iv_ad_stu_update_avt);
        ivAdCusUpdateExit = findViewById(R.id.iv_ad_cus_update_exit);

        edtAdCusUpdateName = findViewById(R.id.edt_ad_cus_update_name);
        edtAdCusUpdateDOB = findViewById(R.id.edt_ad_cus_update_dob);
        edtAdCusUpdatePhone = findViewById(R.id.edt_ad_cus_update_phone);
        edtAdCusUpdateEmail = findViewById(R.id.edt_ad_cus_update_email);
        edtAdCusUpdateAddress = findViewById(R.id.edt_ad_cus_update_address);
        edtAdCusUpdatePassword = findViewById(R.id.edt_ad_cus_update_password);

        rgAdCusUpdateGender = findViewById(R.id.rg_ad_cus_update_gender);
        rgAdCusUpdateStatus = findViewById(R.id.rg_ad_cus_update_vip);

        rbAdCusUpdateFemale = findViewById(R.id.rb_ad_cus_update_female);
        rbAdCusUpdateMale = findViewById(R.id.rb_ad_cus_update_male);
        rbAdCusUpdateActive = findViewById(R.id.rb_ad_cus_update_active_vip);
        rbAdCusUpdateInactive = findViewById(R.id.rb_ad_cus_update_inactive_vip);

        btnAdCusUpdateSave = findViewById(R.id.btn_ad_stu_update_save);
        btnAdCusUpdateDelete = findViewById(R.id.btn_ad_stu_update_delete);
        btnAdCusUpdateExit = findViewById(R.id.btn_ad_stu_update_exit);
        btnAdCusUpdateTakePhoto = findViewById(R.id.btn_ad_cus_update_take_photo);
        btnAdCusUpdateChoosePhoto = findViewById(R.id.btn_ad_cus_update_choose_photo);
        imBtnAdCusUpdateDelDOB = findViewById(R.id.im_btn_ad_cus_update_del_dob);
    }

    private void initView() {
        edtAdCusUpdateName.setText(customerArr.get(position).getCusName());
        edtAdCusUpdateDOB.setText(customerArr.get(position).getCusDOB());
        edtAdCusUpdatePhone.setText(customerArr.get(position).getCusPhone());
        edtAdCusUpdateEmail.setText(customerArr.get(position).getCusEmail());
        edtAdCusUpdateAddress.setText(customerArr.get(position).getCusAddress());
        edtAdCusUpdatePassword.setText(customerArr.get(position).getCusPassword());

        if (!customerArr.get(position).getCusGender().equals("-1")) {
            if (customerArr.get(position).getCusGender().equals("1")) {
                rbAdCusUpdateMale.setChecked(true);
                updateGender = "1";
            } else {
                rbAdCusUpdateFemale.setChecked(true);
                updateGender = "0";
            }
        }


        if (customerArr.get(position).getCusIsVip().equals("1")) {
            rbAdCusUpdateActive.setChecked(true);
            isVip = "1";
        } else {
            rbAdCusUpdateInactive.setChecked(true);
            isVip = "0";
        }


        if (!customerArr.get(position).getCusAvatar().equals("")) {
            Picasso.get()
                    .load(customerArr.get(position).getCusAvatar())
                    .placeholder(R.drawable.review)
                    .error(R.drawable.review)
                    .into(ivAdCusUpdateAvt);
        } else {
            if (!customerArr.get(position).getCusGender().equals("-1")) {
                if (customerArr.get(position).getCusGender().equals("1")) {
                    ivAdCusUpdateAvt.setImageResource(R.drawable.male);
                } else {
                    ivAdCusUpdateAvt.setImageResource(R.drawable.female);
                }
            } else {
                ivAdCusUpdateAvt.setImageResource(R.drawable.review);
            }
        }

    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}