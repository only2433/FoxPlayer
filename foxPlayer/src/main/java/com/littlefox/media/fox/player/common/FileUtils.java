package com.littlefox.media.fox.player.common;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.littlefox.logmonitor.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtils {


	
	public static boolean writeFile(Object obj, String path){
		if (path.equals("")) return false;
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		
		//String encriptString = SimpleCrypto.encrypt(json);

		File f = new File(path);
		File f2 = new File(f.getParent());
		if(!f2.exists())
			f2.mkdirs();
		f2 = null;

		try {
			f.createNewFile();
			if (f.canWrite()){
				FileOutputStream fo = new FileOutputStream(f);
				//fo.write(encriptString.getBytes(), 0, encriptString.getBytes().length);
				fo.write(json.getBytes(), 0, json.getBytes().length);
				fo.flush();
				fo.close();
			}
		} catch (IOException e) {
			Log.exception(e);
			Log.f("e : "+ e.toString());
			
			return false;
		}
		return true;
	}
	
	public static ArrayList<String> getFilelist(String dirpath,String prefix){
		ArrayList<String> ret = new ArrayList<String>();
		File f = new File(dirpath);
		if (!f.exists()) return ret;
		File[] files = f.listFiles();
		Arrays.sort(files);
		for (File subfile : files){
			if (subfile.toString().contains(prefix)){
				try {
					ret.add(subfile.getAbsolutePath());
				} catch (JsonParseException e) {
					e.printStackTrace();
					
				}
			}
		}
		return ret;
	}
	
	public static String getFile(String dirPath, String prefix)
	{
		String ret = "";
		File f = new File(dirPath);
		if (!f.exists()) return ret;
		File[] files = f.listFiles();
		Arrays.sort(files);
		for (File subfile : files){
			if (subfile.toString().contains(prefix)){
				try {
					ret = subfile.getAbsolutePath();
				} catch (JsonParseException e) {
					e.printStackTrace();
					
				}
			}
		}
		return ret;
	}
	
	public static String getStringFromFile(String path)
	{
		String result = "";
		
		File file = new File(path);
		
		if(file.exists() == false)
		{
			return result;
		}
		
		try
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			
			if(fileInputStream != null)
			{
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString ="";
				StringBuilder stringBuilder = new StringBuilder();
				
				while((receiveString = bufferedReader.readLine()) != null)
				{
					stringBuilder.append(receiveString);
				}
				
				inputStreamReader.close();
				result = stringBuilder.toString();
				
			}
		}catch(Exception e)
		{
			
		}
		//result = SimpleCrypto.decrypt(result);
		return result;
	}
	
	public static void deleteFile(String fileName)
	{
		File f = new File(fileName);
		
		if(f.exists())
		{
			f.delete();
		}
	}
	
	public static boolean checkFile(String path) {
		File f = new File(path);

		return f.exists();
	}
	
	public static void DeleteAllWithSuffix(File dir, String suffix) {
		if(suffix==null || suffix.length() == 0) return;

	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++)  {
	        	if(children[i].length()>=suffix.length() && children[i].endsWith(suffix)) {
		        	File temp =  new File(dir, children[i]);
		        	if(temp.isFile()) {
		        		// 파일만 삭제
		        		temp.delete();
		        	}
	        	}
	        }
	    } else {
	    	// 폴더 아니면 패스
	    }
	}
	
	/**
	 * 해당 디렉토리에 있는 파일을 전부 삭제한다.
	 * @param basePath
	 */
	public static void deleteAllFileInPath(String basePath)
	{
		File file = new File(basePath);
		File[] childFileList = file.listFiles();

		try
		{
			for (File childFile : childFileList)
			{
				if (childFile.isDirectory())
				{
					deleteAllFileInPath(childFile.getAbsolutePath()); // 하위 디렉토리
				}
				else
				{
					Log.i("childFile name : "+childFile.getAbsolutePath());
					childFile.delete(); // 하위 파일
				}
			}
			file.delete(); // root 삭제
		}catch(NullPointerException e)
		{
			
		}
		
	}
}
