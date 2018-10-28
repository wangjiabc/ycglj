var campusId=1; //公众号id

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
   }


var code=getQueryString("code");
var state=getQueryString("state");


var valid1,valid2,valid3;
valid1=valid2=valid3=true;

$.get("/ycglj/oauth/test.do", { 
	 campusId:campusId
  }, function(data) {
	//   alert("data="+data);

 if(data=="false"){
	//   alert("false");
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

			    	 /*
		      	      * 此处需要两次执行相同的函数，否则回调后不能执行以下函数
		      	      */ 
			    	 
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
 		       	     /*
 		     	      * 此处需要两次执行相同的函数，否则返回ture时不能执行以下函数
 		     	      */  
 		       	          
 			    	 
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



 wx.ready(function () {
	$('.name input').focus(function(){
		 $('#w1').attr("class","weui_cell");
	 });
	 $('.name input').blur(function(){
	     var reque=$('.name input')[0].value;
	   	$.post('/ycglj/mobile/assetRegister/testName.do',{
	   	   name:reque
	   	   },function(text){ 
	   		var obj=JSON.parse(text);
			 data=obj.data;
	    	if(data=='succeed'){
	    		valid1=true;
	     	}else{
	     		$("#dialog2").attr("style","display:block"); 
	 			$(".weui_dialog_title").html(data);                     
	 			$('#w1').attr("class","weui_cell weui_cell_warn");
	 			valid1=false;
	   	   }
	     });
	 });
	 $('.phone input').focus(function(){
		 $('#w2').attr("class","weui_cell");
	 });
	 $('.phone input').blur(function(){
	     var reque=$('.phone input')[0].value;
	   	$.post('/ycglj/mobile/assetRegister/testPhone.do',{
	   		telephone:reque
	   	   },function(text){ 
	   		var obj=JSON.parse(text);
			 data=obj.data;
	    	if(data=='succeed'){
	    		valid2=true;
	     	}else{
	     		$("#dialog2").attr("style","display:block"); 
	 			$(".weui_dialog_title").html(data);
	 			$('#w2').attr("class","weui_cell weui_cell_warn");
	 			valid2=false;
	   	   }
	     });
	 });

	
	 
	 $.post('/ycglj/mobile/assetRegister/userByopenId.do', {
			campusId:campusId
	       },function(data){
	    	   var obj = $.parseJSON(data);
	    	   console.log(obj);
	    	   if(obj.name!=null)
	    	   $("#name").attr("placeholder",obj.name);
	    	   if(obj.phone!=null)
	    	   $("#phone").attr("placeholder",obj.phone);
	       });
	 
	document.querySelector('#showTooltips').onclick =function(){
		if(valid1==false||valid2==false||valid3==false){
			$("#dialog2").attr("style","display:block"); 
			$(".weui_dialog_title").html("请修改输入错误字段");
			return;
		}
		var name=document.getElementById("name").value;
		var phone=document.getElementById("phone").value;
		var regtlx=document.getElementById("regtlx").value;
		if(campusId!=null&&typeof(campusId)!="undefined"){
			$.post('/ycglj/mobile/assetRegister/insert.do', {
				name:name,
				phone:phone,
				regtlx:regtlx
			},function(data){		
              if(data==1){
           		$("#dialog2").attr("style","display:block"); 
   	 			$(".weui_dialog_title").html("操作成功");
   	 			top.location.href="index.html";
              }else if(data==2){
           	   $("#dialog2").attr("style","display:block"); 
  	 			  $(".weui_dialog_title").html("验证码错误");
              }else if(data==3){
      			$("#dialog2").attr("style","display:block"); 
    			$(".weui_dialog_title").html("验证码已过期");
              }else{
           	   $("#dialog2").attr("style","display:block"); 
   	 			  $(".weui_dialog_title").html("操作失败");
              }	
			},"json");
		}
	 }
	  
   }); //wx.ready