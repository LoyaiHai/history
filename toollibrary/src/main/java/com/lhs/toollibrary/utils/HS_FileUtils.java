package com.lhs.toollibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类(单例)
 * **/
public class HS_FileUtils {

    public boolean isExistFile(String filePath) {
        if (HS_StringUtils.isBlank(filePath)) {
            return false;
        }

        File existFile = new File(filePath);
        if (existFile.isFile()) {
            return existFile.exists();
        }

        return false;
    }

    public boolean isExistFileDir(String filePath) {
        if (HS_StringUtils.isBlank(filePath)) {
            return false;
        }

        File existFile = new File(filePath);
        if (existFile.isDirectory()) {
            return existFile.exists();
        }

        return false;
    }

    public boolean createFile(String filePath, String fileName) {
        try {
            if (HS_StringUtils.isBlank(filePath)) {
                return false;
            }
            File createFile = new File(filePath, fileName);
            if (!createFile.exists()) {
                return createFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean createFileDir(String fileDirPath) {
        if (HS_StringUtils.isBlank(fileDirPath)) {
            return false;
        }

        File createFileDir = new File(fileDirPath);
        if (!createFileDir.exists()) {
            return createFileDir.mkdirs();
        }

        return true;
    }

    /**
     * 拷贝文件到指定目录
     * @param path       文件路径
     * @param folderPath 目录路径
     * @return int 返回状态：0:成功 -1:文件路径为空 -2:目录路径为空
     *                    -3:文件路径不存在
     */
    public int copyFileToFolder(String path, String folderPath) {
        if (HS_StringUtils.isBlank(path)) {
            return -1;
        }

        if (HS_StringUtils.isBlank(folderPath)) {
            return -2;
        }

        return copyFileToFolder(new File(path), folderPath);
    }

    /**
     * 拷贝文件到指定目录
     * @param file       文件
     * @param folderPath 目录路径
     * @return int 返回状态：0:成功 -1:文件路径为空 -2:目录路径为空
     *                    -3:文件路径不存在 -4:源文件File为null
     *                    -5:拷贝的文件在指定目录创建不成功 -6:拷贝文件发生Exception失败
     */
    public int copyFileToFolder(File file, String folderPath) {
        if (HS_StringUtils.isBlank(folderPath)) {
            return -2;
        }

        if (file == null) {
            return -4;
        }

        if (!file.exists()) {
            return -3;
        }

        try {
            FileInputStream inputStream = new FileInputStream(file);
            File createFile = new File(folderPath, file.getName());
            if (!createFile.exists()) {
                boolean createFileNewFile = createFile.createNewFile();
                if (!createFileNewFile) {
                    return -5;
                }
            }
            FileOutputStream outputStream = new FileOutputStream(createFile);
            byte[] data = new byte[1024];
            int length;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            DebugLogJava.e("拷贝文件失败：" + e.getMessage());
        }
        return -6;
    }

    /**
     * 删除文件
     * @param path       文件路径
     * @return int 返回状态：0:成功 -1:文件路径为空 -2:文件不存在
     *                    -3:
     */
    public int deleteFile(String path) {
        if (HS_StringUtils.isBlank(path)) {
            return -1;
        }
        return deleteFile(new File(path), false);
    }

    /**
     * 删除文件
     * @param file       文件路径
     * @return int 返回状态：0:成功 -1:文件为空 -2:文件不存在
     *                    -3:删除文件失败
     */
    public int deleteFile(File file) {
        if (file == null) {
            return -1;
        }
        if (!file.exists()) {
            return -2;
        }
        return deleteFile(file, false);
    }

    public int deleteFile(String path, boolean isDeleteFolder) {
        return deleteFile(new File(path), isDeleteFolder);
    }

    public int deleteFile(File file, boolean isDeleteFolder) {
        if (!file.exists()) {
            return -2;
        }
        if (file.isDirectory()) {
            // 是目录
            // TODO 要判断目录下是否还会有文件
            File[] files = file.listFiles();
            int isContinue = 0;
            for (File itemFile : files) {
                isContinue = deleteFile(itemFile, isDeleteFolder);
                if (isContinue != 0) {
                    isContinue = -3;
                    break;
                }
            }
            if (isContinue != 0) {
                return -3;
            }

            if (isDeleteFolder) {
                // 是文件
                if (file.delete()) {
                    return 0;
                } else {
                    return -3;
                }
            }
        } else {
            // 是文件
            if (file.delete()) {
                return 0;
            } else {
                return -3;
            }
        }

        return 0;
    }








    public HS_FileUtils() {
    }
    private static class HS_FileUtilsHoler {
        private static HS_FileUtils INSTANCE = new HS_FileUtils();
    }
    public static HS_FileUtils getInstance(){
        return HS_FileUtils.HS_FileUtilsHoler.INSTANCE;
    }

}
