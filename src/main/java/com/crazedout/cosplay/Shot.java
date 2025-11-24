package com.crazedout.cosplay;

import java.awt.*;
import java.io.Serializable;

public class Shot implements Drawable, Serializable {

    public String origin;
    public int x,y,speed,size;
    public Sprite.moves dir;
    public Object color = Color.BLACK;
    public boolean visible = true;

    public Shot(int x, int y, int speed, int size, Sprite.moves dir, String origin){
        this.x=x;
        this.y=y;
        this.size=size;
        this.dir=dir;
        this.speed = speed;
        this.origin=origin;
    }

    public void setColor(Object c){
        this.color = (Color)c;
    }

    public void advance(){
        switch(dir){
            case UP:
                y-=speed;
                break;
            case DOWN:
                y+=speed;
                break;
            case LEFT:
                x-=speed;
                break;
            case RIGHT:
                x+=speed;
                break;
        }
    }

    @Override
    public void draw(CosGraphics g, int x, int y, int width, int height) {
        if(visible) {
            g.setColor(color);
            g.fillCircle(x, y, size);
        }
    }

    @Override
    public void draw(CosGraphics g) {
        this.draw(g,x,y,size,size);
    }
}
