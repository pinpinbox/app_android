package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

/**
 * Created by vmage on 2016/7/12.
 */
public class DialogHandselPoint{

    public Activity mActivity;
    public Dialog mDialog;

    private RelativeLayout rClose;
    private TextView tvTitle, tvRestriction, tvDescription, tvLink;
    private ImageView img;


    public DialogHandselPoint(Activity activity) {
        this.mActivity = activity;
        mDialog = new Dialog(activity, R.style.myDialog);

        Window window = mDialog.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }



        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_handsel_point);


        rClose = mDialog.findViewById(R.id.close);
        rClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        tvTitle = mDialog.findViewById(R.id.tvTitle);
        tvDescription = mDialog.findViewById(R.id.tvDescription);
        tvRestriction =mDialog.findViewById(R.id.tvRestriction);
        tvLink = mDialog.findViewById(R.id.tvLink);
        img =mDialog.findViewById(R.id.img);

        TextUtility.setBold(tvTitle, true);

        mDialog.show();
    }


    public TextView getTvTitle(){
        return this.tvTitle;
    }

    public TextView getTvRestriction(){
        return this.tvRestriction;
    }

    public TextView getTvDescription(){
        return this.tvDescription;
    }

    public TextView getTvLink(){
        return this.tvLink;
    }

    public ImageView getImg(){
        return this.img;
    }

    public Dialog getDialog(){
        return this.mDialog;
    }


}
