package Battle;
import Items.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * to paint battles.
 *
 * @author wyx, zqh, mxy
 *
 */


public class MainLoop implements MouseListener, MouseMotionListener{

    JFrame window;
    int curLevel = 0;

    CopyOnWriteArrayList<Zombie> []zombies = new CopyOnWriteArrayList[5];

    CopyOnWriteArrayList<Plant> []plants = new CopyOnWriteArrayList[5];
    CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<SunLight> sunLights = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Integer> chosenPlants = new CopyOnWriteArrayList<Integer>();
    int sunLightValue = 100;
    Timer createZombies;
    Timer createSun;
    Timer advanceAll;
    JLayeredPane battlePane;

    static ImageIcon bgImageIcon, sentence1, sentence2, sentence3;


    static{
        bgImageIcon = new ImageIcon("out/production/PVZ/img/Background.jpg");
        sentence1 = new ImageIcon("out/production/PVZ/img/SentencePrepare.png");
        sentence2 = new ImageIcon("out/production/PVZ/img/SentenceSet.png");
        sentence3 = new ImageIcon("out/production/PVZ/img/SentencePlant.png");

    }


    /**
     * 用于测试
     * @param args
     */
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("植物大战僵尸战斗模块测试");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(1200,900);
        jFrame.setVisible(true);
        new MainLoop(jFrame);
    }

    public MainLoop(JFrame windows){
        /**
         * should be disposed before calling.
         *
         * @Paramater JFrame window: Pass the Main Frame
         */
        //初始化
        window = windows;
        //Init();
        for(int i = 0; i < 5; ++i)
            zombies[i] = new CopyOnWriteArrayList<>();
        for(int i = 0; i < 5; ++i)
            plants[i] = new CopyOnWriteArrayList<>();

        window.dispose();


        // window.addMouseListener(this);
        battlePane = new JLayeredPane();
        window.setContentPane(battlePane);
        window.setLayout(null);
        window.setVisible(true);
        JLabel bgLabel = new JLabel(bgImageIcon);
        bgLabel.setSize(2100, 900);
        //bgLabel.setOpaque(false);
        bgLabel.setLocation(-340, 0);
        battlePane.add(bgLabel);
        battlePane.moveToBack(bgLabel);
        window.repaint();


        battlePane.setPosition(bgLabel, -1);

        new Level(curLevel, zombies, battlePane, chosenPlants);
        //battlePane.add(new JButton(Basic_zombie.img));
        createSun = new Timer(5000, (l)->generateSun());
        createSun.start();
        //判定:Compute
        advanceAll = new Timer(30, (e) -> {
            compute();
            window.repaint();
        });
        advanceAll.start();

        System.out.println("finish");

        //交互内容:
        //植物,铲子的拖动,暂停,阳光拾取
        int i = 10;
        while(i > 9) {
            ;
        }
        //结束
        advanceAll.stop();
        createSun.stop();
        //返回到下一关界面
    }

    //渲染阶段:
    //算时间差, 开始渲染: 清空画布,从头开始画,遍历所有items:
    //地图,植物,僵尸,阳光,小车,选择栏,铲子,暂停,进度条,光标

    /**
     * automatically generate sunlight.
     */
    void generateSun(){
        System.out.println("generate sun");
        Random rd = new Random();
        int X = rd.nextInt(100, window.getWidth() - 100);
        int ty = rd.nextInt( 100, window.getHeight() - 100);
        SunLight sunLight = new SunLight(25, 5, X, 0, X, ty);
        sunLight.addActionListener((l)->{
            sunLightValue += 25;
            sunLights.remove(sunLight);
            SwingUtilities.invokeLater(()->{
                window.remove(sunLight);
                window.repaint();
            });
        });
        sunLights.add(sunLight);
        SwingUtilities.invokeLater(()->{
            battlePane.add(sunLight);
            battlePane.moveToFront(sunLight);
        });

    }


    private void compute()
    {
        //植物的行为:尝试对每一个僵尸发起攻击
//        for(int i = 0;i < 5; ++i)
//            for(Plant plant : plants[i]){
//                Bullet tempBullet = plant.tryAttack(zombies[i]);
//                if(tempBullet != null) { bullets.add(tempBullet); }
//            }
        for(int i = 0; i < 5; ++i) {
            for (Zombie zombie : zombies[i]) {
                if (zombie == null) {
                    System.out.println("Dead Zombie!");
                }
                zombie.advance();
                //zombie.tryAttack(plants[i]);
            }
        }
        long presentTime = new Date().getTime();
        long delay = 10000; //automatically disposed after 10000ms
        for (SunLight sunLight : sunLights) {
            sunLight.advance();
            if (sunLight.getGenerateTime() + delay < presentTime)
                window.getContentPane().remove(sunLight);
        }
        sunLights.removeIf(sunLight -> presentTime > delay + sunLight.getGenerateTime());

    }

    volatile boolean bgArrived, bgBacked; //背景加载地图时移动判定
    private void Init() {
        System.out.println("Start init!");

        JLayeredPane initPane = new JLayeredPane();
        window.setContentPane(initPane);
        window.setLayout(null);
        window.setVisible(true);

        JLabel bg = new JLabel(bgImageIcon);
        bg.setSize(2100, 900);
        bg.setOpaque(false);
        bg.setLocation(0, 0);
        initPane.add(bg);
        initPane.moveToBack(bg);

        //导入关卡信息
        //渲染地图与出没僵尸
        JLabel[] zombies = new JLabel[5];
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            zombies[i] = new JLabel(new ImageIcon("out/production/PVZ/img/Zombie0.gif"));
            zombies[i].setLocation(1600 + r.nextInt(300), 100 + r.nextInt(500));
            zombies[i].setSize(231, 200);
            zombies[i].setBackground(null);

            initPane.add(zombies[i]);
            initPane.moveToFront(zombies[i]);
        }

        bgArrived = false;
        bgBacked = false;

        //前往选植物，展示僵尸
        Timer t = new Timer(1, (e) -> {
            SwingUtilities.invokeLater(()->{
                if (!bgArrived && bg.getLocation().x > -900) {
                    bg.setLocation(bg.getLocation().x - 5, bg.getLocation().y);
                    for (int i = 0; i < 5; i++) {
                        zombies[i].setLocation( zombies[i].getLocation().x - 5, zombies[i].getLocation().y);
                    }
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

        //选植物，展示僵尸
        try {
            sleep(3000);
        } catch (Exception e) {
            //TODO: handle exception
        }

        //返回主战场
        t = new Timer(1, (e) -> {
            SwingUtilities.invokeLater(()->{
                if (!bgBacked && bg.getLocation().x < -340) {
                    bg.setLocation(bg.getLocation().x + 5, bg.getLocation().y);
                    for (int i = 0; i < 5; i++) {
                        zombies[i].setLocation( zombies[i].getLocation().x + 5, zombies[i].getLocation().y);
                    }
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
        JLabel p1 = new JLabel(sentence1);
        JLabel p2 = new JLabel(sentence2);
        JLabel p3 = new JLabel(sentence3);
        p1.setSize(255, 103);
        p2.setSize(255, 103);
        p3.setSize(255, 103);
        p1.setLocation(500, 400);
        p2.setLocation(500, 400);
        p3.setLocation(500, 400);

        initPane.add(p1);
        initPane.moveToFront(p1);
        try {
            sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        initPane.moveToBack(p1);

        initPane.add(p2);
        initPane.moveToFront(p2);
        try {
            sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        initPane.moveToBack(p2);

        initPane.add(p3);
        initPane.moveToFront(p3);
        try {
            sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        initPane.moveToBack(p3);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) //判断坐标
    {


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


/**
 *
 */
class SeedBank extends JFrame
{
    JButton Plant1 = new JButton("Plant1"); // 后期text应当改成以Plant1图片的构造
    JButton Plant2 = new JButton("Plant2"); // 后期text应当改成以Plant2图片的构造
    JButton Plant3 = new JButton("Plant3"); // 后期text应当改成以Plant3图片的构造
    JButton Plant4 = new JButton("Plant4"); // 后期text应当改成以Plant4图片的构造
    JButton Plant5 = new JButton("Plant5"); // 后期text应当改成以Plant5图片的构造
    JButton Plant6 = new JButton("Plant6"); // 后期text应当改成以Plant6图片的构造
    JButton Plant7 = new JButton("Plant7"); // 后期text应当改成以Plant7图片的构造
    JButton Plant8 = new JButton("Plant8"); // 后期text应当改成以Plant8图片的构造
    JButton Plant9 = new JButton("Plant9"); // 后期text应当改成以Plant9图片的构造
    JButton Plant10 = new JButton("Plant10"); // 后期text应当改成以Plant10图片的构造

    public SeedBank()
    {
        super("SeedBank");

        JPanel pnl = new JPanel(new GridLayout());
        pnl.add(Plant1);   pnl.add(Plant2);
        pnl.add(Plant3);   pnl.add(Plant4);
        pnl.add(Plant5);   pnl.add(Plant6);
        pnl.add(Plant7);   pnl.add(Plant8);
        pnl.add(Plant9);   pnl.add(Plant10);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, pnl);
    }
}