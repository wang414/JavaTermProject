package Items;

import javax.swing.*;

public class Shroom extends Plant {
    static String picPath = "src/img/Shroom.gif";
    static ImageIcon img;

    static{
        img = new ImageIcon(picPath);
    }

    public Shroom(int x_, int y_, int number)
    {
        super(300, 0, 0, 0, x_, y_, number);
        setIcon(img);
        setCost(0);
        setSize(96, 96);
    }
}
