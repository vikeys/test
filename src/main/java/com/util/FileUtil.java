package com.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileUtil {
	
	public static String getPath(String filepath) {
		int end = filepath.lastIndexOf("/");
		if (end == -1) {
			end = filepath.lastIndexOf("\\");
		}
		return filepath.substring(0, end + 1);
	}
	
	public static String getFilename(String filepath) {
		int start = filepath.lastIndexOf("/");
		if (start == -1) {
			start = filepath.lastIndexOf("\\");
		}
		return filepath.substring(start + 1, filepath.length());
	}
	
	// 判断path路径是否正确
	private static boolean checkFolder(String path) {
		File file = new File(path);
		return (file.exists()) && file.isDirectory();
	}
	
	// 检查文件是否存在
	public static boolean checkFile(String path, String filename) {
		File file = new File(path + filename);
		return file.exists();
	}
	
	// 根据path新建文件夹
	private static boolean setFolder(String path) {
		File file = new File(path);

		if (!(file.exists() && file.isDirectory())) {
			return file.mkdirs();
		}

		return false;
	}
	
	// 创建文件
	public static boolean createFile(String path, String filename) {
		File file = new File(path + filename);

		// 检查文件夹是否存在
		if (!checkFolder(path)) {
			if (!setFolder(path)) {
				return false;
			}
		}

		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// 把信息写入文件
	public static boolean writeFile(String pathname,String info, boolean isAdd){
		String path = getPath(pathname);
		String filename = getFilename(pathname);
		return writeFile(path, filename, info, isAdd);
	}
	
	// 把信息写入文件
	public static boolean writeFile(String path, String filename,String info, boolean isAdd) {

		// 如果文件不存在
		// 则创建文件，然后写入
		if (!checkFile(path, filename)) {
			if (!createFile(path, filename)) {
				return false;
			}
		}

		File file = new File(path + filename);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, isAdd);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(info);
			writer.close();
			fileWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void writeToFile(String pathname, String info){
		pathname = pathname.replaceAll("%20", " ");
		writeFile(pathname, info, false);
	}

}
