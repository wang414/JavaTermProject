package Items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

//author: zqh
public class SunLight extends JButton {
    public final int sunValue;//阳光提供的阳光值

    int speed;//阳光的下落速度 像素/秒
    int curX;
    int curY;
    int targetX;//阳光停止下落的位置
    int targetY;
    long generateTime;
    static ImageIcon img = new ImageIcon("src/img/Sun.gif");

    public SunLight(int value, int spd, int x, int y,int tx,int ty)
    {
        super(img);
        sunValue = value;
        speed = spd;
        curX = x;
        curY = y;
        targetX = tx;
        targetY = ty;
        generateTime = new Date().getTime();
        setSize(110,110);
        setLocation(x, y);


        setBorder(null);//除去边框
        setFocusPainted(false);//除去焦点的框
        setContentAreaFilled(false);//除去默认的背景填充

    }

    public void advance()
    {
        //System.out.println(String.valueOf(curX)+" "+String.valueOf(curY));
        if(curY < targetY)
        {
            curY += speed;
            this.setLocation(curX, curY);
        }
    }
    public long getGenerateTime(){
        return generateTime;
    }
}
