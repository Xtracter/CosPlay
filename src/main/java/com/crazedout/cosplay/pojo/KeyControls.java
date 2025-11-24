package com.crazedout.cosplay.pojo;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.sprites.DefaultUserSprite;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import static com.crazedout.cosplay.Sprite.moves.JUMP;
import static com.crazedout.cosplay.Sprite.moves.STOP;


public class KeyControls extends KeyAdapter implements Serializable {

    protected Sprite sprite;
    protected boolean leftDown,rightDown,upDown,downDown,resetDown,pauseDown,fireDown,altDown,stopDown, jumpKeyDown;
    protected int fireKey1,fireKey2, altKey1, altKey2, resetKey,pauseKey, stopKey, jumpKey;
    Map map;
    GamePanel panel;

    public KeyControls(){
    }

    public KeyControls(Sprite sprite){
        this.sprite = sprite;
    }

    public KeyControls(Sprite sprite, GamePanel panel, Map map){
        this.sprite = sprite;
        this.map=map;
        this.panel = panel;
    }

    public void setSprite(Sprite s){
        this.sprite = s;
    }

    public void setSprite(Sprite s, GamePanel p, Map m){
        this.sprite = s;
        this.panel=p;
        this.map=m;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public void setStopKey(int key){
        this.stopKey=key;
    }
    public void setFireKey1(int key){
        this.fireKey1=key;
    }
    public void setFireKey2(int key){
        this.fireKey2=key;
    }
    public void setResetKey(int key){
        this.resetKey=key;
    }
    public void setPauseKey(int key){
        this.pauseKey=key;
    }
    public void setAltKey1(int key){
        this.altKey2=key;
    }
    public void setAltKey2(int key){
        this.altKey2=key;
    }
    public void setJumpKey(int key){
        this.jumpKey=key;
    }

    @Override
    public void keyPressed(KeyEvent e){
        super.keyPressed(e);
        if(sprite==null) return;
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                sprite.requestDirection(Sprite.moves.LEFT);
                leftDown=true;
                break;
            case KeyEvent.VK_RIGHT:
                sprite.requestDirection(Sprite.moves.RIGHT);
                rightDown=true;
                break;
            case KeyEvent.VK_UP:
                sprite.requestDirection(Sprite.moves.UP);
                upDown=true;
                break;
            case KeyEvent.VK_DOWN:
                sprite.requestDirection(Sprite.moves.DOWN);
                downDown=true;
                break;
        }

        if(e.getKeyCode()==jumpKey){
            sprite.requestDirection(JUMP);
        }else
        if(e.getKeyCode()==fireKey1){
            ((DefaultUserSprite)sprite).fire();
        }else
        if(e.getKeyCode()==stopKey){
            sprite.requestDirection(STOP);
            stopDown=true;
        }else
        if(e.getKeyCode()==KeyEvent.VK_F1){
            if(panel!=null && map!=null) {
                sprite.snapToGrid(map);
                panel.repaint();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e){
        super.keyReleased(e);
        upDown=false;
        downDown=false;
        resetDown=false;
        rightDown=false;
        leftDown=false;
        fireDown=false;
        altDown=false;
        pauseDown=false;
        stopDown=false;
    }
}
