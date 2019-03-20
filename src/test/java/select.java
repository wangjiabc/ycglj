import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ycglj.manage.dao.LicenseDAO;
import com.ycglj.manage.dao.OrderDAO;
import com.ycglj.manage.dao.UserDAO;
import com.ycglj.manage.daoModel.Users;
import com.ycglj.manage.tools.MyTestUtil;
import com.ycglj.sqlserver.context.Connect;

public class select {
static ApplicationContext applicationContext=new Connect().get();
	
	static UserDAO userDao=(UserDAO) applicationContext.getBean("userdao");
	
	OrderDAO orderDao=(OrderDAO) applicationContext.getBean("orderdao");
	
	static LicenseDAO licenseDAO=(LicenseDAO) applicationContext.getBean("licensedao");
	
	public static void main(String[] args) {
		
		
		
		Map map=userDao.getAllUser(100, 0, null, null, new HashedMap());
	
		long startTime=System.currentTimeMillis();   //获取开始时间
		
		List list=(List) map.get("data");
		
		//JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(list));
		
		//MyTestUtil.print(jsonArray);
		
		String returnXml="";
		//转换成xml的List
		Iterator iterator=  list.iterator();
		int i=0;
		returnXml = "<head>\n<code>1</code>\n<message>成功</message>\n<body>\n";
		while(iterator.hasNext()){
			Users u = (Users) iterator.next();
						returnXml += "<"+u.getId_number()+">"+">";
						returnXml += "<"+u.getName()+">"+">";
						returnXml += "<"+u.getOpen_id()+">"+">";
						returnXml += "<"+u.getCausa()+">"+">";
						returnXml += "<"+u.getPhone()+">"+">";
						returnXml += "<"+u.getSex()+">"+">";
						returnXml += "<"+u.getCard_type()+">"+">\n";
			returnXml += "</body>";
		}
		
		System.out.println(returnXml);

		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("xml解析速度测试结果:");
        System.out.println("解析共使用时间："+(endTime-startTime)+"ms");  
        
        
		
	}
}
