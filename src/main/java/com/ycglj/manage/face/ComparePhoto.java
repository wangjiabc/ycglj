package com.ycglj.manage.face;

import com.alibaba.fastjson.JSONException;
import com.baidu.aip.face.AipFace;

import com.baidu.aip.face.MatchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.ycglj.manage.face.util.Base64Util;
import com.ycglj.manage.face.util.FileUtil;


/**
 *
 * @author
 * 人脸对比
 */
public class ComparePhoto {

	  //设置APPID/AK/SK
    public static final String APP_ID = "15639373";
    public static final String API_KEY = "78cfIUtvRmsC407YpXz0uzAa";
    public static final String SECRET_KEY = "376Nxmf8GYGqrk3rVcpRHGc6xS9j8ZiY";
	
    public static List sample(String img1,String img2) throws JSONException, IOException {
        byte[] bytes = FileUtil.readFileByBytes(img1);
        String photo1 = Base64Util.encode(bytes);
        byte[] bytes2 = FileUtil.readFileByBytes(img2);
        String photo2 = Base64Util.encode(bytes2);
        String image1 = photo1;
        String image2 = photo2;

        // image1/image2也可以为url或facetoken, 相应的imageType参数需要与之对应。
        MatchRequest req1 = new MatchRequest(image1, "BASE64");
        MatchRequest req2 = new MatchRequest(image2, "BASE64");
        ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
        requests.add(req1);
        requests.add(req2);
        return requests;
    }

    
    public static JSONObject match(String img1,String img2){
    	// 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

     // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//      client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//      client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

      // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
      // 也可以直接通过jvm启动参数设置此环境变量
//      System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        
        JSONObject res = null;
        
        try {
			res = client.match(sample(img1,img2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return res;
    }

    public static void main(String[] args) throws JSONException, IOException, org.json.JSONException {

      // 调用接口
        
        String img1="C:\\Users\\WangJing\\Desktop\\tp\\y=2556591740,3797867438&fm=26&gp=0.jpg";
        String img2="C:\\Users\\WangJing\\Desktop\\tp\\y=3457785181,2740050746&fm=26&gp=0.jpg";
        
        JSONObject res = match(img1,img2);
        System.out.println(res.toString(2));
        
        try {
			JSONObject result=res.getJSONObject("result");
			Long score=result.getLong("score");
			
			System.out.println(score);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
