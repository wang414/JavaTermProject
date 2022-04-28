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
        //初始化:init

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
}
