package Items;

import javax.swing.*;
import java.awt.*;

/**
 * Bullet class
 * @author zqh, wyx
 */

public class Bullet extends JButton {
    static ImageIcon[] img = new ImageIcon[3];
    static {
        img[0] = new ImageIcon("out/production/PVZ/img/Pea.gif"); //普通豌豆
        img[1] = new ImageIcon(""); //冰冻豌豆
        img[2] = new ImageIcon(""); //燃烧豌豆
    }

    int damage;//子弹会造成的伤害,可以修改(火炬树桩)

    int curX;//子弹当前的位置
    int curY;
    int velocityX;//子弹的速度向量
    int velocityY;
    double hitRange;//子弹的碰撞判定范围
    double dmgRange;//子弹的伤害溅射范围

    public Bullet(int id, int dmg, int x,int y, int vx, int vy, double hRange, double dRange)
    {
        super(img[id]);//这里构造出子弹的贴图
        damage = dmg;
        curX = x;
        curY = y;
        velocityX = vx;
        velocityY = vy;
        hitRange = hRange;
        dmgRange = dRange;

        setSize(82, 50);
        setLocation(x, y);
        setBorder(null);//除去边框
        setFocusPainted(false);//除去焦点的框
        setContentAreaFilled(false);//除去默认的背景填充
    }
    public int getDamage(){ return damage; }//damage的setter和getter
    public void setDamage(int newDamage) { damage = newDamage; }
    public void update()//更新子弹的位置
    {
        System.out.println(String.valueOf(curX) + " " + String.valueOf(curY));
        curX += velocityX;
        curY += velocityY;
        setLocation(curX, curY);
    }
    public boolean isHit(int targetX, int targetY)//判定子弹是否命中
    {
        return hitRange >= Math.sqrt(
                        Math.pow((double)curX - (double)targetX, 2.0) +
                        Math.pow((double)curY - (double)targetY, 2.0));
    }

    public boolean inRange(int targetX, int targetY)//判定子弹是否造成溅射
    {
        return dmgRange >= Math.sqrt(
                        Math.pow((double)curX - (double)targetX, 2.0) +
                        Math.pow((double)curY - (double)targetY, 2.0));
    }
}
