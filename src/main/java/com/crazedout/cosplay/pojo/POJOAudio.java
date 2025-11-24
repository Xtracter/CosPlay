package com.crazedout.cosplay.pojo;

import com.crazedout.cosplay.Audio;
import com.crazedout.cosplay.Resources;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;

public class POJOAudio extends Audio {

    AudioInputStream in;

    Clip clips[] = new Clip[sounds.length];
    Clip bgClip;

    List<String> soundList = new ArrayList<>();

    public POJOAudio(Resources res){
        super(res);
        soundList = Arrays.asList(sounds);
    }

    @Override
    public void playSound(int id) {
        this.playSound(id,false);
    }

    public void playSound(int id, boolean loop) {
        if(id<0 && id > getNumSounds()-1) return;
        try {
            if (clips[id]!=null && clips[id].isActive()) return;
            in = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(getClass().getResourceAsStream("/" + soundList.get(id))));
            bgClip = AudioSystem.getClip();
            bgClip.open(in);
            bgClip.start();
            if(loop) {
                bgClip.loop(LOOP_CONTINUOUSLY);
            }
            clips[id] = bgClip;
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void setVolume(int id, float v){
        FloatControl gainControl =
                (FloatControl) clips[id].getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(v); // Reduce volume by 10 decibels.
    }

    @Override
    public void stopSound(int id) {
        clips[id].stop();
    }
}
