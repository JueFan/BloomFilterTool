package org.juefan.bloomfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimeNumber {
	/** Creates a new instance of PrimeNumberTest */
	public PrimeNumber() {
	}
	
	public static void main(String[] args){
		//获得一个4位数的随机大素数
		/*long longVar4 = createRadomPrimeNunber(4);
		System.out.println(longVar4);
		//获得一个5位数的随机大素数
		long longVar5 = createRadomPrimeNunber(5);*/
		System.out.println(listPartPrimeNumber(3,1));
	}


	public static long createRadomPrimeNunber(int n){
		long recLong = 0;
		List list = listAllPrimeNumber(n);
		Random rd = new Random();
		int randomIndex = Math.abs( rd.nextInt()%list.size());
		recLong = ((Long)list.get(randomIndex)).longValue();
		return recLong;

	}
	public static List listAllPrimeNumber(int n){
		List list = new ArrayList();
		long low = (long)Math.pow(10,n-1);
		long high = (long)Math.pow(10,n) - 1;
		for(long i= low;i < high;i++){
			if( isSushu(i)) {
				list.add(new Long(i));
			}
		}
		return list;
	}
	
	/**
	 *  返回100个指定素数
	 * @param n 素数的位长度
	 * @param start 素数的开始值
	 * @return 返回最多100个素数
	 */
	public static List listPartPrimeNumber(int n, float start){
		int p = 100;
		int num = 0;
		List list = new ArrayList();
		long low = (long)Math.pow(10,n-1);
		long high = (long)Math.pow(10,n) - 1;
		for(long i= (long) (low * start);i < high;i++){
			if( isSushu(i) && Math.random() >= 0.1) {
				list.add(new Long(i));
				if(num++ >= p)
					break;
			}
		}
		return list;
	}
	
	/**
	 * 判断一个数是否是素数的函数
	 * @param x 待判断的数
	 * @return x是否为素数
	 */
	public static boolean isSushu(long x){
		if(x<2) return false;
	if( x==2)return true;
	for(long i=2;i<= (long)Math.sqrt(x);i++)
		if(x%i==0) return false;
	return true;
	}
}

