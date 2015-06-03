package com.camera.demol.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();
    private static final int BUFFER_SIZE = 8 * 1024;

    public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String CAMERA360_ROOT = SDCARD_ROOT + "/Camera360";

    private FileUtil() {

    }

    /**
     * 将文件转换为byte[] 数据.
     *
     * @param filePath 文件路径
     * @return byte[] 文件数据
     * @throws java.io.IOException
     * @author zengchuanmeng
     */
    public static byte[] getFileData(String filePath) throws IOException {
        return getFileData(new File(filePath));
    }

    /**
     * 将文件转换为byte[] 数据.
     *
     * @param file 文件
     * @return byte[] 文件数据
     * @throws java.io.IOException
     * @author zengchuanmeng
     */
    public static byte[] getFileData(File file) throws IOException {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            return getStreamData(in);
        } finally {
            close(in);
        }
    }

    /**
     * 文件夹检查，不存在则新建
     *
     * @param folderPath 文件夹检查，不存在则新建
     * @return true，存在或新建成功，false，不存在或新建失败
     * @author liubo
     */
    public static boolean checkFolder(String folderPath) {
        if (folderPath == null) {
            return false;
        }
        return checkFolder(new File(folderPath));
    }

    /**
     * 文件夹检查，不存在则新建
     *
     * @param folder 文件夹检查，不存在则新建
     * @return true，存在或新建成功，false，不存在或新建失败
     * @author liubo
     */
    public static boolean checkFolder(File folder) {
        if (folder == null) {
            return false;
        }

        if (folder.isDirectory()) {
            return true;
        }

        return folder.mkdirs();
    }


    public static void close(InputStream in) throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }
    }

    public static void close(OutputStream out) throws IOException {
        if (out != null) {
            out.close();
            out = null;
        }
    }

    public static byte[] getStreamData(InputStream in) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return out.toByteArray();
        } finally {
            close(out);
        }
    }

    /**
     * 保存Bitmap
     *
     * @param
     * @return boolean
     * @author litao
     */
    public static boolean saveBitmap(String path, Bitmap bitmap, int quality) throws IOException, IllegalArgumentException {
        if (TextUtils.isEmpty(path) || bitmap == null) {
            return false;
        }

        boolean flag = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();

                flag = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(out);
        }

        return flag;
    }

    /**
     * 保存Bitmap
     *
     * @param
     * @return boolean
     * @author litao
     */
    public static boolean saveBitmap(String path, Bitmap bitmap, int quality, Bitmap.CompressFormat format) throws IOException, IllegalArgumentException {
        if (TextUtils.isEmpty(path) || bitmap == null) {
            return false;
        }

        boolean flag = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            if (bitmap.compress(format, quality, out)) {
                out.flush();

                flag = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(out);
        }

        return flag;
    }

    /**
     * 保存文件
     *
     * @param data 二进制数据文件
     * @param path 路径
     * @throws java.io.IOException IOException
     * @author liubo
     */
    public static void saveFile(byte[] data, String path) throws IOException {
        if (data == null) {
            throw new IOException("data is null");
        }

        if (path == null) {
            throw new IOException("path is null");
        }

        File parent = new File(path).getParentFile();
        if (!checkFolder(parent)) {
            throw new IOException("Create Folder(" + parent.getAbsolutePath() + ") Failed!");
        }

        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(path));
            out.write(data);
        } finally {
            close(out);
        }
    }

    /**
     * Copy from one stream to another.  Throws IOException in the event of error
     * (for example, SD card is full)
     *
     * @param is Input stream.
     * @param os Output stream.
     */
    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        copyStream(is, os, buffer, BUFFER_SIZE);
    }

    /**
     * Copy from one stream to another.  Throws IOException in the event of error
     * (for example, SD card is full)
     *
     * @param is         Input stream.
     * @param os         Output stream.
     * @param buffer     Temporary buffer to use for copy.
     * @param bufferSize Size of temporary buffer, in bytes.
     */
    public static void copyStream(InputStream is, OutputStream os,
                                  byte[] buffer, int bufferSize) throws IOException {
        try {
            for (; ; ) {
                int count = is.read(buffer, 0, bufferSize);
                if (count == -1) {
                    break;
                }
                os.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw e;
        }
    }

}
