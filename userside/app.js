//app.js
var utils = require('/utils/util.js')

App({
  onLaunch: function() {
    wx.getSystemInfo({
      success: e => {
        this.globalData.StatusBar = e.statusBarHeight;
        let custom = wx.getMenuButtonBoundingClientRect();
        this.globalData.Custom = custom;
        this.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
      }
    })


    //校验登陆是否失效
    wx.checkSession({
      success() {
        console.log("登陆有效");
      },
      fail() {
        console.log("登陆失效,进行登陆");
        wx.login({
          success(res) {
            wx.request({
              url: utils.serviceUrl + '/wxck/login', // 仅为示例，并非真实的接口地址
              header: {
                'content-type': 'application/json' // 默认值
              },
              data: {
                code: res.code
              },
              method: 'post',
              success(res) {
                console.log("用户登陆返回信息" + res.data)
              }
            })

          }
        })
      }
    })


    wx.getSetting({
      success(res) {
        console.log(res);
      }
    })

    // wx.getUserInfo({
    //   success(res) {
    //     console.log("获取用户信息");
    //     console.log(JSON.stringify(res))
    //   }
    // })

  },
  onShow:function(){
    wx.openSetting({
      success(res) {
        console.log("----");
        console.log(res)
      }
    })
  },
  globalData: {
    ColorList: [{
        title: '嫣红',
        name: 'red',
        color: '#e54d42'
      },
      {
        title: '桔橙',
        name: 'orange',
        color: '#f37b1d'
      },
      {
        title: '明黄',
        name: 'yellow',
        color: '#fbbd08'
      },
      {
        title: '橄榄',
        name: 'olive',
        color: '#8dc63f'
      },
      {
        title: '森绿',
        name: 'green',
        color: '#39b54a'
      },
      {
        title: '天青',
        name: 'cyan',
        color: '#1cbbb4'
      },
      {
        title: '海蓝',
        name: 'blue',
        color: '#0081ff'
      },
      {
        title: '姹紫',
        name: 'purple',
        color: '#6739b6'
      },
      {
        title: '木槿',
        name: 'mauve',
        color: '#9c26b0'
      },
      {
        title: '桃粉',
        name: 'pink',
        color: '#e03997'
      },
      {
        title: '棕褐',
        name: 'brown',
        color: '#a5673f'
      },
      {
        title: '玄灰',
        name: 'grey',
        color: '#8799a3'
      },
      {
        title: '草灰',
        name: 'gray',
        color: '#aaaaaa'
      },
      {
        title: '墨黑',
        name: 'black',
        color: '#333333'
      },
      {
        title: '雅白',
        name: 'white',
        color: '#ffffff'
      },
    ]
  }
})