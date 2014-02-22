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
		/*long site = bit / 32;
		long mod1 = bit % 32;*/
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
			System.err.println(mod + "\t" + bit + "\t" + (bit >> 5) + "\t" + set.length);
		}
		return isExist;
	}

	/**
	 * �ж��ַ����Ƿ��Ѿ�����
	 * @param string
	 */
	public static boolean setBitSet1(String string){
		boolean isExist = true;
		try {
			long hash = hashCode(string, SEED[0]);
			for(int j = 0; j < HASH; j++){
				isExist = setBitMap(bitSets[j], MOD[j], hash) && isExist;
			}
			/**���isExist == true�����������ж���ÿһ��λ���鶼�ж�Ϊ1����֪���ַ������ܴ���*/
			if(!isExist){
				total++;
				builder.append(string).append("\n");
			}
		} catch (Exception e) {
		}
		return isExist;
	}
	
	/**
	 * �ж��ַ����Ƿ��Ѿ�����
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
			/**���isExist == true�����������ж���ÿһ��λ���鶼�ж�Ϊ1����֪���ַ������ܴ���*/
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
	 * ��ָ������д��ָ���ļ���
	 * ��׷�ӵķ�ʽд��
	 * @param fileWriter  �ļ�·��
	 * @param context  �洢����
	 * @param bool �Ƿ�׷��д��
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

	/**������в������ļ�*/
	public static void outputParse(String fileName){

		FileWrite(System.getProperty("user.dir") + "\\output\\" + fileName, "Hash������Ϊ��"
				+ SEED[0] + "\n", true);
		for(int j = 0; j < HASH; j++){
			FileWrite(System.getProperty("user.dir") + "\\output\\" + fileName, "Mod����Ϊ��"
					+ MOD[j] + "\n", true);
		}
		FileWrite(System.getProperty("user.dir") + "\\output\\" + fileName, 
				"\nλ���鳤��: " + BIT + "\n"
						+ "�ַ�����ֳ���: " + HASH + "\n", true);
	}

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws IOException {

		System.out.println("������Ҫȥ���ļ��ı���·��:");
		Scanner in = new Scanner(System.in);
		String pathString = in.next();
		System.out.println("�Ƿ�Ҫ���ȥ�غ���ļ�?\t��:1 ��:0");
		in = new Scanner(System.in);
		String IsOutput = in.next();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		System.out.println("��ʼʱ��: " + df.format(new Date()));
		String fileName = df.format(new Date()) + ".txt"; 

		System.gc();  
		long startMem = getMemory(); 
		BloomFilter bloomFilter = new BloomFilter();

		BufferedReader bufferedReader;
		/**�����Ϊ20M*/
		bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(     
				new FileInputStream(pathString))), 1024*1024*20);
		String buf;
		int total = 0;
		
		/**��ȡ�����ж�ģ��*/
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

		System.out.println("��ȡ���ܱ���������: " + total + 
				"\n�ظ��ı���������: " + (total - BloomFilter.total )+ 
				"\nBloomFilterȥ�غ���: " + BloomFilter.total + " ����ͬ����\n" 
				+ "jvmռ���ڴ���Ϊ:" + (endMem - startMem)/(1024*1024) + "Mb\n"
				+ "����ʱ��: " + df.format(new Date()));
	}
}
