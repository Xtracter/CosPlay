package com.crazedout.cosplay;

import com.crazedout.cosplay.Map;
import com.crazedout.cosplay.Sprite;
import com.crazedout.cosplay.SpriteListener;
import com.crazedout.cosplay.sprites.DefaultActorSprite;
import com.crazedout.cosplay.sprites.DefaultUserSprite;
import com.crazedout.cosplay.sprites.DefaultZombieSprite;

import java.io.Serializable;

public class DefaultSpriteWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    String spriteClassName;
    String imageMapFile;
    int speed;
    String name;
    public int typeIndex=0;
    int startTile;


    public DefaultSpriteWrapper(String id, String spriteClassName, String imageMapFile, int speed, int startTile){
        this(id,spriteClassName,imageMapFile,speed,startTile,0);
    }

    public DefaultSpriteWrapper(String id, String spriteClassName, String imageMapFile, int speed, int startTile, int typeIndex){
        this.name=id;
        this.spriteClassName=spriteClassName;
        this.imageMapFile=imageMapFile;
        this.speed=speed;
        this.startTile=startTile;
        this.typeIndex=typeIndex;
    }

    public void setStartTile(int st){
        this.startTile=st;
    }

    public Sprite instantiate(Map map){
        Sprite s = null;
        if(spriteClassName.endsWith("DefaultUserSprite")){
            s = new DefaultUserSprite(map);
            ((DefaultUserSprite)s).setImageMap(this.imageMapFile);
            s.setSpeed(speed);
            s.setId(name);
            s.setStartTile(startTile);
            ((DefaultUserSprite)s).loadImages();
        }else if(spriteClassName.endsWith("DefaultActorSprite")){
            s = new DefaultActorSprite(map);
            s.setSpeed(speed);
            s.setId(name);
            s.setStartTile(startTile);
            ((DefaultActorSprite)s).setImageMap(this.imageMapFile);
            ((DefaultActorSprite)s).loadImages();
        }else if(spriteClassName.endsWith("DefaultZombieSprite")){
            s = new DefaultZombieSprite(map,typeIndex);
            s.setSpeed(speed);
            s.setId(name);
            s.setStartTile(startTile);
        }
        if(map instanceof SpriteListener){
            s.addSpriteListener((SpriteListener)map);
        }

        return s;
    }

}
