package com.crazedout.cosplay.pojo;

import com.crazedout.cosplay.CosBitmap;
import com.crazedout.cosplay.CosGraphics;

import java.awt.*;

public class POJOGraphics implements CosGraphics {

    private Graphics g;
    private Component c;

    public POJOGraphics(Graphics g, Component c){
        this.g=g;
        this.c=c;
    }

    @Override
    public void translate(int x, int y){
        g.translate(x,y);
    }

    @Override
    public void setColor(Object c) {
        g.setColor((Color)c);
    }

    @Override
    public void setStyle(Object style) {
    }

    @Override
    public void drawImage(CosBitmap bm, int x, int y, int width, int height) {
        g.drawImage((Image)bm.getImage(),x,y,width,height,c);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        g.drawRect(x,y,width,height);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x,y,width,height);
    }

    @Override
    public void drawString(String s, int x, int y){
        this.g.drawString(s,x,y);
    }

    @Override
    public void fillCircle(int x, int y, int rad){
        g.fillOval(x,y,rad,rad);
    }

}
