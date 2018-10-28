package com.ycglj.weixin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ycglj.weixin.base.CommonUtil;


@Controller
@RequestMapping("/mobile/map")
public class WechatMapController {

	@RequestMapping("/baiduSwitch")
	public @ResponseBody JSONObject baiduSwitch(HttpServletRequest request,@RequestParam String longitude,
			@RequestParam String latitude){
		JSONObject jsonObject=null;
		String requestUrl = "http://api.map.baidu.com/geoconv/v1/?coords="+longitude+","+latitude+"&from=1&to=5&ak=pQFgFpS0VnMXwCRN6cTc1jDOcBVi3XoD";
		
	   HttpClient client = new DefaultHttpClient();
	        HttpGet httpget = new HttpGet(requestUrl);
	        HttpResponse response;
			try {
				response = client.execute(httpget);
				InputStream is = response.getEntity().getContent();
		        String result = inStream2String(is);
		        jsonObject=JSONObject.parseObject(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	       		
		return jsonObject;
		
	}
	
	// 将输入流转换成字符串
    private static String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray(), "UTF-8");
    }
	
}
