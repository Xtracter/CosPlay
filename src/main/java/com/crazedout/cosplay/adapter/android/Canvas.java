package com.crazedout.cosplay.adapter.android;

import java.awt.Paint;
import java.awt.*;

public class Canvas {

    public Graphics g;
    View view;

    public Canvas(View view, Graphics g){
        this.g = g;
        this.view = view;
    }

    public int getWidth(){
        return view.getWidth();
    }

    public int getHeight(){
        return view.getHeight();
    }

    public void drawRect(Rect r, Paint p){
        g.drawRect(r.left,r.top,r.right-r.left,r.bottom-r.top);
    }

    public void drawRect(RectF r, Paint p){
        g.drawRect((int)r.left,(int)r.top,(int)(r.right-r.left),(int)(r.bottom-r.top));
    }

    public void drawText(String text, int x, int y, Paint p){
        g.drawString(text,x,y);
    }

    public void drawRect(int left, int top, int right, int bottom, Paint paint){
        if(paint!=null) g.fillRect(left,top,right-left,bottom-top);
        else g.drawRect(left,top,right-left,bottom-top);
    }

    public void drawCircle(int x, int y, int rad, Paint p){
        g.fillOval(x,y,rad,rad);
    }

}
