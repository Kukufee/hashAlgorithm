package com.my.hashalgorithm;

/**
 *
 * @author MaYue
 * @Version CreateTime:2019年5月3日下午7:07:17
 *
 */

public class Test {
	public static void main(String[] args) {
		System.out.println(FNV1_32_HASH("127.0.0.1"));
		System.out.println(FNV1_32_HASH("127.0.0.2"));
		System.out.println(FNV1_32_HASH("127.0.0.3"));
		//添加第二行注释
	}
	
	private static Integer FNV1_32_HASH(String target) {
		int p = 1677619;
		//使用Long类型
		int hash = (int)2166136261L;
		char[] charArray = target.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			hash = (hash ^ charArray[i]) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		if (hash < 0) {
			hash = Math.abs(hash);
		}
		return hash;
	}
}
