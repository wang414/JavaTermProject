package Items;

import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Conehead_zombie extends Zombie {
	static ImageIcon img,eatingImg;

	static{
		img = new ImageIcon("src/img/Conehead.gif");
		eatingImg = new ImageIcon("src/img/ConeheadEat.gif");
	}
	public Conehead_zombie(int init_hp, int init_speed, int init_atk,
						   double atk_speed, int x_, int y_, int number)
	{
		super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
		isEating = false;
		setIcon(img);
	}
	boolean healthy = true;
	@Override
	public void setEating() {
		setIcon(eatingImg);

	}

	@Override
	public void setAdvancing() {
		setIcon(img);
	}

	boolean checkHealthy(){
		if(hp > 200)
			return true;
		return false;
	}
}
