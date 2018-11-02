package com.ycglj.weixin.insweptcontroller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.sql.ast.statement.SQLIfStatement.Else;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.User_Data;
import com.ycglj.manage.file.ImageFileFactory;
import com.ycglj.manage.model.Photo;
import com.ycglj.manage.service.PhotoService;
import com.ycglj.manage.service.UserService;
import com.ycglj.manage.service.WeiXinService;
import com.ycglj.manage.singleton.Singleton;
import com.ycglj.manage.tools.CopyFile;
import com.ycglj.manage.tools.FileConvect;
import com.ycglj.manage.tools.FileTypeTest;
import com.ycglj.manage.tools.Md5;
import com.ycglj.sqlserver.context.Connect;
import com.ycglj.weixin.base.AutoAccessToken;

import sun.misc.BASE64Decoder;  
   
  
@Controller  
@RequestMapping("/mobile/file")  
public class FileUploadController {  

	private WeiXinService weixinService;
	
	private PhotoService photoService;
	
	@Autowired
	public void setweixinService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	@Autowired
	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
    @RequestMapping(value="/upload",method=RequestMethod.GET)  
    public @ResponseBody Map<String, Object> fildUpload(HttpServletRequest request,ServletResponse response, 
    		@RequestParam String dataType,@RequestParam String serverId,  
    		@RequestParam Integer campusId,
    		@RequestParam String classType)throws Exception{  
    	HttpServletRequest hrequest = (HttpServletRequest)request;
    	String accessToken;
    	
        //获得物理路径webapp所在路径  
        String pathRoot = request.getSession().getServletContext().getRealPath("");  
        String path="/mobile/photo/";  
        String filePath=pathRoot+path;  
        
        HttpSession session = request.getSession();
        
        String openId=session.getAttribute("openId").toString();

        Object objectClass = null;
        
         File savePath = new File(filePath);//创建新文件  
         System.out.println("filePath="+filePath);
         if (!savePath.exists()) {   
             savePath.mkdir();   
         }  
 
         final long l = System.currentTimeMillis();
         final int i = (int)( l % 100 );
         String uname = i+serverId;//获取文件名  
         try {  
             File file = new File(savePath+"//"+uname);//创建新文件  
             if(file!=null && !file.exists()){  
                 file.createNewFile();  
             }  
             OutputStream oputstream = new FileOutputStream(file); 
             
             accessToken=AutoAccessToken.get(weixinService, campusId);
             
             String photoUrl="https://api.weixin.qq.com/cgi-bin/media/get?access_token="+accessToken+"&media_id="+serverId;
             System.out.println("photoUrl="+photoUrl);
             URL url = new URL(photoUrl);  
             HttpURLConnection uc = (HttpURLConnection) url.openConnection();  
             uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true  
             uc.connect();  
             InputStream iputstream = uc.getInputStream();  
             System.out.println("file size is:"+uc.getContentLength());//打印文件长度  
             byte[] buffer = new byte[4*1024];  
             int byteRead = -1;     
             while((byteRead=(iputstream.read(buffer)))!= -1){  
                 oputstream.write(buffer, 0, byteRead);  
             }  
             oputstream.flush();    
             iputstream.close();  
             oputstream.close();  
         
            
             
             file = new File(savePath+"//"+uname); 
             
         //mime type 检测图片文件类型
         String mimeType="";
         Map<String,String> map=FileTypeTest.getFileType();
         Iterator<Map.Entry<String, String>> entryiterator = map.entrySet().iterator();
         String filetypeHex = String.valueOf(FileTypeTest.getFileHexString(file));
         while (entryiterator.hasNext()) {
             Map.Entry<String,String> entry =  entryiterator.next();
             String fileTypeHexValue = entry.getValue();
             if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                 mimeType=entry.getKey();
                 break;
             }
         }
        
        File file2 = new File(savePath+"//"+uname+"."+mimeType); 
        file.renameTo(file2);
        
        List<String> names=new ArrayList<>();
        names.add(dataType+"."+mimeType);
        
        List<byte[]> files=new ArrayList<>();
        byte[] fileByte=FileConvect.fileToByte(file2);
        files.add(fileByte);
        
        
        if(classType.equals("user")){
        	objectClass=User_Data.class;
        }
        
        Map map2=new ImageFileFactory().upload(objectClass,openId,dataType, names, files);
        
        String imageUrl="/ycglj/mobile/photo/"+uname+"."+mimeType;

        Date date=new Date();       
        
        Photo photo=new Photo();
        
        photo.setCampusId(campusId);
        photo.setOpenId(openId);
        photo.setImageUrl(imageUrl);
        photo.setCreateTime(date);
        
        photoService.insertPhtoByOpenId(photo);
        
        String uuid=(String) map2.get("uuid");
        
        Map<String, Object> result=new HashMap<>();
        
        result.put("state", 1);
        
        result.put("uuid", uuid);
        
        result.put("url", imageUrl);
        
        return result;  
      }catch (Exception e) {
		// TODO: handle exception
    	  e.printStackTrace();
    	  
    	  Map<String, Object> result=new HashMap<>();
    	  
    	  result.put("state", 2);
    	  
    	  return result;
	 }  
    }
    
    
    @RequestMapping(value="inputImage")
	public @ResponseBody Map uploadFilesSpecifyPath(@RequestParam("file") String file,HttpServletRequest request,HttpServletResponse response) throws Exception {  
		 long startTime=System.currentTimeMillis();   //获取开始时间  
		 
		 HttpSession session = request.getSession();
	        
	     String openId=session.getAttribute("openId").toString();

	     Map map=new HashMap<>();
	     

	         try {

	                   map=uploadImage(request, "/upload/", file,openId);

	    

	                //上传成功

	                    return map;
	                 //   Constants.printJson(response, fileName);;

	            } catch (Exception e) {
	                e.printStackTrace();
	              //  Constants.printJson(response, "上传出现异常！异常出现在：class.UploadController.insert()");
	            }

	    
		 
		 
	        long endTime=System.currentTimeMillis(); //获取结束时间  
	        System.out.println("上传文件共使用时间："+(endTime-startTime));  
	        
	        map.put("state", 2);
	       	  
	       	return map;
 
	} 
	
    /**
     * 上传图片
     *  原名称
     * @param request 请求
     * @param path_deposit 存放位置(路径)
     * @param file 文件
     * @param isRandomName 是否随机名称
     * @return 完整文件路径
     */
    public  Map uploadImage(HttpServletRequest request,String path_deposit,String file,String ID) {
        
    	//获得物理路径webapp所在路径  
        String pathRoot = System.getProperty("user.home");
       //存放图片文件的路径
        String path="/mobile/photo/";  
        String filePath=pathRoot+path;  
    	
        Map<String, Object> result=new HashMap<>();
        
    	//上传
        try {

            if(file!=null){
            	
            			UUID uuid=UUID.randomUUID();

                        //判断是否存在目录
                        File savePath = new File(pathRoot+Singleton.filePath);//创建新文件  
                        System.out.println("filePath="+Singleton.filePath);
                        if (!savePath.exists()) {   
                            savePath.mkdir();   
                        }  
               		
                        String mimeType="";
                        Map<String,String> map=FileTypeTest.getFileType();
                        Iterator<Map.Entry<String, String>> entryiterator = map.entrySet().iterator();
                        
                        String fileName=Md5.GetMD5Code(uuid.toString())+new Date().getTime();
                        
                        //上传
                        try{

                        	OutputStream os = null;
                        	
                        	BASE64Decoder decoder = new BASE64Decoder();

                        	String str="data:image/png;base64,";
                        	
                        	String file2=file.replace(str, "");
                        	
                        	byte[] imgbyte = decoder.decodeBuffer(file2);// 解码Base64图片数据
                        	
                        	for (int i = 0; i < imgbyte.length; ++i) {
                				if (imgbyte[i] < 0) {// 调整异常数据
                					imgbyte[i] += 256;
                				}
                        	}
                        	
                        	File newFile = new File(savePath + "\\" + fileName+".png");// 创建新文件
                        	if (newFile != null && !newFile.exists()) {
                        		newFile.createNewFile();
                        	}

                        	os = new FileOutputStream(newFile);
                        	os.write(imgbyte);
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
                        	
                            
                            
                            String uri=fileName+"."+mimeType;
                            
                            User_Data user_Data=new User_Data();
                            user_Data.setOpen_id(ID);
                            user_Data.setUuid(uuid.toString());
                            user_Data.setData_type("sing");
                            user_Data.setType(mimeType);
                            user_Data.setAffirm(0);
                            user_Data.setDate(new Date());
                            user_Data.setUri(uri);
                            userDao.insertUserData(user_Data);
                            CopyFile.set(Singleton.ROOMINFOIMGPATH2, savePath+"\\"+fileName+"."+mimeType, fileName+"."+mimeType);
                          
                          
                        }catch (Exception e) {
							// TODO: handle exception
                        	e.printStackTrace();
						}

                        
                                               
                        result.put("state", 1);
                        
                        result.put("uuid", uuid.toString());
                        
                        result.put("imgUrl", fileName);
                        
                        return result;
         
                }
            result.put("state", 2);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.put("state", 2);
            return result;
        }
    }
    
    public static Integer fildUpload2(HttpServletRequest request,ServletResponse response, 
    		String dataType,String serverId,Integer campusId,String id,
    		String classType,WeiXinService weixinService,
    		PhotoService photoService)throws Exception{  
    	HttpServletRequest hrequest = (HttpServletRequest)request;
    	String accessToken;
    	
        //获得物理路径webapp所在路径  
        String pathRoot = request.getSession().getServletContext().getRealPath("");  
        String path="/mobile/photo/";  
        String filePath=pathRoot+path;  
        
        Object objectClass = null;
        
         File savePath = new File(filePath);//创建新文件  
         System.out.println("filePath="+filePath);
         if (!savePath.exists()) {   
             savePath.mkdir();   
         }  
 
         final long l = System.currentTimeMillis();
         final int i = (int)( l % 100 );
         String uname = i+serverId;//获取文件名  
         try {  
             File file = new File(savePath+"//"+uname);//创建新文件  
             if(file!=null && !file.exists()){  
                 file.createNewFile();  
             }  
             OutputStream oputstream = new FileOutputStream(file); 
             
             accessToken=AutoAccessToken.get(weixinService, campusId);
             
             String photoUrl="https://api.weixin.qq.com/cgi-bin/media/get?access_token="+accessToken+"&media_id="+serverId;
             System.out.println("photoUrl="+photoUrl);
             URL url = new URL(photoUrl);  
             HttpURLConnection uc = (HttpURLConnection) url.openConnection();  
             uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true  
             uc.connect();  
             InputStream iputstream = uc.getInputStream();  
             System.out.println("file size is:"+uc.getContentLength());//打印文件长度  
             byte[] buffer = new byte[4*1024];  
             int byteRead = -1;     
             while((byteRead=(iputstream.read(buffer)))!= -1){  
                 oputstream.write(buffer, 0, byteRead);  
             }  
             oputstream.flush();    
             iputstream.close();  
             oputstream.close();  
         
            
             
             file = new File(savePath+"//"+uname); 
             
         //mime type 检测图片文件类型
         String mimeType="";
         Map<String,String> map=FileTypeTest.getFileType();
         Iterator<Map.Entry<String, String>> entryiterator = map.entrySet().iterator();
         String filetypeHex = String.valueOf(FileTypeTest.getFileHexString(file));
         while (entryiterator.hasNext()) {
             Map.Entry<String,String> entry =  entryiterator.next();
             String fileTypeHexValue = entry.getValue();
             if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                 mimeType=entry.getKey();
                 break;
             }
         }
        
        File file2 = new File(savePath+"//"+uname+"."+mimeType); 
        file.renameTo(file2);
        
        List<String> names=new ArrayList<>();
        names.add(dataType+"."+mimeType);
        
        List<byte[]> files=new ArrayList<>();
        byte[] fileByte=FileConvect.fileToByte(file2);
        files.add(fileByte);
        /*
        if(classType.equals("hidden")){
        	objectClass=Hidden_Data.class;
        }else if(classType.equals("check")){
        	objectClass=Hidden_Check_Date.class;
        }else if(classType.equals("neaten")){
        	objectClass=Hidden_Neaten_Date.class;
        }else if(classType.equals("roomInfo")){
        	objectClass=FileSelfBelong.class;
        }
        */
        new ImageFileFactory().upload(objectClass,id,dataType, names, files);
        
        String imageUrl="/ycglj/mobile/photo/"+uname+"."+mimeType;
        String openId=( String ) hrequest.getSession().getAttribute("openId");
        Date date=new Date();       
        
        Photo photo=new Photo();
        
        photo.setCampusId(campusId);
        photo.setOpenId(openId);
        photo.setImageUrl(imageUrl);
        photo.setCreateTime(date);
        
        photoService.insertPhtoByOpenId(photo);
        
        return 1;  
      }catch (Exception e) {
		// TODO: handle exception
    	  e.printStackTrace();
    	  return 0;
	 }  
    }
    
}  

