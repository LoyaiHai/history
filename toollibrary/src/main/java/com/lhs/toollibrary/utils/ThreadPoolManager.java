package com.lhs.toollibrary.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolManager {

    private static ThreadPoolManager mInstance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return mInstance;
    }

    /**
     * 核心线程池的数量，同时能够执行的线程数量
     */
    private int corePoolSize;
    /**
     * 最大线程池数量，表示当缓冲队列满的时候能继续容纳的等待任务的数量
     */
    private int maximumPoolSize;
    /**
     * 存活时间
     */
    private long keepAliveTime = 1;
    private TimeUnit unit = TimeUnit.HOURS;
    private ThreadPoolExecutor executor;

    private boolean callerState;

    /** 创建队列，用来保存异步请求任务 **/
    private LinkedBlockingDeque<Runnable> mQueue = new LinkedBlockingDeque<>();
    /** 添加异步任务到队列中 **/
    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.push(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** 创建队列线程池的"交互"线程 **/
    public Runnable comunicateThread = new Runnable() {
        @Override
        public void run() {
            Runnable runnable = null;
            callerState = true;
            while (callerState) {
                DebugLogJava.e("创建队列线程池的交互线程--->111");
                try {
                    runnable = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DebugLogJava.e("创建队列线程池的交互线程--->222");
                if (runnable != null) {
                    executor.execute(runnable);
                }
                DebugLogJava.e("创建队列线程池的交互线程--->333");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            DebugLogJava.e("这里是释放这个分配员的");
        }
    };

    public void initTask() {
        DebugLogJava.e("看下这里初始化任务: " + callerState);
        if (!callerState) {
            executor.execute(comunicateThread);
        }
    }

    public void setTaskStatue(boolean dutyState) {
        DebugLogJava.e("设置任务状态：" + dutyState);
        callerState = dutyState;
    }

    public void removTask() {
        DebugLogJava.e("移除任务的叫号员");
        if (executor != null) {
            executor.remove(comunicateThread);
        }
    }

    private ThreadPoolManager() {
        /**
         * 给corePoolSize赋值：当前设备可用处理器核心数*2 + 1,能够让cpu的效率得到最大程度执行（有研究论证的）
         */
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        //虽然maximumPoolSize用不到，但是需要赋值，否则报错
        maximumPoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(
                //当某个核心任务执行完毕，会依次从缓冲队列中取出等待任务
                corePoolSize,
                //5,先corePoolSize,然后new LinkedBlockingQueue<Runnable>(),然后maximumPoolSize,但是它的数量是包含了corePoolSize的
                maximumPoolSize,
                //表示的是maximumPoolSize当中等待任务的存活时间
                keepAliveTime,
                unit,
                //缓冲队列，用于存放等待任务，Linked的先进先出
                new LinkedBlockingQueue<Runnable>(),
                //创建线程的工厂
                //  Executors.defaultThreadFactory(),
                new DefaultThreadFactory(Thread.NORM_PRIORITY, "tiaoba-pool-"),
                //用来对超出maximumPoolSize的任务的处理策略
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (executor == null) {
            corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
            //虽然maximumPoolSize用不到，但是需要赋值，否则报错
            maximumPoolSize = corePoolSize;
            //线程池执行者。
            //参1:核心线程数;参2:最大线程数;参3:线程休眠时间;参4:时间单位;参5:线程队列;参6:生产线程的工厂;参7:线程异常处理策略
            executor = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    //   Executors.defaultThreadFactory(),
                    new DefaultThreadFactory(Thread.NORM_PRIORITY, "tiaoba-pool-"),
                    new ThreadPoolExecutor.AbortPolicy());
        }
        if (runnable != null) {
            executor.execute(runnable);
        }
    }

    /**
     * 移除任务
     */
    public void remove(Runnable runnable) {
        if (runnable != null) {
            executor.remove(runnable);
        }
    }

    /**
     * 创建线程的工厂，设置线程的优先级，group，以及命名
     */
    private static class DefaultThreadFactory implements ThreadFactory {
        /**
         * 线程池的计数
         */
        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        /**
         * 线程的计数
         */
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final ThreadGroup group;
        private final String namePrefix;
        private final int threadPriority;

        DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
            this.threadPriority = threadPriority;
            this.group = Thread.currentThread().getThreadGroup();
            namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(threadPriority);
            return t;
        }
    }

    /**关闭线程池**/
    public void closeThreadPool() {
        executor.shutdownNow();
    }
}
