package Items;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author wyx, zqh, mxy
 *
 */
public abstract class Plant extends Creature{

    /**
     * 返回这个植物是否有攻击性，默认为无攻击性
     * 如果有攻击性需要override
     *
     * @return true:有攻击性, false:无攻击性
     */
    public boolean isAttackable() {return false;}

    /**
     * 首先进行索敌扫描,如果有敌人,检查attack线程是否开启.没有开启则开启
     * attack线程会自动修改getBullet.
     * 如果此次tryAttack中发现getBullet为true,就返回生成子弹并返回.否则返回一个null
     * 默认为无法攻击，如果攻击需要override
     *
     * @param zombies array of arraylist matches to the alive zombies
     * @return
     */
    public Bullet tryAttack(CopyOnWriteArrayList<Zombie> zombies)   {return null;}


    /**
     * 尝试进行一次攻击:
     * 默认为无法攻击，如果攻击需要override
     *
     * @param z
     * @return
     */
    public boolean canAttack(Zombie z) {return false;};

    /**
     * 是否应该进行一次攻击
     *
     * @return
     */
    public boolean needAttack() {return false;};

    /**
     * 表示植物已经种下了
     *
     */
    public void planted() {};


    Plant(int init_hp, int init_speed, int init_atk,
          double atk_speed, int x_, int y_, int number)
    {
        super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
    }
}
