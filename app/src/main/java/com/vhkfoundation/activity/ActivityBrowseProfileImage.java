package com.vhkfoundation.activity;
import android.app.Activity;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.GlobalData;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class ActivityBrowseProfileImage extends BaseActivity implements View.OnClickListener,
        UCropFragmentCallback {
    private Context svContext;
    public static Uri imageUri = null;
    private Button dialogCancel;
    private RelativeLayout openGallery, openCamera;
    private RelativeLayout rlayImageSelect;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    protected static final int REQUEST_CAMERA_ACCESS_PERMISSION = 103;
    private static final int TAKE_PICTURE = 2;
    private static final int BROWSE_PICTURE = 1;
    private UCropFragment fragment;
    private boolean mShowLoader;
    private boolean isShowCrop = true;
    private String mToolbarTitle;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;
    private AlertDialog mAlertDialog;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final int REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "CropImage";
    private Toolbar toolbar;
    private ShowCustomToast customToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_browsecropimage);
        svContext = this;
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(svContext.getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            FontUtils.setFont(root, font);
        }
        customToast = new ShowCustomToast(svContext);

        rlayImageSelect = (RelativeLayout) findViewById(R.id.uploadimage);
        openGallery = (RelativeLayout) findViewById(R.id.dialog_fromgallery);
        openCamera = (RelativeLayout) findViewById(R.id.dialog_fromcamera);
        dialogCancel = (Button) findViewById(R.id.dialog_cancel);

        openGallery.setOnClickListener(this);
        openCamera.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);

        isShowCrop = getIntent().getBooleanExtra("isShowCrop", true);
        ShowUploadDialog();
    }


    private void browseImage(){
        Collection<String> permissionNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            permissionNeeded.add(Manifest.permission.CAMERA);
            permissionNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            permissionNeeded.add(Manifest.permission.CAMERA);
            permissionNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);

        }

        Dexter.withContext(svContext)
                // below line is use to request the number of permissions which are required in our app.
                .withPermissions(permissionNeeded)
                // after adding permissions we are calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            TedBottomPicker.with(ActivityBrowseProfileImage.this)
                                    .setSelectedUri(imageUri)
                                    .setPeekHeight(1200)
                                    .show(uri -> {
                                        Log.d("ted", "uri: " + uri);
                                        Log.d("ted", "uri.getPath(): " + uri.getPath());
                                        if (isShowCrop) {
                                            startCrop(uri);
                                        } else {
                                            if (uri != null) {
                                                imageUri = uri;
                                                onBackPressed();
                                            } else {
                                                customToast.showCustomToast(svContext, getResources().getString(R.string.toast_cannot_retrieve_selected_image), customToast.ToastyInfo);
                                            }
                                        }
                                    });
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, we will show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    // we are displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                // below line is use to run the permissions on same thread and to check the permissions
                .onSameThread().check();


    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBrowseProfileImage.this);

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            builder.setMessage("This app needs camera and storage permission to use this feature. You can grant them in app settings.");
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            builder.setMessage("This app needs camera and access media storage permission to use this feature. You can grant them in app settings.");
        }

        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel();
            // below is the intent from which we are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult.launch(intent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // this method is called when user click on negative button.
            dialog.cancel();
        });
        // below line is used to display our dialog
        builder.show();
    }

    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    TedBottomPicker.with(ActivityBrowseProfileImage.this)
                            //.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2)
                            //.setSelectedUri(imageUri)
                            //.showVideoMedia()
                            //.setPeekHeight(1200)
                            .show(uri -> {
                                Log.d("ted", "uri: " + uri);
                                Log.d("ted", "uri.getPath(): " + uri.getPath());
                                if (isShowCrop) {
                                    startCrop(uri);
                                } else {
                                    if (uri != null) {
                                        imageUri = uri;
                                        onBackPressed();
                                    } else {
                                        customToast.showCustomToast(svContext, getResources().getString(R.string.toast_cannot_retrieve_selected_image), customToast.ToastyInfo);
                                    }
                                }
                            });
                }
            }
    );

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_fromgallery:
                browseImage();
                break;
            case R.id.dialog_fromcamera:
                browseImage();
                break;
            case R.id.dialog_cancel:
//                HideUploadDialog();
                finish();
                break;
            default:
                break;
        }
    }




    private void ShowUploadDialog() {
        rlayImageSelect.setVisibility(View.VISIBLE);
    }


    private void startCrop(@NonNull Uri uri) {
        try {
            String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + GlobalData.getUniqueString();
            destinationFileName += ".jpg";

            UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
            uCrop = basisConfig(uCrop);
            uCrop = advancedConfig(uCrop);
            uCrop.start(ActivityBrowseProfileImage.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop basisConfig(@NonNull UCrop uCrop) {
//        uCrop = uCrop.useSourceImageAspectRatio();
        uCrop = uCrop.withAspectRatio(3, 2);
        uCrop = uCrop.withMaxResultSize(600, 600);

        return uCrop;
    }

    /**
     * Sometimes you want to adjust more options, it's done via {@link UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
//        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100); //From 1 to 100
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
//        options.setMaxScaleMultiplier(5);
//        options.setImageToCropBoundsAnimDuration(666);
//        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);

        /*
        If you want to configure how gestures work for all UCropActivity tabs
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.
        options.setMaxBitmapSize(640);
        * */

       /*
        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        imageUri = UCrop.getOutput(result);
        if (imageUri != null) {
//            ActivityRegister fragment = new ActivityRegister();
//            (fragment).SetProfilePic(svContext);
//            finish();
            onBackPressed();
        } else {
            customToast.showCustomToast(svContext, getResources().getString(R.string.toast_cannot_retrieve_cropped_image), customToast.ToastyInfo);
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            customToast.showCustomToast(svContext, cropError.getMessage(), customToast.ToastyError);
        } else {
            customToast.showCustomToast(svContext, getResources().getString(R.string.toast_unexpected_error), customToast.ToastyInfo);
        }
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        mShowLoader = showLoader;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        switch (result.mResultCode) {
            case RESULT_OK:
                handleCropResult(result.mResultData);
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(result.mResultData);
                break;
        }
        removeFragmentFromScreen();
    }

    public void removeFragmentFromScreen() {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        toolbar.setVisibility(View.GONE);
//        settingsView.setVisibility(View.VISIBLE);
    }


//    @Override
//    public void onSingleImageSelected(Uri uri, String tag) {
//        startCrop(uri);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (requestCode == TAKE_PICTURE) {
                if (isShowCrop) {
                    startCrop(imageUri);
                } else {
                    if (imageUri != null) {

                        onBackPressed();
                    } else {
                        customToast.showCustomToast(svContext, getResources().getString(R.string.toast_cannot_retrieve_selected_image), customToast.ToastyInfo);
                    }
                }

            } else if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            } else {
                final Uri selectedUri = imageUri;
                if (selectedUri != null) {
                    startCrop(selectedUri);
                } else {
                    customToast.showCustomToast(svContext, getResources().getString(R.string.toast_cannot_retrieve_selected_image), customToast.ToastyError);
                }
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    /**
     * This method shows dialog with given title & message.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog message
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    /**



     @Override
     public void onBackPressed() {
     super.onBackPressed();
     finish();
     }

     /**
      * Configures and styles both status bar and toolbar.
     */
    private void setupAppBar() {
        setStatusBarColor(mStatusBarColor);
        toolbar = findViewById(R.id.toolbar);

        // Set all of the Toolbar coloring
        toolbar.setBackgroundColor(mToolbarColor);
        toolbar.setTitleTextColor(mToolbarWidgetColor);
        toolbar.setVisibility(View.VISIBLE);
        final TextView toolbarTitle = toolbar.findViewById(R.id.tv_title);
        toolbarTitle.setTextColor(mToolbarWidgetColor);
        toolbarTitle.setText(mToolbarTitle);

        // Color buttons inside the Toolbar
        Drawable stateButtonDrawable = ContextCompat.getDrawable(getBaseContext(), mToolbarCancelDrawable);
        if (stateButtonDrawable != null) {
            stateButtonDrawable.mutate();
            stateButtonDrawable.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(stateButtonDrawable);
        }

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Sets status-bar color for L devices.
     *
     * @param color - status-bar color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }


    /**
     * By Default isShowCrop is false
     **/
    public static void openBrowseImageScreen(Context context) {
        openBrowseImageScreen(context, false);
    }

    public static void openBrowseImageScreen(Context context, boolean isShowCrop) {
        Intent intent = new Intent(context, ActivityBrowseProfileImage.class);
        intent.putExtra("isShowCrop", isShowCrop);
        context.startActivity(intent);
    }

    private void PermissionPermit(Collection<String> permissionNeeded, Context con) {
        Dexter.withContext(con)
                .withPermissions(permissionNeeded).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
    }

}
