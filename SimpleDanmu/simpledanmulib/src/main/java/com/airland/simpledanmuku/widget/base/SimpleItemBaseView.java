package com.airland.simpledanmuku.widget.base;

import android.content.Context;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;


public abstract class SimpleItemBaseView extends RelativeLayout {

    public int rowNumber = 1;

    public SimpleItemBaseView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            width = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.UNSPECIFIED);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(width, height);
    }

    protected int dp2px(Context context, int dp) {
        return (int) (TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

}
