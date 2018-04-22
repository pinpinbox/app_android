package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.app.Activity;
import android.app.Dialog;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;

/**
 * Created by vmage on 2016/5/20.
 */
public class NoConnect {

    public Dialog dlgNoConnect;

    public NoConnect(final Activity mActivity){


        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.NOCONNECT);
        d.show();
        dlgNoConnect = d.getmDialog();



    }

    public Dialog getDlgNoConnect(){
        return this.dlgNoConnect;
    }


}
