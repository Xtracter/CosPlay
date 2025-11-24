package com.crazedout.cosplay;

public abstract class Audio {

    protected Resources resources;

    protected String[] sounds = {
            "default",
            "sound/bgmusic.au",
            "sound/bgsound.au",
            "sound/choptree.au",
            "sound/click.au",
            "sound/gong.au",
            "sound/goresplat.au",
            "sound/helicopter.au",
            "sound/lans_zombie.au",
            "sound/opengate.au",
            "sound/reload2.au",
            "sound/sax.au",
            "sound/shotgun2.au",
            "sound/troll.au",
            "sound/zombie_dies.au",
            "sound/zombie_scream1.au",
            "nosound",
    };

    public final static int DEFAULT = 0;
    public final static int BGMUSIC = 1;
    public final static int BGSOUND = 2;
    public final static int CHOP_TREE = 3;
    public final static int CLICK = 4;
    public final static int GONG = 5;
    public final static int GORE_SPLAT = 6;
    public final static int HELICOPTER = 7;
    public final static int ZOMBIE_1 = 8;
    public final static int OPEN_GATE = 9;
    public final static int RELOAD = 10;
    public final static int SAX = 11;
    public final static int SHOTGUN = 12;
    public final static int TROLL = 13;
    public final static int ZOMBIE_DIES = 14;
    public final static int ZOMBIE_2 = 15;
    public final static int NO_SOUND = 16;

    public Audio(Resources resources){
        this.resources=resources;
    }

    public int getNumSounds(){
        return sounds.length;
    }

    public int findId(String name){
        int n = 0;
        for(String s:sounds){
            if(s.endsWith(name)) return n;
            n++;
        }
        return -1;
    }

    public abstract void playSound(int id);
    public abstract void playSound(int id, boolean loop);
    public abstract void stopSound(int id);
    public abstract void setVolume(int id, float v);

}
