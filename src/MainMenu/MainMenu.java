package MainMenu;

/*
    主界面类。
    使用方法：MainMenu类下有一个static MainMenu startWindow，此即主界面；
            请补充selectGame()函数以向Battle组实现的战斗界面传入衔接参数等
    配套文件：背景音乐 背景图片
*/



import Battle.MainLoop;

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
        getLayeredPane().add(menu, 0);
        menu.repaint();
        setResizable(false);
        setVisible(true);
    }
    static MainMenu startWindow;
    static MusicStuff menuBgm;

    public static void change(int id){// 换背景图

        startWindow.dispose();
        startWindow=new MainMenu(id+1);
    }
    public static void selectGame(){
        // 切换bgm
        menuBgm.stopMusic();
        menuBgm.playMusic("src/battlebgm_day.wav");
        // TODO
        System.out.println("请选择关卡：");
        new MainLoop(startWindow);

    }
    public static void main(String[] args) {
        //初始化: 贴图, 区域判定, 背景音乐
        startWindow=new MainMenu(0);
        //播放背景音乐
        menuBgm = new MusicStuff();
        // 背景音乐的路径，如需要改变请改这里：
        menuBgm.playMusic("src/menubgm.wav");
        //调用战斗模块: 正确导入既定关卡

        //退出功能
    }
}
