package com.lhs.toollibrary.demo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class HandlerDemo {
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:


                    break;

                case 1:


                    break;

                default:
                    break;
            }
        }
    };
}
