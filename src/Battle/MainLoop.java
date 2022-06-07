package Battle;

import Items.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * to paint battles.
 *
 * @author wyx, zqh, mxy
 */


public class MainLoop implements MouseListener, MouseMotionListener {

    JFrame window;
    int curLevel = 0;

    static ImageIcon[] bgImageIcon = new ImageIcon[3];
    static ImageIcon sentence1, sentence2, sentence3;

    static {
        bgImageIcon[0] = new ImageIcon("src/img/Background0.jpg");
        bgImageIcon[1] = new ImageIcon("src/img/Background1.jpg");
        bgImageIcon[2] = new ImageIcon("src/img/Win.gif");
        sentence1 = new ImageIcon("src/img/SentencePrepare.png");
        sentence2 = new ImageIcon("src/img/SentenceSet.png");
        sentence3 = new ImageIcon("src/img/SentencePlant.png");

    }

    CopyOnWriteArrayList<Zombie>[] zombies = new CopyOnWriteArrayList[5];
    CopyOnWriteArrayList<SunLight> sunLights = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Integer> chosenPlants = new CopyOnWriteArrayList<Integer>();
    boolean[][] hasPlanted = new boolean[5][9];
    AtomicInteger sunLightValue = new AtomicInteger(75);
    Timer createZombies;
    Timer createSun;
    Timer advanceAll;
    JLayeredPane battlePane;

    SeedBank seedBank = new SeedBank();//植物列表
    Plant curPlant = null;//即将种下去的植物

    final AtomicInteger gameOver = new AtomicInteger(0);
    public MainLoop(JFrame windows, int curL) {
        /**
         * should be disposed before calling.
         *
         * @Paramater JFrame window: Pass the Main Frame
         */
        //初始化
        window = windows;
        curLevel = curL;
        Init(curLevel);
        for (int i = 0; i < 5; ++i) {
            zombies[i] = new CopyOnWriteArrayList<>();
            plants[i] = new CopyOnWriteArrayList<>();
            bullets[i] = new CopyOnWriteArrayList<>();
        }


        window.dispose();

        window.addMouseMotionListener(this);
        window.addMouseListener(this);
        battlePane = new JLayeredPane();
        window.setContentPane(battlePane);
        window.setLayout(null);
        window.setVisible(true);
        JLabel bgLabel = new JLabel(bgImageIcon[curLevel]);
        bgLabel.setSize(2100, 900);
        //bgLabel.setOpaque(false);
        bgLabel.setLocation(-340, 0);
        battlePane.add(bgLabel);
        battlePane.moveToBack(bgLabel);
        window.repaint();
        Sunflower.setBattlePanel(battlePane);
        Sunflower.setSunLights(sunLights);
        Sunflower.setSunLightvalue(sunLightValue);
        SunShr.setBattlePanel(battlePane);
        SunShr.setSunLights(sunLights);
        SunShr.setSunLightvalue(sunLightValue);

        for (int i = 0; i < 5; i++) {
            cars[i] = new Car(-60, 150 + i * 150);
        }
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 5; i++) {
                battlePane.add(cars[i]);
                battlePane.moveToFront(cars[i]);
            }
        });

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                hasPlanted[i][j] = false;
            }
        }
        seedBank = new SeedBank();
        seedBank.init();
        SwingUtilities.invokeLater(() -> {
            battlePane.add(seedBank);
            battlePane.moveToFront(seedBank);
        });

        battlePane.setPosition(bgLabel, -1);
        Level level = new Level(curLevel, zombies, battlePane, chosenPlants, gameOver);
        Thread t = level.getThread();
        t.start();
        //battlePane.add(new JButton(Basic_zombie.img));
        createSun = new Timer(5000, (l) -> generateSun());
        if (curLevel == 0)
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
        synchronized (gameOver) {
            while (!(gameOver.get() == -1 || (gameOver.get() == 1 && isZombieEmpty()))) {
                try {
                    wait();
                } catch (Exception ignored) {
                }
            }
        }
        //window.dispose();
        for (int i = 0; i < 5; ++i) {
            for (Plant plant : plants[i]) {
                plant.receiveDamage(10000);
            }
            for (Zombie zombie : zombies[i]) {
                zombie.receiveDamage(10000);
            }
        }
        compute();
        t.stop();
        advanceAll.stop();
        if (curLevel == 0)
            createSun.stop();
        if (gameOver.get() == -1) {
            System.out.println("you are dead");
            JLabel dead = new JLabel(new ImageIcon("src/img/Dead.png"));
            dead.setLocation(0, 0);
            dead.setSize(1085, 900);
            SwingUtilities.invokeLater(() -> {
                battlePane.add(dead);
                battlePane.moveToFront(dead);
            });
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else
            System.out.println("you win");
        System.out.flush();


        //结束


        if (gameOver.get() == 1) {
            System.out.println("Win Time！！");
            JLabel lb = new JLabel(bgImageIcon[2]);
            lb.setSize(1200, 900);
            lb.setLocation(0, 0);
            lb.setVisible(true);
            battlePane.add(lb);
            battlePane.moveToFront(lb);
            //window.add(lb);
            long preTime = new Date().getTime();
            while (preTime + 4000 > new Date().getTime())
                ;

            if (curLevel == 0)
                new Thread(() -> new MainLoop(windows, 1)).start();

        } else {
            System.exit(0);
        }
        windows.dispose();
        //返回到下一关界面
    }

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
        new MainLoop(jFrame, 1);
    }

    private boolean isZombieEmpty() {
        boolean flg = true;
        for (int i = 0 ; i < 5; ++i){
            if (zombies[i].size() != 0){
                flg = false;
                break;
            }
        }
        return flg;
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
        int X = rd.nextInt(200, window.getWidth() - 100);
        int ty = rd.nextInt(300, window.getHeight() - 100);
        SunLight sunLight = new SunLight(25, 5, X, -100, X, ty);
        sunLight.addActionListener((l) -> {
            sunLightValue.addAndGet(25);
            sunLights.remove(sunLight);
            SwingUtilities.invokeLater(() -> {
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
                if (plant.needAttack()) {
                    //需要进行一次攻击
                    for (int j = 0; j < 5; j++) {
                        for (Zombie zombie : zombies[j]) {
                            plant.canAttack(zombie);
                        }
                    }
                }

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
                if (zombie.isArriveHouse()) {
                    System.out.println("arrive at house");
                    gameOver.set(-1);
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (cars[i].isUsed() == false) {
                cars[i].update();
                for (Zombie zombie : zombies[i]) {
                    if (cars[i].isHit(zombie)) {
                        zombie.receiveDamage(cars[i].getDamage());
                        cars[i].start();
                    }
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
                    hasPlanted[((plant.getY() - 120) / 150)][((plant.getX() - 40) / 120)] = false;
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
        seedBank.sunValueText.setText(sunLightValue.toString());

    }

    volatile boolean bgArrived, bgBacked; //背景加载地图时移动判定
    private void Init(int number) {
        System.out.println("Start init!");

        JLayeredPane initPane = new JLayeredPane();
        window.setContentPane(initPane);
        window.setLayout(null);
        window.setVisible(true);

        JLabel bg = new JLabel(bgImageIcon[number]);
        bg.setSize(2100, 900);
        bg.setOpaque(false);
        bg.setLocation(0, 0);
        initPane.add(bg);
        initPane.moveToBack(bg);

        //导入关卡信息
        //渲染地图与出没僵尸
        JLabel[] zombies = new JLabel[7];
        Random r = new Random();
        for (int i = 0; i < 7; i++) {
            if (i < 5) {
                zombies[i] = new JLabel(new ImageIcon("src/img/Zombie0.gif"));
            } else {
                zombies[i] = new JLabel(new ImageIcon("src/img/Conehead.gif"));
            }
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
                    for (int i = 0; i < 7; i++) {
                        zombies[i].setLocation(zombies[i].getLocation().x - 5, zombies[i].getLocation().y);
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
                    for (int i = 0; i < 7; i++) {
                        zombies[i].setLocation(zombies[i].getLocation().x + 5, zombies[i].getLocation().y);
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
            if(curPlant != null && !hasPlanted[((e.getY() - 120) / 150)][((e.getX() - 40) / 120)])
            {
                if(sunLightValue.get() >= curPlant.getCost())
                {

                    System.out.println("plant");
                    curCard.setBlack();
                    sunLightValue.getAndAdd(-curPlant.getCost());
                    hasPlanted[((e.getY() - 120) / 150)][((e.getX() - 40) / 120)] = true;
                    curPlant.setX(((e.getX() - 40) / 120) * 120 + 60);
                    curPlant.setY(((e.getY() - 120) / 150) * 150 + 150);

                    //curPlant.setSize(96,96);
                    plants[(e.getY() - 120) / ((900 - 120) / 5)].add(curPlant);//加入后台植物清单
                    //System.out.println(e.getY() / (900 / 5));
                    //JButton tmpPlant = new JButton(curPlant.getIcon());//准备绘制
                    curPlant.planted();
                    final Plant tmpPlant = curPlant;
                    SwingUtilities.invokeLater(() -> {
                        battlePane.add(tmpPlant);//将植物塞入战斗图层
                        battlePane.moveToFront(tmpPlant);
                        window.repaint();
                    });
                    curPlant = null;
                }
            }
        }
    }

    Card curCard;

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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (usingShovel) {
            movingShovel.setLocation(e.getX() - movingShovel.getWidth() / 2, e.getY() - movingShovel.getHeight());
            movingShovel.setVisible(true);
            battlePane.moveToFront(movingShovel);
        } else {
            movingShovel.setVisible(false);
        }
    }

    /**
     * 用非顶层容器JPanel实现，需将JButton的初始化参数改成对应的图片数据，需调整size
     * 初始化需要调用init()方法
     */
    class SeedBank extends JPanel {
        static ImageIcon img_seedbank = new ImageIcon("src/img/SeedBank.png");

        Card[] Plants = new Card[6];
        JButton sunValueText = new JButton(sunLightValue.toString());
        JLabel image = new JLabel(img_seedbank);

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

            sunValueText.setLocation(25, 90);
            sunValueText.setSize(60, 30);
            sunValueText.setFont(new Font("微软雅黑", Font.BOLD, 18));
            sunValueText.setContentAreaFilled(false);
            sunValueText.setBorder(null);

            this.add(sunValueText);
            for (int i = 0; i < 6; ++i) {
                Plants[i] = new Card();
                Plants[i].setBorder(null);//除去边框
                Plants[i].setFocusPainted(false);//除去焦点的框
                Plants[i].setContentAreaFilled(false);//除去默认的背景填充

            }
            Plants[0].setImg0(new ImageIcon("src/img/SunFlower0.png"));
            Plants[1].setImg0(new ImageIcon("src/img/Peashooter0.png"));
            Plants[2].setImg0(new ImageIcon("src/img/CherryBomb0.png"));
            Plants[3].setImg0(new ImageIcon("src/img/WallNut0.png"));
            Plants[4].setImg0(new ImageIcon("src/img/Repeater0.png"));
            Plants[5].setImg0(new ImageIcon("src/img/Shroom0.png"));
            Plants[0].setImg2(new ImageIcon("src/img/SunFlower2.png"));
            Plants[1].setImg2(new ImageIcon("src/img/Peashooter2.png"));
            Plants[2].setImg2(new ImageIcon("src/img/CherryBomb2.png"));
            Plants[3].setImg2(new ImageIcon("src/img/WallNut2.png"));
            Plants[4].setImg2(new ImageIcon("src/img/Repeater2.png"));
            Plants[5].setImg2(new ImageIcon("src/img/Shroom2.png"));

            if (curLevel == 1) {
                Plants[0].setImg0(new ImageIcon("src/img/SunShr0.png"));
                Plants[0].setImg2(new ImageIcon("src/img/SunShr2.png"));
            }

            for (int i = 0; i < 6; ++i) {
                Plants[i].useImg0();
            }


            Plants[0].addActionListener(e -> {
                if (Plants[0].canPlant) {
                    if (curLevel == 1)
                        curPlant = new SunShr(1, 1, curLevel);
                    else
                        curPlant = new Sunflower(0, 20, curLevel);
                    curCard = Plants[0];
                }

            });
            Plants[1].addActionListener(e -> {
                if (Plants[1].canPlant) {
                    curPlant = new Peashooter(1, 1, curLevel);
                    curCard = Plants[1];
                }
            });
            Plants[2].addActionListener(e -> {
                if (Plants[2].canPlant) {
                    curPlant = new CherryBomb(1, 1, curLevel);
                    curCard = Plants[2];
                }
            });
            Plants[3].addActionListener(e -> {
                if (Plants[3].canPlant) {
                    curPlant = new WallNut(1, 1, curLevel);
                    curCard = Plants[3];
                }
            });
            Plants[4].addActionListener(e -> {
                if (Plants[4].canPlant) {
                    curPlant = new Repeater(1, 1, curLevel);
                    curCard = Plants[4];
                }
            });
            Plants[5].addActionListener(e -> {
                if (Plants[5].canPlant) {
                    curPlant = new Shroom(1, 1, curLevel);
                    curCard = Plants[5];
                }
            });
            int num = 1;
            for (JButton btn : Plants)
            {
                btn.setSize(79, 110);
                btn.setLocation(23 + 90 * (num++), 8);
                this.add(btn);
                //btn.setBorder(null);//除去边框
                //btn.setFocusPainted(false);//除去焦点的框
                //btn.setContentAreaFilled(false);//除去默认的背景填充
            }

            this.setLayout(null);
            this.add(image);
            //this.add(pnl);

            this.setVisible(true);

        }
    }
    static class Card extends JButton{
        ImageIcon[] img = new ImageIcon[3];
        int waitingTime = 7500;
        void setImg0(ImageIcon img){
            this.img[0] = img;
        }
        void setImg2(ImageIcon img){
            this.img[2] = img;
        }
        void useImg0(){
            SwingUtilities.invokeLater(()->setIcon(img[0]));
            canPlant = true;
        }
        void useImg2(){
            SwingUtilities.invokeLater(()->setIcon(img[2]));
            canPlant = false;
        }
        Boolean canPlant = true;
        void setBlack(){
            useImg2();
            Timer timer = new Timer(waitingTime, (l)->{useImg0();});
            timer.setRepeats(false);
            timer.start();
        }
    }
}



