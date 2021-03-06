package com.airland.simpledanmuku.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.airland.simpledanmuku.judge.IStateJudge;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class MessageManager<T extends AbstractMessage> {

    private Handler mHandler;
    private LinkedList<T> mQueue;
    private TakeMessageThread mTakeMessageThread;
    private IMessageDispatchMessage<T> mMessageDispatchMessage;
    private IStateJudge iStateJudge;
    private volatile boolean flag;
    static final int OFF_WORKING = 0x00;
    static final int WORKING = 0x01;
    private int currentState = OFF_WORKING;
    private ReentrantLock lock;
    private Condition notEmpty;

    MessageManager(IMessageDispatchMessage<T> iMessageDispatchMessage, IStateJudge iStateJudge) {
        lock = new ReentrantLock(false);
        notEmpty = lock.newCondition();
        this.iStateJudge = iStateJudge;
        init(iMessageDispatchMessage);
    }

    void init(IMessageDispatchMessage<T> iMessageDispatchMessage) {
        if (currentState == OFF_WORKING) {
            currentState = WORKING;
            flag = true;
            mMessageDispatchMessage = iMessageDispatchMessage;
            mQueue = new LinkedList<>();
            mTakeMessageThread = new TakeMessageThread();
            mTakeMessageThread.start();
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (mMessageDispatchMessage != null) {
                        mMessageDispatchMessage.dispatchMessage((T) msg.obj);
                    }

                }
            };
        }
    }

    void addMessage(T message) {
        if (flag) {
            ReentrantLock currentlock = lock;
            try {
                currentlock.lockInterruptibly();
                insertMessage(message);
                notEmpty.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (currentlock.isHeldByCurrentThread())
                    currentlock.unlock();
            }
        }
    }

    private void insertMessage(T message) {
        if (mQueue.size() > 0) {
            AbstractMessage max = mQueue.getFirst();
            AbstractMessage min = mQueue.getLast();
            if (message.mPriority > max.getPriority()) {
                mQueue.addFirst(message);
            } else {
                if (message.mPriority <= min.getPriority()) {
                    mQueue.addLast(message);
                } else {
                    int size = mQueue.size();
                    boolean isFind = false;
                    for (int i = size - 1; i > 0; i--) {
                        AbstractMessage abstractMessage = mQueue.get(i);
                        if (abstractMessage.mPriority >= message.mPriority) {
                            isFind = true;
                            mQueue.add(i + 1, message);
                            break;
                        }
                    }
                    if (!isFind) {
                        mQueue.add(message);
                    }
                }
            }
        } else {
            mQueue.addLast(message);
        }
    }


    void stopDealWithMessage() {
        if (currentState == WORKING) {
            currentState = OFF_WORKING;
            flag = false;
            if (mHandler != null)
                mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
            if (mTakeMessageThread != null)
                mTakeMessageThread.interrupt();
            mTakeMessageThread = null;
            mMessageDispatchMessage = null;
        }

    }

    void clearQueue() {
        ReentrantLock currentlock = lock;
        try {
            currentlock.lockInterruptibly();
            mQueue.clear();
            notEmpty.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (currentlock.isHeldByCurrentThread())
                currentlock.unlock();
        }
    }

    private class TakeMessageThread extends Thread {
        @Override
        public void run() {
            while (flag && !isInterrupted()) {
                ReentrantLock currentlock = lock;
                try {
                    currentlock.lockInterruptibly();
                    while (mQueue.size() == 0)
                        notEmpty.await();
                    T message = mQueue.getFirst();
                    if (iStateJudge == null || iStateJudge.getIndicator(message.mRowNumber)) {
                        if (mHandler != null) {
                            setIndicator(message.mRowNumber, false);
                            Message message1 = mHandler.obtainMessage();
                            message1.obj = message;
                            message1.sendToTarget();
                            mQueue.removeFirst();
                        }
                        if (currentlock.isHeldByCurrentThread())
                            currentlock.unlock();
                    } else {
                        if (currentlock.isHeldByCurrentThread())
                            currentlock.unlock();
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    if (currentlock.isHeldByCurrentThread())
                        currentlock.unlock();
                }
            }
        }
    }

    void setIndicator(int row, boolean state) {
        if (iStateJudge != null) {
            ReentrantLock currentlock = lock;
            try {
                currentlock.lockInterruptibly();
                iStateJudge.setIndicator(row, state);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (currentlock.isHeldByCurrentThread())
                    currentlock.unlock();
            }
        }

    }

    interface IMessageDispatchMessage<T> {
        void dispatchMessage(T message);
    }

}
