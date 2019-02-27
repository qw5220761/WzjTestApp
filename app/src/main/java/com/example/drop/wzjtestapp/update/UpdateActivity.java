package com.example.drop.wzjtestapp.update;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.drop.wzjtestapp.R;

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
public class UpdateActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
    }
    public void update(View view){
        Intent updateIntent = new Intent(this, UpdateIntentService.class);
        updateIntent.setAction(UpdateIntentService.ACTION_UPDATE);
        updateIntent.putExtra("appName", "update-1.0.1");
        //随便一个apk的url进行模拟
        updateIntent.putExtra("downUrl", "http://gdown.baidu.com/data/wisegame/38cbb321c273886e/YY_30086.apk");
        startService(updateIntent);
    }
}
