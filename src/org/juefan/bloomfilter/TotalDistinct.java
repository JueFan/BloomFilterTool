package org.juefan.bloomfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.juefan.io.FileIO;


/**经计算后的结果为: 86386252*/
public class TotalDistinct {

	public static List<StringBuilder> setList = new ArrayList<StringBuilder>();

	public static void Write(String filePath){
		for(int i = 0; i < setList.size(); i++){
			FileWrite(filePath + Integer.toString(i), setList.get(i).toString(), true);
		}
		setList.clear();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		System.out.println("开始时间: " + df.format(new Date()));
		int SPLIT  = 50;
		for(int i = 0; i < SPLIT; i++)
			setList.add(new StringBuilder());
		BufferedReader bufferedReader;
		bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(     
				new FileInputStream(System.getProperty("user.dir") + "\\input\\title.txt.gz"))), 1024*1024*20);
		String filePath = System.getProperty("user.dir") + "\\output\\split\\file_";
		String buf;
		int Num = 0;
		while ((buf = bufferedReader.readLine()) != null){
			int Start = (int) ((Long.MAX_VALUE & buf.hashCode())%SPLIT);
			setList.get(Start).append(buf.toString()).append("\n");
			if(++Num % 1000000 == 0){
				System.out.println(df.format(new Date()) + "\t数据写入");
				Write(filePath);
				for(int i = 0; i < SPLIT; i++)
					setList.add(new StringBuilder());
			}
		}
		Write(filePath);
		bufferedReader.close();
		
		List<String> pathList = FileIO.getListFiles(System.getProperty("user.dir") + "\\output\\split", null);
		Set<String> set = new HashSet<String>();
		int Total = 0;
		for(int i = 0; i < pathList.size(); i++){		
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(pathList.get(i))), 1024*1024*20);
			while ((buf = bufferedReader.readLine()) != null) {
				set.add(buf);
			}
			Total += set.size();
			set.clear();
			System.out.println(df.format(new Date()) + "\t数据读取\t当前去重后有: " + Total + "条");
			bufferedReader.close();
		}
		System.out.println("文件中去重后总的数据有: " + Total + " 条");
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
			FileWriter fileWriter = new FileWriter(fileName, bool);
			fileWriter.write(context);
			fileWriter.flush();
		}catch (Exception e) {
		}
	}
}
