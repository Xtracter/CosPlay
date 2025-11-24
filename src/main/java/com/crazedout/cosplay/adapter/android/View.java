package com.crazedout.cosplay.adapter.android;

import javax.swing.*;
import java.awt.*;

public class View extends JPanel {

    Resources r;
    int widthPixels,heightPixels;

    public View(int w, int h){
        super(null);
        widthPixels = w;
        heightPixels = h;
        r = new Resources(this);
    }

    public void onDraw(Canvas c){

    }

    public void setBackgroundColor(Color c){
        setBackground(c);
    }

    public Resources getResources(){
        return r;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.onDraw(new Canvas(this,g));
        g.setColor(Color.GRAY);
        g.drawRect(0,0,getWidth()-1,getHeight()-1);
    }

}
