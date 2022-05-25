package MainMenu;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;


public class Menu extends JPanel {
    // 背景图路径数组
    static String[] bgImgPath={"0.png","1.png","2.png","3.png"};
    private Image menuBackgroundImg;// 背景图图片
    private javax.swing.JPanel menuPanel;
    public Menu(int id) {// Menu初始化
        // initComponents();
        // JPanel初始化
        //.out.println("启动初始化");
        menuPanel = new javax.swing.JPanel();
        setPreferredSize(new java.awt.Dimension(1200, 900));

        menuPanel.setOpaque(false);
        menuPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(id<2){// 还是感觉点一下换一个窗口太sb了，就整两个算了。。
                    MainMenu.change(id);
                }else{
                    System.out.println("请输入关卡信息");
                }
                // 进入关卡选择

            }
        });

        // 接下来初始化界面
        // 采用分组布局
        // 这个代码来自GitHub，完全看不懂，区域判定也是寄的，最终效果是鼠标点击任意位置都执行……
        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
                menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1200, Short.MAX_VALUE)
        );
        menuPanelLayout.setVerticalGroup(
                menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 900, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(0, Short.MAX_VALUE)
                                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(0, Short.MAX_VALUE))
        );
        // 画布大小
        setSize(1200, 900);

        // 背景图
        menuBackgroundImg = new ImageIcon(this.getClass().getResource(bgImgPath[id])).getImage();

    }
    @Override
    public void paintComponent(Graphics g) {// 绘制背景图
        super.paintComponent(g);
        g.drawImage(menuBackgroundImg, 0, 0, null);

    }
}