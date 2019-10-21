package com.airland.simpledanmuku.widget.base;


public interface ISimpleDanmuView {
    int RUNNING = 2;
    int STOP = 3;
    void startItemDanmuView(final SimpleItemBaseView simpleItemBaseView);
    void endItemDanmuView(final SimpleItemBaseView simpleItemBaseView);
    int getState();
}
