package com.crazedout.cosplay.adapter.android;

import java.awt.*;

public class Rect {

    public int left,top,right,bottom;

    public Rect(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean contains(int x, int y){
        return new Rectangle(left,top,right-left,bottom-top).contains(x,y);
    }

}
