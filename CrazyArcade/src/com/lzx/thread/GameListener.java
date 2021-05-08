package com.lzx.thread;

import com.lzx.model.manager.ElementManager;
import com.lzx.model.vo.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class GameListener implements KeyListener {
	private List<?> list;
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	//按下 左37 右39 下40 上38 w87 a65 s83 d68 空格32 enter10
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("keypressed"+e.getKeyCode());
		list = ElementManager.getInstance().getElementList("play");
		Player oneplayer = (Player)list.get(0);
		Player twoPlayer = (Player)list.get(1);
		switch (e.getKeyCode()) {
		case 65:
			oneplayer.setLEFT(true);
//			oneplayer.setStop(false);
			break;
		case 87:
			oneplayer.setUP(true);
//			oneplayer.setStop(false);
			break;
		case 68:
			oneplayer.setRIGHT(true);
//			oneplayer.setStop(false);
			break;
		case 83:
			oneplayer.setDOWN(true);
//			oneplayer.setStop(false);
			break;
		case 32:
			oneplayer.setPk(true);
			break;
		case 37:
			twoPlayer.setLEFT(true);
			break;
		case 38:
			twoPlayer.setUP(true);
			break;
		case 39:
			twoPlayer.setRIGHT(true);
			break;
		case 40:
			twoPlayer.setDOWN(true);
			break;
		case 10:
			twoPlayer.setPk(true);
			break;
		}
	}
	//松开
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("keyreleased"+e.getKeyCode());
		list = ElementManager.getInstance().getElementList("play");
		Player oneplayer = (Player)list.get(0);
		Player twoPlayer = (Player)list.get(1);
		switch (e.getKeyCode()) {
		case 65:
//			if (oneplayer.isLEFT()) {
//				oneplayer.setStop(true);
//			}
			oneplayer.setLEFT(false);
			
			break;
		case 87:
//			if (oneplayer.isUP()) {
//				oneplayer.setStop(true);
//			}
			oneplayer.setUP(false);
			break;
		case 68:
//			if (oneplayer.isRIGHT()) {
//				oneplayer.setStop(true);
//			}
			oneplayer.setRIGHT(false);
			break;
		case 83:
//			if (oneplayer.isDOWN()) {
//				oneplayer.setStop(true);
//			}
			oneplayer.setDOWN(false);
			break;	
		case 32:
			oneplayer.setPk(false);
			break;
		case 37:
			twoPlayer.setLEFT(false);
			break;
		case 38:
			twoPlayer.setUP(false);
			break;
		case 39:
			twoPlayer.setRIGHT(false);
			break;
		case 40:
			twoPlayer.setDOWN(false);
			break;	
		case 10:
			twoPlayer.setPk(false);
			break;
		}
	}
}
