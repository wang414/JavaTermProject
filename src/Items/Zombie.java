package Items;

import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Zombie extends Creature{
	public boolean isEating;
    static ImageIcon zombieDie;

    static {
        zombieDie = new ImageIcon("src/img/ZombieDie.gif");
    }

    Zombie(int init_hp, int init_speed, int init_atk,
           double atk_speed, int x_, int y_, int number)
    {
        super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
        isEating = false;
        setSize(231,200);
        setLocation(x - 100, y - 75);
    }

    public void advance()
    {
        //System.out.println("present HP is "+String.valueOf(hp));
        if (isEating == false) {
            x -= speed;
            setLocation(x - 100, y - 75);
        }
    }
    public boolean isArriveHouse() {
        if (x < -100)
            return true;
        return false;
    }

    public boolean isHitting(Plant plant) {
        return x >= plant.x + 10 && x <= plant.x  + 30;
    }

    abstract public void setEating();

    abstract public void setAdvancing();


}
