package com.airland.simpledanmuku.message;


import com.airland.simpledanmuku.widget.SimpleItemBaseView;

public interface ISimpleMessageAdapter<T extends AbstractMessage>{
    int getRowCount();
    SimpleItemBaseView getView(T t);
}
