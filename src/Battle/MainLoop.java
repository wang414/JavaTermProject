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
    ArrayList<Zombie> zombies;
    ArrayList<Plant> plants;
    ArrayList<Integer> chosenPlants;
    Timer createZombies;
    Timer createSun;
    Timer rending;
    Render battlePanel;
    static String bgImg = "";
    public MainLoop(JFrame windows)
    {
        /**
         * should be disposed before calling.
         *
         * @Paramater main windows
         */
        //初始化:init
        window = windows;
        Init();
        windows.dispose();
        windows.setLayout(null);
        initMenu();
        initBattle();
        battlePanel = new Render();
        windows.getLayeredPane().add(battlePanel);
        //战斗主循环:
        //渲染阶段:render
        rending = new Timer(60, (ActionEvent e)->
        {
            battlePanel.repaint();
        });
        //判定阶段:Compute

        //交互内容:
        //植物,铲子的拖动,暂停,阳光拾取

        //结束
    }
    private void initMenu(){
    }
    private void initBattle(){

    }
    class Render extends JLayeredPane
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            //绘制背景
            Image bg = curLevel.Background;
            g.drawImage(bg, 0, 0, null);

            //绘制功能栏+铲子+暂停

            //绘制植物+小车
            for(int i = 0;i < plants.size();i++)
            {
                g.drawImage(plants.get(i).img, plants.get(i).x, 0, null);
            }

            //绘制僵尸
            for(int i = 0;i < zombies.size();i++)
            {
                g.drawImage(zombies.get(i).img, 0, 0, null);
            }

            //绘制子弹

            //绘制阳光
        }
        //渲染阶段:
        //算时间差, 开始渲染: 清空画布,从头开始画,遍历所有items:
        //地图,植物,僵尸,阳光,小车,选择栏,铲子,暂停,进度条,光标
    }
    private void Init(){
        //导入关卡信息
        //渲染地图与出没僵尸
        //选植物
        //开始战斗
    }
}
