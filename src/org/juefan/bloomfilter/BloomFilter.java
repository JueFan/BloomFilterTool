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

@SuppressWarnings("unchecked")
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
	}

	/**存储分片位数组的值*/
	public static int[][] bitSets = new int[HASH][];
	public static int exits = 0;
	public static int p = 0;
	/**记录去重后的总量*/
	public static int total = 0;
	public static int memory = 0;
	public static StringBuilder builder = new StringBuilder();

	/**位数组初始化全部为0*/
	public BloomFilter(){
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
		/*long site = bit / 32;
		long mod1 = bit % 32;*/
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
			System.err.println(mod + "\t" + bit + "\t" + (bit >> 5) + "\t" + set.length);
		}
		return isExist;
	}

	/**
	 * 判断字符串是否已经存在
	 * @param string
	 */
	public static boolean setBitSet1(String string){
		boolean isExist = true;
		try {
			long hash = hashCode(string, SEED[0]);
			for(int j = 0; j < HASH; j++){
				isExist = setBitMap(bitSets[j], MOD[j], hash) && isExist;
			}
			/**如果isExist == true则表明上面的判断中每一次位数组都判断为1，可知该字符串可能存在*/
			if(!isExist){
				total++;
				builder.append(string).append("\n");
			}
		} catch (Exception e) {
		}
		return isExist;
	}
	
	/**
	 * 判断字符串是否已经存在
	 * @param string
	 */
	public static boolean setBitSet2(String string){
		boolean isExist = true;
		try {
			exits = 0;		
			p = 0;
			long hash = hashCode(string, SEED[0]);
			for(int j = 0; j < HASH; j++){
				isExist = setBitMap(bitSets[j], MOD[j], hash) && isExist;
			}
			/**如果isExist == true则表明上面的判断中每一次位数组都判断为1，可知该字符串可能存在*/
			if(!isExist){
				total++;
			}
		} catch (Exception e) {
		}
		return isExist;
	}

	private static long getMemory(){  
		Runtime runtime = Runtime.getRuntime();  
		return runtime.totalMemory() - runtime.freeMemory();  
	}  

	/**
	 * 将指定内容写入指定文件中
	 * 以追加的方式写入
	 * @param fileWriter  文件路径
	 * @param context  存储内容
	 * @param bool 是否追加写入
	 */
	public static void FileWrite(String fileName, String context, boolean bool){
		try{
			@SuppressWarnings("resource")
			FileWriter fileWriter = new FileWriter(fileName, bool);
			fileWriter.write(context);
			fileWriter.flush();
		}catch (Exception e) {
		}
	}

	/**输出所有参数到文件*/
	public static void outputParse(String fileName){

		FileWrite(System.getProperty("user.dir") + "\\output\\" + fileName, "Hash串种子为："
				+ SEED[0] + "\n", true);
		for(int j = 0; j < HASH; j++){
			FileWrite(System.getProperty("user.dir") + "\\output\\" + fileName, "Mod种子为："
					+ MOD[j] + "\n", true);
		}
		FileWrite(System.getProperty("user.dir") + "\\output\\" + fileName, 
				"\n位数组长度: " + BIT + "\n"
						+ "字符串拆分长度: " + HASH + "\n", true);
	}

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws IOException {

		System.out.println("请输入要去重文件的本地路径:");
		Scanner in = new Scanner(System.in);
		String pathString = in.next();
		System.out.println("是否要输出去重后的文件?\t是:1 否:0");
		in = new Scanner(System.in);
		String IsOutput = in.next();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		System.out.println("开始时间: " + df.format(new Date()));
		String fileName = df.format(new Date()) + ".txt"; 

		System.gc();  
		long startMem = getMemory(); 
		BloomFilter bloomFilter = new BloomFilter();

		BufferedReader bufferedReader;
		/**缓冲池为20M*/
		bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(     
				new FileInputStream(pathString))), 1024*1024*20);
		String buf;
		int total = 0;
		
		/**读取数据判断模块*/
		if(IsOutput.equals("1")){
			while ((buf = bufferedReader.readLine()) != null){   
				total++;
				BloomFilter.setBitSet1(buf);
				if(BloomFilter.total % 200000 == 0){
					FileWrite(System.getProperty("user.dir") + "\\output\\Distinct.txt" , builder.toString(), true);
					builder.delete(0, builder.length());
				}
			}  
			FileWrite(System.getProperty("user.dir") + "\\output\\Distinct.txt" , builder.toString(), true);
			builder.delete(0, builder.length());
		}else {
			while ((buf = bufferedReader.readLine()) != null){   
				total++;
				BloomFilter.setBitSet2(buf);
			}  
		}
		
		System.gc();
		long endMem = getMemory(); 

		System.out.println("读取的总标题数量有: " + total + 
				"\n重复的标题条数有: " + (total - BloomFilter.total )+ 
				"\nBloomFilter去重后有: " + BloomFilter.total + " 个不同标题\n" 
				+ "jvm占用内存量为:" + (endMem - startMem)/(1024*1024) + "Mb\n"
				+ "结束时间: " + df.format(new Date()));
	}
}
