package com.ycglj.manage.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ycglj.manage.dao.UserDAO;
import org.springframework.context.ApplicationContext;

import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.daoModel.FileSelfBelong;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.manage.tools.CopyFile;
import com.ycglj.manage.tools.FileTypeTest;
import com.ycglj.manage.tools.Md5;
import com.ycglj.sqlserver.context.Connect;

public abstract class AbstractFileUpload {

	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	public AbstractFileUpload() {
		// TODO Auto-generated constructor stub
		
	}
	
	public Map<String,Object> uploadFile(Object object,String ID,String dataType,String license,List<String> names, List<byte[]> files,Integer currently) {
        String pathRoot = System.getProperty("user.home");
               
        BufferedOutputStream os=null;

      //mime type 检测文件类型
        String mimeType="";
        Map<String,String> map=FileTypeTest.getFileType();
        Iterator<Map.Entry<String, String>> entryiterator = map.entrySet().iterator();
       
         File savePath = new File(pathRoot+Singleton.filePath);//创建新文件  
         System.out.println("filePath="+Singleton.filePath);
         if (!savePath.exists()) {   
             savePath.mkdir();   
         }  
		
       Iterator<byte[]> iterator=files.iterator();

       Map<String, Object> result=new HashMap<>();
       
       int i=0;
       try {  
         while(iterator.hasNext()){
        	 String name=names.get(i);
        	 byte[] file=iterator.next();
             File newFile = new File(savePath+"\\"+name);//创建新文件  
             if(newFile!=null && !newFile.exists()){  
                 newFile.createNewFile();  
             }  
             
             os = new BufferedOutputStream(new FileOutputStream(newFile));
             os.write(file);
             os.flush();  
             os.close();  
                     
             String filetypeHex = String.valueOf(FileTypeTest.getFileHexString(newFile));
             while (entryiterator.hasNext()) {
                 Map.Entry<String,String> entry =  entryiterator.next();
                 String fileTypeHexValue = entry.getValue();
                 if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                     mimeType=entry.getKey();
                     break;
                 }
              }
          System.out.println("mimeType="+mimeType);

            String s=name;
          	mimeType=s.substring(s.lastIndexOf('.')+1); //获取后缀名
       
            System.out.println("mimeType="+mimeType); 
     		UUID uuid=UUID.randomUUID();		
     		Date date=new Date();
     		
     		String fileName=Md5.GetMD5Code(uuid.toString())+date.getTime();
         
            File newFile2 = new File(savePath+"\\"+fileName+"."+mimeType); 
            System.out.println("newFile="+newFile.getName());
            newFile.renameTo(newFile2);
            System.out.println("newFile2="+newFile2.getName());
            String uri=fileName+"."+mimeType;
            System.out.println("uri="+savePath+"\\"+fileName+"."+mimeType);
            System.out.println("ID="+ID);
            System.out.println(name);
            System.out.println(mimeType);
            System.out.println(uri);
            
            System.out.println("object="+object);
            
            if(object==User_Data.class){          	
             //cpoy到资产管理FTP目录
              User_Data user_Data=new User_Data();
              user_Data.setOpen_id(ID);
              user_Data.setUuid(uuid.toString());
              user_Data.setData_type(dataType);
              user_Data.setType(mimeType);
              user_Data.setAffirm(0);
              user_Data.setDate(new Date());
              user_Data.setUri(uri);
              System.out.println("license="+license);
              if(license!=null&&!license.equals("")){
            	  user_Data.setLicense(license);
              }
              if(currently==1){
            	  user_Data.setCurrently(1);
              }
              userDao.insertUserData(user_Data);
              CopyFile.set(Singleton.ROOMINFOIMGPATH2, savePath+"\\"+fileName+"."+mimeType, fileName+"."+mimeType);
            }else if(object==FileSelfBelong.class){
            	
            	FileSelfBelong fileSelfBelong=new FileSelfBelong();
            	fileSelfBelong.setLicense(license);
        		fileSelfBelong.setUpFileFullName(uri);
        		fileSelfBelong.setFileType(mimeType);
            	fileSelfBelong.setDate(new Date());
            	
            	licenseDAO.insertIntoFileSelfBelong(fileSelfBelong);
                CopyFile.set(Singleton.ROOMINFOIMGPATH2, savePath+"\\"+fileName+"."+mimeType, fileName+"."+mimeType);
            }
            
            result.put("state", 1);
            
            result.put("uuid", uuid.toString());
            
            result.put("imgUrl", fileName+"."+mimeType);
            
            i++;
          }  

         return result;  
         
        }catch (Exception e) {
     		// TODO: handle exception
       	  e.printStackTrace();
       	  
       	  result.put("state", 2);
       	  
       	  return result;
   	    } 

	}
	
	protected abstract Map<String, Object> upload(Object object,String ID,String dataType,String license,List<String> names, List<byte[]> files);
}
