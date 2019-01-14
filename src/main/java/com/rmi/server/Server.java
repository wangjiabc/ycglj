package com.rmi.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;


import com.alibaba.fastjson.JSONObject;

public interface Server {

	public String helloWorld(String name);
	
}
