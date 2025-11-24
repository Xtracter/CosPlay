package com.crazedout.cosplay;

import java.io.Serializable;

public class ViewPort implements Serializable {

    private int size;
    public int tileLeft, tileTop,tileWidth,tileHeight;
    transient GamePanel gamePanel;

    public ViewPort(GamePanel gamePanel, int tileLeft, int tileTop, int tileWidth, int tileHeight, int tileSize){
        this.gamePanel = gamePanel;
        this.tileLeft=tileLeft;
        this.tileTop=tileTop;
        this.tileWidth=tileWidth;
        this.tileHeight=tileHeight;
        this.size=tileSize;
    }

    public GamePanel getGamePanel(){
        return this.gamePanel;
    }

    public int getPixLeft(){
        return this.tileLeft*this.size;
    }

    public int getPixTop(){
        return this.tileTop*this.size;
    }

    public int getPixWidth(){
        return tileWidth*size;
    }

    public int getPixHeight(){
        return tileHeight*size;
    }

    public int getSize(){
        return this.size;
    }

}
