package com.lhs.toollibrary.demo;

/**
 * 单例demo
 * **/
public class SingletonDemo {
    public SingletonDemo() {

    }

    private static class SingletonHoler{
        private static SingletonDemo INSTANCE = new SingletonDemo();
    }

    public static SingletonDemo getInstance(){
        return SingletonHoler.INSTANCE;
    }
}
