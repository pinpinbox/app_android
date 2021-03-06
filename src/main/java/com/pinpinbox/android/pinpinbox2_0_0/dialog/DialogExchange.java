package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

/**
 * Created by kevin9594 on 2018/3/17.
 */

public class DialogExchange {

    private DismissExcute dismissExcute;

    public Activity mActivity;
    public Dialog mDialog;

    public DialogExchange(Activity activity){

        this.mActivity = activity;
        mDialog = new Dialog(activity, R.style.myDialog);

        Window window = mDialog.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }

        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_exchange);

        RelativeLayout rClose = (RelativeLayout)mDialog.findViewById(R.id.close);
        TextView tvClose = (TextView)mDialog.findViewById(R.id.tvClose);

        rClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不關閉
            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(dismissExcute!=null){
                    dismissExcute.AfterDismissDo();
                }
            }
        });

    }


    public void show(){
        if(mDialog!=null){
            mDialog.show();
        }
    }

    public void dismiss(){
        if(mDialog!=null){
            mDialog.dismiss();
        }
    }



    public void setDismissExcute(DismissExcute dismissExcute) {
        this.dismissExcute = dismissExcute;
    }


}
