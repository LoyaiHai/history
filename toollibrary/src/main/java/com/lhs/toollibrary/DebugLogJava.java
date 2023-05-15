package com.lhs.toollibrary;

import android.util.Log;

/**
 * 日志打印类
 *
 * @author liuhaisheng
 * @Date 202110091030
 **/
public class DebugLogJava {
    private final static String NIIS = "023===>";

    private static void buildLog(String tag, String message, int methodCount, IDebugLogListener logListener) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
//        int methodCount = 1;
        int stackOffset = getStackOffset(trace);

        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > methodCount - 1; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StackTraceElement element = trace[stackIndex];

            StringBuilder builder = new StringBuilder();
            builder.append(getSimpleClassName(element.getClassName()))
                    .append(".")
                    .append(element.getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(element.getFileName())
                    .append(":")
                    .append(element.getLineNumber())
                    .append(")")
                    .append(" " + NIIS + " ")
                    .append(message);
            if (tag.equals("none-tag")) {
                logListener.logCallback(getSimpleClassName(element.getClassName()), builder.toString());
            } else {
                logListener.logCallback(tag, builder.toString());
            }
        }
    }


    public static void d(String msg) {
        String tag = "none-tag";
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.d(tag, message);
                executeLog(tag, message);
            }
        });
    }

    public static void d(String tag, String msg) {
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.d(tag, message);
                executeLog(tag, message);
            }
        });
    }

    public static void d(String tag, String msg, int methodCount) {
        buildLog(tag, msg, methodCount, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.d(tag, message);
                executeLog(tag, message);
            }
        });
    }

    public static void i(String msg) {
        String tag = "none-tag";
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.i(tag, message);
                executeLog(tag, message);
            }
        });
    }

    public static void i(String tag, String msg) {
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.i(tag, message);
                executeLog(tag, message);
            }
        });
    }
    public static void i(String tag, String msg,int methodCount) {
        buildLog(tag, msg, methodCount, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.i(tag, message);
                executeLog(tag, message);
            }
        });
    }

    public static void w(String msg) {
        String tag = "none-tag";
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.w(tag, message);
                executeLog(tag, message);
            }
        });
    }

    public static void w(String tag, String msg) {
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.w(tag, message);
                executeLog(tag, message);
            }
        });
    }

    public static void e(String msg) {
        String tag = "none-tag";
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.e(tag, message);
//                executeLog(tag, message);
            }
        });
    }

    public static void e(String tag, String msg) {
        buildLog(tag, msg, 1, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.e(tag, message);
//                executeLog(tag, message);
            }
        });
    }
    public static void e(String tag, String msg,int methodCount) {
        buildLog(tag, msg, methodCount, new IDebugLogListener() {
            @Override
            public void logCallback(String tag, String message) {
                Log.e(tag, message);
                executeLog(tag, message);
            }
        });
    }

    private static int getStackOffset(StackTraceElement[] trace) {
        for (int i = 2; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(DebugLogJava.class.getName())) {
                return --i;
            }
        }
        return -1;
    }

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private static void executeLog(String tag, String message) {
        try {
            for (IDebugLogImpl debugLogCallback : DebugLogImplQueue.linkedList) {
                debugLogCallback.debugLogCallback(tag, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
