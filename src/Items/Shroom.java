package Items;

import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Shroom extends CyclicPlant {
    static String picPath = "src/img/Shroom.gif";
    static String WakePicPath = "src/img/ShroomWake.gif";
    static ImageIcon img, imgWake;

    static{
        img = new ImageIcon(picPath);
        imgWake = new ImageIcon(WakePicPath);
    }

    public Shroom(int x_, int y_, int number)
    {
        super(300, 0, 20, 1.5, x_, y_, number);
        setIcon(img);
        setCost(0);
        setSize(96, 96);

        bulletId = 3;
        if (number == 1) {
            setIcon(imgWake);
        }
    }

    @Override
    public void setY(int y) {
        super.setY(y - 20);
    }

    @Override
    public boolean canAttack(Zombie z)
    {
        if (number == 0) { //如果在白天，就无法攻击
            return false;
        }
        //是否可以进行攻击
        //默认的单行索敌机制: 只对自身所在行且在自身之前的僵尸进行索敌
        return x < z.x + 10 && x + 375 > z.x/* && y == z.y*/; //已经保证只会搜索本行的僵尸
    }

    @Override
    public Bullet tryAttack(CopyOnWriteArrayList<Zombie> zombies)
    {
        //对僵尸尝试进行攻击
        Bullet res = null;

        for(int i = 0;i < zombies.size();i++)//对于每一个僵尸
        {
            if(canAttack(zombies.get(i))) //如果能够打到
            {
                if(attackCycle.isRunning())//在打了在打了
                {
                    if(getBullet) {
                        res = new Bullet(bulletId, atk, x + 25, y + 45, peashSpeed, 0, 20.0, 0.0); //视情况决定是否返回一个新的子弹
                        getBullet = false;
                    }
                    return res;
                }
                else//没在打
                {
                    attackCycle.start();//开打!
                    res = new Bullet(bulletId, atk, x + 25, y + 45, peashSpeed, 0, 20.0, 0.0);;
                    getBullet = false;
                    return res;
                }
            }
        }
        //一个能打的都没有,停止攻击
        attackCycle.stop();

        return res;
    }
}
