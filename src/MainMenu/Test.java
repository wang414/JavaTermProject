package MainMenu;

import javax.swing.*;
import Battle.MainLoop;

public class Test extends JFrame {
    public static void main(String[] args) {
        JFrame jFrame = new Test();
        jFrame.setSize(1200,900);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        new MainLoop(jFrame, 0);
    }
}
