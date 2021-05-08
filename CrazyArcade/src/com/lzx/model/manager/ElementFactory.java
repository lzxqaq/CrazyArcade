package com.lzx.model.manager;


import com.lzx.model.load.ElementLoad;
import com.lzx.model.vo.Enemy;
import com.lzx.model.vo.Player;
import com.lzx.model.vo.SuperElement;

import java.util.List;
import java.util.Map;

/*
 * 任务：
 * 依据参数不同 自动读取资源 填充VO对象 存储到元素管理器
 * 工厂的作用 将比较复杂的构造方式进行封装
 */
public class ElementFactory {

	public static SuperElement elementFactory(String name) {
		Map<String, List<String> > playMap = ElementLoad.getInstance().getPlayMap();
		Map<String, List<String> > boxMap = ElementLoad.getInstance().getBoxMap();
		List<String> gamelist = ElementLoad.getInstance().getGameList(); 
		switch (name) {
		case "onePlayer":
			List<String> list = playMap.get(name);
			String string = list.get(0);//[playerA,playFire,150,300,40,40]
			return Player.createPlayer(string);
		case "twoPlayer":
			List<String> list1 = playMap.get(name);
			String string1 = list1.get(0);//[playerA,playFire,150,300,40,40]
			return Player.createPlayer(string1);
		case "enemy":
			String string2 = gamelist.get(gamelist.size()-1);
			return Enemy.createEnemy(string2);
		
		default:
			break;
		}
		return null;
	}
	
}
