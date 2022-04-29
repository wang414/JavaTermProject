package Items;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//author: zqh
public class SunLight extends JButton {
    public final int sunValue;//阳光提供的阳光值

    int speed;//阳光的下落速度 像素/秒
    int curX;
    int curY;
    int targetX;//阳光停止下落的位置
    int targetY;

    SunLight(int value, int spd, int x, int y,int tx,int ty)
    {
        sunValue = value;
        speed = spd;
        curX = x;
        curY = y;
        targetX = tx;
        targetY = ty;
    }
    void advance()
    {
        if(curY > targetY)
        {
            curY += speed;
            this.setLocation(curX, curY);
        }
    }
}
