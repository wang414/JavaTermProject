package MainMenu;
import com.sun.tools.javac.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

// 循环播放背景音乐，只支持wav格式
class musicStuff {
    void playMusic(String Location) {
        try {
            File musicPath = new File(Location);// 音乐路径
            if(musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
public class MainMenu extends JFrame {
    public MainMenu(int id) {
        // 主界面的构造函数。点击功能交给继承JPanel的Menu处理。
        // 这个参数id主要用作背景图片选取。因为想主界面多做几个图，而不会更换背景图，所以选择了直接销毁窗口重新造一个
        System.out.println("id:"+id);
        Menu menu = new Menu(id);
        menu.setLocation(0, 0);
        setSize(1200, 900);// 界面大小：初步定为1200* 900，要修改请在Menu类里也一并修改。
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 退出功能：点叉
        getLayeredPane().add(menu, new Integer(0));
        menu.repaint();
        setResizable(false);
        setVisible(true);
    }
    static MainMenu startWindow;

    public static void change(int id){// 换背景图
        //System.out.println("Change");

        startWindow.dispose();
        startWindow=new MainMenu(id+1);
    }
    public static void main(String[] args) {
        //初始化: 贴图, 区域判定, 背景音乐
        startWindow=new MainMenu(0);
        // 播放背景音乐
        musicStuff musicObject = new musicStuff();
        // 背景音乐的路径，如需要改变请改这里：
        musicObject.playMusic("src/bgm.wav");
        //调用战斗模块: 正确导入既定关卡
        //退出功能
    }
}
