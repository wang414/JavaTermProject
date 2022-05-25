package Items;

import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Conehead_zombie extends Zombie {
	static{
		img = new ImageIcon("out/production/PVZ/img/Conehead.gif");
	}
	Conehead_zombie(int init_hp, int init_speed, int init_atk,
		   double atk_speed, int x_, int y_, int number)
	{
		super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
		isEating = false;
	}
	@Override
	public void tryAttack(CopyOnWriteArrayList<Plant> plants)
	{

	}
}
