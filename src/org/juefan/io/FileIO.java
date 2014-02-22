package org.juefan.io;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileIO {
	private String fileName;
	private String pathString;
	public  List<String> fileList;
	public static List<String> fileNameList = new ArrayList<String>(); 

	public FileIO(){
		fileName = new String();
		fileList = new ArrayList<String>();
	}

	/**设置要读取的文件的文件路径*/
	public void setFileName(String fileString){
		this.fileName = fileString;
	}

	/**设置文件路径*/
	public void setPath(String path){
		this.pathString = path;
	}

	/**读取文件夹内的所有文件名称*/
	public void setFileNameList(){
		for(File file : new File(pathString).listFiles()){
			fileNameList.add(file.getAbsolutePath());
		}
	}

	/**将fileList数组复制出来
	 * 并将原有的fileList清空*/
	public List<String> cloneList(){
		List<String> tmpList = new ArrayList<String>();
		tmpList.addAll(fileList);
		fileList.clear();
		return tmpList;
	}

	/**读取文件内的数据并存储至fileList*/
	public void FileRead(){
		FileRead("gbk");
	}

	/**
	 * 读取文件内的数据
	 * 将fileName文件内的内容按行存储进fileList数组中
	 */
	public void FileRead(String code){
		fileList.clear();
		try{
			Scanner fileScanner = new Scanner(new File(fileName), code);
			while(fileScanner.hasNextLine()){
				fileList.add(fileScanner.nextLine());
			}
		}catch (Exception e) {
		}
	}


	/**读取文件名列表里面的所有数据*/
	public void FileListRead(){
		FileListRead("gbk");
	}

	/**
	 * 读取文件名列表里面的所有数据
	 * @param code	文件编码格式
	 */
	public void FileListRead(String code){
		System.out.println("进入文件读取程序......");
		try{
			for(String file: fileNameList){
				if(new File(file).exists()){
					Scanner fileScanner = new Scanner(new File(file), code);
					while(fileScanner.hasNextLine()){
						fileList.add(fileScanner.nextLine());
					}
				}
			}
		}catch (Exception e) {
			System.out.println("文件不存在!");
		}
		System.out.println("共有 " + fileNameList.size() + " 个文件");
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

	/**
	 * 将指定内容写入指定文件中
	 * 以追加的方式写入
	 * @param fileWriter  文件路径
	 * @param context  存储内容
	 */
	public static void FileWrite(String fileName, String context){
		try{
			FileWriter fileWriter = new FileWriter(fileName, true);
			fileWriter.write(context);
			fileWriter.flush();
		}catch (Exception e) {
		}
	}

	/** 
	 * @param path 文件路径 
	 * @param suffix 后缀名 
	 * @param isdepth 是否遍历子目录 
	 * @return 满足要求的文件路径名列表
	 */ 
	public static List<String> getListFiles(String path, String suffix, boolean isdepth) { 
		File file = new File(path); 
		return FileIO.listFile(file ,suffix, isdepth); 
	} 

	public static List<String> getListFiles(String path, String suffix) { 
		File file = new File(path); 
		return FileIO.listFile(file ,suffix); 
	} 

	public static List<String> listFile(File f, String suffix) { 
		return FileIO.listFile( f,  suffix, true);
	}

	/** 
	 * 读取目录及子目录下指定文件名的路径 并放到一个数组里面返回遍历 
	 * @author juefan
	 */ 
	public static List<String> listFile(File f, String suffix, boolean isdepth) { 
		//是目录，同时需要遍历子目录 
		if (f.isDirectory() && isdepth == true) { 
			//listFiles()是返回目录下的文件路径集合
			File[] t = f.listFiles(); 
			for (int i = 0; i < t.length; i++) {
				listFile(t[i], suffix, isdepth); 
			} 
		} 
		else { 
			String filePath = f.getAbsolutePath(); 
			if(suffix =="" || suffix == null) { 
				fileNameList.add(filePath); 
			} 
			else { 
				//最后一个.(即后缀名前面的.)的索引
				int begIndex = filePath.lastIndexOf("."); 
				//System.out.println("索引为 :"+begIndex);
				String tempsuffix = ""; 
				//防止是文件但却没有后缀名结束的文件
				if(begIndex != -1){  
					//tempsuffix取文件的后缀
					tempsuffix = filePath.substring(begIndex + 1, filePath.length()); 
				} 
				if(tempsuffix.equals(suffix)) { 
					fileNameList.add(filePath); 
				} 
			} 
		} 
		return fileNameList; 
	} 

}
