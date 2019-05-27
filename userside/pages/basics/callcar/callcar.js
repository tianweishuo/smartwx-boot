// pages/basics/CallCat/CallCat.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    latitude: 0,
    longitude: 0,
    openSetting:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    var that = this;
    wx.getLocation({
      type: 'gcj02',
      success(res) {
        that.setData({
          openSetting: ""
        })
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude
        })
      },
      fail:function(){
        console.log("拒绝");
        that.setData({
          openSetting: "openSetting"
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {
  },
  /**
   * 一键叫车
   */
  callCar: function() {
    console.log("叫车");
    var that = this;
    wx.getSetting({
      success(res) {
        console.log(JSON.stringify(res));
        if (res.authSetting['scope.userLocation']) {
          // 地图选择
          wx.chooseLocation({
            success: function(res) {
              console.log(JSON.stringify(res));
              wx.navigateTo({
                url:"/pages/waitforcar/waitforcar"
              })
            }
          })
        }
      }
    })
    }
})