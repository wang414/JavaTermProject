package Items;

import javax.swing.*;
import java.awt.*;

/**
 * Bullet class
 * @author zqh, wyx
 */

public class Bullet extends JButton {
    int damage;//子弹会造成的伤害,可以修改(火炬树桩)

    int curX;//子弹当前的位置
    int curY;
    int velocityX;//子弹的速度向量
    int velocityY;
    double hitRange;//子弹的碰撞判定范围
    double dmgRange;//子弹的伤害溅射范围
    public Bullet(ImageIcon img, int dmg, int x,int y,int vx, int vy, double hRange, double dRange)
    {
        super(img);//这里构造出子弹的贴图
        damage = dmg;
        curX = x;
        curY = y;
        velocityX = vx;
        velocityY = vy;
        hitRange = hRange;
        dmgRange = dRange;
    }
    public int getDamage(){ return damage; }//damage的setter和getter
    public void setDamage(int newDamage) { damage = newDamage; }
    public void update()//更新子弹的位置
    {
        System.out.println(String.valueOf(curX) + " " + String.valueOf(curY));
        curX += velocityX;
        curY += velocityY;
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
