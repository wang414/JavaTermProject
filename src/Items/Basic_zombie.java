package Items;

import javax.swing.*;

public final class Basic_zombie extends Zombie {
	static String picPath = "";
	static{
		img = new ImageIcon(picPath);
	}
	Basic_zombie(int init_hp, int init_speed, int init_atk,
				 double atk_speed, int x_, int y_, int number)
	{
		super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
	}
}