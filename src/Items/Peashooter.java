package Items;

import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Peashooter extends CyclicPlant implements Attackable{

	static String picPath = "src/img/Peashooter.gif";
	static ImageIcon img;
	static{
		img = new ImageIcon(picPath);
		bulletId = 0;
	}

	public Peashooter(int init_hp, int init_speed, int init_atk,
			   double atk_speed, int x_, int y_, int number)
	{
		super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
		setSize(96, 96);
	}
}
