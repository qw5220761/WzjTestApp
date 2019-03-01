package com.example.drop.wzjtestapp.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.drop.wzjtestapp.MyApplication;
import com.example.drop.wzjtestapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class ReadAndWriteTextUtil {
    File writeFile;
    public static String readLine(Context context,String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
        } catch (Exception e1) {
            if(e1 instanceof FileNotFoundException){
                ToastUtil.showToast("未找到文件");
            }
            e1.printStackTrace();
        }
        if(inputStream==null){
            return "读取失败InputStream为空";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getFilePath(String filrDir) {
        String file_dir = "";
        // SD卡是否存在
        boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        // Environment.getExternalStorageDirectory()相当于File file=new File("/sdcard")
        boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
        if (isSDCardExist && isRootDirExist) {
            file_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+filrDir+"/";
        } else {
            // MyApplication.getInstance().getFilesDir()返回的路劲为/data/data/PACKAGE_NAME/files，其中的包就是我们建立的主Activity所在的包
            file_dir = MyApplication.getApplication().getFilesDir().getAbsolutePath() + "/"+filrDir+"/";
        }
        return file_dir;
    }
    public void write(String[] args, Context context)  {
        if(writeFile==null||!writeFile.exists()){
            savePackageFile();
        }
        try {
            // 写数据
            RandomAccessFile raf = new RandomAccessFile(writeFile, "rwd");
            raf.seek(writeFile.length());
            for (int x = 0; x < args.length; x++) {
                raf.write((args[x] ).getBytes());
                raf.write("\r\n".getBytes());// 写入一个换行
            }
            // 释放资源
            raf.close();
            LogUtil.e("文件写入成功");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void savePackageFile() {

        try {
            writeFile = new File(getFilePath("txt")  );
            if (!writeFile.exists()) {
                writeFile.mkdir();
            }
            File file = new File(getFilePath("txt")  , "myTestWrite.txt");
            file.createNewFile();
            Log.e("","文件创建成功");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
