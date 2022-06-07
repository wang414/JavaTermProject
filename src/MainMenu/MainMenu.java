package MainMenu;

/*
    主界面类。
    使用方法：MainMenu类下有一个static MainMenu startWindow，此即主界面；
            请补充selectGame()函数以向Battle组实现的战斗界面传入衔接参数等
    配套文件：背景音乐 背景图片
*/



import Battle.MainLoop;

import javax.swing.JFrame;
import javax.swing.*;



public class MainMenu extends JFrame {
    public MainMenu(int id) {
        // 主界面的构造函数。点击功能交给继承JPanel的Menu处理。
        // 这个参数id主要用作背景图片选取。因为想主界面多做几个图，而不会更换背景图，所以选择了直接销毁窗口重新造一个
        //System.out.println("id:"+id);
        Menu menu = new Menu(id);
        menu.setLocation(0, 0);
        setSize(1200, 900);// 界面大小：初步定为1200* 900，要修改请在Menu类里也一并修改。
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 退出功能：点叉
        getContentPane().add(menu, 0);
        menu.repaint();
        setResizable(false);
        setVisible(true);
    }
    static MainMenu startWindow;
    static MusicStuff menuBgm;
    static String[] bgmPath={"src/bgm/battlebgm_day.wav","src/bgm/battlebgm_night.wav"};

    public static void change(int id){// 换背景图

        startWindow.dispose();
        startWindow=new MainMenu(id+1);
    }

    // 选择关卡请使用selectGame。
    public static void selectGame(int model){
        // 关卡id(model)映射：0白天，1晚上

        // 切换bgm
        menuBgm.stopMusic();
        menuBgm.playMusic(bgmPath[model]);
        // 进入Battle
        System.out.println("请选择关卡：");

        // 进入关卡：
        // 6.5 0:10:发现之前窗口变白也是没开线程的问题，可以继续用startWindow。
        new Thread(() -> {new MainLoop(startWindow, model);}).start();

    }
    public static void main(String[] args) {
        //初始化: 贴图, 区域判定, 背景音乐
        startWindow=new MainMenu(0);
        //播放背景音乐"sr
        menuBgm = new MusicStuff();
        // 背景音乐的路径，如需要改变请改这里：
        menuBgm.playMusic("src/bgm/menubgm.wav");
     //   System.out.println("To end");
        //调用战斗模块: 正确导入既定关卡
        //System.exit(0);
        //退出功能
    }
}
