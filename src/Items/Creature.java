package Items;

import javax.swing.*;

/** @author yby */

public abstract class Creature extends JButton {
	int hp;
	int speed;
	int atk;
	double atk_speed;
	long last_atk;
	int x;
	int y;
	String picture_path;
	int number;
}
