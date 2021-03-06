package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

/**
 * Created by vmage on 2016/12/20.
 */
public class DialogQRCode {

    public Activity mActivity;
    public Dialog mDialog;

    private RelativeLayout rClose;
    public ImageView bigQRcodeImg;
    public String string;


    public DialogQRCode(Activity activity, Bitmap bitmap){
        this.mActivity = activity;
        mDialog = new Dialog(activity, R.style.myDialog);

        Window window = mDialog.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }


        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_qrcode);

        rClose = (RelativeLayout)mDialog.findViewById(R.id.close);

        bigQRcodeImg = (ImageView)mDialog.findViewById(R.id.bigQRcodeImg);

        rClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        bigQRcodeImg.setImageBitmap(bitmap);

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
}
