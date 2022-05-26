package Battle;
import Items.Plant;
import Items.Zombie;
import Items.SunLight;
import Items.Bullet;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

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
    CopyOnWriteArrayList<Bullet> bullets;
    CopyOnWriteArrayList<SunLight> sunLights;
    CopyOnWriteArrayList<Integer> chosenPlants = new CopyOnWriteArrayList<Integer>();
    int sunLightValue = 100;
    Timer createZombies;
    Timer createSun;
    Timer advanceAll;
    JLayeredPane battlePane;

    SeedBank seedBank;//植物列表
    Plant curPlant;//即将种下去的植物

    static ImageIcon bgImageIcon, sentence1, sentence2, sentence3;
    JLabel bgLabel = new JLabel(bgImageIcon);

    static{
        bgImageIcon = new ImageIcon("out/production/PVZ/img/Background.jpg");
        sentence1 = new ImageIcon("out/production/PVZ/img/SentencePlant.png");
        sentence2 = new ImageIcon("out/production/PVZ/img/SentencePrepare.png");
        sentence3 = new ImageIcon("out/production/PVZ/img/SentenceSet.png");

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

    public MainLoop(JFrame windows)
    {
        /**
         * should be disposed before calling.
         *
         * @Paramater JFrame window: Pass the Main Frame
         */
        //初始化
        window = windows;
        for(int i = 0; i < 5; ++i)
            zombies[i] = new CopyOnWriteArrayList<>();
        for(int i = 0; i < 5; ++i)
            plants[i] = new CopyOnWriteArrayList<>();
        //new Level(curLevel, zombies, window, chosenPlants);
        Init();
        window.dispose();
        window.setLayout(null);
        window.addMouseListener(this);
        battlePane = new JLayeredPane();
        window.setContentPane(battlePane);
        battlePane.add(bgLabel);
        bgLabel.setBounds(-340,0,2100,900);
        battlePane.setPosition(bgLabel, -1);

        createSun = new Timer(5000, (l)->generateSun());
        createSun.start();
        //判定:Compute
        advanceAll = new Timer(30, (e) -> {
            compute();
            window.repaint();
        });
        advanceAll.start();


        //交互内容:
        //植物,铲子的拖动,暂停,阳光拾取


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
        Random rd = new Random();
        int Y = rd.nextInt(100, window.getWidth() - 100);
        int tx = rd.nextInt( 100, window.getHeight() - 100);
        SunLight sunLight = new SunLight(25, 5, 0, Y, tx, Y);
        sunLight.addActionListener((l)->{
            sunLightValue += 25;
            SwingUtilities.invokeLater(()->{
                window.remove(sunLight);
                window.repaint();
            });
        });
        sunLights.add(sunLight);
        SwingUtilities.invokeLater(()->{window.getContentPane().add(sunLight);});
    }


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
                zombie.tryAttack(plants[i]);
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
        JLayeredPane initPane = new JLayeredPane();
        window.setContentPane(initPane);

        JLabel bg = new JLabel(bgImageIcon);
        bg.setSize(2100, 900);
        bg.setOpaque(false);
        bg.setLocation(0, 0);
        initPane.moveToBack(bg);

        //导入关卡信息
        //渲染地图与出没僵尸
        JLabel[] zombies = new JLabel[5];
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            zombies[i] = new JLabel(new ImageIcon("out/production/PVZ/img/Zombie0.png"));
            zombies[i].setLocation(1600 + r.nextInt(300), 100 + r.nextInt(500));
            zombies[i].setSize(231, 200);
            zombies[i].setBackground(null);
            window.getLayeredPane().add(zombies[i]);
            window.getLayeredPane().moveToFront(zombies[i]);
        }

        bgArrived = false;
        bgBacked = false;

        //前往选植物，展示僵尸
        Timer t = new Timer(1, (e) -> {
            SwingUtilities.invokeLater(()->{
                if (!bgArrived && bgLabel.getLocation().x > -900) {
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
            Thread.sleep(3000);
        } catch (Exception e) {
            //TODO: handle exception
        }

        //返回主战场
        t = new Timer(1, (e) -> {
            SwingUtilities.invokeLater(()->{
                if (!bgBacked && bgLabel.getLocation().x < -340) {
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) //判断坐标
    {
        if(e.getX() > 10 && e.getY() > 50)//是否在可种植区域内
        {
            if(curPlant != null)
            {
                curPlant.setX(e.getX());//调整植物坐标
                curPlant.setY(e.getY());
                plants[e.getY() / (900 / 5)].add(curPlant);//种进去
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
}


/**
 * 用非顶层容器JPanel实现，需将JButton的初始化参数改成对应的图片数据，需调整size
 * 初始化需要调用init()方法
 */
class SeedBank extends JPanel
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

    public void init()
    {
        this.setLayout(new GridLayout());
        this.add(Plant1);   this.add(Plant2);
        this.add(Plant3);   this.add(Plant4);
        this.add(Plant5);   this.add(Plant6);
        this.add(Plant7);   this.add(Plant8);
        this.add(Plant9);   this.add(Plant10);

        this.setSize(100, 200);
    }
}