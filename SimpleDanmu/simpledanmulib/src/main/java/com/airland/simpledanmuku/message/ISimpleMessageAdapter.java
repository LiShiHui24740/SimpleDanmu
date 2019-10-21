package com.airland.simpledanmuku.message;


import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;

public interface ISimpleMessageAdapter<T extends AbstractMessage>{
    SimpleItemBaseView getView(T t);
}
