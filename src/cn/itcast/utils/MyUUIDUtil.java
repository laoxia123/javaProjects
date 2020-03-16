package cn.itcast.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 随机生成字符串
 * @author Administrator
 *
 */
public class MyUUIDUtil {
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
