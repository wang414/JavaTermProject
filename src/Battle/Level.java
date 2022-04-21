package Battle;

import Items.Plant;
import Items.Zombie;
import java.util.Timer;

public class Level {
    int[] zombies;//按出现顺序存储的僵尸序列.zombies[i]表示第i个僵尸的类型,i是其编号
    int[] positions;//position[i]表示第i个僵尸应该出现的行,如果不在正常行数内,就随机选取
    int[] plants;//本关卡可选的植物,plant[i]表示本关卡可选植物的第i个植物的类型
    long[] checkpoint;
    /* 为 k 个单调递增序列, 其中k为僵尸潮的次数, 0代表开始一波新的潮(需要判断所有僵尸已经全部死亡),
    checkpoint[i]>0时, 表示距离上一次僵尸潮时间为checkpoint[i]时,创建这个僵尸类型zombies[i],在第position[i]行
    */
    String name;   // 关卡名称
    String BackgroundPATH; //关卡贴图路径
    int background_type; //0 白天草坪 1 黑夜草坪 2 白天泳池 3 黑夜泳池 5 屋顶
    Level()
    {
        //读.txt进行关卡编辑
    }

}
