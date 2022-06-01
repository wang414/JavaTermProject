package Battle;
import Items.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

/**
 * to paint battles.
 *
 * @author wyx, zqh, mxy
 *
 */


public class MainLoop implements MouseListener{

    JFrame window;
    int curLevel = 0;

    CopyOnWriteArrayList<Zombie> []zombies = new CopyOnWriteArrayList[5];

    CopyOnWriteArrayList<Plant> []plants = new CopyOnWriteArrayList[5];
    CopyOnWriteArrayList<Bullet> []bullets = new CopyOnWriteArrayList[5];
    CopyOnWriteArrayList<SunLight> sunLights = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Integer> chosenPlants = new CopyOnWriteArrayList<Integer>();
    int sunLightValue = 100;
    Timer createZombies;
    Timer createSun;
    Timer advanceAll;
    JLayeredPane battlePane;

    SeedBank seedBank;//植物列表
    Plant curPlant;//即将种下去的植物

    static ImageIcon bgImageIcon, sentence1, sentence2, sentence3;


    static{
        bgImageIcon = new ImageIcon("src/img/Background.jpg");
        sentence1 = new ImageIcon("src/img/SentencePrepare.png");
        sentence2 = new ImageIcon("src/img/SentenceSet.png");
        sentence3 = new ImageIcon("src/img/SentencePlant.png");

    }

    final AtomicBoolean gameover = new AtomicBoolean(false);
    /**
     * 用于测试
     * @param args
     */
    public static void main(String[] args)
    {
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
        for(int i = 0; i < 5; ++i) {
            zombies[i] = new CopyOnWriteArrayList<>();
            plants[i] = new CopyOnWriteArrayList<>();
            bullets[i] = new CopyOnWriteArrayList<>();
        }



        window.dispose();


        window.addMouseListener(this);
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

        SeedBank seedbank = new SeedBank();
        seedbank.init();
        SwingUtilities.invokeLater(()-> {
            battlePane.add(seedbank);
            battlePane.moveToFront(seedbank);
        });

        battlePane.setPosition(bgLabel, -1);
        Level level = new Level(curLevel, zombies, battlePane, chosenPlants);
        Thread t = level.getThread();
        t.start();
        //battlePane.add(new JButton(Basic_zombie.img));
        createSun = new Timer(5000, (l)->generateSun());
        createSun.start();

        //debug
//        for (int i = 0; i < 5; i++) {
//            Peashooter tmpShooter = new Peashooter(100, 0, 20, 1, 1100, 120 + i * 150, 0);
//            plants[i].add(tmpShooter);
//            battlePane.add(tmpShooter);
//            battlePane.moveToFront(tmpShooter);
//        }

        //判定:Compute
        advanceAll = new Timer(30, (e) -> {
            compute();
            window.repaint();
        });
        advanceAll.start();

        System.out.println("finish");

        //交互内容:
        //植物,铲子的拖动,暂停,阳光拾取
        synchronized (gameover) {
            while (gameover.get()==false) {
                try {
                    wait();
                } catch (Exception ignored) {
                }
            }
            //window.dispose();
            System.out.println("you are dead");
            System.out.flush();
        }

        //结束
        advanceAll.stop();
        createSun.stop();
        t.stop();
        //System.exit(0);
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
        SunLight sunLight = new SunLight(25, 5, X, -100, X, ty);
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
        for(int i = 0;i < 5; ++i) {
            for (Plant plant : plants[i]) {


                Bullet tempBullet = plant.tryAttack(zombies[i]);
                if (tempBullet != null) {
                    //System.out.println("Bullet Created!");
                    bullets[i].add(tempBullet);
                    battlePane.add(tempBullet);
                    battlePane.moveToFront(tempBullet);
                }
            }
        }

        for(int i = 0; i < 5; ++i) {
            for (Zombie zombie : zombies[i]) {
                if (zombie == null) {
                    System.out.println("Dead Zombie!");
                }

                boolean advance = true;
                for (Plant plant : plants[i]) {
                    if (zombie.isHitting(plant)) {
                        if (!zombie.isEating) {
                            zombie.setEating();
                        }
                        zombie.isEating = true;
                        advance = false;
                        plant.receiveDamage(zombie.getDamage());
                    }
                }

                if (advance == false) { //被植物挡住不能前进
                    continue;
                }

                if (zombie.isEating) {
                    zombie.setAdvancing();
                }
                zombie.isEating = false;
                zombie.advance();
                if (zombie.isArriveHouse())
                {
                    System.out.println("arrive at house");
                    gameover.set(true);
                }
            }
        }



        for (int i = 0; i < 5; i++) {
            for (Bullet bullet : bullets[i]) {
                //System.out.println("Bullet Update!");
                bullet.update();
                for (Zombie zombie : zombies[i]) {
                    if (bullet.isHit(zombie)) {
                        //bullets[i].remove(bullet);
                        bullet.hitZombie(zombie);
                    }
                }
            }
        }

        //处理死去的僵尸
        for (int i = 0; i < 5; ++i) {
            for (Zombie zombie : zombies[i]) {
                if (zombie.isDead()) {
                    zombies[i].remove(zombie);
                    battlePane.remove(zombie);
                }
            }
        }
        //处理用过的植物子弹
        for (int i = 0; i < 5; ++i) {
            for (Bullet bullet : bullets[i]) {
                if (bullet.isUsed()) {
                    bullets[i].remove(bullet);
                    battlePane.remove(bullet);
                }
            }
        }
        //处理死去的植物
        for (int i = 0; i < 5; ++i) {
            for (Plant plant : plants[i]) {
                if (plant.isDead()) {
                    plants[i].remove(plant);
                    battlePane.remove(plant);
                }
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
            zombies[i] = new JLabel(new ImageIcon("src/img/Zombie0.gif"));
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
    public void mouseClicked(MouseEvent e) //判断坐标
    {
        //System.out.println("clicking");
        if(e.getY() > 120 && e.getX() > 40)//是否在可种植区域内
        {
            if(curPlant != null)
            {
                System.out.println("print");
                curPlant.setX(((e.getX() - 40) / 120) * 120 + 60);
                curPlant.setY(((e.getY() - 120) / 150) * 150 + 150);
                curPlant.setSize(96,96);
                plants[(e.getY() - 120) / ((900 - 120) / 5)].add(curPlant);//加入后台植物清单
                //System.out.println(e.getY() / (900 / 5));
                //JButton tmpPlant = new JButton(curPlant.getIcon());//准备绘制
                final Plant tmpPlant = curPlant;
                SwingUtilities.invokeLater(()->{
                    battlePane.add(tmpPlant);//将植物塞入战斗图层
                    battlePane.moveToFront(tmpPlant);
                    window.repaint();
                });
                curPlant = null;
            }
        }
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

    /**
     * 用非顶层容器JPanel实现，需将JButton的初始化参数改成对应的图片数据，需调整size
     * 初始化需要调用init()方法
     */
    class SeedBank extends JPanel
    {
        JButton[] Plants = new JButton[10];

        public void init()
        {
            image.setSize(656, 128);
            image.setLocation(0, 0);

            this.setSize(656, 128);
            this.setLocation(0, 0);
            /*
            image.setBorder(null);//除去边框
            image.setFocusPainted(false);//除去焦点的框
            image.setContentAreaFilled(false);//除去默认的背景填充
            */

            for (int i = 0; i < 10; ++i)
                Plants[i] = new JButton("Plants" + i);

            JPanel pnl = new JPanel();
            pnl.setSize(665, 100);
            pnl.setLocation(200, 10);
            pnl.setLayout(null);

            int num = 1;
            for (JButton btn : Plants)
            {
                btn.setSize(60, 90);
                btn.setLocation(60 * (num++), 5);
                pnl.add(btn);
                btn.setBorder(null);//除去边框
                btn.setFocusPainted(false);//除去焦点的框
                btn.setContentAreaFilled(false);//除去默认的背景填充
            }

            this.setLayout(null);
            this.add(pnl);

            this.setVisible(true);

        }
    }
}



