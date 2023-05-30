package com.xuancanhit.hotelmanagementsystem.ui.activities.customer;

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

import com.squareup.picasso.Picasso;
import com.xuancanhit.hotelmanagementsystem.R;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.APIUtils;
import com.xuancanhit.hotelmanagementsystem.presentation.retrofit.DataClient;
import com.xuancanhit.hotelmanagementsystem.ui.activities.admin.customer.AdminCustomerUpdateActivity;

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

public class CustomerUpdateActivity extends AppCompatActivity {

    public static final int CUSTOMER_CHANGE_PASSWORD_ACTIVITY = 2;
    public static final int RESULT_CHANGE_PASSWORD_OK = 3;
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;

    EditText edtCustomerUpdateEmail, edtCustomerUpdateName, edtCustomerUpdatePhone,edtCustomerUpdateDOB,edtCustomerUpdateAddress;
    Button btnCustomerUpdateTakePhoto, btnCustomerUpdateChoosePhoto, btnCustomerUpdateSave, btnCustomerUpdateDelete, btnCustomerUpdateExit, btnCustomerChangePassword;
    ImageView ivCustomerUpdateAvatar, ivCustomerUpdateExit;
    private ImageButton imBtnCustomerUpdateDelDOB;
    private RadioGroup rgCustomerUpdateGender, rgCustomerUpdateStatus;
    private RadioButton rbCustomerUpdateMale, rbCustomerUpdateFemale, rbCustomerUpdateActive, rbCustomerUpdateInactive;
    String updateGender = "1", isVip = "0";

    //for date of birth
    final Calendar calendar = Calendar.getInstance();
    ArrayList<Customer> customerArr;
    String customerEmail, customerName, customerAvatar, customerPhone, customerAddress,customerDOB,customerGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_update);

        //Connect layout
        initUI();
        //Set on Views



        initView();


        imBtnCustomerUpdateDelDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCustomerUpdateDOB.setText("");
            }
        });

        //Button Delete
        btnCustomerUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this customer account");
                builder.setMessage("Are you sure want to delete account customer " + customerArr.get(0).getCusName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccCustomer();
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

        //Button Change Password
//        btnCustomerChangePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CustomerUpdateActivity.this, CustomerChangePasswordActivity.class);
//                intent.putExtra("CUSTOMER_DATA_FROM_UPDATE_TO_CHANGE_PASSWORD", customerArr);
//                startActivityForResult(intent, CUSTOMER_CHANGE_PASSWORD_ACTIVITY);
//            }
//        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        edtCustomerUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CustomerUpdateActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        rgCustomerUpdateGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_cus_update_male) {
                    updateGender = "1";
                } else {
                    updateGender = "0";
                }
            }
        });


        //Button Save
        btnCustomerUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtCustomerUpdateName)) {
                    edtCustomerUpdateName.setError("Please enter customer's name");
                }
                if (isEmptyEditText(edtCustomerUpdateEmail)) {
                    edtCustomerUpdateEmail.setError("Please enter customer's email");
                }

                if (isEmailValid(edtCustomerUpdateEmail)) {
                    customerName = edtCustomerUpdateName.getText().toString();
                    customerEmail = edtCustomerUpdateEmail.getText().toString();
                    customerPhone = edtCustomerUpdatePhone.getText().toString();
                    customerDOB = edtCustomerUpdateDOB.getText().toString();
                    customerAddress = edtCustomerUpdateAddress.getText().toString();

                    if (customerName.length() > 0 && customerEmail.length() > 0) {
                        if (!realPath.equals("")) {
                            uploadInfoWithPhoto();
                        } else {
                            uploadInfo();
                        }
                    }
                }
                else {
                    edtCustomerUpdateEmail.setError("Email address not valid");
                }

            }
        });

        //Button Exit
        btnCustomerUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivCustomerUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button Choose Photo
        btnCustomerUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnCustomerUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
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

    private void deleteAccCustomer() {
        String currentAvatar;
        if (!customerArr.get(0).getCusAvatar().equals("")) {
            currentAvatar = customerArr.get(0).getCusAvatar();
            currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
        } else {
            currentAvatar = "NO_IMAGE_CUSTOMER_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteCustomerData(customerArr.get(0).getCusId(), currentAvatar);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res.trim().equals("CUSTOMER_ACC_DELETED_SUCCESSFUL")) {
                    Toast.makeText(CustomerUpdateActivity.this, "Deleted Customer " + customerArr.get(0).getCusName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CustomerUpdateActivity.this, CustomerLoginActivity.class);
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
                    Log.d("Updated Ad Photo", customerAvatar);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Ad Photo", t.getMessage());
            }
        });

    }

    private void uploadInfo() {
        String currentAvatar, newAvatar;
        if (customerArr.get(0).getCusAvatar().equals("")) {
            currentAvatar = "NO_DELETE_CURRENT_IMAGE";
            if (realPath.equals("")) {
                newAvatar = "";
            } else {
                newAvatar = APIUtils.BASE_URL + "customer/images/" + customerAvatar;
            }
        } else {

            if (realPath.equals("")) {
                currentAvatar = "NO_DELETE_CURRENT_IMAGE";
                newAvatar = customerArr.get(0).getCusAvatar();
            } else {
                currentAvatar = customerArr.get(0).getCusAvatar();
                currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/")+1);
                newAvatar = APIUtils.BASE_URL + "customer/images/" + customerAvatar;
            }
        }


        DataClient insertData = APIUtils.getData();
        Call<String> callbackInfo = insertData.UpdateCustomerData(customerArr.get(0).getCusId(), customerEmail, customerName, customerPhone,customerDOB, customerAddress,customerGender, newAvatar, currentAvatar);
        customerArr.get(0).setCusEmail(customerEmail);
        customerArr.get(0).setCusName(customerName);
        customerArr.get(0).setCusPhone(customerPhone);
        customerArr.get(0).setCusDOB(customerDOB);
        customerArr.get(0).setCusAddress(customerAddress);
        customerArr.get(0).setCusGender(customerGender);
        customerArr.get(0).setCusAvatar(newAvatar);
        callbackInfo.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("Updated cus Info", result);
                if (result.trim().equals("CUSTOMER_UPDATE_SUCCESSFUL")) {
                    Toast.makeText(CustomerUpdateActivity.this, "Successfully Updated Customer Information", Toast.LENGTH_SHORT).show();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Cus Info", t.getMessage());
            }
        });
    }


//    private void initView() {
//        Intent intent = getIntent();
//        customerArr = intent.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_VIEW_PROFILE_TO_UPDATE");
//        edtCustomerUpdateName.setText(customerArr.get(0).getCusName());
//        edtCustomerUpdateEmail.setText(customerArr.get(0).getCusEmail());
//        edtCustomerUpdatePhone.setText(customerArr.get(0).getCusPhone());
//        if (!customerArr.get(0).getCusAvatar().equals("")) {
//            Picasso.get()
//                    .load(customerArr.get(0).getCusAvatar())
//                    .placeholder(R.drawable.admin)
//                    .error(R.drawable.admin)
//                    .into(ivCustomerUpdateAvatar);
//        }
//    }

//    private void initView() {
//        Intent intent = getIntent();
//        customerArr = intent.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_VIEW_PROFILE_TO_UPDATE");
//
//        if (customerArr != null && customerArr.size() > 0) {
//            edtCustomerUpdateName.setText(customerArr.get(0).getCusName());
//            edtCustomerUpdateEmail.setText(customerArr.get(0).getCusEmail());
//            edtCustomerUpdatePhone.setText(customerArr.get(0).getCusPhone());
//            if (!customerArr.get(0).getCusAvatar().equals("")) {
//                Picasso.get()
//                        .load(customerArr.get(0).getCusAvatar())
//                        .placeholder(R.drawable.review)
//                        .error(R.drawable.review)
//                        .into(ivCustomerUpdateAvatar);
//            }
//        }
//    }

    private void initView() {
        Intent intent = getIntent();
        customerArr = intent.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_VIEW_PROFILE_TO_UPDATE");
        if (customerArr != null && customerArr.size() > 0) {
            edtCustomerUpdateName.setText(customerArr.get(0).getCusName());
            edtCustomerUpdateDOB.setText(customerArr.get(0).getCusDOB());
            edtCustomerUpdatePhone.setText(customerArr.get(0).getCusPhone());
            edtCustomerUpdateEmail.setText(customerArr.get(0).getCusEmail());
            edtCustomerUpdateAddress.setText(customerArr.get(0).getCusAddress());


            if (!customerArr.get(0).getCusGender().equals("-1")) {
                if (customerArr.get(0).getCusGender().equals("1")) {
                    rbCustomerUpdateMale.setChecked(true);
                    updateGender = "1";
                } else {
                    rbCustomerUpdateFemale.setChecked(true);
                    updateGender = "0";
                }
            }

//
//            if (customerArr.get(0).getCusIsVip().equals("1")) {
//                rbCustomerUpdateActive.setChecked(true);
//                isVip = "1";
//            } else {
//                rbCustomerUpdateInactive.setChecked(true);
//                isVip = "0";
//            }


            if (!customerArr.get(0).getCusAvatar().equals("")) {
                Picasso.get()
                        .load(customerArr.get(0).getCusAvatar())
                        .placeholder(R.drawable.review)
                        .error(R.drawable.review)
                        .into(ivCustomerUpdateAvatar);
            } else {
                if (!customerArr.get(0).getCusGender().equals("-1")) {
                    if (customerArr.get(0).getCusGender().equals("1")) {
                        ivCustomerUpdateAvatar.setImageResource(R.drawable.male);
                    } else {
                        ivCustomerUpdateAvatar.setImageResource(R.drawable.female);
                    }
                } else {
                    ivCustomerUpdateAvatar.setImageResource(R.drawable.review);
                }
            }
        }
    }






    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtCustomerUpdateDOB.setText(sdf.format(calendar.getTime()));
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
                    ivCustomerUpdateAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivCustomerUpdateAvatar.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
            }
        }

        //Change password
        else if (resultCode == RESULT_CHANGE_PASSWORD_OK) {
            if (requestCode == CUSTOMER_CHANGE_PASSWORD_ACTIVITY) {
                customerArr = data.getParcelableArrayListExtra("CUSTOMER_DATA_FROM_CHANGE_PASSWORD_TO_UPDATE");
            }
        }
    }

    // Save image(from image view) when take photo
    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable) ivCustomerUpdateAvatar.getDrawable()).getBitmap();
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
        edtCustomerUpdateEmail = findViewById(R.id.edt_cus_update_email);
        edtCustomerUpdateName = findViewById(R.id.edt_cus_update_name);
        edtCustomerUpdatePhone = findViewById(R.id.edt_cus_update_phone);
        edtCustomerUpdateDOB = findViewById(R.id.edt_cus_update_dob);
        edtCustomerUpdateAddress = findViewById(R.id.edt_cus_update_address);
        btnCustomerUpdateChoosePhoto = findViewById(R.id.btn_cus_update_choose_photo);
        btnCustomerUpdateTakePhoto = findViewById(R.id.btn_cus_update_take_photo);
        btnCustomerUpdateSave = findViewById(R.id.btn_cus_update_save);
        btnCustomerUpdateDelete = findViewById(R.id.btn_cus_update_delete);
        btnCustomerUpdateExit = findViewById(R.id.btn_cus_update_exit);
        ivCustomerUpdateAvatar = findViewById(R.id.iv_cus_update_avt);
        ivCustomerUpdateExit = findViewById(R.id.iv_cus_update_exit);
        rgCustomerUpdateGender = findViewById(R.id.rg_cus_update_gender);
        imBtnCustomerUpdateDelDOB = findViewById(R.id.im_btn_cus_update_del_dob);
        rbCustomerUpdateMale = findViewById(R.id.rb_cus_update_male);
        rbCustomerUpdateFemale = findViewById(R.id.rb_cus_update_female);
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

    @Override
    public void onBackPressed() {
        backToMenu();
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = getIntent();
        intent.putExtra("CUSTOMER_DATA_FROM_UPDATE_TO_MENU", customerArr);
        setResult(CustomerMenuActivity.RESULT_OK, intent);
        finish();
    }
}