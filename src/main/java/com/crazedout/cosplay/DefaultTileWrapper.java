package com.crazedout.cosplay;

import java.io.Serializable;

public class DefaultTileWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    public String image,defImage,defBgImage,file;
    public int x,y,size,index;
    public boolean wall,grave,exit,ladder;
    public String itemName;
    public String itemType;
    public int itemSoundId;

    public DefaultTileWrapper(){
    }


}
