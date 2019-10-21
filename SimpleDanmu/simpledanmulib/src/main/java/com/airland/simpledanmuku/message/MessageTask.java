package com.airland.simpledanmuku.message;

import com.airland.simpledanmuku.judge.IStateJudge;
import com.airland.simpledanmuku.widget.base.ISimpleDanmuView;
import com.airland.simpledanmuku.widget.base.SimpleItemBaseView;



public final class MessageTask<T extends AbstractMessage> implements IMessageDeal<T> {
    private ISimpleDanmuView iSimpleDanmuView;
    private ISimpleMessageAdapter iSimpleMessageAdapter;
    private MessageManager<T> messageManager;
    private MessageManager.IMessageDispatchMessage<T> iMessageDispatchMessage = new MessageManager.IMessageDispatchMessage<T>() {
        @Override
        public void dispatchMessage(T message) {
            if (iSimpleMessageAdapter != null && iSimpleDanmuView.getState() == ISimpleDanmuView.RUNNING) {
                SimpleItemBaseView simpleItemBaseView = iSimpleMessageAdapter.getView(message);
                simpleItemBaseView.setRowNumber(message.mRowNumber);
                iSimpleDanmuView.startItemDanmuView(simpleItemBaseView);
            }
        }
    };

    public MessageTask(ISimpleDanmuView simpleDanmuView, ISimpleMessageAdapter<T> iSimpleMessageAdapter, IStateJudge iStateJudge) {
        this.iSimpleDanmuView = simpleDanmuView;
        messageManager = new MessageManager<>(iMessageDispatchMessage, iStateJudge);
        this.iSimpleMessageAdapter = iSimpleMessageAdapter;
    }

    @Override
    public void addInMessageQueue(T message) {
        messageManager.addMessage(message);
    }

    @Override
    public void endDealWithMessage() {
        messageManager.stopDealWithMessage();
    }

    @Override
    public void clearMessageQueue() {
        messageManager.clearQueue();
    }

    @Override
    public void setNextIndicator(int row, boolean state) {
        messageManager.setIndicator(row, state);
    }

    @Override
    public void resumeDealWithMessage() {
        messageManager.init(iMessageDispatchMessage);
    }

}
