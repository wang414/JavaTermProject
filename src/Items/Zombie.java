package Items;
public abstract class Zombie extends Creature{
	public boolean isEating;

    Zombie(int init_hp, int init_speed, int init_atk,
           double atk_speed, int x_, int y_, int number)
    {
        super(init_hp, init_speed, init_atk, atk_speed, x_, y_, number);
        isEating = false;
    }

    public void advance()
    {
        if (isEating == false) {
            x -= speed;
            setLocation(x, y);
        }
    }
}
