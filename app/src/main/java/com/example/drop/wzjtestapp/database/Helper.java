package com.example.drop.wzjtestapp.database;

import android.content.Context;

import com.example.drop.wzjtestapp.database.greendao.DaoMaster;
import com.example.drop.wzjtestapp.database.greendao.DaoSession;
import com.example.drop.wzjtestapp.utils.LogUtil;

import org.greenrobot.greendao.database.Database;

/**
 * Created by liang_xs on 2017/11/28.
 */
public class Helper extends DaoMaster.OpenHelper{

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static final String DBNAME = "wzjTest.db";

    public Helper(Context context){
        super(context,DBNAME,null);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        LogUtil.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
        if (oldVersion < newVersion) {
            LogUtil.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
            //更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
//             MigrationHelper.getInstance().migrate(db, UserDao.class,XXDao.class);
        }
    }

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    DBNAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
