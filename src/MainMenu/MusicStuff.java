package MainMenu;


/* 可以循环播放背景音乐，只支持wav格式
   使用示例：
   MusicStuff bgMusic = new MusicStuff();
        bgMusic.playMusic("src/bgm.wav");
        bgMusic.stopMusic();

   * 在MainMenu中，本人采用了静态全局变量menuBgm以保证同时只播放一首。

 */
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicStuff {
    Clip clip;
    void playMusic(String Location) {
        try {
            File musicPath = new File(Location);// 音乐路径
            if(musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    void stopMusic(){
        clip.stop();
    }
}
