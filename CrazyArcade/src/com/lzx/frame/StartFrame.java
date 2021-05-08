package com.lzx.frame;

import com.lzx.thread.GameListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame implements ActionListener {
    private JPanel contentPane;

    public StartFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        setContentPane(contentPane);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setResizable(false);
        setSize(650, 550);
        setTitle("疯狂泡泡");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        init();

    }
    public void init(){
        contentPane.setLayout(null);
        JButton jButton = new JButton("开始游戏");
        jButton.setIcon(new ImageIcon("img/bg/startgame.png"));
        jButton.setBounds(200,380,220,63);
        jButton.setContentAreaFilled(false);
        contentPane.add(jButton);

        JLabel jLabel = new JLabel();
        jLabel.setIcon(new ImageIcon("img/bg/start.png"));
        jLabel.setBounds(0,0,640,480);
        contentPane.add(jLabel);

        jButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        this.setVisible(false);
        //窗体加载
        GameJFrame gameJFrame = new GameJFrame();
        GameJPanel gameJPanel = new GameJPanel();
        GameListener gameListener = new GameListener();

        //监听set注入
        gameJFrame.setKeyListener(gameListener);
        //监听绑定
        gameJFrame.addListener();
        //窗体set注入
        gameJFrame.setJPanel(gameJPanel);
        //窗体绑定
        gameJFrame.addJPanel();

        //监听加载
        //自动化
        gameJFrame.start();
    }
}
