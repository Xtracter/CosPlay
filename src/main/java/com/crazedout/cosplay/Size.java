package com.crazedout.cosplay;

import java.io.Serializable;

public class Size implements Serializable {

    public int width,height;

    public Size(int width, int height){
        this.width=width;
        this.height=height;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

}
