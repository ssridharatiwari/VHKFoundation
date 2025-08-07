package com.vhkfoundation.retrofit;


import static com.vhkfoundation.commonutility.GlobalVariables.ISTESTING;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vhkfoundation.R;
import com.vhkfoundation.activity.ActivitySplash;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.dialogandpicker.CustomeProgressDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceRetroFit {
    private final Context context;
    private final WebServiceListenerRetroFit listener;
    private Call<String> updateAppCall;
    private String postUrl;
    private String bodyString;
    private String[] imagePath;
    private final boolean isShowText = false;
    private boolean isDialogShow = true;
    private LinkedList<String> lstUploadData = new LinkedList<>();

    RequestBody fullName = RequestBody.create(MultipartBody.FORM, "Your Name");

    private final Map<String, String> params = new HashMap<String, String>();
    HashMap<String, String> data = new HashMap<>();

    private CustomeProgressDialog customeProgressDialog;
    private ShowCustomToast customToast;

    public static WebServiceRetroFit getInstance(Context svContext, WebServiceListenerRetroFit listener) {
        return new WebServiceRetroFit(svContext, listener);
    }

    public static void callWebService(Context svContext, String postUrl, LinkedList<String> lstUploadData, WebServiceListenerRetroFit listener) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, listener, true);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static void callWebServiceWithoutLoader(Context svContext, String postUrl, LinkedList<String> lstUploadData, WebServiceListenerRetroFit listener) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, listener, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.isDialogShow = true;
        LoadDataRetrofit(callReturn());
    }

    public void callWebServiceWithoutLoader(String postUrl, LinkedList<String> lstUploadData) {
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.isDialogShow = false;
        LoadDataRetrofit(callReturn());
    }

    public WebServiceRetroFit(Context context, WebServiceListenerRetroFit listener) {
        this.context = context;
        this.listener = listener;
    }

    public WebServiceRetroFit(Context context, String postUrl, LinkedList<String> lstUploadData, WebServiceListenerRetroFit listener) {
        this.context = context;
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.listener = listener;
    }

    public WebServiceRetroFit(Context context, String postUrl, LinkedList<String> lstUploadData, WebServiceListenerRetroFit listener,
                              boolean isDialogShow) {
        this.context = context;
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.listener = listener;
        this.isDialogShow = isDialogShow;
    }

    public WebServiceRetroFit(Context context, String postUrl, HashMap<String, String> lstUploadData, WebServiceListenerRetroFit listener,
                              boolean isDialogShow) {
        this.context = context;
        this.postUrl = postUrl;
        this.data = lstUploadData;
        this.listener = listener;
        this.isDialogShow = isDialogShow;
    }

    public WebServiceRetroFit(Context context, String postUrl, LinkedList<String> lstUploadData, WebServiceListenerRetroFit listener,
                              String[] imagePath) {
        this.context = context;
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.imagePath = new String[imagePath.length];
        this.imagePath = imagePath;
        this.listener = listener;
    }

    public void LoadDataRetrofit(Call<String> call) {
        customToast = new ShowCustomToast(context);
        if (ISTESTING) {
            System.out.println("API Call ---> " + postUrl);
            for (int i = 0; i < lstUploadData.size(); i++) {
                System.out.println(lstUploadData.get(i) + ".........." + i + "..........");
            }
        }

        if (isDialogShow) {
            customeProgressDialog = new CustomeProgressDialog(context, R.layout.lay_customprogessdialog);
            TextView textView = customeProgressDialog.findViewById(R.id.loader_showtext);
            if (isShowText) {
                textView.setVisibility(View.VISIBLE);
//                textView.setText(getDialogText(postUrl));
            } else {
                textView.setVisibility(View.GONE);
            }
            customeProgressDialog.setCancelable(false);
            customeProgressDialog.show();
        }

        if (updateAppCall != null) {
            try {
                Response<String> response = updateAppCall.execute();
                int code = response.code();
                if (code == 200 || code == 300) {
                    String result = response.body();
                    if (ISTESTING) System.out.println("updateAppCall Response -> " + result);
                    JSONObject json = new JSONObject(result);
                    String str_status = json.getString("status");
                    String str_message = json.getString("message");
                    String str_is_login = json.getString("is_login");

                    if (str_is_login.equals("0")) {
                        hideProgress();
                        customToast.showCustomToast(context, str_message, customToast.ToastyError);
                        ActivitySplash.LogoutNow(context);
                    } else {
                        ((Activity) context).runOnUiThread(() -> call.clone().enqueue(webServiceCallback));
                    }
                } else {
                    hideProgress();
                    if (ISTESTING)
                        System.out.println("updateAppCall Failed -> " + response.message());
                }
            } catch (Exception ex) {
                hideProgress();
                ex.printStackTrace();
                if (ISTESTING)
                    System.out.println("updateAppCall error occurred -> " + ex.getMessage());
            }
        } else {
            call.enqueue(webServiceCallback);
        }
    }

    public void LoadImageRetrofit(Call<String> call) {
        if (ISTESTING) {
            System.out.println("API Call ---> " + postUrl);
            for (int i = 0; i < lstUploadData.size(); i++) {
                System.out.println(lstUploadData.get(i) + ".........." + i + "..........");
            }
        }

        if (isDialogShow) {
            customeProgressDialog = new CustomeProgressDialog(context, R.layout.lay_customprogessdialog);
            TextView textView = customeProgressDialog.findViewById(R.id.loader_showtext);
            if (isShowText) {
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }

            customeProgressDialog.setCancelable(false);
            customeProgressDialog.show();
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                try {
                    if (ISTESTING) {
                        System.out.println(response + "..........webservice response..........");
                    }

                    int code = response.code();
                    if (code == 200 || code == 300) {
                        bodyString = response.body();

                        if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                            customeProgressDialog.dismiss();
                        }
                    } else {
                        bodyString = response.message();
                        if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                            customeProgressDialog.dismiss();
                        }
                        listener.onWebServiceRetroError(bodyString, postUrl);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    listener.onWebServiceRetroActionComplete(bodyString, postUrl);
                } catch (JSONException e) {
                    if (ISTESTING) {
                        customToast.showCustomToast(context, "Some error occurred\n" + e.getMessage(), customToast.ToastyError);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                    customeProgressDialog.dismiss();
                }
                listener.onWebServiceRetroError(t.toString(), postUrl);
            }
        });
    }

    private final Callback<String> webServiceCallback = new Callback<String>() {
        @Override
        public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
            try {
                if (ISTESTING)
                    System.out.println(response + "..........webservice response..........");

                int code = response.code();
                if (code == 200 || code == 300) {
                    bodyString = response.body();
                    try {
                        if (ISTESTING) {
                            System.out.println("API ---> " + postUrl + "\nJSON Response ---> " + bodyString);
                        }
                        if (listener != null)
                            listener.onWebServiceRetroActionComplete(bodyString, postUrl);
                    } catch (JSONException e) {
                        if (ISTESTING)
                            customToast.showCustomToast(context, "Some error occurred\n" + e.getMessage(), customToast.ToastyError);
                    }
                } else {
                    bodyString = response.message();
                    if (listener != null) listener.onWebServiceRetroError(bodyString, postUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hideProgress();
            }
        }

        @Override
        public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            hideProgress();
            if (ISTESTING) Log.e("vhk foundation", t.toString());
        }
    };

    private void hideProgress() {
        if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
            customeProgressDialog.dismiss();
        }
    }

    public Call<String> callReturn() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        switch (postUrl) {
            case ApiInterface.LOGINAUTH:
                return apiService.loginAuth(lstUploadData.get(0), lstUploadData.get(1));

            case ApiInterface.REGISTRATIONAUTH:
                return apiService.registerAuth(lstUploadData.get(0), lstUploadData.get(1), lstUploadData.get(2), lstUploadData.get(3), lstUploadData.get(4), lstUploadData.get(5));

            case ApiInterface.CHANGEPASSWORDAUTH:
                return apiService.changePasswordAuth(lstUploadData.get(0), lstUploadData.get(1), lstUploadData.get(2), lstUploadData.get(3));

            case ApiInterface.UPDATEUSERDETAILS:
                return apiService.updateUserDetails(lstUploadData.get(0), lstUploadData.get(1), lstUploadData.get(2), lstUploadData.get(3));

            case ApiInterface.GETUSERDETAILS:
                return apiService.getUserDetails(lstUploadData.get(0), lstUploadData.get(1));

            case ApiInterface.UPDATEBASICDETAIL:
                return apiService.updateDetails(lstUploadData.get(0), lstUploadData.get(1),
                        lstUploadData.get(2), lstUploadData.get(3), lstUploadData.get(4),
                        lstUploadData.get(5), lstUploadData.get(6));

            case ApiInterface.PERSONALDETAIL:
                return apiService.personalDetail(
                        lstUploadData.get(0), lstUploadData.get(1),
                        lstUploadData.get(2), lstUploadData.get(3),
                        lstUploadData.get(4), lstUploadData.get(5),
                        lstUploadData.get(6),lstUploadData.get(7)
                        ,lstUploadData.get(8)/*, lstUploadData.get(9)*/
                );
            case ApiInterface.BankDetails:
                return apiService.bankDetail(
                        lstUploadData.get(0), lstUploadData.get(1),
                        lstUploadData.get(2), lstUploadData.get(3),
                        lstUploadData.get(4)

                );

            case ApiInterface.KycDetails:
                return apiService.kycDetail(
                        lstUploadData.get(0), lstUploadData.get(1),
                        lstUploadData.get(2), lstUploadData.get(3)
                );

           /* case ApiInterface.OccuPationDetails1:
                return apiService.occupationDetails(
                        lstUploadData.get(0), lstUploadData.get(1),
                        lstUploadData.get(2), lstUploadData.get(3),
                        lstUploadData.get(4), lstUploadData.get(5),
                        lstUploadData.get(6), lstUploadData.get(7)
                        , lstUploadData.get(8)
                );
*/



          /*  case ApiInterface.FAMILYDETAIL:
                return apiService.familyDetails(lstUploadData.get(0), lstUploadData.get(1));*/

           case ApiInterface.GETDONATIONPOSTLIST:
                return apiService.getDonationPostList(lstUploadData.get(0),lstUploadData.get(1), lstUploadData.get(2));

            case ApiInterface.GETSTATES:
                return apiService.getStates();

            case ApiInterface.GETDISTRICTS:
                return apiService.getDistricts(lstUploadData.get(0));

            case ApiInterface.GETEVENTLIST:
                return apiService.getEventList(lstUploadData.get(0),lstUploadData.get(1));


            case ApiInterface.GETDONATIONEVENTLIST:
                return apiService.getDonationEventList(lstUploadData.get(0));

            case ApiInterface.GETCATEGORYLIST:
                return apiService.getCategoryList();

            case ApiInterface.CREATEPOST:
                return apiService.createPost(data);

            case ApiInterface.GETFEEDLIST:
                return apiService.getFeedList(lstUploadData.get(0),lstUploadData.get(1));

            case ApiInterface.GETFEEDDETAILS:
                return apiService.getFeedDetails(lstUploadData.get(0),lstUploadData.get(1));

            case ApiInterface.SETLIKE:
                return apiService.setLike(lstUploadData.get(0),lstUploadData.get(1),lstUploadData.get(2));
            case ApiInterface.GETHOMEDATA:
                return apiService.getHomeFeedData(lstUploadData.get(0));

            case ApiInterface.POSTCOMMENT:
                return apiService.postComment(lstUploadData.get(0),lstUploadData.get(1),lstUploadData.get(2));

            case ApiInterface.SETCOMMENTLIKE:
                return apiService.setCommentLike(lstUploadData.get(0),lstUploadData.get(1),lstUploadData.get(2));

            case ApiInterface.GETCOMMENTSLIST:
                return apiService.getCommentsList(lstUploadData.get(0),lstUploadData.get(1));

            case ApiInterface.GETDONATIONLISTCATWISE:
                return apiService.getDonationListCatWise(lstUploadData.get(0));
                        //lstUploadData.get(1));

            case ApiInterface.GETDONATIONLISTCATLIKE:
                return apiService.getDonationListCatLike(lstUploadData.get(0),lstUploadData.get(1),lstUploadData.get(2));

            case ApiInterface.GETDONATIONDETAILS:
                return apiService.getDonationDetails(lstUploadData.get(0),lstUploadData.get(1));

            case ApiInterface.SETVOLUNTEER:
                return apiService.setVolunteer(lstUploadData.get(0),lstUploadData.get(1));


            default:
                new ShowCustomToast(context).showToast("Please declare url in WebService class", context);
                break;
        }
        return null;
    }
}