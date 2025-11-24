package com.crazedout.cosplay;

public final class Rectangle {

    private int top,left,width,height;

    public Rectangle(int left, int top, int width, int height){
        this.left=left;
        this.top=top;
        this.width=width;
        this.height=height;
    }

    public int getX(){
        return this.left;
    }

    public int getY(){
        return this.top;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public int getRight(){

        return this.left+this.width;
    }

    public int getBottom(){
        return this.top+this.height;
    }
}
