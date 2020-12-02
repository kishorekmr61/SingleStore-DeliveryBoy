package com.project.delieveryapp;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/Delivery/GetORUpdateOrders?")
    Call<List<CustomerDetail>> GetOrders(@Query("BranchID") String BranchID,
                                         @Query("StaffID") String StaffID,
                                         @Query("AssignID") String AssignID,
                                         @Query("Condition") String Condition);


    @GET("api/Delivery/GetORUpdateOrders?")
    Call<ResponseBody> updateOrders(@Query("BranchID") String BranchID,
                                    @Query("StaffID") String StaffID,
                                    @Query("AssignID") String AssignID,
                                    @Query("Condition") String Condition);

    @GET("api/Delivery/LoginUser?")
    Call<ResponseBody> LoginUser(@Query("mobileno") String mobileno,
                                 @Query("pwd") String pwd);

    @PUT("api/Grocerry/GrocOrderStatus?")
    Call<ResponseBody> GrocOrderStatus(@Query("orderid") String orderid,
                                       @Query("statusid") String statusid,
                                       @Query("vendorid") String vendorid,
                                       @Query("BranchID") String BranchID);

    @POST("api/Admin/VerifyStaff")
    @FormUrlEncoded
    Call<ResponseBody> VerifyStaff(@Field("MobileNo") String MobileNo);

    @POST("api/Admin/VerifyStaffOtp")
    @FormUrlEncoded
    Call<ResponseBody> VerifyStaffOtp(@Field("MobileNo") String MobileNo, @Field("Otp") String Otp);


    @POST("api/Admin/ChangeStaffPassword")
    @FormUrlEncoded
    Call<ResponseBody> ChangeStaffPassword(@Field("MobileNo") String MobileNo, @Field("Password") String Password);


    @GET("api/Admin/GetStaffByID?")
    Call<ResponseBody> GetStaffByID(@Query("MobileNo") String mobileno);

    @POST("api/Admin/UpdateStaffDeviceId")
    @FormUrlEncoded
    Call<ResponseBody> UpdateStaffDeviceId(@Field("UserId") String UserId, @Field("DeviceId") String DeviceId,
                                           @Field("DeviceType") String DeviceType, @Field("Type") String Type);


}
