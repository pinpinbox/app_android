package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.Gradient.ScrimUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.StringUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerAuthorAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.GAControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.UrlClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopBoard;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * Created by kevin9594 on 2016/12/28.
 */
public class AuthorActivity extends DraggerActivity implements View.OnClickListener {


    private Activity mActivity;
    private ExStaggeredGridLayoutManager manager;

    private GetCreativeTask getCreativeTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;
    private AttentionTask attentionTask;
    private FollowTask followTask;

    private PopBoard board;

    private RecyclerAuthorAdapter authorAdapter;

    private List<ItemAlbum> albumList;

    private JSONArray p40JsonArray;
    private ItemUser itemUser;

    private RelativeLayout rBackgroundParallax, rCreativeName;
    private RecyclerView rvAuthor;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private SmoothProgressBar pbLoadMore;
    private RoundCornerImageView userImg;
    private ImageView backImg, bannerImg, messageImg, aboutImg, shareImg, webImg, facebookImg, googleImg, instagramImg, linkedinImg, pinterestImg, twitterImg, youtubeImg;
    private LinearLayout linLink;
    private TextView tvName, tvAttention, tvFollow, tvViewed, tvSponsor, tvCreativeName, tvGuide;
    private View viewHeader;

    private String id, token;
    private String p40Result = "", p40Message = "";
    private String strPicture = "", strName = "";
    private String user_id = "";

    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isDoingMore = false;
    private boolean shareElement = true;
    private boolean showSponsor = false;
    private boolean from_album_info = false;


    private int round; //listview添加前的初始值
    private int rangeCount; //listview每次添加的數量
    private int intChangeFollowItem = 0;
    private int doingType;
    private int deviceType;
    private static final int PHONE = 10001;
    private static final int TABLE = 10002;

    private float scale = 0;

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", mActivity.getClass(), "onLoad");

                if (isDoingMore) {
                    MyLog.Set("e", AuthorActivity.class, "正在讀取更多項目");
                    return;
                }

                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", mActivity.getClass(), "sizeMax");
            }
        }

    };


    /*從 myfollow進來 需要用此method*/
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

//    @Override
//    public void onActivityReenter(int resultCode, Intent data) {
//        super.onActivityReenter(resultCode, data);
//
//        // Postpone the shared element return transition.
//        postponeEnterTransition();
//
//        // TODO: Call the "scheduleStartPostponedTransition()" method
//        // above when you know for certain that the shared element is
//        // ready for the transition to begin.
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_author);

        supportPostponeEnterTransition();

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);

        doScheme(getIntent());

        if (user_id.equals("")) {
            user_id = getIntent().getExtras().getString(Key.author_id);
        }

        getBundle();
        init();
        setRecyclerRefreshLayouControl();
        setRecycler();

        setShareElementAnim();

    }

    private void doScheme(Intent intent) {

        DensityUtility.setScreen(this);

        PPBApplication.getInstance().setSharedPreferences(getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE));

        try {
            Uri uri = intent.getData();
            try {
                if (uri != null) {

                    uriControl(intent, uri);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            /**2016.10.07*/
            intChangeFollowItem = bundle.getInt(Key.changeFollowStatusItem, -1);
            strPicture = bundle.getString(Key.picture, "");
            strName = bundle.getString(Key.name, "");
            shareElement = bundle.getBoolean(Key.shareElement, true);
            showSponsor = bundle.getBoolean("showSponsor", false);
            from_album_info = bundle.getBoolean(Key.from_album_info, false);

        } else {

            DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());

        }

    }

    private void init() {

        if (SystemUtility.isTablet(getApplicationContext())) {
            //平版
            deviceType = TABLE;
        } else {
            //手機
            deviceType = PHONE;
        }

        mActivity = this;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        albumList = new ArrayList<>();

        round = 0;
        rangeCount = 16;

        rBackgroundParallax = (RelativeLayout) findViewById(R.id.rBackgroundParallax);
        rCreativeName = (RelativeLayout) findViewById(R.id.rCreativeName);

        backImg = (ImageView) findViewById(R.id.backImg);
        bannerImg = (ImageView) findViewById(R.id.bannerImg);
        messageImg = (ImageView) findViewById(R.id.messageImg);
        aboutImg = (ImageView) findViewById(R.id.aboutImg);
        shareImg = (ImageView) findViewById(R.id.shareImg);

        tvAttention = (TextView) findViewById(R.id.tvAttention);
        tvCreativeName = (TextView) findViewById(R.id.tvCreativeName);

        rvAuthor = (RecyclerView) findViewById(R.id.rvAuthor);
        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.pinPinBoxRefreshLayout);

        //20171002
        SmoothProgressBar pbRefresh = (SmoothProgressBar) findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(findViewById(R.id.vRefreshAnim), pbRefresh);
        pinPinBoxRefreshLayout.setUserBgViewScale(rBackgroundParallax);


        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();


        rvAuthor.addItemDecoration(new SpacesItemDecoration(16, true));

        rvAuthor.setItemAnimator(new DefaultItemAnimator());
        rvAuthor.addOnScrollListener(mOnScrollListener);

        /*********************************************************************************************************/

        viewHeader = LayoutInflater.from(this).inflate(R.layout.header_2_0_0_user, null);
        userImg = viewHeader.findViewById(R.id.userImg);

        tvName = viewHeader.findViewById(R.id.tvName);
        tvFollow = viewHeader.findViewById(R.id.tvFollow);
        tvViewed = viewHeader.findViewById(R.id.tvViewed);
        tvSponsor = viewHeader.findViewById(R.id.tvSponsor);
        tvGuide = viewHeader.findViewById(R.id.tvGuide);

        /*2017.09.08 不讓圖片偏移*/
        tvName.setText(strName);


        /*********************************************************************************************************/

//        TextUtility.setBold((TextView) viewHeader.findViewById(R.id.tvAlbumsTitle), true);
//        TextUtility.setBold(tvName, tvCreativeName, tvFollow, tvViewed, tvSponsor);

        linLink = viewHeader.findViewById(R.id.linLink);

        webImg = viewHeader.findViewById(R.id.webImg);
        facebookImg = viewHeader.findViewById(R.id.facebookImg);
        googleImg = viewHeader.findViewById(R.id.googleImg);
        instagramImg = viewHeader.findViewById(R.id.instagramImg);
        linkedinImg = viewHeader.findViewById(R.id.linkedinImg);
        pinterestImg = viewHeader.findViewById(R.id.pinterestImg);
        twitterImg = viewHeader.findViewById(R.id.twitterImg);
        youtubeImg = viewHeader.findViewById(R.id.youtubeImg);


        webImg.setOnClickListener(this);
        facebookImg.setOnClickListener(this);
        googleImg.setOnClickListener(this);
        instagramImg.setOnClickListener(this);
        linkedinImg.setOnClickListener(this);
        pinterestImg.setOnClickListener(this);
        twitterImg.setOnClickListener(this);
        youtubeImg.setOnClickListener(this);


        shareImg.setOnClickListener(this);
        backImg.setOnClickListener(this);
        tvAttention.setOnClickListener(this);
        messageImg.setOnClickListener(this);
        aboutImg.setOnClickListener(this);





        /*20171208一次性訊息 活動結束拿掉*/
//        rSponsorMessage
//                tvBuyP


        RelativeLayout rSponsorMessage = viewHeader.findViewById(R.id.rSponsorMessage);
        TextView tvBuyP = viewHeader.findViewById(R.id.tvBuyP);


        if (showSponsor) {

            rSponsorMessage.setVisibility(View.VISIBLE);

            tvBuyP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(mActivity, BuyPointActivity.class));
                    ActivityAnim.StartAnim(mActivity);

                }
            });


        } else {

            rSponsorMessage.setVisibility(View.GONE);

        }


    }

    private void setRecyclerRefreshLayouControl() {


        pinPinBoxRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });


    }

    private void setdata() {
        if (itemUser.isFollow()) {
            /*已關注*/
            attenionSuccess();
        } else {
            /*未關注*/
            attenionCancel();
        }

        if (strPicture == null || strPicture.equals("") || strPicture.equals("null")) {
            strPicture = itemUser.getPicture();
            if (strPicture == null || strPicture.equals("")) {
                userImg.setImageResource(R.drawable.member_back_head);
            } else {
                Picasso.get()
                        .load(strPicture)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.member_back_head)
                        .tag(getApplicationContext())
                        .into(userImg);
            }
        }

        String strCover = itemUser.getCover();
        if (strCover == null || strCover.equals("") || strCover.equals("null")) {
            bannerImg.setImageResource(R.drawable.bg200_user_default);
        } else {
            Picasso.get()
                    .load(strCover)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg200_user_default)
                    .tag(getApplicationContext())
                    .into(bannerImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            rBackgroundParallax.setBackgroundColor(Color.parseColor(ColorClass.GREY_SECOND));
                            bannerImg.setAlpha(0.8f);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }


        tvName.setText(itemUser.getName().trim());

        if (itemUser.getCreative_name().equals("")) {
            rCreativeName.setVisibility(View.GONE);
        } else {
            rCreativeName.setVisibility(View.VISIBLE);
            tvCreativeName.setText(itemUser.getCreative_name().trim());
            tvCreativeName.setVisibility(View.VISIBLE);
            try {
                if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {

                    if (rCreativeName != null) {
                        rCreativeName.setBackground(
                                ScrimUtil.makeCubicGradientScrimDrawable(
                                        Color.parseColor(ColorClass.BLACK_ALPHA),
                                        8, //渐变层数
                                        Gravity.BOTTOM)); //起始方向

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        int intFollow = itemUser.getCount_from();
        int intViewed = itemUser.getViewed();
        int intSponsor = itemUser.getBesponsored();

        StringUtil.ThousandToK(tvFollow, intFollow);
        StringUtil.ThousandToK(tvViewed, intViewed);
        StringUtil.ThousandToK(tvSponsor, intSponsor);


        if (intSponsor < 1) {

            viewHeader.findViewById(R.id.linSponsorList).setVisibility(View.INVISIBLE);

        }


        authorAdapter.notifyDataSetChanged();

        setUrl();

        if (albumList.size() > 0) {
            tvGuide.setVisibility(View.GONE);
        } else {
            tvGuide.setVisibility(View.VISIBLE);
        }


    }

    private boolean checkUrl(String url, View v) {

        boolean isUrlExist = false;

        if (url == null || url.equals("") || url.equals("null")) {

            v.setVisibility(View.GONE);
            isUrlExist = false;
        } else {

            v.setVisibility(View.VISIBLE);
            isUrlExist = true;
        }
        return isUrlExist;
    }

    private void setUrl() {

        boolean bWeb = checkUrl(itemUser.getWeb(), webImg);

        boolean bFacebook = checkUrl(itemUser.getFacebook(), facebookImg);

        boolean bGoogle = checkUrl(itemUser.getGoogle(), googleImg);

        boolean bInstagram = checkUrl(itemUser.getInstagram(), instagramImg);

        boolean bLinkedin = checkUrl(itemUser.getLinkedin(), linkedinImg);

        boolean bPinterest = checkUrl(itemUser.getPinterest(), pinterestImg);

        boolean bTwitter = checkUrl(itemUser.getTwitter(), twitterImg);

        boolean bYoutube = checkUrl(itemUser.getYoutube(), youtubeImg);


        if (!bWeb && !bFacebook && !bGoogle && !bInstagram && !bLinkedin && !bPinterest && !bTwitter && !bYoutube) {
            linLink.setVisibility(View.GONE);
        } else {
            linLink.setVisibility(View.VISIBLE);
        }


    }

    private void setRecycler() {

        authorAdapter = new RecyclerAuthorAdapter(mActivity, albumList);
        rvAuthor.setAdapter(authorAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(authorAdapter);
        rvAuthor.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        manager = null;

        if (deviceType == TABLE) {

            //平版
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        } else if (deviceType == PHONE) {

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }


        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvAuthor.getAdapter(), manager.getSpanCount()));
        rvAuthor.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvAuthor, viewHeader);

        authorAdapter.setOnRecyclerViewListener(new RecyclerAuthorAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }


                Bundle bundle = new Bundle();

                bundle.putString(Key.album_id, albumList.get(position).getAlbum_id());
                bundle.putBoolean("return", true);
                bundle.putInt(Key.image_width, albumList.get(position).getCover_width());
                bundle.putInt(Key.image_height, albumList.get(position).getCover_height());

                if (SystemUtility.Above_Equal_V5()) {

                    bundle.putBoolean(Key.shareElement, true);
                    bundle.putString(Key.cover, albumList.get(position).getCover());
                    bundle.putInt(Key.image_orientation, albumList.get(position).getImage_orientation());

                    final ImageView img = v.findViewById(R.id.coverImg);

                    img.setTransitionName(albumList.get(position).getCover());

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                                    mActivity,
                                    img,
                                    ViewCompat.getTransitionName(img)
                            );

                    startActivity(
                            new Intent(mActivity, AlbumInfoActivity.class).putExtras(bundle), options.toBundle()
                    );


                } else {

                    bundle.putBoolean(Key.shareElement, false);
                    startActivity(
                            new Intent(mActivity, AuthorActivity.class).putExtras(bundle)
                    );
                    ActivityAnim.StartAnimFromBottom(mActivity);

                }

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


        //20171109
        manager.setScrollEnabled(false);


    }

    private void setShareElementAnim() {

        if (SystemUtility.Above_Equal_V5()) {
            userImg.setTransitionName(strPicture);
        }


        if (!strPicture.equals("")) {
            Picasso.get()
                    .load(strPicture)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(getApplicationContext())
                    .into(userImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            MyLog.Set("d", AuthorActivity.class, "onSuccess strPicture => " + strPicture);

                            if (SystemUtility.Above_Equal_V5()) {
                                scheduleStartPostponedTransition(userImg);
                            }

                            doGetCreative();

                        }

                        @Override
                        public void onError(Exception e) {

                            MyLog.Set("d", AuthorActivity.class, "onError strPicture => " + strPicture);


                            if (SystemUtility.Above_Equal_V5()) {
                                scheduleStartPostponedTransition(userImg);
                            }

                            doGetCreative();
                        }
                    });
        } else {

            if (SystemUtility.Above_Equal_V5()) {
                startPostponedEnterTransition();
            }
            doGetCreative();

        }

    }

    private void attenionSuccess() {
        tvAttention.setBackgroundResource(R.drawable.click_2_0_0_second_grey_button_frame_white_radius);
        tvAttention.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvAttention.setText(R.string.pinpinbox_2_0_0_button_follow_cancel);
    }

    private void attenionCancel() {
        tvAttention.setBackgroundResource(R.drawable.click_2_0_0_pink_button_radius);
        tvAttention.setTextColor(Color.parseColor(ColorClass.WHITE)); //pinpinbox_2_0_0_first_pink
        tvAttention.setText(R.string.pinpinbox_2_0_0_button_follow);
    }

    private void backCheck() {
        if (SystemUtility.getSystemVersion() >= SystemUtility.V5) {

            if (shareElement) {


                supportFinishAfterTransition();

                //視覺上先隱藏
                findViewById(R.id.rActionBar).setVisibility(View.GONE);
                rBackgroundParallax.setVisibility(View.GONE);


            } else {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }


        } else {
            MyLog.Set("d", getClass(), "手機版本小於5.0");

            finish();
            ActivityAnim.FinishAnim(mActivity);
        }
    }

    private void systemShare() {

        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        if (itemUser.getPicture() != null && !itemUser.getPicture().equals("")) {
//            Uri u = Uri.parse(itemUser.getPicture());
//            intent.putExtra(Intent.EXTRA_STREAM, u);
//        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, itemUser.getName() + " , " + UrlClass.shareUserUrl + itemUser.getUser_id());//分享內容
        startActivity(Intent.createChooser(intent, mActivity.getTitle()));

    }

    private void cleanPicasso() {

        int count = albumList.size();

        for (int i = 0; i < count; i++) {
            Picasso.get().invalidate(albumList.get(i).getCover());
        }

        if (itemUser != null && itemUser.getPicture() != null && !itemUser.getPicture().equals("")) {
            try {
//                Picasso.get().invalidate(itemUser.getPicture());

                Picasso.get().invalidate(strPicture);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.gc();
    }


    private void callProtocol40(String range) {

        String strJson = "";
        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P40_GetCreative,
                    SetMapByProtocol.setParam40_getcreative(id, token, user_id, range), null);

            if (BuildConfig.DEBUG) {
                Logger.json(strJson);
            }

        } catch (SocketTimeoutException timeout) {
            p40Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p40Result = jsonObject.getString(Key.result);
                if (p40Result.equals("1")) {

                    if (itemUser == null) {
                        itemUser = new ItemUser();
                        itemUser.setUser_id(getIntent().getExtras().getString(Key.author_id));
                    }

                    String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONObject jsonData = new JSONObject(jdata);

                    String user = JsonUtility.GetString(jsonData, ProtocolKey.user);
                    JSONObject jsonUser = new JSONObject(user);
                    itemUser.setDescription(JsonUtility.GetString(jsonUser, ProtocolKey.description));
                    itemUser.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                    itemUser.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
//                    itemUser.setCreative_name(JsonUtility.GetString(jsonUser, ProtocolKey.creative_name));
                    itemUser.setViewed(JsonUtility.GetInt(jsonUser, ProtocolKey.viewed));
                    itemUser.setCover(JsonUtility.GetString(jsonUser, ProtocolKey.cover));

                    String strCreativeName = JsonUtility.GetString(jsonUser, ProtocolKey.creative_name);

                    if (StringUtils.isTrimEmpty(strCreativeName)) {

                        strCreativeName = "";

                    }

                    itemUser.setCreative_name(strCreativeName);

                    itemUser.setDisscuss(JsonUtility.GetBoolean(jsonUser, ProtocolKey.discuss));

                    String userstatistics = JsonUtility.GetString(jsonData, ProtocolKey.userstatistics);
                    JSONObject jsonUserStatistics = new JSONObject(userstatistics);
                    itemUser.setBesponsored(JsonUtility.GetInt(jsonUserStatistics, ProtocolKey.besponsored));


                    String sociallink = JsonUtility.GetString(jsonUser, ProtocolKey.sociallink);

                    if (sociallink != null && !sociallink.equals("")) {
                        JSONObject jsonLink = new JSONObject(sociallink);
                        itemUser.setFacebook(JsonUtility.GetString(jsonLink, ProtocolKey.facebook));
                        itemUser.setGoogle(JsonUtility.GetString(jsonLink, ProtocolKey.google));
                        itemUser.setInstagram(JsonUtility.GetString(jsonLink, ProtocolKey.instagram));
                        itemUser.setLinkedin(JsonUtility.GetString(jsonLink, ProtocolKey.linkedin));
                        itemUser.setPinterest(JsonUtility.GetString(jsonLink, ProtocolKey.pinterest));
                        itemUser.setTwitter(JsonUtility.GetString(jsonLink, ProtocolKey.twitter));
                        itemUser.setWeb(JsonUtility.GetString(jsonLink, ProtocolKey.web));
                        itemUser.setYoutube(JsonUtility.GetString(jsonLink, ProtocolKey.youtube));
                    }

                    String follow = JsonUtility.GetString(jsonData, ProtocolKey.follow);
                    JSONObject jsonFollow = new JSONObject(follow);
                    itemUser.setCount_from(JsonUtility.GetInt(jsonFollow, ProtocolKey.count_from));
                    itemUser.setFollow(JsonUtility.GetBoolean(jsonFollow, ProtocolKey.follow));

                    String album = JsonUtility.GetString(jsonData, ProtocolKey.album);
                    p40JsonArray = new JSONArray(album);

                    int minHeight = DensityUtility.dip2px(mActivity.getApplicationContext(), 72);

                    for (int i = 0; i < p40JsonArray.length(); i++) {

                        JSONObject jsonAlbum = (JSONObject) p40JsonArray.get(i);

                        ItemAlbum itemAlbum = new ItemAlbum();
                        itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                        itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));
//                        String cover = JsonUtility.GetString(jsonAlbum, ProtocolKey.cover);
                        itemAlbum.setCover(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover));


                        try {
                            int width = jsonAlbum.getInt(ProtocolKey.cover_width);
                            int height = jsonAlbum.getInt(ProtocolKey.cover_height);
                            int image_height = StaggeredHeight.setImageHeight(width, height);

                            if (image_height < minHeight) {
                                image_height = minHeight;
                            }

                            itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                            itemAlbum.setCover_height(image_height);

                            itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));

                            if (width > height) {
                                itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                            } else if (height > width) {
                                itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                            } else {
                                itemAlbum.setImage_orientation(0);
                            }


                        } catch (Exception e) {
                            itemAlbum.setCover_hex("");
                            itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                            itemAlbum.setCover_height(PPBApplication.getInstance().getStaggeredWidth());
                            MyLog.Set("e", this.getClass(), "圖片長寬無法讀取");
                        }

                        String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                        JSONObject jsonUsefor = new JSONObject(usefor);
                        itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                        itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                        itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                        itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));

                        try {
                            String creative = JsonUtility.GetString(jsonData, ProtocolKey.creative);
                            JSONObject jsonCreative = new JSONObject(creative);
                            itemUser.setInfo_url(JsonUtility.GetString(jsonCreative, ProtocolKey.info_url));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        albumList.add(itemAlbum);

                    }
                } else if (p40Result.equals("0")) {
                    p40Message = jsonObject.getString(Key.message);
                } else {
                    p40Result = "";
                }
            } catch (Exception e) {
                p40Result = "";
                e.printStackTrace();
            }
        }

    }

    private void toWebView(String title, String url) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);

        Intent intent = new Intent(mActivity, WebViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);

    }

    private void doGetCreative() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        getCreativeTask = new GetCreativeTask();
        getCreativeTask.execute();

    }

    public void doRefresh() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        if (getCreativeTask != null && !getCreativeTask.isCancelled()) {
            getCreativeTask.cancel(true);
        }
        getCreativeTask = null;


        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = null;

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
        }
        moreDataTask = null;

        if (attentionTask != null && !attentionTask.isCancelled()) {
            attentionTask.cancel(true);
        }
        attentionTask = null;

        if (albumList.size() > 0) {
            cleanPicasso();
        }

        albumList.clear();

        authorAdapter.notifyDataSetChanged();

        round = 0;


        refreshTask = new RefreshTask();
        refreshTask.execute();

    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    private void doFollow() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        attentionTask = new AttentionTask();
        attentionTask.execute();

    }

    private void doFollowTask() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        followTask = new FollowTask();
        followTask.execute();
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoingTypeClass.DoDefault:
                        doGetCreative();
                        break;
                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;

                    case DoingTypeClass.DoRefresh:
                        doRefresh();
                        break;

                    case DoingTypeClass.DoChangeFollow:
                        doFollow();
                        break;

                    case DoingTypeClass.DoFollowTask:
                        doFollowTask();
                        break;

                }
            }
        };
        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }


    private class GetCreativeTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            round = 0;
            sizeMax = false;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            callProtocol40(round + "," + rangeCount);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p40Result.equals("1")) {

                setdata();

                ViewControl.AlphaTo1((LinearLayout) viewHeader.findViewById(R.id.linContents));

                ViewControl.AlphaTo1((LinearLayout) viewHeader.findViewById(R.id.linData));

                ViewControl.AlphaTo1(tvName);


                /*header bg 動畫*/
                mOnScrollListener.setBackgroundParallaxViews(rBackgroundParallax);


                manager.setScrollEnabled(true);


                if (p40JsonArray.length() == 0) {
                    sizeMax = true; // 已達最大值
                } else {

                    if (p40JsonArray.length() < rangeCount) {
                        MyLog.Set("d", mActivity.getClass(), "項目少於" + rangeCount);
                        sizeMax = true;
                        return;
                    }

                    round = round + rangeCount;

                }


                Bundle bundle = getIntent().getExtras();

                boolean pinpinboard = bundle.getBoolean(Key.pinpinboard, false);

                if (pinpinboard) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (board == null) {
                                board = new PopBoard(mActivity, PopBoard.TypeUser, itemUser.getUser_id(), (RelativeLayout) findViewById(R.id.rBackground), true);
                            } else {
                                board.clearList();
                                board.doGetBoard();
                            }
                        }
                    }, 200);
                }

            } else if (p40Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p40Message);

            } else if (p40Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    private class MoreDataTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDoingMore = true;
            doingType = DoingTypeClass.DoMoreData;
            pbLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.progressiveStart();
        }

        @Override
        protected Object doInBackground(Void... params) {
            callProtocol40(round + "," + rangeCount);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            isDoingMore = false;
            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();
            if (p40Result.equals("1")) {

                if (p40JsonArray.length() == 0) {
                    sizeMax = true;
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                } else {

                    authorAdapter.notifyItemRangeInserted(albumList.size(), rangeCount);

                    if (p40JsonArray.length() < rangeCount) {
                        MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                        sizeMax = true;
                        return;
                    }

                    round = round + rangeCount;

                }

            } else if (p40Result.equals("0")) {


                DialogV2Custom.BuildError(mActivity, p40Message);

            } else if (p40Result.equals(Key.timeout)) {
                connectInstability();
            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());

            }
        }
    }

    private class RefreshTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoRefresh;

        }

        @Override
        protected Object doInBackground(Void... params) {

            callProtocol40(round + "," + rangeCount);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            sizeMax = false;
            isNoDataToastAppeared = false;


            pinPinBoxRefreshLayout.setRefreshing(false);

            if (p40Result.equals("1")) {


                setdata();


                if (p40JsonArray.length() == 0) {
                    sizeMax = true; // 已達最大值
                } else {

                    authorAdapter.notifyDataSetChanged();

                    if (p40JsonArray.length() < rangeCount) {
                        MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                        sizeMax = true;
                        return;
                    }

                    round = round + rangeCount;

                }


            } else if (p40Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p40Message);

            } else if (p40Result.equals(Key.timeout)) {
                connectInstability();
            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());

            }


        }

    }

    private class AttentionTask extends AsyncTask<Void, Void, Object> {

        private int p12Result = -1;
        private String p12Message = "";

        private int followstatus;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoChangeFollow;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<String, String>();
            data.put(Key.id, id);
            data.put(Key.token, token);
            data.put("authorid", itemUser.getUser_id());
            String sign = IndexSheet.encodePPB(data);

            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);
            sendData.put("authorid", itemUser.getUser_id());
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P12_ChangeFollowStatus, sendData, null);//12
                MyLog.Set("d", getClass(), "p12strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p12Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p12Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p12Result == 1) {
                        String obj_data = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONObject obj = new JSONObject(obj_data);

                        followstatus = JsonUtility.GetInt(obj, ProtocolKey.followstatus);

                        if (followstatus == 0) {
                            itemUser.setFollow(false);
                        } else if (followstatus == 1) {
                            itemUser.setFollow(true);
                        }


                    } else if (p12Result == 0) {
                        p12Message = JsonUtility.GetString(jsonObject, Key.message);
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
            if (p12Result == 1) {

                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();

                switch (followstatus) {
                    case 0://取消追蹤

                        itemUser.setCount_from(itemUser.getCount_from() - 1);
                        tvFollow.setText(itemUser.getCount_from() + "");
                        itemUser.setFollow(false);
                        attenionCancel();

                        for (int i = 0; i < activityList.size(); i++) {
                            if (activityList.get(i).getClass().getSimpleName().equals(MyFollowActivity.class.getSimpleName())) {
                                ((MyFollowActivity) activityList.get(i)).removeItemById(itemUser.getUser_id());

                                shareElement = false;

                                break;
                            }
                        }


                        changeUserListFollowStatus(activityList);


                        break;
                    case 1://追蹤
                        attenionSuccess();
                        itemUser.setCount_from(itemUser.getCount_from() + 1);
                        tvFollow.setText(itemUser.getCount_from() + "");
                        itemUser.setFollow(true);

                        for (int i = 0; i < activityList.size(); i++) {
                            if (activityList.get(i).getClass().getSimpleName().equals(MyFollowActivity.class.getSimpleName())) {
                                ((MyFollowActivity) activityList.get(i)).doRefresh();
                                shareElement = false;
                                break;
                            }
                        }

                        changeUserListFollowStatus(activityList);


                        doFollowTask();


                        break;
                }


            } else if (p12Result == 0) {
                DialogV2Custom.BuildError(mActivity, p12Message);

            } else if (p12Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }


        private void changeUserListFollowStatus(List<Activity> activityList) {

            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i).getClass().getSimpleName().equals(SponsorListActivity.class.getSimpleName())) {
                    ((SponsorListActivity) activityList.get(i)).changeUserFollow();

                    break;
                }
            }

            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i).getClass().getSimpleName().equals(LikeListActivity.class.getSimpleName())) {
                    ((LikeListActivity) activityList.get(i)).changeUserFollow();

                    break;
                }
            }


            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i).getClass().getSimpleName().equals(FollowMeActivity.class.getSimpleName())) {
                    ((FollowMeActivity) activityList.get(i)).changeUserFollow();
                    break;
                }
            }


        }


    }

    private class FollowTask extends AsyncTask<Void, Void, Object> {

        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;
        private String p83Result = "", p83Message = "";

        private int numberofcompleted;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFollowTask;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> map = new HashMap<>();
            map.put(MapKey.id, id);
            map.put(MapKey.token, token);
            map.put(MapKey.task_for, TaskKeyClass.follow_user);
            map.put(MapKey.platform, "google");
            String sign = IndexSheet.encodePPB(map);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(MapKey.id, id);
            sendData.put(MapKey.token, token);
            sendData.put(MapKey.task_for, TaskKeyClass.follow_user);
            sendData.put(MapKey.platform, "google");
            sendData.put(MapKey.type, Value.user);
            sendData.put(MapKey.type_id, itemUser.getUser_id());
            sendData.put("sign", sign);

            String strJson = "";


            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                MyLog.Set("d", mActivity.getClass(), "p83strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = jsonObject.getString(Key.result);

                    if (p83Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = object.getString(Key.task);
                        String event = object.getString(Key.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = taskObj.getString(Key.name);
                        reward = taskObj.getString(Key.reward);
                        reward_value = taskObj.getString(Key.reward_value);
                        restriction = taskObj.getString(Key.restriction);
                        restriction_value = taskObj.getString(Key.restriction_value);

                        numberofcompleted = taskObj.getInt(Key.numberofcompleted);

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("3")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("0")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else {
                        p83Result = "";
                    }
                } catch (Exception e) {
                    p83Result = "";
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p83Result.equals("1")) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name);
                    d.getTvRestriction().setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_count) + numberofcompleted + "/" + restriction_value);
                    d.getTvRestriction().setVisibility(View.VISIBLE);
                } else {
                    d.getTvTitle().setText(name);
                }

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /*獲取當前使用者P點*/
                    String point = PPBApplication.getInstance().getData().getString(Key.point, "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    PPBApplication.getInstance().getData().edit().putString(Key.point, newP).commit();


                } else {
                    d.getTvDescription().setText(reward_value);
                }


                if (url == null || url.equals("") || url.equals("null")) {
                    d.getTvLink().setVisibility(View.GONE);
                } else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            Intent intent = new Intent(mActivity, WebViewActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    });
                }


            } else if (p83Result.equals("2")) {


            } else if (p83Result.equals("3")) {


            } else if (p83Result.equals("0")) {


            } else if (p83Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private String getValue(Intent intent) {
        String value = "";
        try {
            String dataString = intent.getDataString();
            String arg = dataString.substring(dataString.indexOf("?") + 1, dataString.length());

            String[] strs = arg.split("&");
            HashMap<String, String> map = new HashMap<String, String>();
            for (int x = 0; x < strs.length; x++) {
                String[] strs2 = strs[x].split("=");
                if (strs2.length == 2) {
                    System.out.println(strs2[0] + " , " + strs2[1]);
                    map.put(strs2[0], strs2[1]);
                    value = strs2[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    private void uriControl(Intent intent, Uri uri) {

//        List<String> strPathPrefix1 = uri.getPathSegments();
//        String param0 = strPathPrefix1.get(0);

        user_id = getValue(intent);
        MyLog.Set("d", getClass(), "user_id => " + user_id);


//        String a = uri.getPath();
//        MyLog.Set("d", getClass(), "uri.getPath() => " + a);

//        switch (param0) {
//            case "index":
//                MyLog.Set("d", getClass(), "param0 => index");
//                String param1 = strPathPrefix1.get(1);
//
//                MyLog.Set("d", getClass(), "param1 => " + param1);
//
//                break;
//
//            case "creative":
//
//                FlurryUtil.onEvent(FlurryKey.from_web_to_author);
//
//                MyLog.Set("e", getClass(), "creativecreativecreativecreativecreativecreativecreative");
//                break;
//
//
//        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        try {
            Uri uri = intent.getData();
            try {
                if (uri != null) {


                    //20171018
//                    List<Activity> acList = SystemUtility.SysApplication.getInstance().getmList();
//                    for (int i = 0; i < acList.size(); i++) {
//
//                        Activity ac = acList.get(i);
//                        String strAcName = ac.getClass().getSimpleName();
//
//                        if (!strAcName.equals(MainActivity.class.getSimpleName())) {
//                            ac.finish();
//                        }
//                    }

                    uriControl(intent, uri);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (view.getId()) {

            case R.id.shareImg:
                systemShare();
                break;

            case R.id.backImg:
                backCheck();
                break;

            case R.id.messageImg:

                if (!itemUser.isDisscuss()) {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_board_is_close);

                    return;
                }


                if (board == null) {
                    board = new PopBoard(mActivity, PopBoard.TypeUser, itemUser.getUser_id(), (RelativeLayout) findViewById(R.id.rBackground), true);
                } else {
                    board.clearList();
                    board.doGetBoard();
                }

                break;

            case R.id.aboutImg:

                Bundle bundle = new Bundle();
//                bundle.putString(Key.url, UrlClass.shareUserUrl + itemUser.getUser_id() + "&appview=true");
                bundle.putString(Key.url, itemUser.getInfo_url());
                bundle.putString(Key.title, itemUser.getName());

                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(mActivity);

                break;

            case R.id.tvAttention:


                if (itemUser == null) {
                    DialogV2Custom.BuildUnKnow(mActivity, "itemUser == null");
                    return;
                }


                if (!itemUser.isFollow()) {
                    doFollow();
                } else {

                    if (intChangeFollowItem != -1) {

                        DialogV2Custom d = new DialogV2Custom(mActivity);
                        d.setStyle(DialogStyleClass.CHECK);
                        d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_remove_from_followlist);
                        d.setCheckExecute(new CheckExecute() {
                            @Override
                            public void DoCheck() {
                                doFollow();
                            }
                        });
                        d.show();
                    } else {
                        doFollow();
                    }
                }

                break;

            case R.id.webImg:
                toWebView("home", itemUser.getWeb());
                break;

            case R.id.facebookImg:
                toWebView("facebook", itemUser.getFacebook());
                break;

            case R.id.googleImg:
                toWebView("google plus", itemUser.getGoogle());
                break;

            case R.id.instagramImg:
                toWebView("instagram", itemUser.getInstagram());
                break;

            case R.id.linkedinImg:
                toWebView("linkedin", itemUser.getLinkedin());
                break;

            case R.id.pinterestImg:
                toWebView("pinterest", itemUser.getPinterest());
                break;

            case R.id.twitterImg:
                toWebView("twitter", itemUser.getTwitter());
                break;

            case R.id.youtubeImg:
                toWebView("youtube", itemUser.getYoutube());
                break;

        }


    }


    @Override
    public void onBackPressed() {

        backCheck();

    }


    @Override
    protected void onPause() {

        cleanPicasso();

        super.onPause();
    }

    @Override
    public void onResume() {


        GAControl.sendViewName("用戶專區");


        super.onResume();
    }


    @Override
    public void onDestroy() {


        cancelTask(getCreativeTask);
        getCreativeTask = null;

        cancelTask(moreDataTask);
        moreDataTask = null;

        cancelTask(attentionTask);
        attentionTask = null;

        cancelTask(refreshTask);
        refreshTask = null;

        cancelTask(followTask);
        followTask = null;

        Recycle.IMG(backImg);
        Recycle.IMG(webImg);
        Recycle.IMG(facebookImg);
        Recycle.IMG(googleImg);
        Recycle.IMG(instagramImg);
        Recycle.IMG(linkedinImg);
        Recycle.IMG(pinterestImg);
        Recycle.IMG(twitterImg);
        Recycle.IMG(youtubeImg);

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        cleanPicasso();
        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }

}
