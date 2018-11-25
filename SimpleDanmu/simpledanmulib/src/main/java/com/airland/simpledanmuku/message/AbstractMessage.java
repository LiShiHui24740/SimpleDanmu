package com.airland.simpledanmuku.message;



public abstract class AbstractMessage{
    protected int mPriority;
    protected int mRowNumber;

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public int getRowNumber() {
        return mRowNumber;
    }

    public void setRowNumber(int mRowNumber) {
        this.mRowNumber = mRowNumber;
    }
}
