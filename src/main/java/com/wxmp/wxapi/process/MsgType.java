/*
 * FileName：MsgType.java 
 * <p>
 * Copyright (c) 2017-2020, <a href="http://www.webcsn.com">hermit (794890569@qq.com)</a>.
 * <p>
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl-3.0.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.wxmp.wxapi.process;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息类型：所有微信涉及到的消息类型统一管理
 */
@Slf4j
public enum MsgType {

	Text("text","文本消息"),//文本消息
	News("news","图文消息"),//图文消息
	Location("location","地理位置消息"),//地理位置消息
	Image("image","图片消息"),//图片消息
	Voice("voice","语音消息"),//语音消息
	Video("video","视频消息"),//视频消息
	Event("event","事件消息"),//事件消息

	MPNEWS("mpnews","群发图文消息"),//群发图文消息
	SUBSCRIBE("subscribe","订阅消息"),//订阅消息
	UNSUBSCRIBE("unsubscribe","取消订阅"),//取消订阅

	LINK("link","链接消息");
	
	public String name;
	public String value;
	
	MsgType(String name,String value) {
	     this.name = name;
	     this.value = value;
	}

	public static String getValue(String name) {
		MsgType[] msgTypes = values();
		for (MsgType msgType : msgTypes) {
			if (msgType.name.equals(name)) {
				return msgType.value;
			}
		}
		return null;
	}


	@Override
	public String toString() {
		return this.name;
	}

	public static void main(String[] args){

		System.out.print(getValue("text"));
	}
}
