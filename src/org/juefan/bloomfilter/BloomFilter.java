package org.juefan.bloomfilter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class BloomFilter {

	/**产生素数的起始数*/
	public static  float START = 6.5F;
	/**HASH函数的个数*/
	public static  int HASH = 8;
	/**位数组长度=10^BIT*START*/
	public static  int BIT = 9;
	/**HASH函数的值*/
	public static  long[] MOD = new long[HASH];
	/**字符串转化成hashcode用到的算子*/
	public static  long[] SEED = new long[HASH];

	public static int[][] bitSets = new int[HASH][];
	

	/**存储分片位数组的值*/

	public static int exits = 0;
	public static int p = 0;
	/**记录去重后的总量*/
	public static int total = 0;
	public static int memory = 0;
	public static StringBuilder builder = new StringBuilder();

	/**初始化HASH函数的值和算子的值*/
	static {	
		MOD[0] = 650000011;
		MOD[1] = 650000017;
		MOD[2] = 650000047;
		MOD[3] = 650000063;
		MOD[4] = 650000077;
		MOD[5] = 650000119;
		MOD[6] = 650000149;
		MOD[7] = 650000161;
		SEED[0] = 650011;
        init();
	}
	
	/**位数组初始化全部为0*/
	private static void init(){
		int[][] list = new int[HASH][];
		for(int j = 0; j < HASH; j++){
			long size = (MOD[j] >> 5) + 1;
			int[] set = new int[(int) size];
			list[j] = set;
		}
		bitSets = list;
	}

	
	/**
	 * 重新设置各个参数
	 * @param hash 位数组的个数
	 * @param start 每个位数组的长度值(的最大位数值，例如6553500可以输入6.5)
	 * @param bit 每个位数组的长度位(例如6553500则可以输入7)
	 */
	public static void resetBitset(int hash, float start, int bit){
		HASH = hash;
		START = start;
		BIT = bit;
		List<Long> list = PrimeNumber.listPartPrimeNumber(BIT,	START);
		List<Long> seList = PrimeNumber.listPartPrimeNumber(6, START);
		for(int j = 0; j < HASH; j++){
			MOD[j] = list.get(j).longValue();
			SEED[j] = seList.get(j).longValue();
		}
	}
	
	/**
	 * 字符串转化数值方法
	 * 采用long类型的好处是每个字符串只需要运行一次hashcode
	 * 任意1亿个long类型随机数，全部都不重复的概率是99.96%
	 * 所以有理由相信long类型的hashcode与每个字符串几乎一一对应
	 * @param string 待转化字符串
	 * @param seed 素数算子
	 * @return 转化后的值
	 */
	public static long hashCode(String string, long seed){
		long result = 0;
		int len = string.length();
		for(int i = 0; i < len; i++){
			result = seed * result + string.charAt(i);
		}
		return ((Long.MAX_VALUE ) & (result));
	}

	/**
	 * 判断字符串是否在单片位数组中存在
	 * @param set 单片位数组
	 * @param mod 某个hash函数的值
	 * @param hash 字符串转化后的hashcode
	 */
	public static boolean setBitMap(int[] set, long mod, long hash){
		boolean isExist = false;
		/**字符串在位数组中的位置*/
		long bit = hash%mod;
		/**int类型的位置*/
		long site = bit >> 5;
		/**int类型中32位的第mod1位*/
		long mod1 = bit & 31;
		/**位数组中该字符串的位置是否为1
		 * 如果是则退出时exits+1，否则将该位置变成1
		 * */
		try {
			if(((set[(int) site] >> mod1) & 1) == 1){
				isExist = true;
			}
			else {
				set[(int) site] = set[(int) site] | (1 << mod1);
			}	
		} catch (Exception e) {
		}
		return isExist;
	}

	/**
	 * 判断字符串是否已经存在
	 * @param string
	 */
	public static boolean isExist(String string){
		boolean isExist = true;
		try {
			long hash = hashCode(string, SEED[0]);
			for(int j = 0; j < HASH; j++){
				isExist = setBitMap(bitSets[j], MOD[j], hash) && isExist;
			}
		} catch (Exception e) {
		}
		return isExist;
	}
	

	private static long getMemory(){  
		Runtime runtime = Runtime.getRuntime();  
		return runtime.totalMemory() - runtime.freeMemory();  
	}  

	public static void main(String[] args) {
		int sum = 0;
		for(int i = 0; i < 10000000; i++){
			if(!BloomFilter.isExist(String.valueOf(i))){
				sum += 1;
			}
		}
		System.out.println(sum);
		
	}
}

