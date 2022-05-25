package Battle;

import Items.Basic_zombie;
import Items.Conehead_zombie;
import Items.Plant;
import Items.Zombie;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/** @author yf */
public class Level extends Thread{//调用initZombie()即可

    int[] zombies=new int[50];//按出现顺序存储的僵尸序列.zombies[i]表示第i个僵尸的类型,i是其编号
    int sumZombies;//僵尸总数目
    int sumTypeZombies;//僵尸总种类数
    int [][]typeZombies=new int[10][5];//僵尸基本数据 0 init_hp,1 int init_speed,2 int init_atk,3 double atk_speed,

    int[] positions=new int[50];//position[i]表示第i个僵尸应该出现的行,如果不在正常行数内,就随机选取

    int[] plants=new int[50];//本关卡可选的植物,plant[i]表示本关卡可选植物的第i个植物的类型
    long[] checkpoint=new long[50];
    /* 为 k 个单调递增序列, 其中k为僵尸潮的次数, 0代表开始一波新的潮(需要判断所有僵尸已经全部死亡),
    checkpoint[i]>0时, 表示距离上一次僵尸潮时间为checkpoint[i]时,创建这个僵尸类型zombies[i],在第position[i]行
    */

    String name;   // 关卡名称
    String BackgroundPATH; //关卡贴图路径
    Image Background;
    int background_type; //0 白天草坪 1 黑夜草坪 2 白天泳池 3 黑夜泳池 5 屋顶

    CopyOnWriteArrayList<Zombie> []zombies2;
    JLayeredPane bgpane;

    public Level(int level, CopyOnWriteArrayList<Zombie> []zombies2, JLayeredPane bgpane, CopyOnWriteArrayList<Integer> chosenPlants) {
        BufferedReader br;
        this.zombies2 = zombies2;
        this.bgpane = bgpane;
        File file = new File("test.txt");
        try
        {
            br = new BufferedReader(new FileReader(file));
            name = br.readLine();
            BackgroundPATH = br.readLine();
            //读入图片！！！Image=br.read();
            background_type = Integer.parseInt(br.readLine());
            int num = 0, type;

            //读入每种僵尸的基本特征
            sumTypeZombies= Integer.parseInt(br.readLine());
            for(int i=0;i<sumTypeZombies;i++){
                for(int j=0;j<4;j++){
                    typeZombies[i][j]= Integer.parseInt(br.readLine());
                }
            }

            //读入植物类型
            while ((type = Integer.parseInt(br.readLine())) != -1)
            {
                plants[num] = type;
                chosenPlants.add(type);
                num++;
            }

            //读入僵尸类型
            num=0;
            while ((type = Integer.parseInt(br.readLine())) != -1)
            {
                zombies[num] = type;
                num++;
            }
            sumZombies=num;

            //读入僵尸位置
            num=0;
            while ((type = Integer.parseInt(br.readLine())) != -1)
            {
                Random rnd = new Random();
                if (type == 0)
                {

                    type = rnd.nextInt(4) + 1;
                }
                positions[num] = type;
                num++;
            }

            //读入checkpoint
            long cp;num=0;
            while ((cp = Long.parseLong(br.readLine())) != -1)
            {
                checkpoint[num] = cp;
                num++;
            }
            br.close();
            Thread t=new Thread(this);
            t.start();
        }
        catch (IOException e)
        {

        }
    }

    @Override
    public void run(){
        initZombie();
    }

    void initZombie()
    {
        Zombie z;
        for(int i=0;i<sumZombies;i++)
        {
            if(checkpoint[i]>0){
                try
                {
                    Thread.sleep(checkpoint[i]);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                z=getZombie(zombies[i],positions[i]);
                System.out.println(z);
                Zombie finalZ = z;
                if (z != null) {
                    System.out.println("create");
                    zombies2[positions[i]].add(z);
                    SwingUtilities.invokeLater(()->{
                        bgpane.add(finalZ);
                        bgpane.moveToFront(finalZ);
                    });
                }
            }
            else if(checkpoint[i]==-5)
            {
                while(true){
                    int flag=1;
                    for(int col=0;col<5;col++){
                        if(!zombies2[col].isEmpty()) {
                            flag=0;
                        }
                    }
                    if(flag==1)break;
                }
                z=getZombie(zombies[i],positions[i]);
                Zombie finalZ = z;
                if (z != null) {
                    System.out.println("create");
                    zombies2[positions[i]].add(z);
                    SwingUtilities.invokeLater(()->{
                        bgpane.add(finalZ);
                        bgpane.moveToFront(finalZ);
                    });

                }
                //window.getContentPane().add(finalZ);
            }
            else if(checkpoint[i]==0)
            {
                try
                {
                    Thread.sleep(5000);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                z=getZombie(zombies[i],positions[i]);
                Zombie finalZ = z;
                if (z != null) {
                    zombies2[positions[i]].add(z);
                    System.out.println("create");
                    SwingUtilities.invokeLater(()->{
                        bgpane.add(finalZ);
                        bgpane.moveToFront(finalZ);
                    });
                }

                //window.getContentPane().add(finalZ);
            }
        }
    }

    public Zombie getZombie(int Id, int positionY)
    {

        Zombie z = null;
        switch (Id)
        {
            case 1:
                z = new Basic_zombie(1,5,5,5,1200,100 + 150*positionY,1);

                        //Basic_zombie(int init_hp, int init_speed, int init_atk,
                //				 double atk_speed, int x_, int y_, int number)
                break;
            case 2:
                z = new Basic_zombie(1,5,5,5,1200,100 + 150*positionY,1);
                //z = new Conehead_zombie(1,1,1,1,1,1,1);
                break;
        }
        return z;
    }
}
