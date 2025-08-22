package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.vhkfoundation.R;
import com.vhkfoundation.adapter.ExpandableListAdapter;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.fragment.FragmentActivity;
import com.vhkfoundation.fragment.FragmentDonation;
import com.vhkfoundation.fragment.FragmentHomeDashBoard;
import com.vhkfoundation.fragment.FragmentProfile;
import com.vhkfoundation.model.MenuModel;
import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnItemSelectedListener {

    private Context svContext;
    private ShowCustomToast customToast;
    private ImageView imgMenu,iv_notification;
    private DrawerLayout drawer;
    List<MenuModel> headerList = new ArrayList<>();
    ExpandableListView expandableListView;
    View lay_actionbar;
    ExpandableListAdapter expandableListAdapter;
    public NavigationView navigationView;
    private String[] strMenuItem;
    private String[] strMenuStudentItemMaster;
    private String[] strMenuItemMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        StartApp();
        resumeApp();
    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
    }
    public void resumeApp() {
        headerList = new ArrayList<>();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableListView = findViewById(R.id.expandableListView);
        initToolbar();
        initMenuData();
        prepareMenuData();
        populateExpandableList();
        initBottomMenu();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationMenu(navigationView);

        switchContent(new FragmentHomeDashBoard());
    }



    private void prepareMenuData() {
        List<String> HashMap = new ArrayList<>();
        headerList = new ArrayList<>();
        strMenuItem = new String[strMenuItemMaster.length];
        for (int j = 0; j < strMenuItemMaster.length; j++) {
            MenuModel menuModel = new MenuModel(strMenuItemMaster[j], true, false);
            HashMap.add(strMenuItemMaster[j]);
            headerList.add(menuModel);
        }
    }

    private void populateExpandableList(){
        int[] textureArrayWin = {
                R.drawable.donation_img5,
                R.drawable.calendar_img,
                R.drawable.notification_img3,
                //R.drawable.ic_finance,
                //R.drawable.ic_settings,
                R.drawable.referral_code_img,
                //R.drawable.ic_faq,
                R.drawable.logout_img3
        };
        expandableListAdapter = new ExpandableListAdapter(this, headerList,textureArrayWin);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                onDrawerItemClick(headerList.get(groupPosition).menuName, svContext,"");
//            if(!headerList.get(groupPosition).menuName.equalsIgnoreCase("Logout")){
//                CloseDrawer();
//            }

            CloseDrawer();
            return false;
        });
    }

    public void initToolbar() {
        imgMenu = (ImageView) findViewById(R.id.img_menu);
        iv_notification= (ImageView) findViewById(R.id.iv_notification);
        imgMenu.setOnClickListener(view -> OpenDrawer());
        LinearLayout img_notification = (LinearLayout) findViewById(R.id.img_notification);
        img_notification.setVisibility(View.VISIBLE);
        img_notification.setOnClickListener(view -> {
            Intent svIntent = new Intent(svContext, ActivityNotification.class);
            svContext.startActivity(svIntent);
        });
    }



    public void onDrawerItemClick(String title, Context svContext,String type) {
        if(title.equalsIgnoreCase("Logout")){
            openbottomLogout();
        } else if(title.equalsIgnoreCase("Referral code")){
            Intent intent = new Intent(svContext,ActivityRefer.class);
            startActivity(intent);
        } else if(title.equalsIgnoreCase("Donate")){
            Intent intent = new Intent(svContext,ActivityDonate.class);
            intent.putExtra("isMenu","1");
            startActivity(intent);
        } else if(title.equalsIgnoreCase("Events")){
            Intent intent = new Intent(svContext,ActivityEvent.class);
            startActivity(intent);
        }

//            if(title.equalsIgnoreCase("Attendance")){
//                Intent intent = new Intent(svContext,ActivityAttendance.class);
//                svContext.startActivity(intent);
//                //Toast.makeText(svContext.getApplicationContext(), "Attendance",Toast.LENGTH_LONG).show();
//            } else if(title.equalsIgnoreCase("Finance")){
//                Intent intent = new Intent(svContext,ActivityFinance.class);
//                svContext.startActivity(intent);
//                //Toast.makeText(svContext.getApplicationContext(), "Finance",Toast.LENGTH_LONG).show();
//            }
    }

    private void CloseDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    private void OpenDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    private void initMenuData() {
        strMenuItemMaster = new String[] {
                "Donate",
                "Events",
                "Notifications",
                //"Finance",
                //"Settings",
                "Referral Code",
                //"FAQ",
                "Logout"
        };
    }

    private void initBottomMenu() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {

            Fragment selectFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    switchContent(new FragmentHomeDashBoard());
                    break;
                case R.id.nav_activity:
                    switchContent(new FragmentActivity());
                    break;
                case R.id.nav_donation:
                    switchContent(new FragmentDonation());
                    break;
                case R.id.nav_profile:
                    switchContent(new FragmentProfile());
                    break;
            }
            return true;
        });

    }
    public void initNavigationMenu(NavigationView navigationView) {
        View navigation_header = navigationView.getHeaderView(0);
        ImageView ll_close = navigation_header.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
        });
        TextView tvName = navigation_header.findViewById(R.id.tv_user_name);
        TextView tvMemberId = navigation_header.findViewById(R.id.tv_user_id);

        tvName.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USERNAME, ""));
        tvMemberId.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USERCODE, ""));
    }
    public void switchContent(Fragment fragment) {
        hideKeyboard();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(!item.getTitle().equals("Logout")){
            CloseDrawer();
        }

        return true;
    }

    private void openbottomLogout(){
        BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(svContext, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(svContext).inflate(R.layout.bottom_sheet_dialog_logout, bottomSheetDialog1.findViewById(R.id.BottomSheetContainer));
        bottomSheetDialog1.setContentView(bottomSheetView);
        bottomSheetDialog1.setCancelable(false);
        bottomSheetDialog1.show();

        AppCompatTextView btn_cancel = bottomSheetDialog1.findViewById(R.id.btn_cancel);
        AppCompatTextView btn_logout = bottomSheetDialog1.findViewById(R.id.btn_logout);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1.hide();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1.hide();
                Intent intent = new Intent(ActivityMain.this,ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
