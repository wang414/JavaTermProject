package Items;

import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Basic_zombie extends Zombie {
	static ImageIcon img;
	static ImageIcon zombieEat;
	static{
		img = new ImageIcon("src/img/Zombie0.gif");
		zombieEat = new ImageIcon("src/img/ZombieEat.gif");
	}
	public Basic_zombie(int init_hp, int init_speed, int init_atk,
						double atk_speed, int x_, int y_, int number)
	{
		super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
		setIcon(img);
	}

	@Override
	public void setEating() {
		setIcon(zombieEat);
	}

	@Override
	public void setAdvancing() {
		setIcon(img);
	}


}