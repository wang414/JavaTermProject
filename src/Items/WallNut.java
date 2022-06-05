package Items;

import javax.swing.*;

public class WallNut extends Plant {
    static String picPath = "src/img/WallNut.gif";
    static ImageIcon img;

    static{
        img = new ImageIcon(picPath);
    }

    public WallNut(int x_, int y_, int number)
    {
        super(1000, 0, 0, 0, x_, y_, number);
        setIcon(img);
        setCost(50);
        setSize(96, 96);
    }
}
