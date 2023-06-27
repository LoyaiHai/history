package com.lhs.godhasnowind;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lhs.toollibrary.utils.DebugLogJava;
import com.lhs.toollibrary.utils.HS_FileManager;
import com.lhs.toollibrary.utils.HS_FileUtils;
import com.lhs.toollibrary.utils.ThreadPoolManager;

import java.io.File;

public class TaskLogActivity extends AppCompatActivity {

    private Button btn_init_task;
    private Button btn_add_task;
    private Button btn_close_task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_log);

        btn_init_task = findViewById(R.id.btn_init_task);
        btn_add_task = findViewById(R.id.btn_add_task);
        btn_close_task = findViewById(R.id.btn_close_task);

        initListener();
    }

    private int curentValue;

    private void initListener() {
        btn_init_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadPoolManager.getInstance().initTask();

                String filesDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Aebell" + File.separator + "log";

                boolean fileDirState = HS_FileUtils.getInstance().createFileDir(filesDir);
                DebugLogJava.e("文件目录是否存在: " + fileDirState);
                if (fileDirState) {
                    boolean fileStatue = HS_FileUtils.getInstance().createFile(filesDir, "TaskLog.txt");
                    DebugLogJava.e("文件是否存在：" + fileStatue);

                }
            }
        });

        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadPoolManager.getInstance().addTask(new Runnable() {
                    @Override
                    public void run() {
                        curentValue++;

                        DebugLogJava.e("这里的添加任务为：" + curentValue);

                        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + "Aebell" + File.separator + "log" + File.separator + "TaskLog.txt";

                        String content = "这里的添加任务为：" + curentValue;

                        boolean write = HS_FileManager.getInstance().write(filePath, content, true);

                        DebugLogJava.e("这里是写文件内容是否成功的结果：" + write);
                    }
                });

            }
        });

        btn_close_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadPoolManager.getInstance().setTaskStatue(false);
                ThreadPoolManager.getInstance().removTask();
            }
        });
    }
}
