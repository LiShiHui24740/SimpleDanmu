package com.airland.simpledanmuku.judge;


public interface IStateJudge {
    void resetState(boolean initState);

    boolean getIndicator(int row);

    void setIndicator(int row, boolean state);
}
