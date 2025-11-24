package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.CosBitmap;
import com.crazedout.cosplay.Item;
import com.crazedout.cosplay.pojo.ImageBitmap;

import java.awt.*;
import java.io.File;

public class CosImageBitmap extends ImageBitmap {

    private static final long serialVersionUID = 1L;

    String name,file;
    boolean wall,exit,gun,grave,ladder;
    boolean defTile, defBg, tileBg;
    Item item;

    public CosImageBitmap(Image i, File file){
        super(i,file);
        this.image=i;
        this.name = file.getName();
        this.file = file.getAbsolutePath();
    }

    public CosBitmap toCosBitmap(){

        CosBitmap cb = new CosBitmap() {
            @Override
            public Object getImage() {
                return null;
            }

            @Override
            public void setImage(Object image) {

            }

            @Override
            public String getName() {
                return file;
            }

            @Override
            public void setName(String name) {

            }

            @Override
            public String getFile() {
                return file;
            }

            @Override
            public void setFile(String file) {

            }
        };
        return cb;
    }

    public void setItem(Item i){
        this.item=i;
    }

    public Item getItem(){
        return item;
    }

    public Image getImage(){
        return this.image;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void setGun(boolean gun) {
        this.gun = gun;
    }

    public void setLadder(boolean l) {
        this.ladder=l;
    }

    public boolean isWall() {
        return wall;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isGun() {
        return gun;
    }

    public boolean isGrave() {
        return grave;
    }

    public boolean isLadder() {return this.ladder;}

}
