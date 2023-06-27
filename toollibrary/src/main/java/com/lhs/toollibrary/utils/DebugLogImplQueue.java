package com.lhs.toollibrary.utils;


import java.util.concurrent.ArrayBlockingQueue;


public class DebugLogImplQueue {

    public static ArrayBlockingQueue<IDebugLogImpl> linkedList = new ArrayBlockingQueue<>(1000);

    public void register(IDebugLogImpl callback) {
        linkedList.add(callback);
    }

    public void unRegister(IDebugLogImpl callback) {
        linkedList.remove(callback);
    }

}
