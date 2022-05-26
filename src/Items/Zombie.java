package Items;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Zombie extends Creature{
	public boolean isEating;

    Zombie(int init_hp, int init_speed, int init_atk,
           double atk_speed, int x_, int y_, int number)
    {
        super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
        isEating = false;
        setSize(231,200);
    }

    public void advance()
    {
        System.out.println(String.valueOf(x)+" "+String.valueOf(y));
        if (isEating == false) {
            x -= speed;
            setLocation(x, y);
        }
    }
    abstract public void tryAttack(CopyOnWriteArrayList<Plant> plants);
}
