package com.xuancanhit.hotelmanagementsystem.presentation.retrofit;

import com.xuancanhit.hotelmanagementsystem.presentation.model.Admin;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Employee;
import com.xuancanhit.hotelmanagementsystem.presentation.model.FoodItem;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Room;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

//Config Method Send Data To Server
public interface DataClient {
    //    //Customer
//    @FormUrlEncoded
//    @POST("insert.php")
//    Call<String> InsertCustomerData(@Field("CustomerName") String CustomerName,
//                                   @Field("CustomerEmail") String CustomerEmail,
//                                   @Field("CustomerPassword") String CustomerPassword,
//                                   @Field("CustomerAvatar") String CustomerAvatar);
//
    //customer
    @Multipart
    @POST("uploadImage.php")
    Call<String> UploadCustomerPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("login.php")
    Call<List<Customer>> LoginCustomerData(@Field("CustomerEmail") String CustomerEmail,
                                           @Field("CustomerPassword") String CustomerPassword);

    @FormUrlEncoded
    @POST("admin/updateCustomer.php")
    Call<String> UpdateCustomerData(@Field("CustomerId") String CustomerId,
                                   @Field("CustomerName") String CustomerName,
                                    @Field("CustomerPhone") String CustomerPhone,
                                   @Field("CustomerDOB") String CustomerDOB,
                                   @Field("CustomerAddress") String CustomerAddress,
                                   @Field("CustomerEmail") String CustomerEmail,
                                   @Field("CustomerAvatar") String CustomerAvatar,
                                   @Field("CustomerGender") String CustomerGender,
                                   @Field("CustomerCurrentAvatar") String CustomerCurrentAvatar);

    @GET("delete.php")
    Call<String> DeleteCustomerData(@Query("CustomerId") String CustomerId, @Query("CustomerAvatar") String CustomerAvatar);

    //
//    @FormUrlEncoded
//    @POST("changePassword.php")
//    Call<String> ChangePasswordCustomerData(@Field("CustomerId") String CustomerId,
//                                           @Field("CustomerNewPassword") String CustomerNewPassword);
//
//    //Report
//    @FormUrlEncoded
//    @POST("report.php")
//    Call<String> ReportCustomerData(@Field("CustomerId") String CustomerId,
//                                   @Field("CustomerReport") String CustomerReport);
//
    //Admin
    @FormUrlEncoded
    @POST("admin/login.php")
    Call<List<Admin>> LoginAdminData(@Field("AdminEmail") String AdminEmail,
                                     @Field("AdminPassword") String AdminPassword);

    @Multipart
    @POST("admin/uploadImage.php")
    Call<String> UploadAdminPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("admin/update.php")
    Call<String> UpdateAdminData(@Field("AdminId") String AdminId,
                                 @Field("AdminEmail") String AdminEmail,
                                 @Field("AdminName") String AdminName,
                                 @Field("AdminPhone") String AdminPhone,
                                 @Field("AdminAvatar") String AdminAvatar,
                                 @Field("AdminCurrentAvatar") String AdminCurrentAvatar);

    @FormUrlEncoded
    @POST("admin/forgotPassword.php")
    Call<String> ForgotPasswordAdminData(@Field("AdminId") String AdminId,
                                         @Field("AdminEmail") String AdminEmail,
                                         @Field("AdminNewPassword") String AdminNewPassword);

    @GET("admin/delete.php")
    Call<String> DeleteAdminData(@Query("AdminId") String AdminId, @Query("AdminAvatar") String AdminAvatar);

    @FormUrlEncoded
    @POST("admin/changePassword.php")
    Call<String> ChangePasswordAdminData(@Field("AdminId") String AdminId,
                                         @Field("AdminNewPassword") String AdminNewPassword);

    //Admin Manager
    //Add Customer
    @FormUrlEncoded
    @POST("admin/addCustomer.php")
    Call<String> AdminAddCustomerData(@Field("CustomerName") String CustomerName,
                                      @Field("CustomerPhone") String CustomerPhone,
                                      @Field("CustomerAddress") String CustomerAddress,
                                      @Field("CustomerEmail") String CustomerEmail,
                                      @Field("CustomerIsVip") String CustomerIsVip,
                                      @Field("CustomerPassword") String CustomerPassword,
                                      @Field("CustomerAvatar") String CustomerAvatar,
                                      @Field("CustomerDOB") String CustomerDOB,
                                      @Field("CustomerGender") String CustomerGender);

    //View All Customer
    @POST("admin/viewAllCustomer.php")
    Call<List<Customer>> AdminViewAllCustomerData();


    //Update
    @FormUrlEncoded
    @POST("admin/updateCustomer.php")
    Call<String> AdminUpdateCustomerData(@Field("CustomerId") String CustomerId,
                                         @Field("CustomerName") String CustomerName,
                                         @Field("CustomerPhone") String CustomerPhone,
                                         @Field("CustomerAddress") String CustomerAddress,
                                         @Field("CustomerEmail") String CustomerEmail,
                                         @Field("CustomerIsVip") String CustomerIsVip,
                                         @Field("CustomerPassword") String CustomerPassword,
                                         @Field("CustomerAvatar") String CustomerAvatar,
                                         @Field("CustomerCurrentAvatar") String CustomerCurrentAvatar,
                                         @Field("CustomerDOB") String CustomerDOB,
                                         @Field("CustomerGender") String CustomerGender);





    //Food
    @POST("admin/food/viewAllFood.php")
    Call<List<FoodItem>> AdminViewAllFoodItemData();

    @FormUrlEncoded
    @POST("admin/food/addFood.php")
    Call<String> AdminAddFoodItemData(
            @Field("FoodItemName") String FoodItemName,
            @Field("FoodItemPrice") String FoodItemPrice,
            @Field("FoodItemDes") String FoodItemDes,
            @Field("FoodItemImage") String FoodItemImage);

    @Multipart
    @POST("admin/food/uploadImage.php")
    Call<String> UploadFoodItemPhoto(@Part MultipartBody.Part photo);

    @GET("admin/food/delete.php")
    Call<String> DeleteFoodItemData(@Query("FoodItemId") String FoodItemId, @Query("FoodItemImage") String FoodItemImage);

    @FormUrlEncoded
    @POST("admin/food/updateFood.php")
    Call<String> AdminUpdateFoodItemData(@Field("FoodItemId") String FoodItemId,
                                     @Field("FoodItemName") String FoodItemName,
                                     @Field("FoodItemPrice") String FoodItemPrice,
                                     @Field("FoodItemDes") String FoodItemDes,
                                     @Field("FoodItemImage") String FoodItemImage,
                                     @Field("FoodItemCurrentImage") String FoodItemCurrentImage);



//employee
    @POST("admin/employee/viewAllEmployee.php")
    Call<List<Employee>> AdminViewAllEmployeeData();

    @FormUrlEncoded
    @POST("admin/employee/addEmployee.php")
    Call<String> AdminAddEmployeeData(
            @Field("EmployeeName") String EmployeeName,
            @Field("EmployeePosition") String EmployeePosition,
            @Field("EmployeeSalary") String EmployeeSalary,
            @Field("EmployeePhone") String EmployeePhone,
            @Field("EmployeeAvatar") String EmployeeAvatar);

    @Multipart
    @POST("admin/employee/uploadImage.php")
    Call<String> UploadEmployeePhoto(@Part MultipartBody.Part photo);

    @GET("admin/employee/deleteEmployee.php")
    Call<String> DeleteEmployeeData(@Query("EmployeeId") String EmployeeId, @Query("EmployeeAvatar") String EmployeeAvatar);

    @FormUrlEncoded
    @POST("admin/employee/updateEmployee.php")
    Call<String> AdminUpdateEmployeeData(@Field("EmployeeId") String EmployeeId,
                                         @Field("EmployeeName") String EmployeeName,
                                         @Field("EmployeePosition") String EmployeePosition,
                                         @Field("EmployeeSalary") String EmployeeSalary,
                                         @Field("EmployeePhone") String EmployeePhone,
                                         @Field("EmployeeAvatar") String EmployeeAvatar,
                                         @Field("EmployeeCurrentAvatar") String EmployeeCurrentAvatar);



    //Room
    @POST("admin/room/viewAllRoom.php")
    Call<List<Room>> AdminViewAllRoomData();

    @FormUrlEncoded
    @POST("admin/room/addRoom.php")
    Call<String> AdminAddRoomData(
            @Field("RoomName") String RoomName,
            @Field("RoomPrice") String RoomPrice,
            @Field("RoomDes") String RoomDes,
            @Field("RoomImage") String RoomImage);

    @Multipart
    @POST("admin/room/uploadImage.php")
    Call<String> UploadRoomPhoto(@Part MultipartBody.Part photo);

    @GET("admin/room/delete.php")
    Call<String> DeleteRoomData(@Query("RoomId") String RoomId, @Query("RoomImage") String RoomImage);


    //Update
    @FormUrlEncoded
    @POST("admin/room/updateRoom.php")
    Call<String> AdminUpdateRoomData(@Field("RoomId") String RoomId,
                                     @Field("RoomName") String RoomName,
                                     @Field("RoomPrice") String RoomPrice,
                                     @Field("RoomDes") String RoomDes,
                                     @Field("RoomImage") String RoomImage,
                                     @Field("RoomCurrentImage") String RoomCurrentImage);





}
