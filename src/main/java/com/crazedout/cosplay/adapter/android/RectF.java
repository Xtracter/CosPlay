package com.crazedout.cosplay.adapter.android;

import java.awt.*;

public class RectF {

    float left,top,right,bottom;

    public RectF(float left, float top, float right, float bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public RectF(Rect r){
        this.left = r.left;
        this.top = r.top;
        this.right = r.right;
        this.bottom = r.bottom;
    }

    public boolean contains(int x, int y){
        return (new Rectangle((int)left,(int)top,(int)(right-left),(int)(bottom-top))).contains(x,y);
    }

}
