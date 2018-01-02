package com.example.drop.wzjtestapp.utils;


import android.content.SharedPreferences;
import android.net.Credentials;
import android.preference.PreferenceActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.drop.wzjtestapp.MyApplication;

import java.io.Serializable;

/**
 * @ClassName: CommonPreference 
 * @Description: SharedPreference管理
 * @author liang_xs
 */
public class CommonPreference {
	private static final String PREF_FILE_NAME = "common_settings";
	public static final String IS_FIRST = "isFirst";
	public static final String DISTURB_STATUS = "DISTURB_STATUS";
	public static final String OPEN = "OPEN";
	public static final String NIGHT_ONLY = "NIGHT_ONLY";
	public static final String CLOSE = "CLOSE";
	private static final String USER_ID = "user_id";
	private static final String CREDENTIALS = "Credentials";
	public static final String ALI_PUSH_TOKEN = "ali_push_token";


	public static String getStringValue(String keyWord, String defaultValue) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		return sp.getString(keyWord, defaultValue);
	}
	
	public static void setStringValue(String keyWord, String value) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		sp.edit().putString(keyWord, value).commit();
	}
	
	public static int getIntValue(String keyWord, int defaultValue) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		return sp.getInt(keyWord, defaultValue);
	}
	
	public static void setIntValue(String keyWord, int value) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		sp.edit().putInt(keyWord, value).commit();
	}
	
	public static boolean getBooleanValue(String keyWord, boolean defaultValue) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		return sp.getBoolean(keyWord, defaultValue);
	}
	
	public static void setBooleanValue(String keyWord, boolean isCheck) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		sp.edit().putBoolean(keyWord, isCheck).commit();
	}
	

	public static long getLongValue(String keyWord, long defaultValue) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		return sp.getLong(keyWord, defaultValue);
	}
	
	public static void setLongValue(String keyWord, long value) {
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		sp.edit().putLong(keyWord, value).commit();
	}

	public static void setSerializable(String key, Serializable value) {
		String json=JSON.toJSONString(value);
		setStringValue(key, json);
	}

	public static <T extends Serializable> T getSerializable(String key, TypeReference<T> type) {
		try {
			return JSON.parseObject(getStringValue(key, null), type);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void setObject(String key, Object obj){
		String jsonString=JSON.toJSONString(obj);
		setStringValue(key, jsonString);
	}
	
	
	public static <T> T getObject(String key, Class<T> clazz){
		
		try {
			String json=getStringValue(key, "");
			return JSON.parseObject(json, clazz);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static void remove(String key){
		SharedPreferences sp = MyApplication.getApplication()
				.getSharedPreferences(PREF_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}

	private final static String API_HOST = "_api_host";
	private final static String DEFAULT_DEV_HOST = "http://api-test:8080/api/";

	public static void setApiHost(String url){
		CommonPreference.setStringValue(API_HOST,url);
	}

	public static String getApiHost(){
		return CommonPreference.getStringValue(API_HOST,DEFAULT_DEV_HOST);
	}

	public static int getUserId() {
		return CommonPreference.getIntValue(USER_ID, 0);
	}

	public static void setUserId(int userId){
		CommonPreference.setIntValue(USER_ID,userId);
	}

	public static Credentials getCredentials() {
		return getObject(CREDENTIALS, Credentials.class);
	}

	public static void setCredentials(Credentials result) {
		setObject(CREDENTIALS, result);
	}

	public static boolean isLogin(){
		return getUserId() == 0? false : true;
	}

	public static String getToken(){
		return getStringValue("token", "");
	}
	public static void setToken(String token) {
		setStringValue("token", token);
	}
	public static String getRongToken(){
		return getStringValue("rong_token", "");
	}
	public static void setRongToken(String rongToken){
		setStringValue("rong_token", rongToken);
	}

	public static void clearUserInfo(){
		setUserId(0);
		setRongToken("");
	}
}
