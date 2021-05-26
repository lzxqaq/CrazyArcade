package com.lzx.model.manager;

import com.lzx.model.load.ElementLoad;
import com.lzx.model.vo.*;

import java.util.*;

/**
 * 元素管理器
 * @author user
 *	设计模式 --单例模式：全局只有一个实例
 *
 *	hashcode Object类 集合排序
 */
public class ElementManager {
	// 集合 NPC元素 场景元素
	Map< String , List<SuperElement> > map;
	SuperElement[][] objects ;
	//初始化
	protected void init(){
		map = new HashMap< String , List<SuperElement> >();
		List<SuperElement> plays = new ArrayList<>();
		//plays.add(Player.createPlayer("1"));
		map.put("play", plays);
		List<SuperElement> playfires1 = new ArrayList<>();
		map.put("bubble", playfires1);
		List<SuperElement> enemys = new ArrayList<>();
		map.put("enemylist", enemys);
		List<SuperElement> playfires = new ArrayList<>();
		map.put("playfire", playfires);
		List<SuperElement> boxs = new ArrayList<>();
		map.put("box", boxs);
		List<SuperElement> props = new ArrayList<>();
		map.put("prop", props);
		List<SuperElement> backgrounds = new ArrayList<>();
		map.put("background", backgrounds);
		List<SuperElement> bombs = new ArrayList<>();
		map.put("bomb",bombs);
		objects =  new SuperElement[12][12];
		//		List<SuperElement> backgrounds = new ArrayList<>();
		//		backgrounds.add(new Background());
		//		map.put("background", backgrounds);
		//		List<SuperElement> stars = new ArrayList<>();
		//		stars.add(new Star(150,300,40,40));
		//		map.put("z",stars);
	}
	//得到一个完整的map集合
	public Map<String, List<SuperElement>> getMap() {
		return map;
	}
	//得到一个元素的集合
	public List<SuperElement> getElementList(String key){
		return map.get(key);
	}
	//单例 ：需要一个唯一的引用
	private static ElementManager elementManager;
	//构造方法私有化，只有本类中可以new
	private ElementManager(){
		init();
	}
	static{
		if (elementManager == null) {
			elementManager = new ElementManager();
		}
	}
	//提供出来给予外部访问的唯一入口 同步 线程保护锁
	public static /*synchronized*/ ElementManager getInstance(){
		//		if (elementManager == null) {
		//			elementManager = new ElementManager();
		//		}
		return elementManager;
	}
	//set
	public void setElementByPx(int row,int col,SuperElement superElement) {

		objects[row/40][col/40] = superElement;
	}
	public void setElementByIndex(int row,int col,SuperElement superElement) {
		objects[row][col] = superElement;
	}
	//get
	public SuperElement getElementByPx(int row,int col) {
		if(row<0||row>440||col<0||col>440)
			return null;
		else
			return objects[row/40][col/40];
	}
	public SuperElement getElementByIndex(int row,int col) {
		return objects[row][col];
	}
	//移除
	public SuperElement removeElementByPx(int row,int col){
		SuperElement superElement = getElementByPx(row, col);
		setElementByPx(row, col, null);
		return superElement;
	}
	public SuperElement removeElementByIndex(int row,int col){
		SuperElement superElement = getElementByIndex(row, col);
		setElementByIndex(row, col, null);
		return superElement;
	}
	//更新
	public void updateElementByPx(int preRow,int currentRow,int preCol,int currentCol) {
		setElementByPx(currentRow, currentCol, removeElementByPx(preRow, preCol));
	}
	public void updateElementByIndex(int preRow,int currentRow,int preCol,int currentCol) {
		setElementByIndex(currentRow, currentCol, removeElementByIndex(preRow, preCol));
	}
	//资源加载
	public void load() {
		// TODO Auto-generated method stub
		ElementLoad elementLoad = ElementLoad.getInstance();
		elementLoad.readImagePro();
		elementLoad.readPlayerPro();
		elementLoad.readGamePro();
		elementLoad.readBoxPro();
		//开放一个状态 界面可以做前面的过渡信息 。。。。
		//调用工厂 创建玩家。 敌机等·
		Background background = Background.createBackground(2);
		map.get("background").add(background);
		Player player =(Player) ElementFactory.elementFactory("onePlayer");
		map.get("play").add(player);
		//setElementByPx(player.getY(), player.getX(), player);
		player = (Player) ElementFactory.elementFactory("twoPlayer");
		map.get("play").add(player);
		System.out.println("ljr123"+map.get("play").size());
//		System.out.println("set player "+player.getY()+" "+player.getX()+player);
//		System.out.println(objects[4][4].getObjectType());


		List<SuperElement> boxs = map.get("box");
		//{box02=[unboombox,imageB,1,0,0], box01=[boombox,imageA,0,0,1]}
		Map<String, List<String> > boxMap = elementLoad.getBoxMap();
		Set<String> keys = boxMap.keySet();
		for (String string : keys) {
			String temp = boxMap.get(string).get(0);
			String[] arr = temp.split(",");
			if (arr[0].equals("boombox")) {
				Box box = BoomBox.createBox(temp);
				boxs.add(box);
				setElementByPx(box.getY(), box.getX(), box);
			}
			else {
				Box box = UnBoomBox.createBox(temp);
				boxs.add(box);
				setElementByPx(box.getY(), box.getX(), box);
			}
		}
		//GridCell.cout();
	}
	public void changeReverseTime(Player player,int currentTime) {
		int reverseTime = player.getReverseTime();
		int startTime = player.getStartReverseTime();
		int a;
		int perTime = Player.perReverseTime;
		if (reverseTime>=perTime) {
			if (currentTime>=(startTime+perTime)) {
				a = (currentTime-startTime)/perTime;
				player.setReverseTime(reverseTime-a*perTime);
				player.setStartReverseTime(player.getStartReverseTime()+a*perTime);
			}
		}

	}
	//控制流程     time:游戏的进行时间
	public void linkGame(int time) {
		Player player1 = (Player)(map.get("play").get(0));
		Player player2 = (Player)(map.get("play").get(1));
		int reverseTime = player1.getReverseTime();

		int startReverseTime = player1.getStartReverseTime();

		int a;
		int perTime = Player.perReverseTime;
		if (reverseTime>=perTime) {
			if (time>=(startReverseTime+perTime)) {
				a = (time-startReverseTime)/perTime;
				player1.setReverseTime(reverseTime-a*perTime);
				player1.setStartReverseTime(startReverseTime+a*perTime);
			}
		}
		reverseTime = player2.getReverseTime();
		startReverseTime = player2.getStartReverseTime();
		if (reverseTime>=perTime) {
			if (time>=(startReverseTime+perTime)) {
				a = (time-startReverseTime)/perTime;
				player2.setReverseTime(reverseTime-a*perTime);
				player2.setStartReverseTime(startReverseTime+a*perTime);
			}
		}
		if(time%10==0) {
			System.out.println(player1);
			System.out.println();
			System.out.println(player2);
		}
//		if (player1.isFlow()) {
//			if ((player1.getStartFlowTime()+50) <= time) {
//				player1.recover();
//			}
//		}
//		if (player2.isFlow()) {
//			if ((player2.getStartFlowTime()+50) <= time) {
//				player2.recover();
//			}
//		}
//		if (time%20 == 0) {
//			System.out.println(player1);
//			System.out.println(player2);
//		}
//		Player one = (Player)map.get("play").get(0);
//		changeReverseTime(one, time);
//		Player two = (Player)map.get("play").get(1);
//		changeReverseTime(two, time);
		// TODO Auto-generated method stub
//		if (time%10 == 0) {
//			List<SuperElement> props = map.get("prop");
//			if (props.size()>0) {
//				for (SuperElement superElement : props) {
//					System.out.println("prop y ="+superElement.getY()+" x="+superElement.getX());
//					setElementByPx(superElement.getY(), superElement.getX(),((Prop)superElement));
//				}
//			}
//		}
//		if (time%20 == 0) {
////			cout();
////			System.out.println(map.get("prop").size());
////
////			System.out.println();
//		}
		//得到流程list
//		List<String> list = ElementLoad.getInstance().getGameList();
//		if (list.size() == 0) {
//			//System.out.println("流程结束");
//			return ;
//		}
//		String string = list.get(list.size()-1);
//		String[] arr = string.split(",");
//		String temp = arr[arr.length-1];
//		Integer integer = Integer.parseInt(temp);
		//		if (time>=integer) {
		//			map.get("enemylist").add(ElementFactory.elementFactory("enemy"));
		//			list.remove(list.size()-1);
		//		}
		//		if (time == 流程的time) {
		//			工厂创建敌机 添加到元素管理
		//		        流程单里面最前面的清除
		//		}
	}
	public boolean isAllowPassByPx(int row,int col) {
		SuperElement superElement = getElementByPx(row, col);
		if (superElement == null)
			return true;
		int type = superElement.getObjectType();
		System.out.println(type);
		return type<=2;
	}
	public boolean isAllowPassByIndex(int row,int col) {
		SuperElement superElement = getElementByIndex(row, col);
		if (superElement == null)
			return true;
		int type = superElement.getObjectType();
		System.out.println(type);
		return type<=2;
	}
	public void cout() {
		for(int i=0;i<12;i++) {
			for(int j=0;j<12;j++) {
				SuperElement superElement = getElementByIndex(i, j);
				if (superElement!=null) {
					System.out.print(superElement.getObjectType()+" ");
				}
				else {
					System.out.print(0+" ");
				}
				
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}
	public SuperElement[][] getObjects() {
		return objects;
	}
	public void setObjects(SuperElement[][] objects) {
		this.objects = objects;
	}
	public boolean isBomb(int row,int col) {
		SuperElement superElement = getElementByPx(row, col);
		if (superElement == null)
			return true;
		int type = superElement.getObjectType();
		if(type==4) return false;
		else return true;
	}
	public boolean isBoomBox(int row,int col) {
		SuperElement superElement = getElementByPx(row, col);
		if (superElement == null)
			return false;
		int type = superElement.getObjectType();
		if(type==3) return true;
		else return false;
	}
}
