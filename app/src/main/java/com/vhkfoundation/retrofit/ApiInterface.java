package com.vhkfoundation.retrofit;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

   // String LOGINAUTH = "userAuth";

    String LOGINAUTH = "login";
  //  String REGISTRATIONAUTH = "registerationAuth";
    String REGISTRATIONAUTH = "sign-up";
    String UPDATEBASICDETAIL = "updateBasicDetail";
    String PERSONALDETAIL = "personalDetail";
    String FAMILYDETAIL = "familyDetail";
    String OccuPationDetails = "occupationDetails";
    String BankDetails = "bankDetails";
    String KycDetails = "kycDetails";

    String CHANGEPASSWORDAUTH = "updatePasswordAuth";
    String GETUSERDETAILS = "getUserDetails";
    String GETDONATIONPOSTLIST = "getDonationPostList";
    String UPDATEUSERDETAILS = "updateUserData";
    String GETDONATIONEVENTLIST = "getDonationEvent";
    String GETCATEGORYLIST = "getCategoryList";

    String CREATEPOST = "saveFeed";
    String GETSTATES = "getStates";
    String GETDISTRICTS = "getDistricts";

    String GETFEEDLIST = "getFeedList";
    String GETFEEDDETAILS = "getFeedDetail";

    String SETLIKE = "like";

    String GETHOMEDATA = "getHomeFeedList";

    String POSTCOMMENT = "saveComment";

    String SETCOMMENTLIKE = "commentLike";

    String GETCOMMENTSLIST = "getCommentList";

    String GETDONATIONLISTCATWISE = "getEventCategoryList";
    String GETEVENTLIST = "getEventList";

    String GETDONATIONLISTCATLIKE = "eventLike";

    String GETDONATIONDETAILS = "getEventDetails";

   // String SETVOLUNTEER = "setVolunteer";
    String SETVOLUNTEER = "checkVolunteer";

    @FormUrlEncoded
    @POST(LOGINAUTH)
    Call<String> loginAuth(@Field("mobile") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST(REGISTRATIONAUTH)
    Call<String> registerAuth(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile") String mobile, @Field("password_confirmation") String password_confirmation, @Field("referral_id") String referral_id);
    @FormUrlEncoded
    @POST(UPDATEBASICDETAIL)
    Call<String> updateDetails(@Field("user_id") String user_id, @Field("mobile") String mobile, @Field("email") String email,
                               @Field("pincode") String pincode, @Field("state_id") String state_id ,@Field("district_id") String district_id, @Field("city_name") String city_name);

    @FormUrlEncoded
    @POST(PERSONALDETAIL)
    Call<String> personalDetail(@Field("user_id") String user_id, @Field("father_name") String father_name,
                                @Field("mother_name") String mother_name, @Field("dob") String dob,
                                @Field("physically_handicapped") String physically_handicapped, @Field("gender") String gender
            , @Field("marital_status") String marital_status, @Field("qualification") String qualification
            , @Field("alternative_number") String alternative_number
    );

    @FormUrlEncoded
    @POST(BankDetails)
    Call<String> bankDetail(@Field("user_id") String user_id,
                            @Field("account_holder_name") String account_holder_name,
                            @Field("account_number") String account_number,
                            @Field("ifsc_code") String ifsc_code,
                            @Field("bank_name") String bank_name
    );
 @FormUrlEncoded
    @POST(KycDetails)
    Call<String> kycDetail(@Field("user_id") String user_id,
                            @Field("pancard_number") String account_holder_name,
                            @Field("adhar_number") String account_number,
                            @Field("proof_of_address") String ifsc_code
    );

   /* @FormUrlEncoded
    @POST(OccuPationDetails1)
    Call<String> occupationDetails1(@Field("user_id") String user_id,
                                    @Field("occupation_detail_type") String occupation_detail_type,
                                    @Field("company") String company,
                                    @Field("name") String name,
                                    @Field("department") String department,
                                    @Field("company_address") String company_address,
                                    @Field("city_name") String city_name,
                                    @Field("state_id") String state_id,
                                    @Field("postal_code") String postal_code
    );


    @FormUrlEncoded
    @POST(OccuPationDetails2)
    Call<String> occupationDetails2(
            @Field("user_id") String user_id,
            @Field("occupation_detail_type") String occupation_detail_type,
            @Field("employment_type") String company,
            @Field("monthly_salary") String name,
            @Field("office_address") String department,
            @Field("designation") String company_address,
            @Field("mode_of_salary") String city_name,
            );

    @FormUrlEncoded
    @POST(OccuPationDetails3)
    Call<String> occupationDetails(
            @Field("user_id") String user_id,
            @Field("occupation_detail_type") String occupation_detail_type,
            @Field("employment_type") String company,
            @Field("monthly_salary") String name,
            @Field("office_address") String department,
            @Field("designation") String company_address,
            @Field("mode_of_salary") String city_name,
            );*/


 /*   @FormUrlEncoded
    @POST(FAMILYDETAIL)
    Call<String> familyDetails(@Field("user_id") String user_id,
                               @Body String json_data
    );*/

 /*   @POST(FAMILYDETAIL)
    Call<String> familyDetails(
            @Field("user_id") String user_id,
            @Body RequestBody requestBody
    );
*/


    @FormUrlEncoded
    @POST(CHANGEPASSWORDAUTH)
    Call<String> changePasswordAuth(@Field("user_id") String user_id, @Field("old_password") String old_password, @Field("password") String password, @Field("cpassword") String cpassword);

    /* @FormUrlEncoded
     @POST(GETUSERDETAILS)
     Call<String> getUserDetails(@Field("user_id") String user_id);
    */

    //@FormUrlEncoded
    @GET(GETUSERDETAILS)
    Call<String> getUserDetails(
            @Query("user_id") String user_id,
            @Header("Authorization")  String authHeader
    );



    @POST(GETDONATIONPOSTLIST)
    Call<String> getDonationPostList(@Query("user_id") String user_id,@Query("is_emergency") String is_emergency,@Query("category_id") String category_id);

    @POST(GETSTATES)
    Call<String> getStates();

    @FormUrlEncoded
    @POST(GETDISTRICTS)
    Call<String> getDistricts(@Field("state_id") String state_id);

    @FormUrlEncoded
    @POST(GETEVENTLIST)
    Call<String> getEventList(@Field("user_id") String user_id, @Field("category_id") String category_id);

    @FormUrlEncoded
    @POST(UPDATEUSERDETAILS)
    Call<String> updateUserDetails(@Field("user_id") String user_id, @Field("name") String name, @Field("email") String email, @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(GETDONATIONEVENTLIST)
    Call<String> getDonationEventList(@Field("user_id") String user_id);

    @POST(CREATEPOST)
    Call<String> createPost(@Body HashMap<String, String> body);

    @POST(GETCATEGORYLIST)
    Call<String> getCategoryList();

    @Multipart
    @POST(CREATEPOST)
    Call<String> savePost(@Part("user_id") RequestBody user_id, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("is_emergency") RequestBody is_emergency, @Part("is_donate") RequestBody is_donate, @Part MultipartBody.Part filePart);

    @FormUrlEncoded
    @POST(GETFEEDLIST)
    Call<String> getFeedList(@Field("user_id") String user_id, @Field("is_emergency") String is_emergency);

    @FormUrlEncoded
    @POST(GETFEEDDETAILS)
    Call<String> getFeedDetails(@Field("user_id") String user_id, @Field("feed_id") String feed_id);

    @FormUrlEncoded
    @POST(SETLIKE)
    Call<String> setLike(@Field("user_id") String user_id, @Field("feed_id") String feed_id, @Field("is_like") String is_like);

    @FormUrlEncoded
    @POST(GETHOMEDATA)
    Call<String> getHomeFeedData(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(POSTCOMMENT)
    Call<String> postComment(@Field("user_id") String user_id, @Field("feed_id") String feed_id, @Field("comment") String comment);

    @FormUrlEncoded
    @POST(SETCOMMENTLIKE)
    Call<String> setCommentLike(@Field("user_id") String user_id, @Field("comment_id") String comment_id, @Field("is_comment_like") String is_comment_like);

    @FormUrlEncoded
    @POST(GETCOMMENTSLIST)
    Call<String> getCommentsList(@Field("user_id") String user_id, @Field("feed_id") String feed_id);

    @FormUrlEncoded
    @POST(GETDONATIONLISTCATWISE)
    Call<String> getDonationListCatWise(@Field("user_id") String user_id);
    //,@Field("category_id") String cat_id);

    @FormUrlEncoded
    @POST(GETDONATIONLISTCATLIKE)
    Call<String> getDonationListCatLike(@Field("user_id") String user_id, @Field("event_id") String cat_id, @Field("is_like") String Is_like);

    @FormUrlEncoded
    @POST(GETDONATIONDETAILS)
    Call<String> getDonationDetails(@Field("user_id") String user_id, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(SETVOLUNTEER)
    Call<String> setVolunteer(@Field("user_id") String user_id, @Field("is_volunteer") String is_volunteer);


}
