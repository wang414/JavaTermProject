package Items;

import javax.swing.Timer;
import java.util.ArrayList;

/**
 *
 * @author wyx, zqh, mxy
 *
 */
public abstract class Plant extends Creature{

    public abstract boolean canAttack(Zombie z);
    public abstract Bullet tryAttack(ArrayList<Zombie> zombies);//尝试进行一次攻击:
    // 首先进行索敌扫描,如果有敌人,检查attack线程是否开启.没有开启则开启
    // attack线程会自动修改getBullet.
    // 如果此次tryAttack中发现getBullet为true,就返回生成子弹并返回.否则返回一个null

    Plant(int init_hp, int init_speed, int init_atk,
          double atk_speed, int x_, int y_, int number)
    {
        super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
    }
}
