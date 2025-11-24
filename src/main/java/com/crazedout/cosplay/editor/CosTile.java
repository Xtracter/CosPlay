package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.pojo.ImageBitmap;
import com.crazedout.cosplay.CosGraphics;
import com.crazedout.cosplay.Tile;

import java.awt.*;

public class CosTile extends Tile {

    boolean mark = false;

    public CosTile(int x, int y, int size, int index){
        super(x,y,size,index);
    }

    public void setImage(ImageBitmap bm){
        this.image = bm;
        if(bm==null) this.file=null;
        if(bm!=null) {
            this.file=bm.getFile();
            this.wall = ((CosImageBitmap)bm).wall;
            this.exit = ((CosImageBitmap)bm).exit;
            this.grave = ((CosImageBitmap)bm).grave;
            this.ladder = ((CosImageBitmap)bm).ladder;
            this.item = ((CosImageBitmap)bm).item;
        }else {
            this.wall=false;
            this.exit=false;
            this.grave=false;
            this.ladder=false;
            this.image=null;
        }
    }

    @Override
    public void draw(CosGraphics g){
        super.draw(g);
        if(mark){
            g.setColor(Color.RED);
            g.drawRect(x-1,y-1,size+2,size+2);
        }
    }

}
