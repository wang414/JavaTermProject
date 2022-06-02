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
	public Sunflower(int init_hp, int x_, int y_, int number)
	{
		super(init_hp, 0, 0, 0, x_, y_, number);
		setIcon(img);
	}
	Timer sunGenerator;
	static CopyOnWriteArrayList<SunLight> sunLights;
	static JLayeredPane battlePanel;
	static AtomicInteger sunLightvalue;
	public static void setSunLights(CopyOnWriteArrayList<SunLight> l){
		sunLights = l;
	}
	public static void setBattlePanel(JLayeredPane j){
		battlePanel = j;
	}
	public static void setSunLightvalue(AtomicInteger v){
		sunLightvalue = v;
	}

	public void actionPerformed(ActionEvent e) {
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

	@Override
	public boolean isDead() {
		boolean flg = super.isDead();
		if (flg)
			sunGenerator.stop();
		return flg;
	}
}
