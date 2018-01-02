package com.example.drop.wzjtestapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.example.drop.wzjtestapp.database.Helper;

/**
 * MultiDexApplication ：解决方法数超限问题
 * Created by liang on 2017/11/28.
 */
public class MyApplication extends MultiDexApplication {
    private static final String TAG = "MyApplication";
    private static MyApplication mApplication;
    private Helper mHelper;


    public static MyApplication getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(this);
//        initFresco();
        setDatabase();
//        initRongCloud();
//        initCloudChannel(this);
    }

    private void initRongCloud(){
        /**
         * 注意：
         *
         * IMKit SDK调用第一步 初始化
         *
         * context上下文
         *
         * 只有两个进程需要初始化，主进程和 push 进程

        if (getApplicationInfo().packageName.equals(CommonUtils.getCurProcessName(getApplicationContext()))
                || "io.rong.push".equals(CommonUtils.getCurProcessName(getApplicationContext()))) {
            try {
                RongPushClient.registerMiPush(getApplicationContext(), "2882303761517494747", "5661749461747");
                RongPushClient.registerHWPush(getApplicationContext());
                RongIM.init(this);
                /**
                 * 融云SDK事件监听处理
                 *
                 * 注册相关代码，只需要在主进程里做。
                 *
                if (getApplicationInfo().packageName
                        .equals(CommonUtils.getCurProcessName(getApplicationContext()))) {
                    RongCloudEvent.init(this);
                }
                RongPushClient.checkManifest(getApplicationContext());
            } catch (RongException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        */
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        //mHelper = new Helper(new GreenDaoUtils(this));
        mHelper = new Helper(this);
    }
/*
    private void initFresco() {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this).
                setMaxCacheSize(100 * ByteConstants.MB).build();

        Supplier<MemoryCacheParams> bitmapCacheParamsSupplier = new Supplier<MemoryCacheParams>() {

            @Override
            public MemoryCacheParams get() {
                MemoryCacheParams param = new MemoryCacheParams(
                        20*ByteConstants.MB
                        , Integer.MAX_VALUE
                        ,20*ByteConstants.MB
                        , Integer.MAX_VALUE
                        , Integer.MAX_VALUE);
                return param;
            }
        };
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(this,config);
    }
     */


    /**
     * 初始化云推送通道
     * @param applicationContext

    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i(TAG, "init cloudchannel success");
                String deviceId = pushService.getDeviceId();
                CommonPreference.setStringValue(CommonPreference.ALI_PUSH_TOKEN, deviceId);
                LogUtil.e("deviceId>>>>>>>>>",deviceId);
//				startRequestForUploadPushDevice(deviceId);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtil.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        // 初始化小米通道，自动判断是否支持小米系统推送，如不支持会跳过注册
        //MiPushRegister.register(applicationContext, "小米AppID", "小米AppKey");
        // 初始化华为通道，自动判断是否支持华为系统推送，如不支持会跳过注册
        //HuaWeiRegister.register(applicationContext);
    }
     */
}
