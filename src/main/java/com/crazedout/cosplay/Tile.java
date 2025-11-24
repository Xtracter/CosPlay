package com.crazedout.cosplay;

import java.io.Serializable;

public class Tile implements Drawable, Serializable {

    protected int index = -1;
    protected CosBitmap image,defaultImage,defaultBgImage;
    protected String file;
    protected int size;
    protected boolean wall,grave,exit,ladder;
    protected Object bgColor;
    public int x,y;
    protected Item item;

    public Tile(int x, int y, int size, int index){
        this.x=x;
        this.y=y;
        this.index = index;
        this.size = size;
    }

    public Tile(Tile t){
        this.x=t.x;
        this.y=t.y;
        this.index=t.index;
        this.size=t.size;
        this.file=t.file;
        this.image=t.image;
        this.defaultImage=t.defaultBgImage;
        this.wall=t.wall;
        this.item=t.item;
        this.ladder=t.ladder;
    }

    public boolean contains(int x, int y){
        return this.inside(x,y);
    }

    private boolean inside(int X, int Y) {
        int w = this.size;
        int h = this.size;
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int x = this.x;
        int y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > X) &&
                (h < y || h > Y));
    }

    public void setItem(Item i){
        this.item=i;
    }

    public boolean isGrave(){
        return this.grave;
    }

    public  void setGrave(boolean g) {
        this.grave=g;
    }

    public void setExit(boolean e){
        this.exit=e;
    }

    public boolean isExit(){
        return this.exit;
    }

    public Item getItem(){
        return this.item;
    }

    public int getIndex(){
        return this.index;
    }

    public String getFile(){
        return this.file;
    }

    public void setDefaultImage(CosBitmap bm){
        this.defaultImage = bm;
    }

    public CosBitmap getDefaultImage(){
        return this.defaultImage;
    }

    public CosBitmap getDefaultBgImage(){
        return this.defaultBgImage;
    }

    public void setDefaultBgImage(CosBitmap bm){
        this.defaultBgImage = bm;
    }

    public int getSize(){
        return this.size;
    }

    public void setImage(CosBitmap bm){
        this.image = bm;
    }

    public CosBitmap getImage(){
        return this.image;
    }

    public boolean isLadder() {
        return this.ladder;
    }

    public void setLadder(boolean ladder) {
        this.ladder = ladder;
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public void draw(CosGraphics g, int x, int y, int width, int height){
        int size=width;
        if(bgColor!=null){
            g.fillRect(x,y,width,width);
        }

        if(defaultBgImage!=null && image!=null){
            g.drawImage(defaultBgImage,x,y,size,size);
        }

        if(image!=null && !image.getName().equals("blank.png")){
            g.drawImage(image,x,y,size,size);
        }
        else if(image==null || image.getName().equals("blank.png")){
            if(defaultImage!=null){
                g.drawImage(defaultImage,x,y,size,size);
            }
        }
    }

    public void draw(CosGraphics g) {

        if(bgColor!=null){
            g.fillRect(x,y,size,size);
        }

        if(defaultBgImage!=null && image!=null){
            g.drawImage(defaultBgImage,x,y,size,size);
        }

        if(image!=null && !image.getName().equals("blank.png")){
            g.drawImage(image,x,y,size,size);
        }
        else if(image==null || image.getName().equals("blank.png")){
            if(defaultImage!=null){
                g.drawImage(defaultImage,x,y,size,size);
            }
        }
    }


}
