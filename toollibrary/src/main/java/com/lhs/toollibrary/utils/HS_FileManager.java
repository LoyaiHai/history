package com.lhs.toollibrary.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HS_FileManager {

    /**
     * 写文件
     * **/
    public boolean write(String filePath, String content, boolean append) {
        if (HS_StringUtils.isBlank(filePath)) {
            return false;
        }

        if (HS_StringUtils.isBlank(content)) {
            return false;
        }

        File writeFile = new File(filePath);
        if (!writeFile.exists()) {
            return false;
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(writeFile, append);
            long timeMillis = System.currentTimeMillis();
            String stampToString = HS_TimeUtils.getStampToString(timeMillis, "yyyy-MM-dd HH:mm:ss");
            stampToString = stampToString + ": ";
            fileOutputStream.write(stampToString.getBytes());
            fileOutputStream.write(content.getBytes());
            fileOutputStream.write("\n".getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }
    }












    public HS_FileManager() {
    }

    private static class FileManagerHoler{
        private static HS_FileManager INSTANCE = new HS_FileManager();
    }

    public static HS_FileManager getInstance(){
        return HS_FileManager.FileManagerHoler.INSTANCE;
    }
}
