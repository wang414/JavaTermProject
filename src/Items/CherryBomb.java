package Items;

import javax.swing.*;

import static java.lang.Thread.sleep;

public class CherryBomb extends Plant {
    static String picPath = "src/img/CherryBomb.gif";
    static ImageIcon img, boom;
    static{
        img = new ImageIcon(picPath);
        boom = new ImageIcon("src/img/Boom.png");
    }

    boolean needAttackFlag;

    public CherryBomb(int x_, int y_, int number)
    {
        super(300, 0, 1800, 0, x_, y_, number);
        setIcon(img);
        setCost(150);
        setSize(133, 96);
        needAttackFlag = false;
    }

    @Override
    public void planted() {
        Timer timer0 = new Timer(250, (l)->{
            hp = -1;
        });
        timer0.setRepeats(false);
        Timer timer = new Timer(650, (l)->{
            if (isDead() == false) {
                System.out.println("Bomb!!!!!!");
                needAttackFlag = true;

                setIcon(boom);
                setSize(256, 192);


                timer0.start();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public boolean needAttack() {
        return needAttackFlag;
    }

    @Override
    public boolean canAttack(Zombie z) {
        if ((x - z.x <= 255 && z.x - x <= 200) && (y - z.y <= 315 && z.y - y <= 165)) {
            needAttackFlag = false;
            z.receiveDamage(atk);
            return true;
        }
        return false;
    };
}
