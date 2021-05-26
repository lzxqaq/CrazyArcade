package com.lzx.model.load;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class ElementLoad {
	//工具类 单例模式
	//图片
	private Map<String, ImageIcon> imageMap;
	//合金弹头 Map<String,List<ImageIcon>>
	//主角 敌人
	private Map<String, List<String> > playMap;
	private Map<String, List<String> > enemyMap;
	//箱子
	private Map<String, List<String> > boxMap;
	private List<String> gameList;//游戏的流程控制 敌机兵力出现控制
	//敌机出现的顺序

	//pro文件读取对象
	Properties properties ;

	private static ElementLoad load;
	private ElementLoad(){
		imageMap = new HashMap<>();
		playMap = new HashMap<>();
		enemyMap = new HashMap<>();
		boxMap = new HashMap<>();
		gameList = new ArrayList<>();
		properties = new Properties();
	}
	public static synchronized ElementLoad getInstance(){
		if (load == null) {
			load = new ElementLoad();
		}
		return load;
	}
	public static void main(String[] args) {
		//getInstance().readPlayerPro();
		getInstance().readImagePro();
		//System.out.println("ASfa");
		//getInstance().readEnemyPro();
		//getInstance().readGamePro();
		//getInstance().readBoxPro();
	}
	public void readBoxPro() {
		Random r = new Random();

		String str = "com/lzx/pro/box"+(r.nextInt(2))+".pro";
		InputStream inputStream = ElementLoad.class
				.getClassLoader().getResourceAsStream(str);
		properties.clear();
		try {
			properties.load(inputStream);
			for (Object object : properties.keySet()) {
				String string = properties.getProperty(object.toString());
				List<String> boxs = new ArrayList<>();
				boxs.add(string);
				boxMap.put(object.toString(), boxs);
			}
			System.out.println(boxMap);
		}catch(Exception e){

		}
	}
	//读取流程
	public void readGamePro(){
		InputStream inputStream = ElementLoad.class
				.getClassLoader().getResourceAsStream("com/lzx/pro/GameRunA.pro");
		properties.clear();
		try {
			properties.load(inputStream);
			for (Object object : properties.keySet()) {
				String string = properties.getProperty(object.toString());
				gameList.add(string);
			}
			System.out.println(gameList);
		}catch(Exception e){

		}
	}
	//读取敌机配置
	public void readEnemyPro(){
		InputStream inputStream = ElementLoad.class
				.getClassLoader().getResourceAsStream("com/lzx/pro/GameRunA.pro");
		properties.clear();
		try {
			properties.load(inputStream);

			for (Object object : properties.keySet()) {
				String string = properties.getProperty(object.toString());
				String[] arr  = string.split(",");
				System.out.println(arr[arr.length-1]);
				List<String> enemys = new ArrayList<>();
				enemys.add(string);
				enemyMap.put(object.toString(), enemys);
			}
			System.out.println(enemyMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//读取主角配置
	public void readPlayerPro(){
		InputStream inputStream = ElementLoad.class
				.getClassLoader().getResourceAsStream("com/lzx/pro/play.pro");
		try {
			properties.clear();//清除上一次的数据
			properties.load(inputStream);

			for (Object object : properties.keySet()) {
				String string = properties.getProperty(object.toString());
				List<String> players = new ArrayList<>();
				players.add(string);
				playMap.put(object.toString(), players);
			}
			System.out.println(playMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//读取图片资源
	public void readImagePro(){
		//src目录 根目录
		InputStream inputStream = ElementLoad.class
				.getClassLoader().getResourceAsStream("com/lzx/pro/mapA.pro");
		try {
			properties.clear();
			properties.load(inputStream);
			Set<Object> set = properties.keySet();
			for (Object object : set) {
				String url = properties.getProperty((String) object);
				System.out.println(object+":"+url);
				imageMap.put(object.toString(), new ImageIcon(url));
			}
			System.out.println(imageMap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void readImageProB(){
		//src目录 根目录
		InputStream inputStream = ElementLoad.class
				.getClassLoader().getResourceAsStream("com/lzx/pro/mapB.pro");
		try {
			properties.load(inputStream);
			Set<Object> set = properties.keySet();
			for (Object object : set) {
				String url = properties.getProperty((String) object);
				System.out.println(object+":"+url);
				File file = new File(url);
				File[] arrFile = file.listFiles();
				System.out.println(Arrays.toString(arrFile));
			}
			System.out.println(imageMap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Map<String, ImageIcon> getImageMap() {
		return imageMap;
	}
	public Map<String, List<String>> getPlayMap() {
		return playMap;
	}
	public Map<String, List<String>> getEnemyMap() {
		return enemyMap;
	}
	public List<String> getGameList() {
		return gameList;
	}
	public Map<String, List<String>> getBoxMap() {
		return boxMap;
	}


}
