package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ChangeTypeListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2017/3/18.
 */
public class RecyclerUserAdapter extends RecyclerView.Adapter  {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private ChangeTypeListener changeTypeListener;

    public void setChangeTypeListener(ChangeTypeListener changeTypeListener) {
        this.changeTypeListener = changeTypeListener;

    }


    private Activity mActivity;

    private List<ItemUser> itemUserList;


    public RecyclerUserAdapter(Activity activity, List<ItemUser> itemUserList) {
        this.mActivity = activity;
        this.itemUserList = itemUserList;
    }


    @Override
    public int getItemCount() {
        return itemUserList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_user, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.tvName.setText(itemUserList.get(position).getName());

        String url  = itemUserList.get(position).getPicture();

        if(url!=null&& !url.equals("")) {
            Picasso.get()
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.userImg);
        }else {
            holder.userImg.setImageResource(R.drawable.member_back_head);
        }

        boolean follow = itemUserList.get(position).isFollow();

        if(follow){
            holder.tvFollow.setText(R.string.pinpinbox_2_0_0_button_follow_cancel);
            holder.tvFollow.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
            holder.tvFollow.setBackgroundResource(R.drawable.click_2_0_0_second_grey_button_frame_white_radius);
        }else {
            holder.tvFollow.setText(R.string.pinpinbox_2_0_0_button_follow);
            holder.tvFollow.setTextColor(Color.parseColor(ColorClass.WHITE));
            holder.tvFollow.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);

        }


        holder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTypeListener.changeType(position);
            }
        });



    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private LinearLayout linBackground;
        private RoundedImageView userImg;
        private TextView tvName, tvFollow;


        public ViewHolder(View itemView) {
            super(itemView);


            linBackground = itemView.findViewById(R.id.linBackground);

            linBackground.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            userImg = itemView.findViewById(R.id.userImg);
            tvName = itemView.findViewById(R.id.tvName);
            tvFollow = itemView.findViewById(R.id.tvFollow);

            linBackground.setOnClickListener(this);
            linBackground.setOnLongClickListener(this);



        }


        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position, v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position, v);
            }
            return false;
        }

    }


    public void addData(int position, ItemUser itemUser) {
        itemUserList.add(position, itemUser);
        notifyItemInserted(position);
    }

    public void setData(int position, ItemUser itemUser) {
        itemUserList.set(position, itemUser);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        itemUserList.remove(position);
        notifyItemRemoved(position);
        if (position != itemUserList.size()) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, itemUserList.size() - position);
        }

    }


}
