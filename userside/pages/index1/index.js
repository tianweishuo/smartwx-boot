var call = require("../../utils/request.js");

Page({
  data: {
    PageCur: 'basics'
  },
  onShow: function() {
    //获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          //已授信,登陆
          wx.checkSession({
            success() {
              console.log("登陆有效");
              // wx.getUserInfo({
              //   withCredentials: true,
              //   success: function(res) {
              //     console.log(JSON.stringify(res));
              //     call.request("/wxck/login", res, function(res) {
              //       console.log(res);
              //     }, function() {
              //       console.log("发送失败");
              //     });
              //   }
              // })
            },
            fail() {
              console.log("登陆失效,进行登陆");
              wx.login({
                success(res) {
                  console.log(JSON.stringify(res));
                  call.request("/wxck/getWxSession", {
                      code: res.code
                    },
                    function(res) {
                      console.log(res);
                    },
                    function() {
                      console.log("服务器连接异常");
                    });
                // 
                }
              })
            }
          })
        } else {
          //未授权，跳转到授权页面
          // wx.navigateTo({
          //   url: '/pages/auth/auth',
          // })
        }
      }
    })
  },
  NavChange(e) {
    this.setData({
      PageCur: e.currentTarget.dataset.cur
    })
  }
})