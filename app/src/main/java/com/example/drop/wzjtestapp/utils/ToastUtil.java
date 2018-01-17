package com.example.drop.wzjtestapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.drop.wzjtestapp.MyApplication;

/**
 * Created by drop on 2018/1/17.
 */

public class ToastUtil {
    public static Toast toast;

    //连续型Toast
    public static void showToast(Context context, String str){
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        }else {
            toast.setText(str);
        }
        toast.show();
    }

    public static void showToast(String str){
        showToast(MyApplication.getApplication(),str);
    }

    public static void showToast(String str, int duration){
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getApplication(), str, duration);
        }else {
            toast.setText(str);
        }
        toast.show();
    }

}
