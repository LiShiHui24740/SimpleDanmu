package com.airland.simpledanmuku.message;


public class AbstractMessage {
    protected int mPriority;
    protected int mRowNumber;
    protected int msgType;
    protected Object bean;

    public AbstractMessage() {
    }

    public AbstractMessage(Object bean) {
        this.bean = bean;
    }

    public AbstractMessage(int msgType, Object bean) {
        this.msgType = msgType;
        this.bean = bean;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

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

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
