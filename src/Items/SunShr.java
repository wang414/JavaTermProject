package Items;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SunShr extends Plant implements ActionListener {

    static ImageIcon img0, img1;
    static CopyOnWriteArrayList<SunLight> sunLights;
    static JLayeredPane battlePanel;
    static AtomicInteger sunLightvalue;
    static JPanel seedBank;

    static {
        img0 = new ImageIcon("src/img/SunShrSmall.gif");
        img1 = new ImageIcon("src/img/SunShr.gif");
    }

    Timer sunGenerator0, sunGenerator1, setBigger;
    public SunShr(int x_, int y_, int number) {
        super(300, 0, 0, 0, x_, y_, number);
        setIcon(img0);
        setCost(25);
        setSize(96, 96);

    }

    public static void setSunLights(CopyOnWriteArrayList<SunLight> l) {
        sunLights = l;
    }

    public static void setBattlePanel(JLayeredPane j) {
        battlePanel = j;
    }

    public static void setSunLightvalue(AtomicInteger v) {
        sunLightvalue = v;
    }

    public static void setSeedBank(JPanel s) {
        seedBank = s;
    }

    private void grow(){
        isSmall = false;
        SwingUtilities.invokeLater(()->setIcon(img1));
    }
    boolean isSmall = true;
    void generateSun() {
        System.out.println("should small generate sun");

        final SunLight sunLight = new SunLight(25, 5, x, y, x, y + 50);

        if (isSmall){
            sunLight.setSmallSunLight();
        }


        sunLight.addActionListener((l) -> {
            sunLightvalue.addAndGet(sunLight.sunValue);
            sunLights.remove(sunLight);
            SwingUtilities.invokeLater(() -> {
                battlePanel.remove(sunLight);
                battlePanel.repaint();
            });
        });
        SwingUtilities.invokeLater(() -> {
            battlePanel.add(sunLight);
            battlePanel.moveToFront(sunLight);
        });
        sunLights.add(sunLight);
    }

    @Override
    public void planted() {
        setBigger = new Timer(60000, (l)->grow());
        setBigger.setRepeats(false);
        setBigger.start();
        sunGenerator1 = new Timer(24000, this);
        sunGenerator0 = new Timer(7000, (l) -> {
            generateSun();
            sunGenerator1.start();
        });
        sunGenerator0.setRepeats(false);
        sunGenerator0.start();
    }

    public void actionPerformed(ActionEvent e) {
        generateSun();
    }

    @Override
    public boolean isDead() {
        boolean flg = super.isDead();
        if (flg) {
            sunGenerator0.stop();
            sunGenerator1.stop();
            setBigger.stop();
        }
        return flg;
    }
}
