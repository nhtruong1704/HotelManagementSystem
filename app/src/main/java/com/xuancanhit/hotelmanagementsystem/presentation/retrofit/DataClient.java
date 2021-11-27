package com.xuancanhit.hotelmanagementsystem.presentation.retrofit;

import com.xuancanhit.hotelmanagementsystem.presentation.model.Admin;
import com.xuancanhit.hotelmanagementsystem.presentation.model.Customer;

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
//    //Student
//    @FormUrlEncoded
//    @POST("insert.php")
//    Call<String> InsertCustomerData(@Field("StudentName") String StudentName,
//                                   @Field("StudentEmail") String StudentEmail,
//                                   @Field("StudentPassword") String StudentPassword,
//                                   @Field("StudentAvatar") String StudentAvatar);
//
//    @Multipart
//    @POST("uploadImage.php")
//    Call<String> UploadStudentPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("login.php")
    Call<List<Customer>> LoginCustomerData(@Field("CustomerEmail") String StudentEmail,
                                          @Field("CustomerPassword") String StudentPassword);

//    @FormUrlEncoded
//    @POST("update.php")
//    Call<String> UpdateCustomerData(@Field("StudentId") String StudentId,
//                                   @Field("StudentNo") String StudentNo,
//                                   @Field("StudentName") String StudentName,
//                                   @Field("StudentDOB") String StudentDOB,
//                                   @Field("StudentClass") String StudentClass,
//                                   @Field("StudentPhone") String StudentPhone,
//                                   @Field("StudentEmail") String StudentEmail,
//                                   @Field("StudentAvatar") String StudentAvatar,
//                                   @Field("StudentGender") String StudentGender,
//                                   @Field("StudentCurrentAvatar") String StudentCurrentAvatar);
//
//    @GET("delete.php")
//    Call<String> DeleteCustomerData(@Query("StudentId") String StudentId, @Query("StudentAvatar") String StudentAvatar);
//
//    @FormUrlEncoded
//    @POST("changePassword.php")
//    Call<String> ChangePasswordCustomerData(@Field("StudentId") String StudentId,
//                                           @Field("StudentNewPassword") String StudentNewPassword);
//
//    //Report
//    @FormUrlEncoded
//    @POST("report.php")
//    Call<String> ReportCustomerData(@Field("StudentId") String StudentId,
//                                   @Field("StudentReport") String StudentReport);
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
                                 @Field("AdminAvatar") String AdminAvatar,
                                 @Field("AdminCurrentAvatar") String AdminCurrentAvatar);
//
//    @FormUrlEncoded
//    @POST("admin/forgotPassword.php")
//    Call<String> ForgotPasswordAdminData(@Field("AdminId") String AdminId,
//                                         @Field("AdminEmail") String AdminEmail,
//                                         @Field("AdminNewPassword") String AdminNewPassword);
//
    @GET("admin/delete.php")
    Call<String> DeleteAdminData(@Query("AdminId") String AdminId, @Query("AdminAvatar") String AdminAvatar);
//
//    @FormUrlEncoded
//    @POST("admin/changePassword.php")
//    Call<String> ChangePasswordAdminData(@Field("AdminId") String AdminId,
//                                         @Field("AdminNewPassword") String AdminNewPassword);
//
//    //Admin Manager
//    //Add Student
//    @FormUrlEncoded
//    @POST("admin/addStudent.php")
//    Call<String> AdminAddCustomerData(@Field("StudentNo") String StudentNo,
//                                     @Field("StudentName") String StudentName,
//                                     @Field("StudentDOB") String StudentDOB,
//                                     @Field("StudentClass") String StudentClass,
//                                     @Field("StudentGender") String StudentGender,
//                                     @Field("StudentPhone") String StudentPhone,
//                                     @Field("StudentEmail") String StudentEmail,
//                                     @Field("StudentPassword") String StudentPassword,
//                                     @Field("StudentActive") String StudentActive,
//                                     @Field("StudentAvatar") String StudentAvatar);
//
//    //View All
//    @POST("admin/viewAllStu.php")
//    Call<List<Student>> AdminViewAllCustomerData();
//
//    //Update
//    @FormUrlEncoded
//    @POST("admin/updateStudent.php")
//    Call<String> AdminUpdateCustomerData(@Field("StudentId") String StudentId,
//                                        @Field("StudentNo") String StudentNo,
//                                        @Field("StudentName") String StudentName,
//                                        @Field("StudentDOB") String StudentDOB,
//                                        @Field("StudentClass") String StudentClass,
//                                        @Field("StudentPhone") String StudentPhone,
//                                        @Field("StudentEmail") String StudentEmail,
//                                        @Field("StudentAvatar") String StudentAvatar,
//                                        @Field("StudentGender") String StudentGender,
//                                        @Field("StudentPassword") String StudentPassword,
//                                        @Field("StudentActive") String StudentActive,
//                                        @Field("StudentCurrentAvatar") String StudentCurrentAvatar);
//
//    //Send Notice
//    @FormUrlEncoded
//    @POST("admin/sendNotice.php")
//    Call<String> AdminNoticeToCustomerData(@Field("StudentNotice") String StudentNotice);
//
//    //Reply
//    @FormUrlEncoded
//    @POST("admin/replyStudent.php")
//    Call<String> AdminReplyCustomerData(@Field("StudentId") String StudentId,
//                                       @Field("StudentReply") String StudentReply);
}
