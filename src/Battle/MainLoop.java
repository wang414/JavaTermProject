package Battle;
import Items.Plant;
import Items.Zombie;
import Items.SunLight;
import Items.Bullet;
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
    int sunLightValue;
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
        bgArrived = false;
        bgBacked = false;

        Timer t = new Timer(1, (e) -> {
            SwingUtilities.invokeLater(()->{
                if (!bgArrived && bgLabel.getLocation().x > -900) {
                    bgLabel.setLocation(bgLabel.getLocation().x - 5, bgLabel.getLocation().y);
                } else {
                    bgArrived = true;
                }
            });
        });

        t.start();
        while (!bgArrived) {
            bgBacked = false;
        }
        t.stop();

        //选植物
        t = new Timer(1, (e) -> {
            SwingUtilities.invokeLater(()->{
                if (!bgBacked && bgLabel.getLocation().x < -340) {
                    bgLabel.setLocation(bgLabel.getLocation().x - 5, bgLabel.getLocation().y);
                } else {
                    bgBacked = true;
                }
            });
        });
        t.start();
        while (!bgBacked) {
            bgArrived = false;
        }
        t.stop();

        //开始战斗
        JLabel p1 = new JLabel(new ImageIcon("PrepareGrowPlants1.png"));
        JLabel p2 = new JLabel(new ImageIcon("PrepareGrowPlants2.png"));
        JLabel p3 = new JLabel(new ImageIcon("PrepareGrowPlants3.png"));
        p1.setSize(255, 103);
        p2.setSize(255, 103);
        p3.setSize(255, 103);
        p1.setLocation(500, 400);
        p2.setLocation(500, 400);
        p3.setLocation(500, 400);

        initPane.add(p1);
        initPane.moveToFront(p1);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        initPane.moveToBack(p1);

        initPane.add(p2);
        initPane.moveToFront(p2);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        initPane.moveToBack(p2);

        initPane.add(p3);
        initPane.moveToFront(p3);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        initPane.moveToBack(p3);
    }
}
