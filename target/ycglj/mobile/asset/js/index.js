var campusId=1; //公众号id

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
   }


var code=getQueryString("code");
var state=getQueryString("state");

 $.get("/ycglj/oauth/test.do", { 
	 campusId:campusId
  }, function(data) {
	  // alert("data="+data);
  if(data=="false"){
	  // alert("false");
     if(code!=null){   	  
          $.get("/ycglj/oauth/getUserInfo.do", {
             code:code,
             state:state,
              campusId:campusId
           }, function(text) {
        	  var obj = $.parseJSON(text);          
        	 $("#brand").html(obj.campusName);
        	 $(".headimgUrl").attr("src",obj.headimgUrl);
        	 $(".nickName").html(obj.nickName);
	 
        	url=document.location.toString();
        	
        	$.ajax({
			     url : "../../wechat/sign.do",
			     data : {
			    	 campusId:campusId,
			    	 url:url
			     },
			     async: false,
			     type : "GET",
			     success : function(data) {
			    	 var ticket=JSON.parse(data);
			    	 wx.config({
			    		  debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				      		appId: ticket.appId,
				      		timestamp: ticket.timestamp,
				      		nonceStr: ticket.nonceStr,
				      		signature: ticket.signature,
				      		jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
				                            'onMenuShareAppMessage', 'onMenuShareQQ',
				                            'onMenuShareWeibo', 'hideMenuItems',
				                            'showMenuItems', 'hideAllNonBaseMenuItem',
				                            'showAllNonBaseMenuItem', 'translateVoice',
				                            'startRecord', 'stopRecord', 'onRecordEnd',
				                            'playVoice', 'pauseVoice', 'stopVoice',
				                            'uploadVoice', 'downloadVoice', 'chooseImage',
				                            'previewImage', 'uploadImage', 'downloadImage',
				                            'getNetworkType', 'openLocation', 'getLocation',
				                            'hideOptionMenu', 'showOptionMenu', 'closeWindow',
				                            'scanQRCode', 'chooseWXPay',
				                            'openProductSpecificView', 'addCard', 'chooseCard',
				                            'openCard' ]
				      	}); 
			     }
        	});
           
        });
     }else{
  	   location.href=redirectUrl;
      }
    }else{
    	$.get("/ycglj/oauth/getUserInfoByOpenId.do",{
 		   campusId:campusId
 	   },function(text) {
       	  var obj = $.parseJSON(text);

       	 $("#brand").html(obj.campusName);
       	 $(".headimgUrl").attr("src",obj.headimgUrl);
     	 $(".nickName").html(obj.nickName);
       	  

 			 url=document.location.toString();

 			 $.ajax({
 			     url : "../../wechat/sign.do",
 			     data : {
 			    	 campusId:campusId,
 			    	 url:url
 			     },
 			     async: false,
 			     type : "GET",
 			     success : function(data) {
 			    	 var ticket=JSON.parse(data);
 			    	 wx.config({
 			    		debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
 			      		appId: ticket.appId,
 			      		timestamp: ticket.timestamp,
 			      		nonceStr: ticket.nonceStr,
 			      		signature: ticket.signature,
 			      		jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
 			                            'onMenuShareAppMessage', 'onMenuShareQQ',
 			                            'onMenuShareWeibo', 'hideMenuItems',
 			                            'showMenuItems', 'hideAllNonBaseMenuItem',
 			                            'showAllNonBaseMenuItem', 'translateVoice',
 			                            'startRecord', 'stopRecord', 'onRecordEnd',
 			                            'playVoice', 'pauseVoice', 'stopVoice',
 			                            'uploadVoice', 'downloadVoice', 'chooseImage',
 			                            'previewImage', 'uploadImage', 'downloadImage',
 			                            'getNetworkType', 'openLocation', 'getLocation',
 			                            'hideOptionMenu', 'showOptionMenu', 'closeWindow',
 			                            'scanQRCode', 'chooseWXPay',
 			                            'openProductSpecificView', 'addCard', 'chooseCard',
 			                            'openCard' ]
 			      	});
 			     }
 			 });
       });
    }
  });
