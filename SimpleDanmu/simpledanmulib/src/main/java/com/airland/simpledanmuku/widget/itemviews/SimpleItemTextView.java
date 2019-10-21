package com.airland.simpledanmuku.widget.itemviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airland.simpledanmuku.R;
import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;

/**
 * @author AirLand
 * @time on 2019-10-18 13:45
 * @email lish_air@163.com
 * @jianshu https://www.jianshu.com/u/816932948905
 * @gitHub https://github.com/LiShiHui24740
 * @describe:
 */
public class SimpleItemTextView extends SimpleItemBaseView {
    private TextView textView;
    public SimpleItemTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.simple_text_layout, this, true);
        textView = rootView.findViewById(R.id.text_test);
    }

    public TextView getTextView() {
        return textView;
    }
}
