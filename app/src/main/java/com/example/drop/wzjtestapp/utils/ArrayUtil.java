package com.example.drop.wzjtestapp.utils;

import org.json.JSONArray;

import java.util.Collection;

/**
 * 判断数组是否为空
 */
public class ArrayUtil {


	public static boolean isEmpty(Object object){
		if(object instanceof Collection){
			Collection list = (Collection) object;
			if(list!=null && !list.isEmpty()){
				return false;
			}
		}else if(object instanceof Object[]){
			Object[] arr = (Object[]) object;
			if(arr!=null && arr.length>0){
				return false;
			}
		}else if(object instanceof int[]){
			int[] arr = (int[]) object;
			if(arr!=null && arr.length>0){
				return false;
			}
		}else if(object instanceof float[]){
			float[] arr = (float[]) object;
			if(arr!=null && arr.length>0){
				return false;
			}
		}else if(object instanceof double[]){
			double[] arr = (double[]) object;
			if(arr!=null && arr.length>0){
				return false;
			}
		}else if(object instanceof long[]){
			long[] arr = (long[]) object;
			if(arr!=null && arr.length>0){
				return false;
			}
		}else if(object instanceof byte[]){
			byte[] arr = (byte[]) object;
			if(arr!=null && arr.length>0){
				return false;
			}
		}else if(object instanceof boolean[]){
			boolean[] arr = (boolean[]) object;
			if(arr!=null && arr.length>0){
				return false;
			}
		}else if(object instanceof JSONArray){
			JSONArray arr = (JSONArray) object;
			if(arr!=null && arr.length()>0){
				return false;
			}
		}

		return true;
	}
}
