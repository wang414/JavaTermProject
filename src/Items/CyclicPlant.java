package Items;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class CyclicPlant extends Plant implements Attackable{
    Timer attackCycle;//攻击的循环线程
    //Bullet bullet;//这个植物射出的子弹
    //static String bulletPath; //子弹贴图的路径，考虑把图片放到子弹里面处理 by mxy
    boolean getBullet;//决定此次tryAttack要不要生成子弹
    int bulletId;
    static final int peashSpeed = 10;//平直弹道子弹的固定速度
    public CyclicPlant(int init_hp, int init_speed, int init_atk,
                       double atk_speed, int x_, int y_, int number)
    {
        super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
        getBullet = true;
        attackCycle = new Timer((int) ((double) 1000 * atk_speed), (ActionEvent e)->
        {//周期性地改动getBullet即可
           getBullet = true;
        });

        //bullet = new Bullet(0 , atk, x, y, peashSpeed, 0, 10.0, 0.0);
    }
    @Override
    public boolean canAttack(Zombie z)
    {
        //是否可以进行攻击
        //默认的单行索敌机制: 只对自身所在行且在自身之前的僵尸进行索敌
        return x < z.x + 10/* && y == z.y*/; //已经保证只会搜索本行的僵尸
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
                        res = new Bullet(bulletId, atk, x + 25, y, peashSpeed, 0, 20.0, 0.0); //视情况决定是否返回一个新的子弹
                        getBullet = false;
                    }
                    return res;
                }
                else//没在打
                {
                    attackCycle.start();//开打!
                    res = new Bullet(bulletId, atk, x + 25, y, peashSpeed, 0, 20.0, 0.0);;
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
