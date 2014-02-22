package org.juefan.predict;

public class ErrorPredict {
	
	
	/**预估内存靠谱*/
	public static float getMemory(long length, int hash){
		return ((length / 32) * 4 * hash) >> 20;
	}
	
	/**运行时间还未分析*/
	public static float getTimes(long length, int hash){
		return (float)220/(12 * 12)*hash*hash + 200;
	}
	
	/*可以证明第一种方式与第二种方式是等价的*/
	/**第k个随机数出错的概率*/
	public static float getErrorChange(long k, int hash, long length){
		return (float) Math.pow(1 - Math.pow(Math.E, -(double)k/length), hash);
	}
	
	/**第k个随机数出错的概率*/
	public static float getErrorStand(long k, int hash, long length){
		return (float) Math.pow(1 - Math.pow(Math.E, -(double)k * hash/length), hash);
	}
	
	
	public static void getPredict(int hash, long length, int total){
		System.out.println("布隆过滤器待存储数据总量为: " + total);
		double error = 0;
		System.out.println("预估占用内存量: " + (int)getMemory(length, hash) + "M");

		for(int i = 0; i < total; i++)
			error += getErrorChange(i, hash, length);
		
		System.out.println("预计出错个数为: " + (float)error);
	}
	
	
	public static void main(String[] args) throws NumberFormatException{
		
		int hash = 0;
		Long length = 0L;
		int TOTAL = 10000000;
		
		if(args.length == 0){
			 hash = 8;
			 length = 60000000L;
		}else if (args.length == 3) {
			 hash = Integer.parseInt(args[0]);
			 length = Long.parseLong(args[1]);
			 TOTAL = Integer.parseInt(args[2]);
		}
		else {
			System.err.println("参数错误\n请输出俩个参数...");
		}
		
		System.out.println("布隆过滤器待存储数据总量为: " + TOTAL);
		double error = 0;
		System.out.println("预估占用内存量: " + (int)getMemory(length, hash) + "M");

		for(int i = 0; i < TOTAL; i++)
			error += getErrorChange(i, hash, length);
		
		System.out.println("预计出错个数为: " + (float)error);
	}

}
