package com.ycglj.manage.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;

public class Base64Test {
	//图片转化成base64字符串  
    public static String getImageStr(String imgFile)  
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
        InputStream in = null;  
        byte[] data = null;  
        //读取图片字节数组  
        try   
        {  
            in = new FileInputStream(imgFile);          
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        //对字节数组Base64编码  
        byte[] b=Base64.encodeBase64(data);
        String s=new String(b); 
        return s;       //返回Base64编码过的字节数组字符串  
    }  
}
