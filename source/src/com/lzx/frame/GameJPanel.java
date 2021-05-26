package com.lzx.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.lzx.model.manager.ElementManager;
import com.lzx.model.vo.Player;
import com.lzx.model.vo.SuperElement;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameJPanel extends JPanel implements Runnable{
    private ElementManager manager;

    public  GameJPanel() {
        setLayout(null);
        setSize(650, 550);
        manager = ElementManager.getInstance();

    }
    /**
     * paint方法由底层自动调用，重写父类方法
     * 只会执行一次，除非主动调用
     * 帧 ： 50-100毫秒每帧 ， 10-20帧每秒
     */
    //作用 显示
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //给一个判定值 枚举
        //1.前动画
        //System.out.println("11");
        //2.gameRuntime
        gameRunTime(g);//Graphics 画笔
        //this.setBackground(Color.black);
        //3.衔接动画

        ImageIcon imageIcon = new ImageIcon("img/bg/right_sight.png");
        g.drawImage(imageIcon.getImage(), 480,0,160,530, null);
        ImageIcon imageIcon2 = new ImageIcon("img/bg/bottom.png");
        g.drawImage(imageIcon2.getImage(), 0,480,480,50, null);
        g.setColor(Color.RED);

        List<SuperElement> list = manager.getElementList("play");
        if(list.size()==2) {
            Player player = (Player)(manager.getElementList("play").get(0));
            if(player !=null) {
                g.drawString(""+player.getNum(), 560, 60);
                if (player.getNum()>=1000) {
                    ImageIcon img = new ImageIcon("img/bg/gameovers.png");
                    g.drawImage(img.getImage(),0,0,640,550,null);
                    g.setFont(new Font("123",0,50));

                    g.drawString("1p获胜！", 250, 350);
                    return ;
                }
            }

            g.setColor(Color.RED);
            Player player2 = (Player)(manager.getElementList("play").get(1));
            if(player2!=null) {
                g.drawString(""+player2.getNum(), 560, 110);
                if (player2.getNum()>=1000) {
                    ImageIcon img = new ImageIcon("img/bg/gameovers.png");
                    g.drawImage(img.getImage(),0,0,640,550,null);
                    g.setFont(new Font("123",0,50));
                    g.drawString("2p获胜！", 250, 350);
                    return ;

                }
            }
        }



    }
    private void gameRunTime(Graphics g){
        //List<SuperElement> list = ElementManager.getInstance().getElementList("XX");
        //g.drawString("132456", 100, 100);
        Map<String, List<SuperElement> > map = ElementManager.getInstance().getMap();
        Set<String> set = map.keySet();
        List<String> temp = new ArrayList<>(set);
        Collections.sort(temp);
        for (String key : temp) {
            List<SuperElement> list = map.get(key);
            for (SuperElement superElement : list) {
                superElement.showElement(g);
            }
        }

    }
    /**
     * 重写
     * 继承关系的类与类之间的语法现象 多态的一种实现
     * 重写的方法必须与父类的签名一样 返回值 方法名称 参数
     * 重写的方法 访问修饰符可以比父类更加开放
     * 重写的方法抛出异常不可以比父类的更宽
     */
    @Override
    public void run() {
        //死循环 界面会不停止的刷新
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(150);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.repaint();



        }
    }


}
