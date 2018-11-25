package com.airland.simpledanmuku.widget;

public interface ISimpleBaseViewRank<T> {
    void insertRowItem(T t);
    boolean removeRowItem(T t);
    int caculateRow();
}
