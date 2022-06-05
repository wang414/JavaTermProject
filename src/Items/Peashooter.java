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
	static final double atkSpeed = 1.5;
	public Peashooter(int x_, int y_, int number)
	{
		super(300, 0, 20, atkSpeed, x_, y_, number);
		setSize(96, 96);
		setIcon(img);
		setCost(100);
	}
}
