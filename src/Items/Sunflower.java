package Items;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class Sunflower extends Plant implements ActionListener {
	static String picPath = "src/img/SunFlower.gif";
	static ImageIcon img;

	static{
		img = new ImageIcon(picPath);
	}
	public Sunflower(int x_, int y_, int number)
	{
		super(300, 0, 0, 0, x_, y_, number);
		setIcon(img);
		setCost(50);
		setSize(96, 96);

	}
	Timer sunGenerator0,sunGenerator1;
	static CopyOnWriteArrayList<SunLight> sunLights;
	static JLayeredPane battlePanel;
	static AtomicInteger sunLightvalue;
	static JPanel seedBank;
	public static void setSunLights(CopyOnWriteArrayList<SunLight> l){
		sunLights = l;
	}
	public static void setBattlePanel(JLayeredPane j){
		battlePanel = j;
	}
	public static void setSunLightvalue(AtomicInteger v){
		sunLightvalue = v;
	}
	public static void setSeedBank(JPanel s){
		seedBank = s;
	}

	void generateSun(){
		System.out.println("should generate sun");
		final SunLight sunLight = new SunLight(25, 5, x,y,x,y+50);
		sunLight.addActionListener((l)->{
			sunLightvalue.addAndGet(25);
			sunLights.remove(sunLight);
			SwingUtilities.invokeLater(()->{
				battlePanel.remove(sunLight);
				battlePanel.repaint();
			});
		});
		SwingUtilities.invokeLater(()->{
			battlePanel.add(sunLight);
			battlePanel.moveToFront(sunLight);
		});
		sunLights.add(sunLight);
	}

	public void actionPerformed(ActionEvent e) {
		generateSun();
	}

	@Override
	public void planted() {
		super.planted();
		sunGenerator1 = new Timer(24000,this);
		sunGenerator0 = new Timer(7000, (l)->{
			generateSun();
			sunGenerator1.start();
		});
		sunGenerator0.setRepeats(false);
		sunGenerator0.start();
	}

	@Override
	public boolean isDead() {
		boolean flg = super.isDead();
		if (flg) {
			sunGenerator0.stop();
			sunGenerator1.stop();
		}
		return flg;
	}
}
