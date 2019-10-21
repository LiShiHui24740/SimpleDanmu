package com.airland.simpledanmuku.widget.itemviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airland.simpledanmuku.R;
import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;


public class SimpleItemPictureView extends SimpleItemBaseView {

    private ImageView headPicture;
    private TextView name;
    private ImageView level;
    private TextView content;

    public SimpleItemPictureView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.shape_danmu_bg_default);
        setPadding(0, 0, dp2px(context, 16), 0);
        View rootView = LayoutInflater.from(context).inflate(R.layout.simple_picture_layout, this, true);
        headPicture = rootView.findViewById(R.id.id_civ_headpic);
        name = rootView.findViewById(R.id.id_tv_name);
        level = rootView.findViewById(R.id.id_iv_vip);
        content = rootView.findViewById(R.id.id_tv_content);
    }

    public ImageView getHeadPictureView() {
        return headPicture;
    }

    public TextView getNameView() {
        return name;
    }

    public ImageView getLevelView() {
        return level;
    }

    public TextView getContentView() {
        return content;
    }


}
