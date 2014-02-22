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

	/**������������ʼ��*/
	public static  float START = 6.5F;
	/**HASH�����ĸ���*/
	public static  int HASH = 8;
	/**λ���鳤��=10^BIT*START*/
	public static  int BIT = 9;
	/**HASH������ֵ*/
	public static  long[] MOD = new long[HASH];
	/**�ַ���ת����hashcode�õ�������*/
	public static  long[] SEED = new long[HASH];

	/**��ʼ��HASH������ֵ�����ӵ�ֵ*/
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

	/**�洢��Ƭλ�����ֵ*/
	public static int[][] bitSets = new int[HASH][];
	public static int exits = 0;
	public static int p = 0;
	/**��¼ȥ�غ������*/
	public static int total = 0;
	public static int memory = 0;
	public static StringBuilder builder = new StringBuilder();

	/**λ�����ʼ��ȫ��Ϊ0*/
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
	 * �������ø�������
	 * @param hash λ����ĸ���
	 * @param start ÿ��λ����ĳ���ֵ(�����λ��ֵ������6553500��������6.5)
	 * @param bit ÿ��λ����ĳ���λ(����6553500���������7)
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
	 * �ַ���ת����ֵ����
	 * ����long���͵ĺô���ÿ���ַ���ֻ��Ҫ����һ��hashcode
	 * ����1�ڸ�long�����������ȫ�������ظ��ĸ�����99.96%
	 * ��������������long���͵�hashcode��ÿ���ַ�������һһ��Ӧ
	 * @param string ��ת���ַ���
	 * @param seed ��������
	 * @return ת�����ֵ
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
	 * �ж��ַ����Ƿ��ڵ�Ƭλ�����д���
	 * @param set ��Ƭλ����
	 * @param mod ĳ��hash������ֵ
	 * @param hash �ַ���ת�����hashcode
	 */
	public static boolean setBitMap(int[] set, long mod, long hash){
		boolean isExist = false;
		/**�ַ�����λ�����е�λ��*/
		long bit = hash%mod;
		/**int���͵�λ��*/
		long site = bit >> 5;
		/**int������32λ�ĵ�mod1λ*/
		long mod1 = bit & 31;
		/**λ�����и��ַ�����λ���Ƿ�Ϊ1
		 * ��������˳�ʱexits+1�����򽫸�λ�ñ��1
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
	 * �ж��ַ����Ƿ��Ѿ�����
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

}
