package com.ycglj.manage.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import net.coobird.thumbnailator.Thumbnails;

public class CopyFile {
	
	public static void set(String imgPath,String oldPath, String newPath) { 
		//判断是否存在目录
        File targetFile=new File(imgPath);
        //压缩文件目录
        File compressPath=new File(imgPath+"\\compressFile");
        
        if(!targetFile.exists()){
            targetFile.mkdirs();//创建目录
            System.out.println("目录不存在");
        }
        
        if(!compressPath.exists()){
        	compressPath.mkdirs();//创建目录
            System.out.println("压缩文件目录不存在");
        }
        
        try { 
            int bytesum = 0; 
            int byteread = 0; 
            File oldfile = new File(oldPath); 
            if (oldfile.exists()) { //文件存在时 
            	File newFile=new File(imgPath+"\\"+newPath);
            	
            	if(!newFile.exists()){
            		System.out.println("文件不存在");
            		InputStream inStream = new FileInputStream(oldPath); //读入原文件 
            		FileOutputStream fs = new FileOutputStream(imgPath+"\\"+newPath); 
            		byte[] buffer = new byte[1444]; 
            		int length; 
            		while ( (byteread = inStream.read(buffer)) != -1) { 
            			bytesum += byteread; //字节数 文件大小 
            			System.out.println(bytesum); 
            			fs.write(buffer, 0, byteread); 
            		} 
            		inStream.close(); 
            		fs.close();
            		System.out.println(imgPath+"\\"+newPath);
            	}else{
            		System.out.println("文件存在"); 
            	}
            } 
        } 
        catch (Exception e) { 
            System.out.println("复制单个文件操作出错"); 
            e.printStackTrace(); 
        } 
        
        /*
         * 创建压缩文件
         */
        try { 
            int bytesum = 0; 
            int byteread = 0; 
            File oldfile = new File(oldPath); 
            if (oldfile.exists()) { //文件存在时 
            	File compressFile=new File(imgPath+"\\compressFile\\"+newPath);
            	
            	if(!compressFile.exists()){
            		
            		System.out.println("压缩文件不存在");
            		
            		Thumbnails.of(oldfile).scale(0.35f).outputQuality(0.25f).toFile(imgPath+"\\compressFile\\"+newPath);
            		
            		System.out.println(imgPath+"\\compressFile\\"+newPath);
            		
            	}else{
            		System.out.println("压缩文件存在"); 
            	}
            } 
        } 
        catch (Exception e) { 
            System.out.println("压缩文件操作出错"); 
            e.printStackTrace(); 
        } 
        
        
    } 
	
}
