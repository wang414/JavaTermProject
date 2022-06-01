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
	int number;



	public boolean isDead(){
		return  hp <= 0;
	}
	/**
	 * Constructor of abstract class Creature.
	 * Augments hp, speed, atk, atk_speed, x, y, number are initialized by formal parameters.
	 * Augment last_atk is initialized by system's nanotime.
	 * Augment img will be initialized by subclasses.
	 */
	Creature(int init_hp, int init_speed, int init_atk,
			 double init_atk_speed, int x_, int y_, int init_number)
	{
		hp = init_hp;
		speed = init_speed;
		atk = init_atk;
		atk_speed = init_atk_speed;
		last_atk = System.nanoTime();
		x = x_;
		y = y_;
		number = init_number;

		setBorder(null);//除去边框
		setFocusPainted(false);//除去焦点的框
		setContentAreaFilled(false);//除去默认的背景填充
		setLocation(x, y);
	}

	public void setX(int x) {
		this.x = x;
		setLocation(this.x, this.y);
	}
	public void setY(int y) {
		this.y = y;
	}

	public void receiveDamage(int damage){
		hp -= damage;
	}
	public int getDamage() {
		return atk;
	}
}
