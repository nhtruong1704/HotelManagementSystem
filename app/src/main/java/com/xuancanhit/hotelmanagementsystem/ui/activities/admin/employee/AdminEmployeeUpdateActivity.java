package com.xuancanhit.hotelmanagementsystem.ui.activities.admin.employee;

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
import com.xuancanhit.hotelmanagementsystem.presentation.model.Employee;
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

public class AdminEmployeeUpdateActivity extends AppCompatActivity {


    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    String realPath = "";
    Uri imageUri;
    String employeeName, employeePosition, employeeSalary, employeePhone, employeeAvatar;

    private EditText edtAdEmpUpdateName, edtAdEmpUpdatePosition, edtAdEmpUpdateSalary, edtAdEmpUpdatePhone;
    private Button btnAdEmpUpdateSave, btnAdEmpUpdateDelete, btnAdEmpUpdateExit, btnAdEmpUpdateTakePhoto, btnAdEmpUpdateChoosePhoto;

    private ImageView ivAdEmpUpdateAvt, ivAdEmpUpdateExit;


    ArrayList<Employee> employeeArr;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_update);

        //Connect layout
        initUI();

        //Receive Data from AdStuViewProfile
        receiveDataFromAdEmpViewProfile();

        //Set on View
        initView();


        //Button Choose Photo
        btnAdEmpUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnAdEmpUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit
        btnAdEmpUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //ImageView Exit
        ivAdEmpUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                //Check xem co can gui data
            }
        });

        //Button Delete
        btnAdEmpUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminEmployeeUpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete this employee");
                builder.setMessage("Are you sure want to delete this employee " + employeeArr.get(position).getEmpName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEmployee();
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
        btnAdEmpUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyEditText(edtAdEmpUpdateName)) {
                    edtAdEmpUpdateName.setError("Please enter employee's name");
                    return;
                }
                if (isEmptyEditText(edtAdEmpUpdatePosition)) {
                    edtAdEmpUpdatePosition.setError("Please enter employee's position");
                    return;
                }
                if (isEmptyEditText(edtAdEmpUpdateSalary)) {
                    edtAdEmpUpdateSalary.setError("Please enter employee's salary");
                    return;
                }
                if (isEmptyEditText(edtAdEmpUpdatePhone)) {
                    edtAdEmpUpdatePhone.setError("Please enter employee's phone number");
                    return;
                }

                employeeName = edtAdEmpUpdateName.getText().toString();
                employeePosition = edtAdEmpUpdatePosition.getText().toString();
                employeeSalary = edtAdEmpUpdateSalary.getText().toString();
                employeePhone = edtAdEmpUpdatePhone.getText().toString();
                if (employeeName.length() > 0 && employeePosition.length() > 0 && employeeSalary.length() > 0) {
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

    private void deleteEmployee() {
        String currentAvatar;
        if (!employeeArr.get(position).getEmpAvatar().equals("")) {
            currentAvatar = employeeArr.get(position).getEmpAvatar();
            currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/"));
        } else {
            currentAvatar = "NO_CURRENT_IMAGE_EMPLOYEE_UPDATE";
        }
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<String> callback = dataClient.DeleteEmployeeData(employeeArr.get(position).getEmpId(), currentAvatar);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                Toast.makeText(AdminEmployeeUpdateActivity.this, res.trim(), Toast.LENGTH_SHORT).show();
                if (res.trim().equals("EMPLOYEE_ACC_DELETED_SUCCESSFUL")) {
                    Toast.makeText(AdminEmployeeUpdateActivity.this, "Deleted Employee " + employeeArr.get(position).getEmpName() + " Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminEmployeeUpdateActivity.this, AdminEmployeeViewAllActivity.class);
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


    private void receiveDataFromAdEmpViewProfile() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("EMPLOYEE_DATA_FROM_AD_EMPLOYEE_VIEW_PROFILE_TO_UPDATE");
        if (bundle != null) {
            employeeArr = bundle.getParcelableArrayList("EMPLOYEE_DATA_ARRAY");
            position = bundle.getInt("EMPLOYEE_DATA_POSITION");
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
        retrofit2.Call<String> callbackPhoto = dataClient.UploadEmployeePhoto(body);
        callbackPhoto.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null) {
                    employeeAvatar = response.body();
                    Log.d("Updated Employee Photo", employeeAvatar);
                    uploadInfo();
                    backToMenu();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error Updated Emp Photo", t.getMessage());
            }
        });
    }

    private void uploadInfo() {
        String currentAvatar, newAvatar;
        if (employeeArr != null && position < employeeArr.size()) {
            if (employeeArr.get(position).getEmpAvatar().equals("")) {
                //curAva = "", newAva=""
                currentAvatar = "NO_CURRENT_IMAGE_EMPLOYEE_UPDATE";
                if (realPath.equals("")) {
                    newAvatar = "";
                } else {
                    newAvatar = APIUtils.BASE_URL + "admin/employee/images/" + employeeAvatar;
                }
            } else {
                if (realPath.equals("")) {
                    currentAvatar = "NO_CURRENT_IMAGE_EMPLOYEE_UPDATE";
                    newAvatar = employeeArr.get(position).getEmpAvatar();
                } else {
                    currentAvatar = employeeArr.get(position).getEmpAvatar();
                    currentAvatar = currentAvatar.substring(currentAvatar.lastIndexOf("/")+1);
                    newAvatar = APIUtils.BASE_URL + "admin/employee/images/" + employeeAvatar;
                }
            }

            DataClient insertData = APIUtils.getData();
            Call<String> callbackInfo = insertData.AdminUpdateEmployeeData(employeeArr.get(position).getEmpId(),
                    employeeName, employeePosition, employeeSalary, employeePhone, newAvatar, currentAvatar);
  //          if (position < employeeArr.size()) {
                employeeArr.get(position).setEmpName(employeeName);
                employeeArr.get(position).setEmpPosition(employeePosition);
                employeeArr.get(position).setEmpSalary(employeeSalary);
                employeeArr.get(position).setEmpPhone(employeePhone);
                employeeArr.get(position).setEmpAvatar(newAvatar);

      //      }
            callbackInfo.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();
                    Log.d("Updated Emp Info", result);
                    if (result.trim().equals("EMPLOYEE_UPDATE_SUCCESSFUL")) {
                        Toast.makeText(AdminEmployeeUpdateActivity.this, "Successfully Updated Employee Information " + employeeName, Toast.LENGTH_SHORT).show();
                        backToMenu();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Error Updated Emp Info", t.getMessage());
                }
            });
        }
    }

    //Send data to menu and end activity current
    private void backToMenu() {
        Intent intent = new Intent(AdminEmployeeUpdateActivity.this, AdminEmployeeViewProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("EMPLOYEE_DATA_ARRAY", employeeArr);
        bundle.putInt("EMPLOYEE_DATA_POSITION", position);
        // Data resend to AdStuViewProfile STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE - just receiver 1 time
        intent.putExtra("EMPLOYEE_DATA_FROM_EMPLOYEE_ADAPTER_TO_AD_EMPLOYEE_VIEW_PROFILE", bundle);
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
                    ivAdEmpUpdateAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivAdEmpUpdateAvt.setImageBitmap(bitmap);
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
        Bitmap bitmap = ((BitmapDrawable) ivAdEmpUpdateAvt.getDrawable()).getBitmap();
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
        ivAdEmpUpdateAvt = findViewById(R.id.iv_ad_emp_update_avt);
        ivAdEmpUpdateExit = findViewById(R.id.iv_ad_emp_update_exit);

        edtAdEmpUpdateName = findViewById(R.id.edt_ad_emp_update_name);
        edtAdEmpUpdatePosition = findViewById(R.id.edt_ad_emp_update_position);
        edtAdEmpUpdateSalary = findViewById(R.id.edt_ad_emp_update_salary);
        edtAdEmpUpdatePhone = findViewById(R.id.edt_ad_emp_update_phone);

        btnAdEmpUpdateSave = findViewById(R.id.btn_ad_emp_update_save);
        btnAdEmpUpdateDelete = findViewById(R.id.btn_ad_emp_update_delete);
        btnAdEmpUpdateExit = findViewById(R.id.btn_ad_emp_update_exit);
        btnAdEmpUpdateTakePhoto = findViewById(R.id.btn_ad_emp_update_take_photo);
        btnAdEmpUpdateChoosePhoto = findViewById(R.id.btn_ad_emp_update_choose_photo);

    }

//    private void initView() {
//        if (employeeArr != null && position < employeeArr.size()) {
//            edtAdEmpUpdateName.setText(employeeArr.get(position).getEmpName());
//            edtAdEmpUpdatePosition.setText(employeeArr.get(position).getEmpPosition());
//            edtAdEmpUpdateSalary.setText(employeeArr.get(position).getEmpSalary());
//            edtAdEmpUpdatePhone.setText(employeeArr.get(position).getEmpPhone());
//
//
//            if (!employeeArr.get(position).getEmpAvatar().equals("")) {
//                Picasso.get()
//                        .load(employeeArr.get(position).getEmpAvatar())
//                        .placeholder(R.drawable.admin)
//                        .error(R.drawable.admin)
//                        .into(ivAdEmpUpdateAvt);
//            } else {
//
//                ivAdEmpUpdateAvt.setImageResource(R.drawable.admin);
//            }
//        }
//    }

    private void initView() {
        if (employeeArr != null && position < employeeArr.size()) {
            Employee employee = employeeArr.get(position);

            edtAdEmpUpdateName.setText(employee.getEmpName());
            edtAdEmpUpdatePosition.setText(employee.getEmpPosition());
            edtAdEmpUpdateSalary.setText(employee.getEmpSalary());
            edtAdEmpUpdatePhone.setText(employee.getEmpPhone());

            String empAvatar = employee.getEmpAvatar();
            if (empAvatar != null && !empAvatar.equals("")) {
                Picasso.get()
                        .load(empAvatar)
                        .placeholder(R.drawable.employees)
                        .error(R.drawable.employees)
                        .into(ivAdEmpUpdateAvt);
            } else {
                ivAdEmpUpdateAvt.setImageResource(R.drawable.employees);
            }
        }
    }









    @Override
    public void onBackPressed() {
        backToMenu();
        super.onBackPressed();
    }
}