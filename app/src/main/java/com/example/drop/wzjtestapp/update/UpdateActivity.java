package com.example.drop.wzjtestapp.update;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.utils.LogUtil;
import com.example.drop.wzjtestapp.utils.ReadAndWriteTextUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @ClassName: UpdateActivity
 * @Description: 软件更新  https://www.jianshu.com/p/32afaa19fe56
 * @Author: wzj
 * @CreateDate: 2019/2/24 16:18
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UpdateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);



        new RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) {
                if (permission.granted) {
                    //必须要在请求写权限的后面
                } else {
                }
            }

        });
    }
    public void update(View view){
        List<String> stringList = getFilesAllName(ReadAndWriteTextUtil.getFilePath("a_yongsha"));
        if(stringList!=null){
            for(String fileName:stringList){
                LogUtil.showLog(fileName);
                checkInstall(fileName,1);
            }
        }else {
            LogUtil.showLog("没有文件！");
        }
//        Intent updateIntent = new Intent(this, UpdateIntentService.class);
//        updateIntent.setAction(UpdateIntentService.ACTION_UPDATE);
//        updateIntent.putExtra("appName", "1-用啥");
//        //随便一个apk的url进行模拟
//        updateIntent.putExtra("downUrl", "http://gdown.baidu.com/data/wisegame/38cbb321c273886e/YY_30086.apk");
//        startService(updateIntent);
    }

    private void checkInstall(String s,int newVersion){
        String[] ss = s.split("-");
        if(ss.length==2){
            try{
                int apkVersion = Integer.parseInt(ss[1]);
                if(apkVersion >= newVersion){
                    //提示安装
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //剩下的都删除
        //删除文件
        File f = new File(s);
        boolean b = f.delete();
        LogUtil.showLog("删除文件："+b);
    }

    public static List<String> getFilesAllName(String path) {

        File file=new File(path);

        File[] files=file.listFiles();

        if (files == null){
            Log.e("error","空目录");return null;}

        List<String> s = new ArrayList<>();

        for(int i =0;i<files.length;i++){

            s.add(files[i].getAbsolutePath());

        }

        return s;

    }

}
