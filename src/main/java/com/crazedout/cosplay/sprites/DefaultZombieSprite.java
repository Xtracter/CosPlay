package com.crazedout.cosplay.sprites;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.pojo.ClasspathResource;
import com.crazedout.cosplay.pojo.ImageBitmap;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultZombieSprite extends DefaultActorSprite {

    private static final long serialVersionUID = 1L;

    transient ClasspathResource cpRes = new ClasspathResource();

    String types[] = {"z1","z2","z3"};
    String type = "z1";
    int typeIndex = 0;
    public List<CosBitmap> images = new ArrayList<>();

    public DefaultZombieSprite(Map map) {
        this(map,Math.round((int)Math.floor(Math.random() * (2 - 1 - 0 + 1) + 0)));
    }

    public DefaultZombieSprite(Map map, int zombieType) {
        super(map);
        this.type=this.types[zombieType];
        this.typeIndex=zombieType;
        loadImagesX();
    }

    public int getTypeIndex(){
        return this.typeIndex;
    }

    @Override
    public void loadImages(){

    }

    public void hit(Object obj){
        this.visible=false;
    }

    public void loadImagesX(){
        try{
            images.clear();
            String file = type + "_right.png";
            images.add(new ImageBitmap(ImageIO.read(map.getResources().getResource(file)),new File(file)));
            file = type + "_left.png";
            images.add(new ImageBitmap(ImageIO.read(map.getResources().getResource(file)),new File(file)));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    int shaker = 0;
    @Override
    public void draw(CosGraphics g, int x, int y, int width, int height){
        try {
            if (!visible) return;
            switch (dir) {
                case RIGHT:
                case UP:
                    g.drawImage(images.get(0), (shaker++ % 2 == 0) ? x : x + 1, y, width, height);
                    break;
                case LEFT:
                case DOWN:
                    g.drawImage(images.get(1), (shaker++ % 2 == 0) ? x : x - 1, y, width, height);
                    break;
                default:
                    g.drawImage(images.get(0), x, y, width, height);

            }
        }catch(Exception ex){
            // Do Nothing
        }

    }

    @Override
    public void draw(CosGraphics g){
        this.draw(g,x,y,width,height);
    }

}
