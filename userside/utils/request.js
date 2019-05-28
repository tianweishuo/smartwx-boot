var app = new getApp();
//项目URL相同部分,减轻代码量,同事方便项目迁移
var host = 'http://127.0.0.1:8080';

/**
*POST请求,URL:接口 postData 参数,json类型 doSuccess:成功的回调函数  doFail:失败回调函数
*/
function request(url,postData,doSuccess,doFail){
	wx.request({
		url:host+url,
		data:postData,
		header:{
			"content-type": "application/json;charset=UTF-8"
		},
    dataType:'json',
		method:'POST',
		dataType:true,
		success:function(res){
      doSuccess(JSON.parse(res.data));
		},
		fail:function(){
			doFail();
		}
	})
}

//GET请求,不需参数,直接URL调用
 function getData(url,doSuccess,doFail){
 	wx.request({
 		url:host+url,
 		header:{
 			"content-type":"application/json;charset=urf-8",
 		},
 		method:'GET',
 		success:function(res){
 			doSuccess(res.data);
 		},
 		fail:function(){
 			doFail();
 		}
 	})
 }

 module.exports.request = request;
 module.exports.getData = getData;