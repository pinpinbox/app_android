package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumInfo2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumSponsorList2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AppSettings2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Author2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.BuyPoint2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CategoryAll2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.EditProfile2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Event2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeList2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CategoryBookCase2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.FollowMe2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.LikeList2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollect2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyFollow2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.RecentAlbum2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.SponsorList2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.VideoPlayActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;

/**
 * Created by vmage on 2018/2/21.
 */

public class ActivityIntent {


    public static void toAlbumInfo(Activity currentActivity, boolean sharedElement, String album_id, String coverUrl, int imageOrientation, int width, int height, View coverImg) {


        Bundle bundle = new Bundle();

        bundle.putString(Key.album_id, album_id);
        bundle.putBoolean(Key.shareElement, sharedElement);

        if (SystemUtility.Above_Equal_V5() && sharedElement) {

            bundle.putString(Key.cover, coverUrl);
            bundle.putInt(Key.image_orientation, imageOrientation);
            bundle.putInt(Key.image_width, width);
            bundle.putInt(Key.image_height, height);

            coverImg.setTransitionName(coverUrl);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(
                            currentActivity,
                            coverImg,
                            ViewCompat.getTransitionName(coverImg)
                    );

            currentActivity.startActivity(
                    new Intent(currentActivity, AlbumInfo2Activity.class).putExtras(bundle), options.toBundle()
            );


        } else {

            currentActivity.startActivity(
                    new Intent(currentActivity, AlbumInfo2Activity.class).putExtras(bundle)
            );
            ActivityAnim.StartAnimFromBottom(currentActivity);


        }


    }

    public static void toUser(Activity currentActivity, boolean sharedElement, boolean openBoard, String user_id, String picture, String name, View userImg) {

        Bundle bundle = new Bundle();

        bundle.putString(Key.author_id, user_id);
        bundle.putBoolean(Key.shareElement, sharedElement);
        bundle.putBoolean(Key.pinpinboard, openBoard);

        if (SystemUtility.Above_Equal_V5() && sharedElement) {

            bundle.putString(Key.picture, picture);
            bundle.putString(Key.name, name);

            userImg.setTransitionName(picture);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(
                            currentActivity,
                            userImg,
                            ViewCompat.getTransitionName(userImg));
            currentActivity.startActivity(
                    new Intent(currentActivity, Author2Activity.class).putExtras(bundle), options.toBundle()
            );

        } else {
            currentActivity.startActivity(
                    new Intent(currentActivity, Author2Activity.class).putExtras(bundle)
            );
            ActivityAnim.StartAnim(currentActivity);
        }

    }

    public static void toEvent(Activity currentActivity, String event_id) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.event_id, event_id);

        currentActivity.startActivity(
                new Intent(currentActivity, Event2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toExchangeList(Activity currentActivity) {


        currentActivity.startActivity(
                new Intent(currentActivity, ExchangeList2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toCategoryAll(Activity currentActivity, int categoryarea_id, String categoryarea_name) {

        Bundle bundle = new Bundle();
        bundle.putInt(Key.categoryarea_id, categoryarea_id);
        bundle.putString(Key.categoryarea_name, categoryarea_name);

        currentActivity.startActivity(
                new Intent(currentActivity, CategoryAll2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toFeature(Activity currentActivity, int categoryarea_id) {

        Bundle bundle = new Bundle();
        bundle.putInt(Key.categoryarea_id, categoryarea_id);

        currentActivity.startActivity(
                new Intent(currentActivity, CategoryBookCase2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toVideoPlay(Activity currentActivity, String path_or_url) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.path_or_url, path_or_url);

        currentActivity.startActivity(
                new Intent(currentActivity, VideoPlayActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toSettings(Activity currentActivity) {

        currentActivity.startActivity(
                new Intent(currentActivity, AppSettings2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);


    }

    public static void toWeb(Activity currentActivity, String url, String title) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.url, url);
        bundle.putString(Key.title, title);

        currentActivity.startActivity(
                new Intent(currentActivity, WebView2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }


    public static void toFollowMe(Activity currentActivity) {

        currentActivity.startActivity(
                new Intent(currentActivity, FollowMe2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toSponsorList(Activity currentActivity) {

        currentActivity.startActivity(
                new Intent(currentActivity, SponsorList2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toAlbumSponsorList(Activity currentActivity, String album_id) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);

        currentActivity.startActivity(
                new Intent(currentActivity, AlbumSponsorList2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toLikesList(Activity currentActivity, String album_id) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);

        currentActivity.startActivity(
                new Intent(currentActivity, LikeList2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }


    public static void toEditProfile(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, EditProfile2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toWorkManage(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, MyCollect2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toMyFollow(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, MyFollow2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toRecent(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, RecentAlbum2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toBuyPoint(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, BuyPoint2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }


    public static void toAppSetting(Activity currentActivity) {
        currentActivity.startActivity(new Intent(currentActivity, AppSettings2Activity.class));
        ActivityAnim.StartAnim(currentActivity);
    }


}
