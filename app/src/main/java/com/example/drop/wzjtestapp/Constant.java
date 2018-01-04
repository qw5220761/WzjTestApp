package com.example.drop.wzjtestapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.example.drop.wzjtestapp.utils.CommonPreference;

/**
 * Created by wangzj on 2018/1/2 0002.
 */

public class Constant {
    public final static boolean DEBUG = isDebug(MyApplication.getApplication());
    public static final String API_HOST ;
    public static final String OSS_FOLDER_BUCKET = "gongyouhui";
    public static final String OSS_IMG_HEAD = "http://gongyouhui.oss-cn-beijing.aliyuncs.com/";
    //    public static final String OSS_IMG_HEAD = "http://gongyouhui.img-cn-beijing.aliyuncs.com/";
    public final static String HTTP = "http://";
    public final static String HTTPS = "https://";


    public static final String BASE_URL = "";
    public static final String GET_STS_TOKEN = BASE_URL + "sys/v1/getStsToken";
    public static final String GOODS = BASE_URL + "";
    /** 设备注册接口:客户端本地没有token，认为是首次使用，系统自动为手机注册*/
    public static final String SIGN_UP = BASE_URL + "signUp";
    /** 用户登录注册*/
    public static final String SWITCH_USER = BASE_URL + "switchUser";
    /** 退出登录*/
    public static final String LOGOUT= BASE_URL + "logout";



    public static final String QR_CODE = "QR_CODE";
    public static final String EXTRA_ORDERS_LIST = "extra_orders_list";
    public static final String EXTRA_IMAGES = "extra_images";
    public static final String EXTRA_SELECT_BITMAP = "extra_select_bitmap";
    public static final String MAX_ALBUM_COUNT = "max_album_count";
    public static final String EXTRA_IMAGE_INDEX = "extra_image_index";
    public static final String EXTRA_ALBUM_FROM_MAIN = "1"; // 来自主页
    public static final String EXTRA_ALBUM_LIST_FOLDERNAME = "extra_album_list_foldername";
    public static final String EXTRA_ALBUM_LIST_IMAGES = "extra_album_list_images";
    public static final String EXTRA_TO_GALLERY_FROM = "extra_to_gallery_from";
    public static final String EXTRA_DRAW_PHOTO_PATH = "extra_draw_photo_path";
    public static final String EXTRA_PHOTO_DELETE = "extra_photo_delete";
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_MAP = "push_message_extra_map";
    public static final String EXTRA_MAIN_GO_TO_KEY = "extra_main_jump";

    public final static int RESPONSE_SUCCESS_CODE = 100;
    public final static String MAIN_GO_TO_IM = "IM";
    public final static String MAIN_GO_TO_HOME = "HOME";




    public static final int MATCH_SELECT_DYNAMIC_PHOTO = 8;
    public static final int INTENT_FOR_RESULT_ALBUM_TO_GALLERY = 1000;
    public static final int INTENT_FOR_RESULT_ALBUM_TO_ALBUMLIST = 1001;
    public static final int INTENT_FOR_RESULT_MAIN_TO_CAMERA = 1002;
    public static final int INTENT_FOR_RESULT_GALLERY_TO_DRAWPHOTO = 1007;
    public static final int INTENT_FOR_RESULT_GALLERY_TO_CLIPPHOTO = 1009;
    public static final int INTENT_FOR_RESULT_COMMENT_TO_ALBUM = 1012;


    static {
        if(DEBUG){
            API_HOST = CommonPreference.getApiHost();
        }else{
            API_HOST = "https://api/api/";
        }
    }

    public static boolean isDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
