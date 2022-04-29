package Battle;
import Items.Plant;
import Items.Zombie;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.*;

/**
 * to paint battles.
 *
 * @author wyx, zqh, mxy
 *
 */


public class MainLoop {

    JFrame window;
    Level curLevel;
    ArrayList<Zombie> []zombies;
    ArrayList<Plant> []plants;
    ArrayList<SunLight> sunLights;
    ArrayList<Bullet> bullets;
    ArrayList<Integer> chosenPlants;
    Timer createZombies;
    Timer createSun;
    Timer rending;
    JLayeredPane battlePane;
    static final String bgPath = "";
    JLabel bgLabel = new JLabel(new ImageIcon(bgPath));


    public MainLoop(JFrame windows)
    {
        /**
         * should be disposed before calling.
         *
         * @Paramater JFrame window: Pass the Main Frame
         */
        //初始化
        window = windows;
        Init();
        window.dispose();
        window.setLayout(null);
        battlePane = new JLayeredPane();
        window.setContentPane(battlePane);
        battlePane.add(bgLabel);
        bgLabel.setBounds(-340,0,2100,900);
        battlePane.setPosition(bgLabel, -1);

        StartgenerateSun();
        //战斗主循环:
        Timer advanceall = new Timer(30, (e) -> {
            compute();
        });
        advanceall.start();
        //判定阶段:Compute

        //交互内容:
        //植物,铲子的拖动,暂停,阳光拾取

        //结束
    }

    //渲染阶段:
    //算时间差, 开始渲染: 清空画布,从头开始画,遍历所有items:
    //地图,植物,僵尸,阳光,小车,选择栏,铲子,暂停,进度条,光标

    /**
     * automatically generate sun lights.
     */

    private void compute()
    {
        //植物的行为:尝试对每一个僵尸发起攻击
        for(int i = 0;i < 5; ++i)
        for(Plant plant : plants[i]){
            Bullet tempBullet = plant.tryAttack(zombies[i]);
            if(tempBullet != null) { bullets.add(tempBullet); }
        }
        for(int i = 0; i < 5; ++i) {
            for (Zombie zombie : zombies[i]) {
                zombie.advance();
            }
        }

    }

    volatile boolean bgArrived, bgBacked; //背景加载地图时移动判定
    private void Init() {
        JLayeredPane initPane = new JLayeredPane();
        window.setContentPane(initPane);

        //导入关卡信息
        //渲染地图与出没僵尸
        //选植物
        //开始战斗
    }
}
