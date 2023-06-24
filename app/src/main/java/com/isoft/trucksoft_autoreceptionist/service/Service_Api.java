package com.isoft.trucksoft_autoreceptionist.service;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service_Api {


    @Multipart
    @POST("chk_cc.php")
    Call<ResponseBody> myLogin(@Part("c_code") RequestBody c_code, @Part("cc") RequestBody cc);

    @Multipart
    @POST("users_details.php")
    Call<ResponseBody> getDispatcher(@Part("cc") RequestBody cc);

//    @Multipart
//    @POST("get_customers.php")
//    Call<ResponseBody> getsignindetail(@Part("cc") RequestBody cc);

    @Multipart
    @POST("get_returninguser.php")
    Call<ResponseBody> getreturninguser(@Part("cc") RequestBody cc,@Part("mobile") RequestBody mobile);


    @Multipart
    @POST("guestinsertdetail.php")//testdetails.php
   // @POST("testdetails.php")//testdetails.php
    Call<ResponseBody> saveGuestentrydetails(@Part("companyname") RequestBody companyname,
                                         @Part("name") RequestBody name
            , @Part("phone") RequestBody phone
            , @Part("employee") RequestBody employee
            , @Part("reason") RequestBody reason
            , @Part("ipaddress") RequestBody ipaddress
            , @Part("msgdetail") RequestBody msgdetail
            , @Part("cc") RequestBody cc
            , @Part("guestimg") RequestBody guestimg
            , @Part("msgdetailsms") RequestBody msgdetailsms
            , @Part("sign_detail") RequestBody sign_detail
            , @Part("rowid") RequestBody rowid
            , @Part("lat") RequestBody lat
            , @Part("lon") RequestBody lon
            , @Part("address") RequestBody address
            , @Part("empname") RequestBody empname
            , @Part("profimg") RequestBody profimg);





//    @Multipart
//    @POST("guestinsertdetail.php")//testdetails.php
//        // @POST("testdetails.php")//testdetails.php
//    Call<ResponseBody> saveGuestentrydetails(@Part("companyname") RequestBody companyname,
//                                             @Part("name") RequestBody name
//            , @Part("phone") RequestBody phone
//            , @Part("employee") RequestBody employee
//            , @Part("reason") RequestBody reason
//            , @Part("ipaddress") RequestBody ipaddress
//            , @Part("msgdetail") RequestBody msgdetail
//            , @Part("cc") RequestBody cc
//            , @Part("guestimg") RequestBody guestimg
//            , @Part("msgdetailsms") RequestBody msgdetailsms
//            , @Part("sign_detail") RequestBody sign_detail
//            , @Part("rowid") RequestBody rowid
//            , @Part("profimg") RequestBody profimg);
//
//









    @Multipart
    @POST("api/AddMembers")
    Call<ResponseBody> addMember(@Part("bb_id") RequestBody bb_id, @Part("group_id") RequestBody group_id, @Part("f_name") RequestBody f_name, @Part("l_name") RequestBody l_name
            , @Part("sex") RequestBody sex, @Part("dob") RequestBody dob, @Part("address") RequestBody address, @Part("pincode") RequestBody pincode, @Part("taluk") RequestBody taluk, @Part("district") RequestBody district
            , @Part("state") RequestBody state, @Part("phone") RequestBody phone, @Part("email") RequestBody email
            , @Part("username") RequestBody user_name, @Part("password") RequestBody password
            , @Part("con_password ") RequestBody con_password
            , @Part("contribution") RequestBody member_amount
            , @Part("member_position") RequestBody member_position
            , @Part("member_image") RequestBody member_image);


    @Multipart
    @POST("api/GroupDetails")
    Call<ResponseBody> GetaboutGroup(@Part("bb_id") RequestBody bb_id, @Part("group_id") RequestBody group_id);




    @Multipart
    @POST("api/GetGroupEvent")
    Call<ResponseBody> GetmessageGroup(@Part("bb_id") RequestBody bb_id, @Part("grp_id") RequestBody grp_id);

    @Multipart
    @POST("api/MemberList")
    Call<ResponseBody> GetmemberGroup(@Part("bb_id") RequestBody bb_id, @Part("group_id") RequestBody group_id);

    @Multipart
    @POST("api/GroupList")
    Call<ResponseBody> GetGroups(@Part("bb_id") RequestBody bb_id);



}
