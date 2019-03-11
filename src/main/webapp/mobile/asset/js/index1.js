var campusId = 1; //公众号id

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
}


var code = getQueryString("code");
var state = getQueryString("state");
try {
				document.querySelector('#newmap').onclick = function () {
					wx.getLocation({
						success: function (res) {
							alert(JSON.stringify(res));
							var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
							// $("#latitude").val(latitude);
							var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
							// $("#longitude").val(longitude);
							var speed = res.speed; // 速度，以米/每秒计
							// $("#speed").val(speed);
							var accuracy = res.accuracy; // 位置精度
							// $("#accuracy").val(accuracy);

							$.get("/ycglj/mobile/map/baiduSwitch.do", { //微信地理位置坐标转换成百度地图坐标
								longitude: longitude,
								latitude: latitude
							}, function (text) {
								var obj = $.parseJSON(text);
								var result = obj.result;
								var lat = result[0].y;
								var lng = result[0].x;
								location.href = "../../asset/onlineregs/new/index.html?latitude=" + lat + "&longitude=" + lng;
							});

						},
						cancel: function (res) {
							alert('用户拒绝授权获取地理位置');
						}
					});
				}
			} catch (ex) {

			}
// $.get("/ycglj/oauth/test.do", {
// 	campusId: campusId
// }, function (data) {
// 	// alert("data="+data);
// 	if (data == "false") {
// 		// alert("false");
// 		if (code != null) {
// 			$.get("/ycglj/oauth/getUserInfo.do", {
// 				code: code,
// 				state: state,
// 				campusId: campusId
// 			}, function (text) {
// 				var obj = $.parseJSON(text);
// 				$("#brand").html(obj.campusName);
// 				$(".headimgUrl").attr("src", obj.headimgUrl);
// 				$(".nickName").html(obj.nickName);

// 				url = document.location.toString();
// 				console.log(url);

// 				$.ajax({
// 					url: "../../../wechat/sign.do",
// 					data: {
// 						campusId: campusId,
// 						url: url
// 					},
// 					async: false,
// 					type: "GET",
// 					success: function (data) {
// 						var ticket = JSON.parse(data);
// 						wx.config({
// 							debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
// 							appId: ticket.appId,
// 							timestamp: ticket.timestamp,
// 							nonceStr: ticket.nonceStr,
// 							signature: ticket.signature,
// 							jsApiList: ['checkJsApi', 'onMenuShareTimeline',
// 								'onMenuShareAppMessage', 'onMenuShareQQ',
// 								'onMenuShareWeibo', 'hideMenuItems',
// 								'showMenuItems', 'hideAllNonBaseMenuItem',
// 								'showAllNonBaseMenuItem', 'translateVoice',
// 								'startRecord', 'stopRecord', 'onRecordEnd',
// 								'playVoice', 'pauseVoice', 'stopVoice',
// 								'uploadVoice', 'downloadVoice', 'chooseImage',
// 								'previewImage', 'uploadImage', 'downloadImage',
// 								'getNetworkType', 'openLocation', 'getLocation',
// 								'hideOptionMenu', 'showOptionMenu', 'closeWindow',
// 								'scanQRCode', 'chooseWXPay',
// 								'openProductSpecificView', 'addCard', 'chooseCard',
// 								'openCard']
// 						});
// 					}
// 				});

// 			});
// 		} else {
// 			location.href = redirectUrl;
// 		}
// 	} else {
// 		$.get("/ycglj/oauth/getUserInfoByOpenId.do", {
// 			campusId: campusId
// 		}, function (text) {
// 			var obj = $.parseJSON(text);

// 			$("#brand").html(obj.campusName);
// 			$(".headimgUrl").attr("src", obj.headimgUrl);
// 			$(".nickName").html(obj.nickName);


// 			url = document.location.toString();

// 			$.ajax({
// 				url: "../../../wechat/sign.do",
// 				data: {
// 					campusId: campusId,
// 					url: url
// 				},
// 				async: false,
// 				type: "GET",
// 				success: function (data) {
// 					var ticket = JSON.parse(data);
// 					wx.config({
// 						debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
// 						appId: ticket.appId,
// 						timestamp: ticket.timestamp,
// 						nonceStr: ticket.nonceStr,
// 						signature: ticket.signature,
// 						jsApiList: ['checkJsApi', 'onMenuShareTimeline',
// 							'onMenuShareAppMessage', 'onMenuShareQQ',
// 							'onMenuShareWeibo', 'hideMenuItems',
// 							'showMenuItems', 'hideAllNonBaseMenuItem',
// 							'showAllNonBaseMenuItem', 'translateVoice',
// 							'startRecord', 'stopRecord', 'onRecordEnd',
// 							'playVoice', 'pauseVoice', 'stopVoice',
// 							'uploadVoice', 'downloadVoice', 'chooseImage',
// 							'previewImage', 'uploadImage', 'downloadImage',
// 							'getNetworkType', 'openLocation', 'getLocation',
// 							'hideOptionMenu', 'showOptionMenu', 'closeWindow',
// 							'scanQRCode', 'chooseWXPay',
// 							'openProductSpecificView', 'addCard', 'chooseCard',
// 							'openCard']
// 					});
// 				}
// 			});
			
// 		});
// 	}
// });
