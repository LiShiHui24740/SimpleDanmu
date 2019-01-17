package com.airland.simpledanmuku.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.airland.simpledanmuku.judge.DefautJudgeManager;
import com.airland.simpledanmuku.message.AbstractMessage;
import com.airland.simpledanmuku.message.ISimpleMessageAdapter;
import com.airland.simpledanmuku.message.IMessageDeal;
import com.airland.simpledanmuku.message.MessageTask;


public abstract class SimpleBaseDanmuView extends FrameLayout implements ISimpleDanmuView {
    public static final int DEFAULT_ROWCOUNT = 1;
    public static final int DEFAULT_ROW_DISTANCE = 5;
    public static final int DEFAULT_HEIGHT = 40;
    public static final int DEFAULT_ITEM_DISTANCE = 8;
    protected int itemDistance = DEFAULT_ITEM_DISTANCE;
    protected int rowCount = DEFAULT_ROWCOUNT;
    protected int currentState = RUNNING;
    protected int rowDistance = DEFAULT_ROW_DISTANCE;
    protected boolean isEnableOverLayer;//弹幕是否可以重叠
    protected int everyRowHeight = DEFAULT_HEIGHT;
    protected ISimpleBaseViewRank<SimpleItemBaseView> simpleBaseViewRankImp;
    protected IMessageDeal iMessageDeal;
    protected ISimpleMessageAdapter iSimpleMessageAdapter;
    private boolean isCalled;

    public SimpleBaseDanmuView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleBaseDanmuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleBaseDanmuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int dp2px(int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics()) + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        if (View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.EXACTLY) {
            int width_size = getContext().getResources().getDisplayMetrics().widthPixels;
            width = View.MeasureSpec.makeMeasureSpec(width_size, View.MeasureSpec.EXACTLY);
        }
        int height_size;
        if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.EXACTLY) {
            height_size = dp2px(everyRowHeight) * rowCount + (rowCount - 1) * dp2px(rowDistance);
        } else {
            height_size = Math.max(MeasureSpec.getSize(heightMeasureSpec), dp2px(everyRowHeight) * rowCount + (rowCount - 1) * dp2px(rowDistance));
        }
        int height = View.MeasureSpec.makeMeasureSpec(height_size, View.MeasureSpec.EXACTLY);
        super.onMeasure(width, height);
    }

    public <T extends AbstractMessage> void setMessageAdapter(ISimpleMessageAdapter<T> iSimpleMessageAdapter) {
        if (this.iSimpleMessageAdapter == null) {
            this.iSimpleMessageAdapter = iSimpleMessageAdapter;
            rowCount = iSimpleMessageAdapter.getRowCount();
            if (rowCount>1)
            simpleBaseViewRankImp = new SimpleBaseViewRankImp(rowCount);
            iMessageDeal = new MessageTask<>(this, iSimpleMessageAdapter, !isEnableOverLayer ? new DefautJudgeManager(rowCount) : null);
        }
    }

    public <T extends AbstractMessage> ISimpleMessageAdapter<T> getMessageAdapter() {
        return iSimpleMessageAdapter;
    }

    public <T extends AbstractMessage> void addInMessageQueue(T message) {
        if (iMessageDeal != null) {
            if (simpleBaseViewRankImp != null) {
                message.setRowNumber(simpleBaseViewRankImp.caculateRow());
            } else {
                message.setRowNumber(1);
            }
            iMessageDeal.addInMessageQueue(message);
        }
    }

    @Override
    public void startItemDanmuView(SimpleItemBaseView simpleItemBaseView) {
        if (simpleItemBaseView != null && currentState == RUNNING && simpleBaseViewRankImp != null) {
            simpleBaseViewRankImp.insertRowItem(simpleItemBaseView);
        }
    }

    @Override
    public void endItemDanmuView(SimpleItemBaseView simpleItemBaseView) {
        if (simpleItemBaseView != null && simpleBaseViewRankImp != null) {
            if (simpleBaseViewRankImp.removeRowItem(simpleItemBaseView)) {
                if (!isCalled) {
                    if (iMessageDeal != null) {
                        iMessageDeal.setNextIndicator(simpleItemBaseView.rowNumber, true);
                    }
                }
            }
        }else{
            if (!isCalled) {
                if (iMessageDeal != null) {
                    iMessageDeal.setNextIndicator(simpleItemBaseView.rowNumber, true);
                }
            }
        }

    }

    @Override
    public int getState() {
        return currentState;
    }

    public void setEnableOverLayer(boolean enableOverLayer) {
        isEnableOverLayer = enableOverLayer;
    }

    protected void setNextIndicator(int row, boolean state) {
        if (iMessageDeal != null) {
            isCalled = true;
            iMessageDeal.setNextIndicator(row, state);
        }
    }

    public void endDealWithMessage() {
        if (iMessageDeal != null)
            iMessageDeal.endDealWithMessage();
    }

    public void resumeDealWithMessage() {
        if (iMessageDeal != null)
            iMessageDeal.resumeDealWithMessage();
    }

    public void setSimpleBaseViewRankImp(ISimpleBaseViewRank<SimpleItemBaseView> simpleBaseViewRankImp) {
        this.simpleBaseViewRankImp = simpleBaseViewRankImp;
    }
}
