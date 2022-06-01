package Items;

import javax.swing.*;

public final class Sunflower extends Plant {
	static String picPath = "src/img/SunFlower.gif";
	static ImageIcon img;
	static{
		img = new ImageIcon(picPath);
	}
	public Sunflower(int init_hp, int x_, int y_, int number)
	{
		super(init_hp, 0, 0, 0, x_, y_, number);
		setIcon(img);
	}

}
