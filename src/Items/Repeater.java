package Items;

import javax.swing.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class Repeater extends CyclicPlant implements Attackable {
    static String picPath = "src/img/Repeater.gif";
    static ImageIcon img;
    static{
        img = new ImageIcon(picPath);
        bulletId = 0;
    }
    static final double atkSpeed = 1.5;

    boolean repeatBullet;

    public Repeater(int x_, int y_, int number)
    {
        super(300, 0, 20, atkSpeed, x_, y_, number);
        setSize(96, 96);
        setIcon(img);
        setCost(200);
        repeatBullet = false;
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
                        Timer timer = new Timer(175, (l)->{
                            repeatBullet = true;
                        });
                        timer.setRepeats(false);
                        timer.start();

                        getBullet = false;
                        res = new Bullet(bulletId, atk, x + 25, y, peashSpeed, 0, 20.0, 0.0); //视情况决定是否返回一个新的子弹
                    } else if (repeatBullet) {
                        res = new Bullet(bulletId, atk, x + 25, y, peashSpeed, 0, 20.0, 0.0); //视情况决定是否返回一个新的子弹
                        repeatBullet = false;
                    }
                    return res;
                }
                else//没在打
                {
                    attackCycle.start();//开打!
                    getBullet = false;
                    Timer timer = new Timer(175, (l)->{
                        repeatBullet = true;
                    });
                    timer.setRepeats(false);
                    timer.start();
                    res = new Bullet(bulletId, atk, x + 25, y, peashSpeed, 0, 20.0, 0.0);;
                    return res;
                }
            }
        }
        //一个能打的都没有,停止攻击
        attackCycle.stop();

        return res;
    }
}
