package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckValidation;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActCreatePostBinding;
import com.vhkfoundation.retrofit.ApiClient;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCreatePost extends AppCompatActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;
    private EditText et_title, et_desc;
    private final EditText[] edTexts = {et_title, et_desc};
    private final String[] edTextsError = {"Enter title", "Enter description"};
    private final int[] editTextsClickId = {R.id.et_title, R.id.et_desc};
    private ImageView iv_upload, iv_select_image;
    private AppCompatButton btn_create_post;
    private final View[] allViewWithClick = {btn_create_post, iv_upload};
    private final int[] allViewWithClickId = {R.id.btn_create_post, R.id.iv_upload};
    private ImageView imgFrontAadharcard, imgPanCard;
    private ActCreatePostBinding binding;
    private int selectedImageView = 0;
    private Uri imagePost = null;
    private final Uri imagePanCardUri = null;
    private LinkedList<String> lstUploadData = new LinkedList<>();
    private String isEmergency = "0";
    private String isDonate = "0";
    private HashMap<String, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        StartApp();
    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);

        ViewGroup root = findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        resumeApp();
    }

    private void resumeApp() {
        loadToolBar();
        binding.cbEmergency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isEmergency = "1";
                    //customToast.showCustomToast(svContext,"true",customToast.ToastyError);
                    // perform logic
                } else {
                    isEmergency = "0";
                    //customToast.showCustomToast(svContext,"false",customToast.ToastyError);
                }

            }
        });

        binding.cbDonate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDonate = "1";
                    //customToast.showCustomToast(svContext,"true",customToast.ToastyError);
                    // perform logic
                } else {
                    isDonate = "0";
                    //customToast.showCustomToast(svContext,"false",customToast.ToastyError);
                }

            }
        });

    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }

        et_title = editTexts[0];
        et_desc = editTexts[1];
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent svIntent;
                    switch (v.getId()) {
                        case R.id.btn_create_post:
                            //selectedImageView = 0;
                            //svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            //startActivity(svIntent);
                            savePost();
                            break;
                        case R.id.iv_upload:
                            selectedImageView = 0;
                            svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            svIntent.putExtra("isShowCrop",false);
                            startActivity(svIntent);
                            break;

                    }
                }
            });
        }
    }

    private void savePost() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (imagePost == null) {
            response++;
            binding.tvRequiredImg.setVisibility(View.VISIBLE);
        }
        if (response == 0) {
            binding.progressbarLoader.setVisibility(View.VISIBLE);
            //ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

//            lstUploadData = new LinkedList<>();
//            lstUploadData.add(PreferenceConnector.readString(svContext,PreferenceConnector.USERID,""));
//            lstUploadData.add(getEditextValue(et_old_password));
//            lstUploadData.add(getEditextValue(et_new_password));
//            lstUploadData.add(getEditextValue(et_confirm_password));

            data = new HashMap<>();
            data.put("user_id", PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
            data.put("title", et_title.getText().toString().trim());
            data.put("description", et_desc.getText().toString().trim());
            data.put("is_emergency", isEmergency);
            data.put("is_donate", isDonate);
            data.put("image", resizeBase64Image(encodeImage(imagePost)));
            //callWebService(ApiInterface.CREATEPOST, data,true);
            RequestBody user_id = RequestBody.create(MultipartBody.FORM, PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
            RequestBody title = RequestBody.create(MultipartBody.FORM, et_title.getText().toString().trim());
            RequestBody description = RequestBody.create(MultipartBody.FORM, et_desc.getText().toString().trim());
            RequestBody is_emergency = RequestBody.create(MultipartBody.FORM, isEmergency);
            RequestBody is_donate = RequestBody.create(MultipartBody.FORM, isDonate);
            RequestBody images = RequestBody.create(MediaType.parse("image/jpeg"), resizeBase64Image(encodeImage(imagePost)));
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File file = new File(imagePost.getPath());
            //RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), resizeBase64Image(encodeImage(imagePost)));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image_"+timeStamp+".jpeg", requestBody);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            //apiService.savePost(user_id, title, description, is_emergency, is_donate, body);

            Call<String> call = apiService.savePost(user_id, title, description, is_emergency, is_donate, body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("Create Post", "Create Post-----------" + response);
                    JSONObject json = null;
                    if (response.isSuccessful()) {
                        try {
                            json = new JSONObject(response.body());
                            String str_message = json.getString("message");
                            String str_status = json.getString("status");
                            binding.progressbarLoader.setVisibility(View.GONE);
                            if(str_status.equalsIgnoreCase("1")){
                                GlobalVariables.isRefresh=1;
                                finish();
                                customToast.showCustomToast(svContext,str_message,customToast.ToastySuccess);
                            } else {
                                customToast.showCustomToast(svContext,str_message,customToast.ToastyError);
                            }
                            //String status = response.body();
                        } catch (JSONException e) {
                            binding.progressbarLoader.setVisibility(View.GONE);
                            customToast.showCustomToast(svContext,"Something went wrong",customToast.ToastyError);
                            throw new RuntimeException(e);
                        }
                    } else {
                        customToast.showCustomToast(svContext,"Internal server error",customToast.ToastyError);
                        binding.progressbarLoader.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    String err = t.toString();
                    binding.progressbarLoader.setVisibility(View.GONE);
                }
            });
        }
    }


    private void callWebService(String postUrl, HashMap<String, String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this, isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }

        public static RequestBody toRequestBody(File file) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return body;
    }


    private String getEditextValue(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return (edTexts[i]).getText().toString().trim();
            }
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            if (selectedImageView == 0) {
                imagePost = ActivityBrowseProfileImage.imageUri;
                binding.ivSelectImage.setImageURI(null);

//                Glide.with(svContext)
//                        .load(imagePost)
//                        .placeholder(R.drawable.loader)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .dontAnimate()
//                        .into(new CustomTarget<Drawable>() {
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                binding.ivSelectImage.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });
//
//
//                File file = new File(getRealPathFromURI(imagePost));
//                if (file.exists()) {
//                    Drawable d = Drawable.createFromPath(file.getAbsolutePath());
//                    binding.ivSelectImage.setBackground(d);
//                }
                binding.ivSelectImage.setImageURI(imagePost);
                //binding.ivSelectImage.setVisibility(View.VISIBLE);
                binding.cardView.setVisibility(View.VISIBLE);
            }
            ActivityBrowseProfileImage.imageUri = null;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.layActionbar.tvTitle.setText("Create Post");
        binding.layActionbar.imgNotification.setVisibility(View.INVISIBLE);
        binding.layActionbar.llUserImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.CREATEPOST)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");

            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }

    private String encodeImage(Uri imgUri) {
        File imagefile = new File(imgUri.getPath());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);


        if (image.getHeight() <= 400 && image.getWidth() <= 400) {
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 300, 300, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] b = baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }
}
