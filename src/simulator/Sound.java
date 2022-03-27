package simulator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


public class Sound {

    Clip clip;
    AudioInputStream ais;
    public Sound(){
        try{
            ais = AudioSystem.getAudioInputStream(Sound.class.getResourceAsStream("/res/music/1.wav"));
            DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
        }catch(Exception e){}
    }

    public void play(){
        clip.start();
    }

}
