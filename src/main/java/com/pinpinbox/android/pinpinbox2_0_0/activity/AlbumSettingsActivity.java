package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerAlbumSettingsAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerBarCodeAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumSettings;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ScrollLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.SnackManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.HashMapKeyControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.UserGradeKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DismissExcute;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMe;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMyUpLoad;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentScanSearch;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightEndedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightStartedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.SimpleTarget;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.Spotlight;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol33_AlbumSettings;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol34_GetAlbumSettings;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol96_InsertAlbumIndex;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol97_DeleteAlbumIndex;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by kevin9594 on 2017/4/2.
 */
public class AlbumSettingsActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private ItemAlbumSettings.Builder builder;
    private FragmentScanSearch fragmentScanSearch;
    private MediaPlayer mediaPlayer;

    private GetSettingsTask getSettingsTask;
    private GetPhotoCountTask getPhotoCountTask;
    private SendContributeTask sendContributeTask;
    private Protocol33_AlbumSettings protocol33;
    private Protocol34_GetAlbumSettings protocol34;
    private Protocol96_InsertAlbumIndex protocol96;
    private Protocol97_DeleteAlbumIndex protocol97;

    private RecyclerAlbumSettingsAdapter categoryFirstAdapter, categorySecondAdapter;
    private RecyclerBarCodeAdapter barCodeAdapter;

    private List<ItemAlbumSettings> categoryFirstList, categorySecondList, actList;


    private LinearLayout linEnableSc, linCloseSc, linEnableReward, linCloseReward;
    private EditText edName, edDescription, edLocation, edPoint, edScan, edReward;
    private TextView tvToCreation, tvSaveToLeave, tvAct,
            tvSelectFirstPaging, tvIndispensable1, tvIndispensable2, tvIndispensable3,
            tvIndispensable4, tvEnableSc, tvCloseSc, tvEnableReward, tvCloseReward;
    private ImageView scanImg, actImg, addBarCodeImg, backImg, enableScImg, closeScImg, enableRewardImg, closeRewardImg;
    private RecyclerView rvCategoryFirst, rvCategorySecond;

    private String id, token;
    private String album_id;
    private String strName, strDescription, strLocation, strAct;
    private String settings, strUsergrade;
    private String event_id;
    private String strGetName = "", strGetDescription = "", strGetLocation = "", strGetPoint = "", strGetRewardDescription = "";
    private String strPrefixText, strSpecialUrl;
    private String strRewardDescription;

    private List<String> albumindexList;

    private int doingType;
    private static final int RefreshSecondCategoryPaging = 1;
    private int intCategoryarea_id, intCategory_id, intPoint;
    private int barCodeItem;
    private static final int REQUEST_CODE_CAMERA = 104;
    private final int SUCCESS = 0;
    private final int REFUSE = -1;


    private boolean isCreative = false;
    private boolean isContribute = false; //是否投稿模式
    private boolean isToCreation = false;
    private boolean isCreationExist = false;
    private boolean isModify = false;
    private boolean isNewCreate = false;
    private boolean isRewardAfterCollect = false;
    private boolean isDisplayNumOfCollect = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_albumsettings);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();
        init();
        setBold();
        setRecyclers();
        doGetSettings();

    }


    private void TapAct() {

        actImg.setClickable(false);

        SimpleTarget firstTarget = new SimpleTarget.Builder(mActivity)
                .setPoint(actImg)
                .setDescription(getResources().getString(R.string.pinpinbox_2_0_0_other_text_privacy_direction))
                .build();

        Spotlight.with(this)
                .setDuration(400) // duration of Spotlight emerging and disappearing in ms
                .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
                .setTargets(firstTarget) // set targets. see below for more info
                .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
                    @Override
                    public void onStarted() {
//                        Toast.makeText(mActivity.getApplicationContext(), "spotlight is started", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnSpotlightEndedListener(new OnSpotlightEndedListener() { // callback when Spotlight ends
                    @Override
                    public void onEnded() {
//                        Toast.makeText(mActivity.getApplicationContext(), "spotlight is ended", Toast.LENGTH_SHORT).show();


                        PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.first_to_albumsetting, true).commit();
                        MyLog.Set("d", mActivity.getClass(), "save first_to_albumsetting");

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                        actImg.setClickable(true);
//                            }
//                        }, 300);


                    }
                })
                .start(); // start Spotlight


//        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(mActivity);
//        builder.setAnimationInterpolator(new FastOutSlowInInterpolator());
//        builder.setCaptureTouchEventOutsidePrompt(true);
//        builder.setPrimaryText("");
//        builder.setFocalRadius(20f);
//        builder.setBackgroundColourFromRes(R.color.pinpinbox_2_0_0_first_main);
//        builder.setFocalColourFromRes(R.color.pinpinbox_2_0_0_first_main);
//        builder.setSecondaryTextColourFromRes(R.color.white);
//        builder.setTarget(actImg)
//                .setSecondaryText(R.string.pinpinbox_2_0_0_other_text_privacy_direction)
//                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
//                    @Override
//                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
//
//                    }
//
//                    @Override
//                    public void onHidePromptComplete() {
//
//                        PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.first_to_albumsetting, true).commit();
//                        MyLog.Set("d", mActivity.getClass(), "save first_to_albumsetting");
//
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                actImg.setClickable(true);
//                            }
//                        }, 300);
//
//
//                    }
//                }).show();


    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            album_id = bundle.getString(Key.album_id, "");
            isContribute = bundle.getBoolean(Key.isContribute, false);
//            isCreationExist = bundle.getBoolean(Key.isCreationExist, false);
            event_id = bundle.getString(Key.event_id, "");
            isNewCreate = bundle.getBoolean(Key.isNewCreate, false);
            strPrefixText = bundle.getString(Key.prefix_text, "");
            strSpecialUrl = bundle.getString(Key.special, "");

            /*20171115*/
            List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
            for (int i = 0; i < activityList.size(); i++) {
                String mName = activityList.get(i).getClass().getSimpleName();
                if (mName.equals(CreationActivity.class.getSimpleName())) {
                    isCreationExist = true;
                    break;
                }
            }


        }

    }

    private void init() {

        mActivity = this;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        isCreative = PPBApplication.getInstance().getData().getBoolean(Key.creative, false);

        builder = new ItemAlbumSettings.Builder();

        categoryFirstList = new ArrayList<>();
        categorySecondList = new ArrayList<>();
        actList = new ArrayList<>();
        albumindexList = new ArrayList<>();

        linEnableSc = (LinearLayout) findViewById(R.id.linEnableSc);
        linCloseSc = (LinearLayout) findViewById(R.id.linCloseSc);
        linEnableReward = (LinearLayout) findViewById(R.id.linEnableReward);
        linCloseReward = (LinearLayout) findViewById(R.id.linCloseReward);


        edName = (EditText) findViewById(R.id.edName);
        edDescription = (EditText) findViewById(R.id.edDescription);
        edLocation = (EditText) findViewById(R.id.edLocation);
        edPoint = (EditText) findViewById(R.id.edPoint);
        edScan = (EditText) findViewById(R.id.edScan);
        edReward = (EditText) findViewById(R.id.edReward);

        tvToCreation = (TextView) findViewById(R.id.tvToCreation);
        tvSaveToLeave = (TextView) findViewById(R.id.tvSaveToLeave);
        tvAct = (TextView) findViewById(R.id.tvAct);
        tvSelectFirstPaging = (TextView) findViewById(R.id.tvSelectFirstPaging);
        tvIndispensable1 = (TextView) findViewById(R.id.tvIndispensable1);
        tvIndispensable3 = (TextView) findViewById(R.id.tvIndispensable3);
        tvIndispensable4 = (TextView) findViewById(R.id.tvIndispensable4);
        tvEnableSc = (TextView) findViewById(R.id.tvEnableSc);
        tvCloseSc = (TextView) findViewById(R.id.tvCloseSc);
        tvEnableReward = (TextView) findViewById(R.id.tvEnableReward);
        tvCloseReward = (TextView) findViewById(R.id.tvCloseReward);


        scanImg = (ImageView) findViewById(R.id.scanImg);
        actImg = (ImageView) findViewById(R.id.actImg);
        backImg = (ImageView) findViewById(R.id.backImg);
        addBarCodeImg = (ImageView) findViewById(R.id.addBarCodeImg);
        enableScImg = (ImageView) findViewById(R.id.enableScImg);
        closeScImg = (ImageView) findViewById(R.id.closeScImg);
        enableRewardImg = (ImageView) findViewById(R.id.enableRewardImg);
        closeRewardImg = (ImageView) findViewById(R.id.closeRewardImg);


        rvCategoryFirst = (RecyclerView) findViewById(R.id.rvCategoryFirst);
        rvCategorySecond = (RecyclerView) findViewById(R.id.rvCategorySecond);


        scanImg.setOnClickListener(this);
        actImg.setOnClickListener(this);
        backImg.setOnClickListener(this);
        addBarCodeImg.setOnClickListener(this);
        tvToCreation.setOnClickListener(this);
        tvSaveToLeave.setOnClickListener(this);
        linEnableSc.setOnClickListener(this);
        linCloseSc.setOnClickListener(this);
        linEnableReward.setOnClickListener(this);
        linCloseReward.setOnClickListener(this);

        /*20171120 string null error*/
        /*set strAct default*/
        strAct = "open";


        edPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals("") && StringIntMethod.StringToInt(s.toString()) > 50000){
                    edPoint.setText(50000 + "");
                }

            }
        });


    }

    private void setBold() {

//        TextUtility.setBold(
//                (TextView) findViewById(R.id.tv1),
//                (TextView) findViewById(R.id.tv2),
//                (TextView) findViewById(R.id.tv3),
//                (TextView) findViewById(R.id.tv4),
//                (TextView) findViewById(R.id.tvPoint),
//                (TextView) findViewById(R.id.tvAdvanced),
//                (TextView) findViewById(R.id.tvSponsorCountVisibility),
//                (TextView) findViewById(R.id.tvReward));

    }

    private void setRecyclers() {

        rvCategoryFirst.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rvCategorySecond.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));


        categoryFirstAdapter = new RecyclerAlbumSettingsAdapter(mActivity, categoryFirstList);
        categorySecondAdapter = new RecyclerAlbumSettingsAdapter(mActivity, categorySecondList);


        rvCategoryFirst.setAdapter(categoryFirstAdapter);
        rvCategorySecond.setAdapter(categorySecondAdapter);


        recyclerClick(categoryFirstAdapter, categoryFirstList, RefreshSecondCategoryPaging);

        recyclerClick(categorySecondAdapter, categorySecondList, 0);

    }

    private void recyclerClick(final RecyclerAlbumSettingsAdapter adapter, final List<ItemAlbumSettings> list, final int doing) {


        adapter.setOnRecyclerViewListener(new RecyclerAlbumSettingsAdapter.OnRecyclerViewListener() {

            @Override
            public void onItemClick(final int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                isModify = true;

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {

                        if (i == position) {
                            MyLog.Set("d", mActivity.getClass(), "已是選取狀態");

                            return;
                        }

                        MyLog.Set("d", mActivity.getClass(), "取消選取 => " + i);
                        list.get(i).setSelect(false);
                        adapter.notifyItemChanged(i);

                        MyLog.Set("d", mActivity.getClass(), "選取 => " + position);
                        list.get(position).setSelect(true);
                        adapter.notifyItemChanged(position);

                        if (doing == RefreshSecondCategoryPaging) {
                            refreshSecondCategoryPaging(position);
                        }

                        return;
                    }
                }

                MyLog.Set("d", mActivity.getClass(), "選取aaa => " + position);
                list.get(position).setSelect(true);
                adapter.notifyItemChanged(position);

                if (doing == RefreshSecondCategoryPaging) {
                    refreshSecondCategoryPaging(position);
                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


    }

    private void refreshSecondCategoryPaging(int position) {

        categorySecondList.clear();

        try {

            JSONArray secondpagingArray = new JSONArray(categoryFirstList.get(position).getSecondpaging());

            for (int i = 0; i < secondpagingArray.length(); i++) {

                JSONObject o = secondpagingArray.getJSONObject(i);
                categorySecondList.add(
                        builder
                                .id(JsonUtility.GetInt(o, ProtocolKey.id))
                                .name(JsonUtility.GetString(o, ProtocolKey.name))
                                .build()
                );
            }

            categorySecondAdapter.notifyDataSetChanged();
            rvCategorySecond.scrollToPosition(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (categorySecondList.size() == 0) {
            tvSelectFirstPaging.setVisibility(View.VISIBLE);
            rvCategorySecond.setVisibility(View.GONE);
        } else {
            tvSelectFirstPaging.setVisibility(View.GONE);
            rvCategorySecond.setVisibility(View.VISIBLE);
        }

    }

    private void changeActType() {

        if (strAct.equals("close")) {
            actImg.setImageResource(R.drawable.ic200_act_close_pink);
            tvAct.setText(R.string.pinpinbox_2_0_0_button_actclose);
            tvAct.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
            tvIndispensable1.setVisibility(View.GONE);
//            tvIndispensable2.setVisibility(View.GONE);
            tvIndispensable3.setVisibility(View.GONE);
            tvIndispensable4.setVisibility(View.GONE);
        } else if (strAct.equals("open")) {
            actImg.setImageResource(R.drawable.ic200_act_open_white);
            tvAct.setText(R.string.pinpinbox_2_0_0_button_actopen);
            tvAct.setTextColor(Color.parseColor(ColorClass.MAIN_FIRST));
            tvIndispensable1.setVisibility(View.VISIBLE);
//            tvIndispensable2.setVisibility(View.VISIBLE);
            tvIndispensable3.setVisibility(View.VISIBLE);
            tvIndispensable4.setVisibility(View.VISIBLE);
        }

    }

    private void checkToSend() {

        //偶爾發生 error 預設為close
        if (strAct == null) {
            strAct = "close";
        }

        if (strAct.equals("open")) {

            if (edName.getText().toString().equals("")) {
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_name);
                return;
            }

//            if (edDescription.getText().toString().equals("")) {
//                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_description_is_null);
//                return;
//            }


            boolean isSelectCategoryFirst = false;
            for (int i = 0; i < categoryFirstList.size(); i++) {
                if (categoryFirstList.get(i).isSelect()) {
                    isSelectCategoryFirst = true;
                    break;
                }
            }
            if (!isSelectCategoryFirst) {
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_first_paging_is_null);
                return;
            }

            boolean isSelectCategorySecond = false;
            for (int i = 0; i < categorySecondList.size(); i++) {
                if (categorySecondList.get(i).isSelect()) {
                    isSelectCategorySecond = true;
                    break;
                }
            }


            if (!isSelectCategorySecond) {
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_first_second_is_null);
                return;
            }

            setJsonObjectToSend();

        } else {

            if (isContribute) {
                PinPinToast.showErrorToast(mActivity, R.string.open_act_for_contribute);
                return;
            }
            setJsonObjectToSend();
        }

    }

    private void setJsonObjectToSend() {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(ProtocolKey.name, edName.getText().toString());
            jsonObject.put(ProtocolKey.description, edDescription.getText().toString());
            jsonObject.put(ProtocolKey.location, edLocation.getText().toString());
            jsonObject.put(ProtocolKey.usergrade, strUsergrade);
            jsonObject.put(ProtocolKey.act, strAct);

            String p = edPoint.getText().toString();
            if (p.equals("")) {
                p = "0";
            }

            if (p.length() > 1 && p.substring(0, 1).equals("0")) {
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_first_number_can_not_set_zero);
                return;
            }

            if (p.equals("1") || p.equals("2")) {
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_at_least_3p);
                return;
            }



            if(StringIntMethod.StringToInt(p) >50000){
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_max_is_50000p);
                return;
            }



            jsonObject.put(ProtocolKey.point, p);

            for (int i = 0; i < categorySecondList.size(); i++) {
                if (categorySecondList.get(i).isSelect()) {
                    jsonObject.put(ProtocolKey.category_id, categorySecondList.get(i).getId());
                    break;
                }
            }


            if (isDisplayNumOfCollect) {

                jsonObject.put(ProtocolKey.display_num_of_collect, true);

            } else {

                jsonObject.put(ProtocolKey.display_num_of_collect, false);

            }

            if (isRewardAfterCollect) {

                jsonObject.put(ProtocolKey.reward_after_collect, true);
                jsonObject.put(ProtocolKey.reward_description, edReward.getText().toString());

            } else {

                jsonObject.put(ProtocolKey.reward_after_collect, false);

            }


            settings = jsonObject.toString();

            if (BuildConfig.DEBUG) {
                Logger.json(settings);
            }

            doSendSettings();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkModify() {
        String name = edName.getText().toString();
        String description = edDescription.getText().toString();
        String location = edLocation.getText().toString();
        String point = edPoint.getText().toString();
        String rewardDescription = edReward.getText().toString();


        if (!strGetName.equals(name) || !strGetDescription.equals(description) || !strGetLocation.equals(location) || !strGetPoint.equals(point) || !strGetRewardDescription.equals(rewardDescription)) {
            isModify = true;
        }
    }

    private void creationIsNotExistToCreation() {

        Bundle b = new Bundle();
        b.putString(Key.album_id, album_id);
        b.putBoolean(Key.isContribute, isContribute);
        b.putString(Key.event_id, event_id);
        b.putBoolean(Key.isNewCreate, isNewCreate);
        Intent intent = new Intent(mActivity, CreationActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.begin_alpha_enter, R.anim.out_to_right);

//        ActivityAnim.StartAnim(mActivity);


    }

    private void toScan() {

        fragmentScanSearch = new FragmentScanSearch();

        Bundle bundle = new Bundle();
        bundle.putBoolean("reference", true);
        fragmentScanSearch.setArguments(bundle);

        if (!fragmentScanSearch.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frame, fragmentScanSearch, fragmentScanSearch.getClass().getSimpleName()).commit();
        }

    }

    private void setEnableSc() {

        enableScImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        closeScImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        tvEnableSc.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvCloseSc.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

        isDisplayNumOfCollect = true;

    }

    private void setCloseSc() {

        enableScImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        closeScImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);

        tvEnableSc.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvCloseSc.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

        isDisplayNumOfCollect = false;

    }

    private void setEnableReward() {

        enableRewardImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        closeRewardImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        tvEnableReward.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvCloseReward.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

        edReward.setVisibility(View.VISIBLE);

        isRewardAfterCollect = true;


    }

    private void setCloseReward() {

        enableRewardImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        closeRewardImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);

        tvEnableReward.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvCloseReward.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

        edReward.setVisibility(View.GONE);

        isRewardAfterCollect = false;

    }

    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doGetSettings();
                        break;

                    case DoingTypeClass.DoSetSettings:
                        doSetSettings();
                        break;

                    case DoingTypeClass.DoSend:
                        doSendSettings();
                        break;

                    case DoingTypeClass.DoDelete:
                        doDeleteBarCode();
                        break;

                    case DoingTypeClass.DoInsert:
                        doAddBarCode();
                        break;

                    case DoingTypeClass.DoGetPhoto:
                        doCheckPhotoCount();
                        break;

                    case DoingTypeClass.DoSendContribute:
                        doSendContribute();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doGetSettings() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getSettingsTask = new GetSettingsTask();
        getSettingsTask.execute();

    }

    private void doSetSettings() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        protocol34 = new Protocol34_GetAlbumSettings(
                mActivity,
                id,
                token,
                album_id,
                new Protocol34_GetAlbumSettings.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success(ItemAlbum itemAlbum) {

                        strName = itemAlbum.getName();
                        strDescription = itemAlbum.getDescription();
                        strLocation = itemAlbum.getLocation();
                        strAct = itemAlbum.getAct();
                        strRewardDescription = itemAlbum.getReward_description();

                        intCategoryarea_id = itemAlbum.getCategoryarea_id();
                        intCategory_id = itemAlbum.getCategory_id();
                        intPoint = itemAlbum.getPoint();

                        albumindexList = itemAlbum.getAlbumindexList();

                        isRewardAfterCollect = itemAlbum.isReward_after_collect();

                        isDisplayNumOfCollect = itemAlbum.isDisplay_num_of_collect();


                        //設為當前取得
                        strGetName = itemAlbum.getName();
                        strGetDescription = itemAlbum.getDescription();
                        strGetLocation = itemAlbum.getLocation();
                        strGetPoint = itemAlbum.getPoint() + "";
                        strGetRewardDescription = itemAlbum.getReward_description();


                        setData();

                        ViewControl.AlphaTo1(findViewById(R.id.scrollView));

                        boolean bFirstToAlbumSetting = PPBApplication.getInstance().getData().getBoolean(TaskKeyClass.first_to_albumsetting, false);

                        if (!bFirstToAlbumSetting) {
                            TapAct();
                        }


                    }

                    @Override
                    public void TimeOut() {
                        doSetSettings();
                    }


                    private void setData() {

                        if (isNewCreate && isContribute) {
                            edName.setHint(strPrefixText);
                        } else {
                            edName.setText(strName);
                        }


                        edDescription.setText(strDescription);
                        edLocation.setText(strLocation);
                        edPoint.setText(intPoint + "");
                        edReward.setText(strRewardDescription);


                        if (strUsergrade.equals(UserGradeKey.plus) || strUsergrade.equals(UserGradeKey.profession)) {

                            findViewById(R.id.linSponsorData).setVisibility(View.VISIBLE);

                            if (isDisplayNumOfCollect) {

                                setEnableSc();

                            } else {

                                setCloseSc();

                            }


                            if (isRewardAfterCollect && intPoint > 2) {

                                setEnableReward();

                            } else {

                                setCloseReward();

                            }


                        } else {

                            findViewById(R.id.linSponsorData).setVisibility(View.GONE);

                        }


                        if (strUsergrade.equals(UserGradeKey.profession)) {

                            findViewById(R.id.linScan).setVisibility(View.VISIBLE);


                        } else {

                            findViewById(R.id.linScan).setVisibility(View.GONE);

                        }

                        if (isNewCreate) {
                            strAct = "open";
                        }

                        changeActType();

                        for (int i = 0; i < categoryFirstList.size(); i++) {
                            if (intCategoryarea_id == categoryFirstList.get(i).getId()) {
                                categoryFirstList.get(i).setSelect(true);
                                rvCategoryFirst.scrollToPosition(i);
                                categoryFirstAdapter.notifyItemChanged(i);

                                try {

                                    JSONArray secondpagingArray = new JSONArray(categoryFirstList.get(i).getSecondpaging());

                                    for (int k = 0; k < secondpagingArray.length(); k++) {

                                        JSONObject o = secondpagingArray.getJSONObject(k);
                                        categorySecondList.add(
                                                builder
                                                        .id(JsonUtility.GetInt(o, ProtocolKey.id))
                                                        .name(JsonUtility.GetString(o, ProtocolKey.name))
                                                        .build()
                                        );
                                    }

                                    for (int s = 0; s < categorySecondList.size(); s++) {

                                        if (intCategory_id == categorySecondList.get(s).getId()) {
                                            categorySecondList.get(s).setSelect(true);
                                            categorySecondAdapter.notifyDataSetChanged();
                                            rvCategorySecond.scrollToPosition(s);

                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }


                        if (categorySecondList.size() == 0) {
                            tvSelectFirstPaging.setVisibility(View.VISIBLE);
                            rvCategorySecond.setVisibility(View.GONE);
                        } else {
                            tvSelectFirstPaging.setVisibility(View.GONE);
                            rvCategorySecond.setVisibility(View.VISIBLE);
                        }


                        RecyclerView rvBarCode = (RecyclerView) findViewById(R.id.rvBarCode);

                        ScrollLinearLayoutManager manager = new ScrollLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
                        manager.setScrollEnabled(false);

                        rvBarCode.setLayoutManager(manager);

                        barCodeAdapter = new RecyclerBarCodeAdapter(mActivity, albumindexList);
                        rvBarCode.setAdapter(barCodeAdapter);

                        barCodeAdapter.setOnDeleteListenter(new RecyclerBarCodeAdapter.OnDeleteListener() {
                            @Override
                            public void delete(int position) {

                                if (ClickUtils.ButtonContinuousClick()) {
                                    return;
                                }

                                barCodeItem = position;

                                DialogV2Custom d = new DialogV2Custom(mActivity);
                                d.setStyle(DialogStyleClass.CHECK);
                                d.setMessage(getResources().getString(R.string.pinpinbox_2_0_0_other_text_delete) + " " + albumindexList.get(barCodeItem) + " ?");
                                d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_delete);
                                d.setCheckExecute(new CheckExecute() {
                                    @Override
                                    public void DoCheck() {
                                        doDeleteBarCode();
                                    }
                                });
                                d.show();

                            }
                        });

                    }

                }
        );


    }

    private void doSendSettings() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        protocol33 = new Protocol33_AlbumSettings(
                mActivity,
                id,
                token,
                album_id,
                settings,
                new Protocol33_AlbumSettings.TaskCallBack() {

                    @Override
                    public void Prepare() {
                        doingType = DoingTypeClass.DoSend;
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success() {


                        String downLoadPath = DirClass.sdPath + PPBApplication.getInstance().getMyDir() + DirClass.dirAlbumList + album_id;

                        File file = new File(downLoadPath);
                        if (file.exists()) {
                            FileUtility.deleteFileFolder(new File(downLoadPath));
                        }


                        refreshOtherClass();


                        if (isToCreation) {

                            back();

                            return;
                        }


                        if (isContribute) {

                            closeCreation();

                            doSendContribute();


                        } else {

                            closeCreation();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isNewCreate) {
                                        SnackManager.showCollecttionSnack();
                                    }
                                    back();
                                }
                            }, 100);


                        }


                    }

                    @Override
                    public void TimeOut() {
                        doSendSettings();
                    }


                    private void refreshOtherClass() {

                        boolean myCollectIsExist = false;
                        boolean mainIsExist = false;
                        boolean InfoIsExist = false;
                        Activity acMyCollect = null;
                        Activity acMain = null;
                        Activity acInfo = null;
                        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                        for (int i = 0; i < activityList.size(); i++) {

                            String mName = activityList.get(i).getClass().getSimpleName();

                            if (mName.equals(MainActivity.class.getSimpleName())) {
                                acMain = activityList.get(i);
                                mainIsExist = true;
                            }

                            if (mName.equals(MyCollectActivity.class.getSimpleName())) {
                                acMyCollect = activityList.get(i);
                                myCollectIsExist = true;
                            }


                            if (mName.equals(AlbumInfoActivity.class.getSimpleName())) {
                                acInfo = activityList.get(i);
                                InfoIsExist = true;
                            }

                        }


                        if (myCollectIsExist) {
                            MyLog.Set("d", this.getClass(), "myCollectIsExist => " + myCollectIsExist);
                            FragmentMyUpLoad fragment = (FragmentMyUpLoad) ((MyCollectActivity) acMyCollect).getFragment(FragmentMyUpLoad.class.getSimpleName());
                            HashMap<String, Object> getMap = fragment.getClickMap();
                            HashMapKeyControl.changeMapKey(getMap, Key.act, strAct);
                            HashMapKeyControl.changeMapKey(getMap, Key.album_id, album_id);
                            HashMapKeyControl.changeMapKey(getMap, Key.albumname, edName.getText().toString());
                            HashMapKeyControl.changeMapKey(getMap, Key.albumdescription, edDescription.getText().toString());

                            fragment.updateClickPositionItem(getMap);

                        }


                        if (mainIsExist) {

                            FragmentMe fragmentMe = (FragmentMe) ((MainActivity) acMain).getFragment(FragmentMe.class.getSimpleName());

                            if (fragmentMe != null && fragmentMe.getAlbumList() != null) {
                                fragmentMe.doRefresh(false);
                            }

                        }


                        if (InfoIsExist) {

                            ((AlbumInfoActivity) acInfo).doGetAlbumInfo();

                        }

                    }


                }
        );


    }

    private void doAddBarCode() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        protocol96 = new Protocol96_InsertAlbumIndex(
                mActivity,
                id,
                token,
                edScan.getText().toString(),
                album_id,
                new Protocol96_InsertAlbumIndex.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        doingType = DoingTypeClass.DoInsert;
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success() {

                        isModify = true;


                        barCodeAdapter.addData(0, edScan.getText().toString());

                        edScan.setText("");

                    }

                    @Override
                    public void TimeOut() {
                        doAddBarCode();
                    }
                });


    }

    private void doDeleteBarCode() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }


        protocol97 = new Protocol97_DeleteAlbumIndex(
                mActivity,
                id,
                token,
                albumindexList.get(barCodeItem),
                album_id,
                new Protocol97_DeleteAlbumIndex.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        doingType = DoingTypeClass.DoDelete;
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success() {

                        isModify = true;

                        barCodeAdapter.removeData(barCodeItem);

                    }

                    @Override
                    public void TimeOut() {
                        doDeleteBarCode();
                    }
                });

    }

    private void doCheckPhotoCount() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getPhotoCountTask = new GetPhotoCountTask();
        getPhotoCountTask.execute();


    }

    private void doSendContribute() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        sendContributeTask = new SendContributeTask();
        sendContributeTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetSettingsTask extends AsyncTask<Void, Void, Object> {

        private int p32Result = -1;
        private String p32Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P32_GetAlbumdataOptions, SetMapByProtocol.setParam32_getalbumdataoptions(id, token), null);

                MyLog.Set("d", getClass(), this.getClass().getSimpleName() + "p32strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p32Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);

                    p32Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);


                    if (p32Result == 1) {


                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONObject object = new JSONObject(jdata);

                        String firstpaging = JsonUtility.GetString(object, ProtocolKey.firstpaging);
                        String act = JsonUtility.GetString(object, ProtocolKey.act);
                        strUsergrade = JsonUtility.GetString(object, ProtocolKey.usergrade);

                        JSONArray firstpagingArray = new JSONArray(firstpaging);
                        for (int i = 0; i < firstpagingArray.length(); i++) {

                            JSONObject obj = firstpagingArray.getJSONObject(i);

                            categoryFirstList.add(
                                    builder
                                            .id(JsonUtility.GetInt(obj, ProtocolKey.id))
                                            .name(JsonUtility.GetString(obj, ProtocolKey.name))
                                            .secondpaging(JsonUtility.GetString(obj, ProtocolKey.secondpaging))
                                            .select(false)
                                            .build()
                            );

                        }


                        JSONArray actArray = new JSONArray(act);
                        for (int i = 0; i < actArray.length(); i++) {
                            JSONObject obj = actArray.getJSONObject(i);
                            actList.add(
                                    builder
                                            .strId(JsonUtility.GetString(obj, ProtocolKey.id))
                                            .name(JsonUtility.GetString(obj, ProtocolKey.name))
                                            .select(false)
                                            .build()
                            );
                        }

                    } else if (p32Result == 0) {
                        p32Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p32Result == 1) {

                categoryFirstAdapter.notifyDataSetChanged();

                doSetSettings();


            } else if (p32Result == 0) {

                DialogV2Custom.BuildError(mActivity, p32Message);


            } else if (p32Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetPhotoCountTask extends AsyncTask<Void, Void, Object> {


        private int p57Result = -1;

        private String p57Message = "";

        private int photoCount = 0;


        private void getPhotoList() {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P57_GetAlbumOfDiy, SetMapByProtocol.setParam57_getalbumofdiy(id, token, album_id), null);
                MyLog.Set("d", this.getClass(), "p57strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p57Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p57Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p57Result == 1) {

                        String data = jsonObject.getString(ProtocolKey.data);

                        JSONObject jsonData = new JSONObject(data);

                        String photo = JsonUtility.GetString(jsonData, ProtocolKey.photo);

                        JSONArray jsonArray = new JSONArray(photo);

                        photoCount = jsonArray.length();


                    } else if (p57Result == 0) {//if(p57Result.equals("1"))

                        p57Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                    } else {

                        p57Result = -1;

                    }
                } catch (Exception e) {
                    p57Result = -1;
                    e.printStackTrace();
                }
            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetPhoto;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {


            getPhotoList();


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();


            if (p57Result == 1) {

                if (photoCount > 0) {

                    isModify = true;

                    if (strAct.equals("close")) {
                        strAct = "open";
                    } else if (strAct.equals("open")) {
                        strAct = "close";
                    }

                    changeActType();

                } else {

                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_content_is_null);

                }

            } else if (p57Result == 0) {

                DialogV2Custom.BuildError(mActivity, p57Message);

            } else if (p57Result == Key.TIMEOUT) {

                //連同 34 一起重新讀取
                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SendContributeTask extends AsyncTask<Void, Void, Object> {

        private boolean contributionstatus;
        private String p73Result = "", p73Message = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSendContribute;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P73_SwitchStatusOfContribution
                        , SetMapByProtocol.setParam73_switchstatusofcontribution(id, token, event_id, album_id)
                        , null);
                MyLog.Set("d", mActivity.getClass(), "p73strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p73Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p73Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);
                    if (p73Result.equals("1")) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONObject jsonData = new JSONObject(jdata);
                        String event = JsonUtility.GetString(jsonData, ProtocolKey.event);

                        JSONObject jsonEvent = new JSONObject(event);
                        contributionstatus = JsonUtility.GetBoolean(jsonEvent, ProtocolKey.contributionstatus);


                    } else if (p73Result.equals("0")) {
                        p73Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    } else {
                        p73Result = "";
                    }

                } catch (Exception e) {
                    p73Result = "";
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p73Result.equals("1")) {


                if (contributionstatus) {

                    PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_success);

                } else {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_cancel);

                }

                //移至closeCreation() 這裡再check一次
                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                for (int i = 0; i < activityList.size(); i++) {

                    String strClassName = activityList.get(i).getClass().getSimpleName();
                    if (strClassName.equals(EventActivity.class.getSimpleName()) || strClassName.equals(SelectMyWorksActivity.class.getSimpleName())) {

                        MyLog.Set("e", this.getClass(), "remove => " + strClassName);

                        activityList.get(i).finish();
                    }
                }


                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, album_id);
                bundle.putString(Key.event_id, event_id);
                bundle.putBoolean(Key.isNewCreate, isNewCreate);
                bundle.putBoolean(Key.isContribute, isContribute);
                bundle.putString(Key.special, strSpecialUrl);

                Intent intent = new Intent(mActivity, ReaderActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                ActivityAnim.StartAnim(mActivity);


            } else if (p73Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p73Message);
            } else if (p73Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }


//        private void checkClose(){
//
//            List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
//
//            Activity acE = null, acS = null;
//
//            for (int i = 0; i < activityList.size(); i++) {
//
//                String strClassName = activityList.get(i).getClass().getSimpleName();
//
//                if (strClassName.equals(EventActivity.class.getSimpleName())){
//                    acE = activityList.get(i);
//                }
//
//                if (strClassName.equals(SelectMyWorksActivity.class.getSimpleName())){
//                    acS = activityList.get(i);
//                }
//
//                if(acE==null && acS ==null){
//
//
//                    break;
//                }
//
//            }
//
//        }

    }

    private void closeCreation() {

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();

        for (int i = 0; i < activityList.size(); i++) {

            String strClassName = activityList.get(i).getClass().getSimpleName();

            if (strClassName.equals(CreationActivity.class.getSimpleName())
                    || strClassName.equals(EventActivity.class.getSimpleName()) || strClassName.equals(SelectMyWorksActivity.class.getSimpleName())
                    ) {

                activityList.get(i).finish();

            }
        }
    }

    public EditText getEdScan() {
        return this.edScan;
    }


    private int checkPermission(Activity ac, String permission) {

        int doingType = 0;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;
        } else {
            //未授權 判斷是否彈出詢問框 true => 彈出
//            if(ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)){
            doingType = REFUSE;

//            }else {
//                doingType = REFUSE_NO_ASK;
//            }
        }
        return doingType;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(REQUEST_CODE_CAMERA)
    public void requestCameraSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toScan();
            }
        }, 500);


    }

    @PermissionDenied(REQUEST_CODE_CAMERA)
    public void requestCameraFailed() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_camera);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    SystemUtility.getAppDetailSettingIntent(mActivity);
                }
            });
            d.show();

        } else {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");

        }
    }

    @Override
    public void onClick(View view) {
        if (ClickUtils.ButtonContinuousClick_1s()) {
            return;
        }

        switch (view.getId()) {

            case R.id.scanImg:


                if (albumindexList != null && albumindexList.size() < 20) {


                    switch (checkPermission(mActivity, Manifest.permission.CAMERA)) {

                        case SUCCESS:
                            toScan();
                            break;
                        case REFUSE:

                            MPermissions.requestPermissions(mActivity, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                            break;


                    }


                } else {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_is_max);

                }


                break;


            case R.id.actImg:


                doCheckPhotoCount();


                break;

            case R.id.addBarCodeImg:


                if (albumindexList != null && albumindexList.size() < 20) {

                    if (edScan.getText().toString().equals("")) {

                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_barcode);
                        return;
                    }

                    doAddBarCode();

                } else {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_is_max);

                }


                break;


            case R.id.tvToCreation:

                checkModify();

                if (isModify) {

                    final DialogV2Custom d = new DialogV2Custom(mActivity);
                    d.setStyle(DialogStyleClass.CHECK);
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_albuminfo_change_to_save);

                    d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_be_later);
                    d.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d.dismiss();

                            if (isCreationExist) {

                                back(); //OK

                                MyLog.Set("e", mActivity.getClass(), "#1");


                            } else {
                                /*編輯器不存在 intent進入*/
                                creationIsNotExistToCreation();

                                MyLog.Set("e", mActivity.getClass(), "#2");

                            }


                        }
                    });

                    d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_ok);
                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {

                            if (isCreationExist) {
                                isToCreation = true;
                                setJsonObjectToSend();

                                MyLog.Set("e", mActivity.getClass(), "#3");

                            } else {

                                /*編輯器不存在 intent進入*/
                                creationIsNotExistToCreation();

                                MyLog.Set("e", mActivity.getClass(), "#4");


                            }

                        }
                    });
                    d.show();

                } else {

                    if (isCreationExist) {
                        back();
                    } else {
                        /*編輯器不存在 intent進入*/
                        creationIsNotExistToCreation();
                    }
                }


                break;
            case R.id.tvSaveToLeave:

                checkToSend();

                break;

            case R.id.backImg:


                checkModify();

                if (isModify) {

                    final DialogV2Custom d = new DialogV2Custom(mActivity);
                    d.setStyle(DialogStyleClass.CHECK);
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_albuminfo_change_to_exit);

                    d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_continue_edit);
                    d.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d.dismiss();
                        }
                    });

                    d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_immediate_exit);
                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {

                            back();

                        }
                    });
                    d.show();

                } else {

                    back();

                }


                break;


            case R.id.linEnableSc:
                setEnableSc();
                isModify = true;
                break;

            case R.id.linCloseSc:
                setCloseSc();
                isModify = true;
                break;

            case R.id.linEnableReward:

                String p = edPoint.getText().toString();

                if (p == null || p.equals("") || StringIntMethod.StringToInt(p) < 3) {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_at_least_3p_to_enable);

                    return;
                }


                DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setMessage("啟用需遵守個人資料保護法之相關規定，盡善良管理人之注意義務，不得外流或作為本次回饋使用者以外目的之使用，並須遵守本平台含隱私權政策在內之相關規定，如有違反，需自負一切責任。");
                d.setStyle(DialogStyleClass.CHECK);
                d.getTvRightOrBottom().setVisibility(View.GONE);
                d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_i_know);
                d.setDismissExcute(new DismissExcute() {
                    @Override
                    public void AfterDismissDo() {
                        setEnableReward();
                    }
                });
                d.show();
                isModify = true;
                break;

            case R.id.linCloseReward:
                setCloseReward();
                isModify = true;
                break;

        }

    }


    @Override
    public void onBackPressed() {

        if (fragmentScanSearch != null && fragmentScanSearch.isAdded()) {

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(fragmentScanSearch).commit();
            return;

        }


        checkModify();

        if (isModify) {

            final DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_albuminfo_change_to_exit);

            d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_continue_edit);
            d.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d.dismiss();
                }
            });

            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_immediate_exit);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {

                    back();

                }
            });
            d.show();

        } else {

            back();

        }


    }

    @Override
    public void onResume() {
        super.onResume();

        if (mediaPlayer != null) {

            if (isPause) {
                mediaPlayer.start();
            }
        }

        super.onResume();

    }

    @Override
    public void onPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {

                MyLog.Set("d", getClass(), "media pause");

                mediaPlayer.pause();
                isPause = true;


            } else {

                MyLog.Set("d", getClass(), "media recycle");

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

        }

        super.onPause();
    }

    private boolean isPause = false;

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(this);

        cancelTask(getSettingsTask);
        cancelTask(protocol33);
        cancelTask(protocol34);

        cancelTask(protocol96);
        cancelTask(protocol97);


        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Recycle.IMG(scanImg);
        Recycle.IMG(actImg);

        super.onDestroy();
    }


}
