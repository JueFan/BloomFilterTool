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


/**�������Ľ��Ϊ: 86386252*/
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
		System.out.println("��ʼʱ��: " + df.format(new Date()));
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
				System.out.println(df.format(new Date()) + "\t����д��");
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
			System.out.println(df.format(new Date()) + "\t���ݶ�ȡ\t��ǰȥ�غ���: " + Total + "��");
			bufferedReader.close();
		}
		System.out.println("�ļ���ȥ�غ��ܵ�������: " + Total + " ��");
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
			FileWriter fileWriter = new FileWriter(fileName, bool);
			fileWriter.write(context);
			fileWriter.flush();
		}catch (Exception e) {
		}
	}
}
