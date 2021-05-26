package com.lzx.model.vo;

import com.lzx.model.load.ElementLoad;
import com.lzx.model.manager.ElementManager;
import com.lzx.thread.GameThread;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Player extends SuperElement {
	private int hp;//血量
	private int num;//分数
	private ImageIcon img;//图片
	private ImageIcon bombIcon;
	private boolean pk;//攻击状态 默认false
	private int moveX;
	private int	moveY;
	private boolean LEFT = false;
	private boolean RIGHT = false;
	private boolean UP = false;
	private boolean DOWN = false;
	private ElementManager manager;
	private int currentBubble,maxBubble;
	private boolean putBubble;
	private int speed;//移动速度
	private boolean reverseWalk;//反向行走
	private int reverseTime;
	private int startReverseTime;
	private int bubblePower;
	private boolean stop = true;
	private int speedX,speedY;
	private SuperElement block;
	private Rectangle blockRec;
	private int blockX,blockY;
	private int id;
	public static int perReverseTime;
	private boolean isFlow;
	private int startFlowTime;
	private Calendar cal;
	private Calendar cal2;
	private Calendar cal3;


	private boolean isAddSpeed;
	private boolean isMinusSpeed;
	public Player(int x, int y, int w, int h, ImageIcon imageIcon, ImageIcon bombIcon, int id) {
		super(x,y,w,h);
		this.img = imageIcon;
		this.bombIcon = bombIcon;
		hp = 1;
		num = 0;

		pk = false;
		currentBubble = 0;
		maxBubble = 3;
		setObjectType(1);
		bubblePower = 1;
		speed = 10;
		reverseTime = 0;
		startReverseTime = -1;
		manager = ElementManager.getInstance();
		this.id = id;
		isFlow = false;
		startFlowTime = -1;
		cal = Calendar.getInstance();
		cal2 = Calendar.getInstance();
		isAddSpeed = false;
		isMinusSpeed = false;

	}
	static {
		perReverseTime = 50;
	}
	//直接调用这个方法 用来得到一个玩家对象 str包含的是玩家的信息
	public static Player createPlayer(String str){
		//onePlayer=playerC,playFire,160,160,40,40,0
		String[] arr = str.split(",");
		int x = Integer.parseInt(arr[2]),
				y = Integer.parseInt(arr[3]),
				w = Integer.parseInt(arr[4]),
				h = Integer.parseInt(arr[5]),
				id = Integer.parseInt(arr[6]);
		ImageIcon icon = ElementLoad.getInstance().getImageMap().get(arr[0]);
		ImageIcon bombIcon = ElementLoad.getInstance().getImageMap().get(arr[1]);
		Player player =  new Player(x, y, w, h, icon,bombIcon,id);
		return player;
	}
	@Override
	public void showElement(Graphics g) {
		if (isFlow) {
			g.drawImage(bombIcon.getImage(),
					getX(), getY(),
					getX()+getW(), getY()+getH(),
					0+80*moveX,6,
					80+80*moveX,70,
					null);
		}
		else {
			g.drawImage(img.getImage(),
					getX(), getY(),
					getX()+getW(), getY()+getH(),
					26+100*(moveX),40+100*(moveY),
					74+100*(moveX),100+100*(moveY),
					null);
		}
		//		g.drawImage(img.getImage(),
		//				getX(), getY(), //屏幕左上角坐标
		//				getX()+getW(), getY()+getH(), //屏幕右下角坐标
		//				0+80*moveX,6,
		//				80+80*moveX,70,
		//				26+100*(moveX),40+100*(moveY), //图片左上角坐标      60 0
		//				74+100*(moveX),100+100*(moveY), //图片
		//				null);
		// 26 40 74 100   26 140 74 200
		// 126 40 174 100
		// 226 40 274 100
		// 326 40 374 100
		//26+100*(moveX),40+100*(moveY), //图片左上角坐标      60 0
		//74+100*(moveX),100+100*(moveY), //图片右下角坐标  120 60

		// 0   80 70
		// 80    160 70
		// 160    240
		// 240    320
	}
	public void moveUp() {
		int currentSpeed=speed;

		setY(getY()-currentSpeed);
	}
	public void moveDown() {
		int currentSpeed=speed;

		setY(getY()+currentSpeed);
	}
	public void moveLeft() {
		int currentSpeed=speed;

		setX(getX()-currentSpeed);
	}
	public void moveRight() {
		int currentSpeed=speed;

		setX(getX()+currentSpeed);
	}
	public void move2(){
		int type ;
		int x = getX(),
				y = getY();
		int currendSpeed = speed;
		if (reverseWalk) {
			currendSpeed = -speed;
		}
		manager = ElementManager.getInstance();
		Rectangle player = new Rectangle();
		player.setSize(40, 40);
		List<SuperElement> boxs = manager.getElementList("box");
		List<SuperElement> bubbles = manager.getElementList("bubble");
		List<SuperElement> props = manager.getElementList("prop");
		List<SuperElement> blocks = new ArrayList<>();
		SuperElement temp,temp2;
		int ax,ay;
		if (UP) {
			player.setLocation(x, y-currendSpeed);
			if (y==0)return;
			if (judgeBoxKnock(boxs, player))return;
			blocks = judgeBubbleKnock(bubbles, player);
			if (blocks.size()>=2) return;
			else if (blocks.size() == 1) {
				temp = blocks.get(0);
				ax = temp.getX();
				ay = temp.getY();
				if ( ax!=blockX || ay!=blockY)return;
				//				moveUp();
				//				if (!blockRec.intersects(player)) {
				//					blockX = -1;
				//					blockY = -1;
				//					blockRec.setLocation(blockX,blockY);
				//				}
				//				temp2 = judgePropKnock(props, player);
				//				if (temp2!=null) {
				//					type = ((Prop)temp2).getPropClass();
				//					addProp(type);
				//					temp2.setVisible(false);
				//					manager.removeElementByPx(temp2.getY(), temp2.getX());
				//				}
			}
			//			else {
			moveUp();
			if (!blockRec.intersects(player)) {
				blockX = -1;
				blockY = -1;
				blockRec.setLocation(blockX,blockY);
			}
			temp2 = judgePropKnock(props, player);
			if (temp2!=null) {
				type = ((Prop)temp2).getPropClass();
				addProp(type);
				temp2.setVisible(false);
				manager.removeElementByPx(temp2.getY(), temp2.getX());
			}
			//			}
		}
		else if (DOWN) {
			player.setLocation(x, y+currendSpeed);
			if (y==440)return;
			if (judgeBoxKnock(boxs, player))return;
			blocks = judgeBubbleKnock(bubbles, player);
			if (blocks.size()>=2) return;
			else if (blocks.size() == 1) {
				temp = blocks.get(0);
				ax = temp.getX();
				ay = temp.getY();
				if ( ax!=blockX || ay!=blockY)return;
				//				moveDown();
				//				if (!blockRec.intersects(player)) {
				//					blockX = -1;
				//					blockY = -1;
				//					blockRec.setLocation(blockX,blockY);
				//				}
				//				temp2 = judgePropKnock(props, player);
				//				if (temp2!=null) {
				//					type = ((Prop)temp2).getPropClass();
				//					addProp(type);
				//					temp2.setVisible(false);
				//					manager.removeElementByPx(temp2.getY(), temp2.getX());
				//				}
			}
			//			else {
			moveDown();
			if (!blockRec.intersects(player)) {
				blockX = -1;
				blockY = -1;
				blockRec.setLocation(blockX,blockY);
			}
			temp2 = judgePropKnock(props, player);
			if (temp2!=null) {
				type = ((Prop)temp2).getPropClass();
				addProp(type);
				temp2.setVisible(false);
				manager.removeElementByPx(temp2.getY(), temp2.getX());
			}
			//			}
		}
		else if (LEFT) {
			player.setLocation(x-currendSpeed, y);
			if (x==0)return;
			if (judgeBoxKnock(boxs, player))return;
			blocks = judgeBubbleKnock(bubbles, player);
			if (blocks.size()>=2) return;
			else if (blocks.size() == 1) {
				temp = blocks.get(0);
				ax = temp.getX();
				ay = temp.getY();
				if ( ax!=blockX || ay!=blockY)return;
				//				moveLeft();
				//				if (!blockRec.intersects(player)) {
				//					blockX = -1;
				//					blockY = -1;
				//					blockRec.setLocation(blockX,blockY);
				//				}
				//				temp2 = judgePropKnock(props, player);
				//				if (temp2!=null) {
				//					type = ((Prop)temp2).getPropClass();
				//					addProp(type);
				//					temp2.setVisible(false);
				//					manager.removeElementByPx(temp2.getY(), temp2.getX());
				//				}
			}
			//			else {
			moveLeft();
			if (!blockRec.intersects(player)) {
				blockX = -1;
				blockY = -1;
				blockRec.setLocation(blockX,blockY);
			}
			temp2 = judgePropKnock(props, player);
			if (temp2!=null) {
				type = ((Prop)temp2).getPropClass();
				addProp(type);
				temp2.setVisible(false);
				manager.removeElementByPx(temp2.getY(), temp2.getX());
			}
			//			}
		}
		else if (RIGHT) {
			player.setLocation(x+currendSpeed, y);
			if (x==440)return;
			if (judgeBoxKnock(boxs, player))return;
			blocks = judgeBubbleKnock(bubbles, player);
			if (blocks.size()>=2) return;
			else if (blocks.size() == 1) {
				temp = blocks.get(0);
				ax = temp.getX();
				ay = temp.getY();
				if ( ax!=blockX || ay!=blockY)return;
				//				moveRight();
				//				if (!blockRec.intersects(player)) {
				//					blockX = -1;
				//					blockY = -1;
				//					blockRec.setLocation(blockX,blockY);
				//				}
				//				temp2 = judgePropKnock(props, player);
				//				if (temp2!=null) {
				//					type = ((Prop)temp2).getPropClass();
				//					addProp(type);
				//					temp2.setVisible(false);
				//					manager.removeElementByPx(temp2.getY(), temp2.getX());
				//				}
			}
			//			else {
			moveRight();
			if (!blockRec.intersects(player)) {
				blockX = -1;
				blockY = -1;
				blockRec.setLocation(blockX,blockY);
			}
			temp2 = judgePropKnock(props, player);
			if (temp2!=null) {
				type = ((Prop)temp2).getPropClass();
				addProp(type);
				temp2.setVisible(false);
				manager.removeElementByPx(temp2.getY(), temp2.getX());
			}
			//			}
		}
		//		if (UP) {
		//			player.setLocation(x, y-speed);
		//			if ( y==0 || judgeBoxBubbleKnock(boxs, player)) return;
		//			//被泡泡挡了
		//			if (judgeBoxBubbleKnock(bubbles, player)) {
		//				//刚刚未放过炸弹
		//				if (!putBubble) return;
		//
		//				if () {
		//
		//				}
		//				putBubble = false;
		//
		//			}
		//			temp = judgePropKnock(props, player);
		//			if (temp!=null) {
		//				type = ((Prop)temp).getPropClass();
		//				addProp(type);
		//				temp.setVisible(false);
		//				manager.removeElementByPx(y-40, x);
		//			}
		//setSpeedY(-speed);
		//			setY(y-speed);
		//			if (manager.isAllowPassByPx(y-40, x)) {
		//				if(!putBubble) {
		//					//manager.updateElementByPx(y, y-40, x, x);
		//				}
		//				else {
		//					//manager.setElementByPx(y-40, x, this);
		//					putBubble = false;
		//				}
		//				setY(y-speed);
		//			}
		//			ElementManager.getInstance().cout();
		//		}
		//		else if (RIGHT) {
		//			player.setLocation(x+speed, y);
		//			if ( x==440 || judgeBoxBubbleKnock(boxs, player) ) return;
		//			if (judgeBoxBubbleKnock(bubbles, player)) {
		//				if (!putBubble)
		//					return;
		//				putBubble = false;
		//			}
		//			temp = judgePropKnock(props, player);
		//			if (temp!=null) {
		//				type = ((Prop)temp).getPropClass();
		//				addProp(type);
		//				temp.setVisible(false);
		//				manager.removeElementByPx(y, x+40);
		//			}
		//			//setSpeedX(speed);
		//			setX(x+speed);
		//			if(x==440)return;
		//			if (manager.isAllowPassByPx(y, x+40)) {
		//				if(!putBubble) {
		//					//manager.updateElementByPx(y, y, x, x+40);
		//				}
		//				else {
		//					//manager.setElementByPx(y, x+40, this);
		//					putBubble = false;
		//				}
		//				setX(x+speed);
		//			}
		//			ElementManager.getInstance().cout();
		//		}
		//		else if (DOWN) {
		//			player.setLocation(x, y+speed);
		//			if ( y==440 || judgeBoxBubbleKnock(boxs, player) ) return;
		//			if (judgeBoxBubbleKnock(bubbles, player)) {
		//				if (!putBubble)
		//					return;
		//				putBubble = false;
		//			}
		//			temp = judgePropKnock(props, player);
		//			if (temp!=null) {
		//				type = ((Prop)temp).getPropClass();
		//				addProp(type);
		//				temp.setVisible(false);
		//				manager.removeElementByPx(y+40, x);
		//			}
		//			//setSpeedY(speed);
		//			setY(y+speed);
		//			if(y==440)return;
		//			if (manager.isAllowPassByPx(y+40, x)) {
		//				if(!putBubble) {
		//					//manager.updateElementByPx(y, y+40, x, x);
		//				}
		//				else {
		//					//manager.setElementByPx(y+40, x, this);
		//					putBubble = false;
		//				}
		//				setY(y+speed);
		//			}
		//		}
		//		else if (LEFT) {
		//			player.setLocation(x-speed, y);
		//			if ( x==0 || judgeBoxBubbleKnock(boxs, player) ) return;
		//			if (judgeBoxBubbleKnock(bubbles, player)) {
		//				if (!putBubble)
		//					return;
		//				putBubble = false;
		//			}
		//			temp = judgePropKnock(props, player);
		//			if (temp!=null) {
		//				type = ((Prop)temp).getPropClass();
		//				addProp(type);
		//				temp.setVisible(false);
		//				manager.removeElementByPx(y, x-40);
		//			}
		////			setSpeedX(-speed);
		//			setX(x-speed);
		//			//			if(x==0)return;
		//			//			if (manager.isAllowPassByPx(y, x-40)) {
		//			//				if(!putBubble) {
		//			//					//manager.updateElementByPx(y, y, x, x-40);
		//			//				}
		//			//				else {
		//			//					//manager.setElementByPx(y, x-40, this);
		//			//					putBubble = false;
		//			//				}
		//			//				setX(x-speed);
		//			//			}
		//		}
		//		else if (stop) {
		//			setSpeedX(0);
		//			setSpeedY(0);
		//		}
		/*
		 *  3 0 0 3 0 3 0 0 0 0 0 0
 0 3 0 0 0 0 0 3 0 0 0 0
 0 0 0 0 3 0 0 0 3 0 0 0
 0 0 0 0 3 0 3 0 0 3 0 0
 0 0 0 3 1 0 0 0 0 0 0 3
 0 0 0 0 0 0 3 0 0 0 0 0
 0 0 0 0 0 0 4 0 0 0 0 0
 0 0 0 0 4 0 0 0 4 0 0 0
 0 0 0 0 0 0 0 0 0 0 4 4
 0 0 0 4 0 4 0 4 0 0 0 0
 0 0 4 0 0 4 0 0 4 4 0 0
 4 0 0 4 4 0 0 0 4 0 4 4
		 */
	}
	/*
	 * imageProp1=img/prop/addBubble.png
imageProp2=img/prop/addBubblePower.png
imageProp3=img/prop/addHp.png
imageProp4=img/prop/OwnReverseWalk.png
imageProp5=img/prop/AnotherReverseWalk.png
imageProp6=img/prop/RemoveReverse.png
	 */
	public void addProp(int type) {
		switch (type) {
		case 1:
			maxBubble++;
			break;
		case 2:
			bubblePower++;
			break;
		case 3:
			hp++;
			break;
		case 4:
			addReverseTime();
			break;
		case 5:
			Player player = (Player)manager.getElementList("play").get(1-id);
			player.addReverseTime();
			break;
		case 6:
			reverseTime-=50;
			break;
		case 7:
			addSpeed();
			break;

		case 8:
			Player player2 = (Player)manager.getElementList("play").get(1-id);
			player2.minusSpeed();
			break;
		default:
			break;
		}
		System.out.println(this);
	}
	public void addSpeed() {
		cal2 = Calendar.getInstance();
		if (!isAddSpeed) {
			isAddSpeed = true;
			speed+=5;
		}
	}
	public void recoverAddSpeed() {

		speed-=5;
	}
	public void recoverMinusSpeed() {
		speed+=5;
	}
	public void minusSpeed() {
		cal3 = Calendar.getInstance();
		if (!isMinusSpeed) {
			isMinusSpeed = true;
			speed-=5;
		}

	}
	public void addReverseTime() {
		if (reverseTime == 0) {
			setStartReverseTime(GameThread.getTime());
		}
		reverseTime+=perReverseTime;
	}
	public void minusReverseTime() {
		if (reverseTime>0) {
			reverseTime-=50;
		}
	}
	public void bombByBubble(int id) {
		if (hp>=2) {
			hp--;
		}
		else {
			setFlow(true);
			if(id == this.id) {
				num-=200;
			}

			setStartFlowTime(GameThread.getTime());
			cal = Calendar.getInstance();
		}
	}
	public SuperElement judgePropKnock(List<SuperElement> props,Rectangle player) {
		Rectangle prop;
		for (SuperElement superElement : props) {
			prop = new Rectangle(superElement.getX(), superElement.getY(),40,40);
			if (prop.intersects(player)) {
				return superElement;
			}
		}
		return null;
	}
	public List<SuperElement> judgePropKnock1(List<SuperElement> props,Rectangle player){
		List<SuperElement> list = new ArrayList<>();
		Rectangle prop;
		for (SuperElement superElement : props) {
			prop = new Rectangle(superElement.getX(), superElement.getY(),40,40);
			if (prop.intersects(player)) {
				list.add(superElement);
			}
		}
		//		System.out.println("prop list size ="+list.size());
		return list;
	}
	public boolean judgeBoxKnock(List<SuperElement> boxs,Rectangle player) {
		Rectangle box;
		for (SuperElement superElement : boxs) {
			box = new Rectangle(superElement.getX(), superElement.getY(),40,40);
			if (box.intersects(player)) {
				return true;
			}
		}
		return false;
	}
	//  碰撞泡泡
	public List<SuperElement> judgeBubbleKnock(List<SuperElement> bubbles,Rectangle player) {
		List<SuperElement> res = new ArrayList<>();
		Rectangle bubble;
		for (SuperElement superElement : bubbles) {
			bubble = new Rectangle(superElement.getX(), superElement.getY(),40,40);
			if (bubble.intersects(player)) {
				res.add(superElement);
			}
		}
		return res;
	}
	public void updateImage(){
		if (isFlow) {
			moveX = (++moveX)%4;
		}
		else {
			if (UP) {
				if (reverseTime>0) {
					moveY = 0;
				}
				else {
					moveY = 3;
				}
				moveX = (++moveX)%4;
			}
			else if (LEFT) {
				if (reverseTime>0) {
					moveY = 2;
				}
				else {
					moveY = 1;
				}
				moveX = (++moveX)%4;
			}
			else if (RIGHT) {
				if (reverseTime>0) {
					moveY = 1;
				}
				else {
					moveY = 2;
				}
				moveX = (++moveX)%4;
			}
			else if (DOWN) {
				if (reverseTime>0) {
					moveY = 3;
				}
				else {
					moveY = 0;
				}
				moveX = (++moveX)%4;
			}
		}
	}
	public void addFire(){
		//如果pk为false 不添加子弹
		if (!pk) {
			return ;
		}
		//		List<SuperElement> list =
		//			ElementManager.getInstance().getElementList("playfire");
		//		if (list == null) {
		//			list = new ArrayList<SuperElement>();
		//		}
		//		list.add(PlayerFire.createPlayerFire(getX(), getY(), ""));
		//ElementManager.getInstance().getMap().put("playfire", list);
		pk = false;//每按一次空格 只能发射一颗子弹
	}
	public void addBubble() {
		if (!pk) {
			return ;
		}
		if (isFlow) return;
		if (currentBubble<maxBubble) {
			List<SuperElement> list =
					ElementManager.getInstance().getElementList("bubble");
			SuperElement superElement= ElementManager.getInstance().getElementByPx((((getY()+20)/40)*40), ((getX()+20)/40)*40);
			if (superElement!=null &&superElement instanceof Bubble) {
				return;
			}
			Bubble bubble = Bubble.createBubble(((getX()+20)/40)*40, (((getY()+20)/40)*40), new ImageIcon("img/play/asd.png"),id,bubblePower);
			list.add(bubble);

			new Thread() {
				public void run() {
					new audioPlay(Audio.ADD).player();
				}
			}.start();




			for(int i=0,size=list.size();i<size;i++) {
				Bubble bubble2 = (Bubble)list.get(i);
				ElementManager.getInstance().setElementByPx(bubble2.getY(),bubble2.getX(),bubble2);
			}

			currentBubble++;
		}
		pk = false;//每按一次空格 只能发射一颗子弹
	}
	//重写父类的模板
	public void update(){
		super.update();//没有这句话 就是重新制定模板
		//addFire();
		addBubble();
		updateImage();
		updateTime();
	}

	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public ImageIcon getImg() {
		return img;
	}
	public void setImg(ImageIcon img) {
		this.img = img;
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean pk) {
		this.pk = pk;
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		if (!isVisible()) {
			GridCell.setGridByPx(getY(), getX(), 0);
			ElementManager.getInstance().removeElementByPx(getY()/40, getX()/40);
		}
	}
	//	class MoveThread extends Thread{
	//		private Player player;
	//		public MoveThread(Player player) {
	//			super();
	//			this.player = player;
	//		}
	//		@Override
	//		public void run() {
	//			while(true){
	//				player.setX(player.getX()+speedX);
	//				player.setY(player.getY()+speedY);
	//				try {
	//					TimeUnit.MILLISECONDS.sleep(100);
	//				} catch (InterruptedException e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		}
	//	}
	public boolean isLEFT() {
		return LEFT;
	}
	public void setLEFT(boolean lEFT) {
		LEFT = lEFT;
	}
	public boolean isRIGHT() {
		return RIGHT;
	}
	public void setRIGHT(boolean rIGHT) {
		RIGHT = rIGHT;
	}
	public boolean isUP() {
		return UP;
	}
	public void setUP(boolean uP) {
		UP = uP;
	}
	public boolean isDOWN() {
		return DOWN;
	}
	public void setDOWN(boolean dOWN) {
		DOWN = dOWN;
	}
	public boolean isReverseWalk() {
		return reverseWalk;
	}
	public void setReverseWalk(boolean reverseWalk) {
		this.reverseWalk = reverseWalk;
	}
	public int getMaxBubble() {
		return maxBubble;
	}
	public void setMaxBubble(int maxBubble) {
		this.maxBubble = maxBubble;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getBubblePower() {
		return bubblePower;
	}
	public void setBubblePower(int bubblePower) {
		this.bubblePower = bubblePower;
	}
	public boolean isStop() {
		return stop;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	public int getSpeedX() {
		return speedX;
	}
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	public int getSpeedY() {
		return speedY;
	}
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	public int getBlockX() {
		return blockX;
	}
	public void setBlockX(int blockX) {
		this.blockX = blockX;
	}
	public int getBlockY() {
		return blockY;
	}
	public void setBlockY(int blockY) {
		this.blockY = blockY;
	}
	@Override
	public boolean allowpass() {
		// TODO 自动生成的方法存根
		return true;
	}
	public void judgeMove(int type) {
		switch (type) {
		case 0:
			moveUp();
			break;
		case 1:
			moveDown();
			break;
		case 2:
			moveLeft();
			break;
		case 3:
			moveRight();
			break;
		default:
			break;
		}
	}
	public void judgeAddProp() {
		Rectangle player = new Rectangle(40, 40);
		List<SuperElement> props ;
		List<SuperElement> nowProps ;
		player.setLocation(this.getX(), this.getY());
		nowProps =  ElementManager.getInstance().getElementList("prop");
		props = judgePropKnock1(nowProps,player);
		if (props.size() == 0) return;
		//		System.out.println("now props .size"+props.size());
		for (SuperElement superElement : props) {
			Prop prop = (Prop)superElement;
			//			System.out.println("player add prop"+prop.getPropClass());
			addProp(prop.getPropClass());
			prop.setVisible(false);
			manager.removeElementByPx(prop.getY(), prop.getX());
		}
	}
	public void judgeBlock(SuperElement s1,SuperElement s2,int type) {
		SuperElement temp,temp2;
		Rectangle player = new Rectangle();
		player.setSize(40, 40);
		List<SuperElement> props = manager.getElementList("prop");
		int a;
		if (s1==null&&s2==null) {
			judgeMove(type);
			judgeAddProp();
		}
		else if(s1!=null&&s2!=null) {
			if(!s1.allowpass()||!s2.allowpass()) {
				Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
				Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
				Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
				if(r.intersects(r1)||r.intersects(r2)) {
					judgeMove(type);
					judgeAddProp();
				}
			}else {
				judgeMove(type);
				judgeAddProp();
			}
		}
		else if(s1==null&&s2!=null) {
			if(!s2.allowpass()) {
				Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
				Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
				if(r.intersects(r2)) {
					judgeMove(type);
					judgeAddProp();
				}
			}else {
				judgeMove(type);
				judgeAddProp();
			}
		}
		else if(s1!=null&&s2==null) {
			if(!s1.allowpass()) {
				Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
				Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
				if(r.intersects(r1)) {
					judgeMove(type);
					judgeAddProp();
				}
			}else {
				judgeMove(type);
				judgeAddProp();
			}
		}
	}
	/*
	 * //				judgePropKnock(s1);
//				judgePropKnock(s2);
//				player.setLocation(getX(),getY());
//				temp2 = judgePropKnock3(props, player);
//				if (temp2!=null) {
//					a = ((Prop)temp2).getPropClass();
//					addProp(a);
//					temp2.setVisible(false);
//					manager.removeElementByPx(temp2.getY(), temp2.getX());
//				}
	 */
	public SuperElement judgePropKnock3(List<SuperElement> props,Rectangle player) {
		Rectangle prop;
		for (SuperElement superElement : props) {
			prop = new Rectangle(superElement.getX(), superElement.getY(),40,40);
			if (prop.intersects(player)) {
				return superElement;
			}
		}
		return null;
	}
	public void judgePlayerBlock() {
		Player player = (Player) manager.getElementList("play").get(1-id);
		if (player.isFlow) {
			Rectangle r1 = new Rectangle(getX(),getY(),40,40);
			Rectangle r2 = new Rectangle(player.getX(),player.getY(),40,40);
			if (r1.intersects(r2)) {
				player.recover();
				setNum(getNum()+500);
				System.out.println(this);
				System.out.println(player);
			}
		}
	}
	public void move() {
		if (isFlow) return;
		if (RIGHT) {
			if (reverseTime>0) {
				if (getX()<=0) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx((getY()/40)*40,((getX()-speed)/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()-1+40)/40)*40,((getX()-speed)/40)*40);
				judgeBlock(s1, s2, 2);
				judgePlayerBlock();
			}
			else {
				if (getX()>=440) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx((getY()/40)*40,((getX()+40+speed-1)/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()+40-1)/40)*40,((getX()+40+speed-1)/40)*40);
				judgeBlock(s1, s2, 3);
				judgePlayerBlock();
			}
		}
		else if (LEFT) {

			if (reverseTime>0) {
				if (getX()>=440) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx((getY()/40)*40,((getX()+40+speed-1)/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()+40-1)/40)*40,((getX()+40+speed-1)/40)*40);
				judgeBlock(s1, s2, 3);
				judgePlayerBlock();
			}
			else {
				if (getX()<=0) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx((getY()/40)*40,((getX()-speed)/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()-1+40)/40)*40,((getX()-speed)/40)*40);
				judgeBlock(s1, s2, 2);
				judgePlayerBlock();
			}
		}
		else if (UP) {

			if (reverseTime>0) {
				if (getY()>=440) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx(((getY()+40+speed-1)/40)*40, (getX()/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()+40+speed-1)/40)*40, ((getX()-1+40)/40)*40);
				judgeBlock(s1, s2, 1);
				judgePlayerBlock();
			}
			else {
				if (getY()<=0) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx(((getY()-speed)/40*40), (getX()/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()-speed)/40*40), ((getX()-1+40)/40)*40);
				judgeBlock(s1, s2, 0);
				judgePlayerBlock();
			}
		}
		else if (DOWN) {

			if (reverseTime>0) {
				if (getY()<=0) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx(((getY()-speed)/40*40), (getX()/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()-speed)/40*40), ((getX()-1+40)/40)*40);
				judgeBlock(s1, s2, 0);
				judgePlayerBlock();
			}
			else {
				if (getY()>=440) return;
				SuperElement s1 = ElementManager.getInstance().getElementByPx(((getY()+40+speed-1)/40)*40, (getX()/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()+40+speed-1)/40)*40, ((getX()-1+40)/40)*40);
				judgeBlock(s1, s2, 1);
				judgePlayerBlock();
			}
		}
	}
	public void judgePropKnock(SuperElement superElement) {
		if (superElement == null)return;
		System.out.println("judge prop y="+superElement.getY()+" x="+superElement.getX());
		Rectangle r1 = new Rectangle(superElement.getX(), superElement.getY(), 40, 40);
		Rectangle r2 = new Rectangle(getX(), getY(), 40, 40);
		if (!r1.intersects(r2)) return;
		if (superElement instanceof Prop) {
			Prop prop = (Prop)superElement;
			addProp(prop.getPropClass());
			prop.setVisible(false);
			manager.removeElementByPx(prop.getY(), prop.getX());
		}
	}
	public void move3() {
		if(RIGHT) {
			if (reverseTime>0) {
				SuperElement s1 = ElementManager.getInstance().getElementByPx((getY()/40)*40,((getX()-speed)/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()-1+40)/40)*40,((getX()-speed)/40)*40);
				judgeBlock(s1, s2, 2);
			}
			else {
				SuperElement s1 = ElementManager.getInstance().getElementByPx((getY()/40)*40,((getX()+40+speed-1)/40)*40);
				SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()+40-1)/40)*40,((getX()+40+speed-1)/40)*40);
				judgeBlock(s1, s2, 3);
			}
			//			if(s1==null&&s2==null) {
			//				moveRight();
			//				System.out.println("!!");
			//			}
			//			else if(s1!=null&&s2!=null) {
			//				if(!s1.allowpass()||!s2.allowpass()) {
			//					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
			//					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
			//					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
			//					if(r.intersects(r1)||r.intersects(r2)) {
			//						moveRight();
			//					}
			//				}else {
			//					moveRight();
			//				}
			//			}
			//			else if(s1==null&&s2!=null) {
			//				if(!s2.allowpass()) {
			//					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
			//					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
			//					if(r.intersects(r2)) {
			//						moveRight();
			//					}
			//				}else {
			//					moveRight();
			//				}
			//			}
			//			else if(s1!=null&&s2==null) {
			//				if(!s1.allowpass()) {
			//					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
			//					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
			//					if(r.intersects(r1)) {
			//						moveRight();
			//					}
			//				}else {
			//					moveRight();
			//				}
			//			}
		}
		else if(LEFT){
			SuperElement s1 = ElementManager.getInstance().getElementByPx((getY()/40)*40,((getX()-speed)/40)*40);
			SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()-1+40)/40)*40,((getX()-speed)/40)*40);
			if(s1==null&&s2==null) {
				moveLeft();
			}
			else if(s1!=null&&s2!=null) {
				if(!s1.allowpass()||!s2.allowpass()) {
					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r1)||r.intersects(r2)) {
						moveLeft();
					}
				}else {
					moveLeft();
				}
			}
			else if(s1==null&&s2!=null) {
				if(!s2.allowpass()) {
					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r2)) {
						moveLeft();
					}
				}else {
					moveLeft();
				}
			}
			else if(s1!=null&&s2==null) {
				if(!s1.allowpass()) {
					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r1)) {
						moveLeft();
					}
				}else {
					moveLeft();
				}
			}
		}
		else if(UP) {
			System.out.println("x="+getX()+"    y="+getY());
			SuperElement s1 = ElementManager.getInstance().getElementByPx(((getY()-speed)/40*40), (getX()/40)*40);
			SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()-speed)/40*40), ((getX()-1+40)/40)*40);
			if(s1==null&&s2==null) {
				moveUp();
				System.out.println("!!");
			}
			else if(s1!=null&&s2!=null) {
				if(!s1.allowpass()||!s2.allowpass()) {
					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r1)||r.intersects(r2)) {
						moveUp();
					}
				}else {
					moveUp();
				}
			}
			else if(s1==null&&s2!=null) {
				if(!s2.allowpass()) {
					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r2)) {
						moveUp();
					}
				}else {
					moveUp();
				}
			}
			else if(s1!=null&&s2==null) {
				if(!s1.allowpass()) {
					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r1)) {
						moveUp();
					}
				}else {
					moveUp();
				}
			}
		}
		else if(DOWN) {
			System.out.println("x="+getX()+"    y="+getY());
			SuperElement s1 = ElementManager.getInstance().getElementByPx(((getY()+40+speed-1)/40)*40, (getX()/40)*40);
			SuperElement s2 = ElementManager.getInstance().getElementByPx(((getY()+40+speed-1)/40)*40, ((getX()-1+40)/40)*40);
			if(s1==null&&s2==null) {
				moveDown();
				System.out.println("!!");
			}
			else if(s1!=null&&s2!=null) {
				if(!s1.allowpass()||!s2.allowpass()) {
					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r1)||r.intersects(r2)) {
						moveDown();
					}
				}else {
					moveDown();
				}
			}
			else if(s1==null&&s2!=null) {
				if(!s2.allowpass()) {
					Rectangle r2 =new Rectangle(s2.getX(),s2.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r2)) {
						moveDown();
					}
				}else {
					moveDown();
				}
			}
			else if(s1!=null&&s2==null) {
				if(!s1.allowpass()) {
					Rectangle r1 =new Rectangle(s1.getX(),s1.getY(),40,40);
					Rectangle r  =new Rectangle(this.getX(),this.getY(),40,40);
					if(r.intersects(r1)) {
						moveDown();
					}
				}else {
					moveDown();
				}
			}
		}
	}
	public int getCurrentBubble() {
		return currentBubble;
	}
	public void setCurrentBubble(int currentBubble) {
		this.currentBubble = currentBubble;
	}
	public int getReverseTime() {
		return reverseTime;
	}
	public void setReverseTime(int reverseTime) {
		this.reverseTime = reverseTime;
	}
	public int getStartReverseTime() {
		return startReverseTime;
	}
	public void setStartReverseTime(int startReverseTime) {
		this.startReverseTime = startReverseTime;
	}

	public boolean isFlow() {
		return isFlow;
	}
	public void setFlow(boolean isFlow) {
		this.isFlow = isFlow;
	}
	public int getStartFlowTime() {
		return startFlowTime;
	}
	public void setStartFlowTime(int startFlowTime) {
		this.startFlowTime = startFlowTime;
	}
	public void recover() {
		// TODO 自动生成的方法存根
		setFlow(false);
		
	}
	
	public void updateTime() {
		if (isFlow) {
			Calendar cal = Calendar.getInstance();
			if(cal.getTime().getTime()-this.cal.getTime().getTime()>5000) {
				recover();
			}
		}
		if (isAddSpeed) {
			Calendar cal = Calendar.getInstance();
			if (cal.getTime().getTime()-this.cal2.getTime().getTime()>5000) {
				isAddSpeed = false;
				recoverAddSpeed();
			}
		}
		if (isMinusSpeed) {
			Calendar cal = Calendar.getInstance();
			if (cal.getTime().getTime()-this.cal3.getTime().getTime()>5000) {
				isMinusSpeed = false;
				recoverMinusSpeed();
			}
		}
	}
	
	public void addNum(int num1) {
		this.num+=num1;
	}
	public boolean isAddSpeed() {
		return isAddSpeed;
	}
	public void setAddSpeed(boolean isAddSpeed) {
		this.isAddSpeed = isAddSpeed;
	}
	@Override
	public String toString() {
		return "Player [speed=" + speed + ", id=" + id + "]";
	}
	

}
