package org.juefan.bloomfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimeNumber {
	/** Creates a new instance of PrimeNumberTest */
	public PrimeNumber() {
	}
	
	public static void main(String[] args){
		//���һ��4λ�������������
		/*long longVar4 = createRadomPrimeNunber(4);
		System.out.println(longVar4);
		//���һ��5λ�������������
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
	 *  ����100��ָ������
	 * @param n ������λ����
	 * @param start �����Ŀ�ʼֵ
	 * @return �������100������
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
	 * �ж�һ�����Ƿ��������ĺ���
	 * @param x ���жϵ���
	 * @return x�Ƿ�Ϊ����
	 */
	public static boolean isSushu(long x){
		if(x<2) return false;
	if( x==2)return true;
	for(long i=2;i<= (long)Math.sqrt(x);i++)
		if(x%i==0) return false;
	return true;
	}
}

