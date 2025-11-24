package com.crazedout.cosplay;

import java.io.Serializable;

public class Offset implements Serializable {

    public int x,y;

    public Offset(){
        this(0,0);
    }
    public Offset(int x, int y){
        this.x=x;
        this.y=y;
    }

    @Override
    public String toString(){
        return "x: " + x + " y:" + y;
    }

}
