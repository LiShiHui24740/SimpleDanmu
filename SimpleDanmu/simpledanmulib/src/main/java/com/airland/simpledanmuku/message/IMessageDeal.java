package com.airland.simpledanmuku.message;



public interface IMessageDeal<T extends AbstractMessage> {
    void addInMessageQueue(T message);

    void endDealWithMessage();

    void clearMessageQueue();

    void resumeDealWithMessage();

    void setNextIndicator(int row, boolean state);
}
