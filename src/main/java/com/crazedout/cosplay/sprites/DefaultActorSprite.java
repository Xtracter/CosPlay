package com.crazedout.cosplay.sprites;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.pojo.ClasspathResource;
import com.crazedout.cosplay.pojo.ImageBitmap;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.crazedout.cosplay.Actions.HIT_BY_SHOT;
import static com.crazedout.cosplay.Sprite.moves.*;

public class DefaultActorSprite extends Sprite {

    private static final long serialVersionUID = 1L;

    List<CosBitmap> images = new ArrayList<>();
    String defaultImageMap = "ett.png";
    List imageCache = new ArrayList<>();
    CosBitmap splat;
    int aniDelay = 8;

    protected GamePlay gamePlay = new GamePlay(){
        public void spriteAction(Map map, Sprite sp, Action action){
        };
    };

    public DefaultActorSprite(Map map){
        super(map.getStartTile().x,map.getStartTile().y,
                map.getStartTile().getSize(),map.getStartTile().getSize(),map);
        try {
            splat = new ImageBitmap(ImageIO.read(map.getResources().getResource("splat.png")),"splat.png");
            /*
            splat = new ImageBitmap(ImageIO.read(cpRes.getClasspathResource("/sprites/splat.png")),
                    new File("/sprites/splat.png"));
             */
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void setImageMap(String im){
        this.defaultImageMap=im;
    }

    public String getImageMap(){
        return this.defaultImageMap;
    }

    public void setWidth(int w){
        this.width=w;
    }

    public void setAnimationDelay(int d){
        if(d % 2 != 0){
            throw new RuntimeException("Animation delay should be devisable by 2");
        }else{
            aniDelay=d;
        }
    }

    public int getAnimationDelay(){
        return this.aniDelay;
    }

    public void setHeight(int h){
        this.height=h;
    }

    public void loadImages(){
        imageCache.clear();
        String imgs[] = new String[1];
        imgs[0] = defaultImageMap;
        for(String file:imgs) {
            int n = 0;
            CosBitmap iArray[] = new CosBitmap[16];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    iArray[n++] = loadImage(file, i, j);
                }
            }
            imageCache.add(iArray);
        }
        reloadImages(0);
    }

    public void reloadImages(int index){
        images.clear();
        CosBitmap[] arr = (CosBitmap[])imageCache.get(index);
        for(CosBitmap bm:arr){
            images.add(bm);
        }
    }

    public java.util.List<moves> getAllowedMoves(int i){
        Tile t = map.getGrid().get(i);
        ArrayList<moves> list = new ArrayList<moves>();
        if(t.isLadder()){
            list.add(UP);
            return list;
        }
        try {
            if (!map.getGrid().get(t.getIndex() - map.getRows()).isWall()) list.add(moves.LEFT);
            if (!map.getGrid().get(t.getIndex() + map.getRows()).isWall()) list.add(moves.RIGHT);
            if((map.getGameType()!=GameType.SIDE_VIEW_PERSPECTIVE) || (map.getGrid().get(t.getIndex() - 1).isLadder() ||
                    map.getGrid().get(t.getIndex() + 1).isLadder())) {
                if (!map.getGrid().get(t.getIndex() - 1).isWall()) list.add(moves.UP);
                if (!map.getGrid().get(t.getIndex() + 1).isWall()) list.add(moves.DOWN);
            }
        }catch(Exception ex){
            System.out.println(ex.getCause());
        }
        return list;
    }

    public moves getRandDir(int tileIndex) {
        return this.getRandDir(tileIndex,false);
    }

    public moves getRandDir(int tileIndex, boolean ignoreReverseDir){
        java.util.List<moves> list = getAllowedMoves(tileIndex);
        if(ignoreReverseDir && list.size()>1) list.remove(getInvertedDir());
        moves m = dir;
        if(list.size()>0) {
            int r = Math.round((r = (int) Math.floor(Math.random() * (list.size() - 1 - 0 + 1) + 0)));
            m = list.get(r);
        }
        return m;
    }

    int hitCounter=-1;
    public void hit(Object obj){
        hitCounter=0;
        sendSpriteAction(new Action(HIT_BY_SHOT));
        dir = STOP;
    }

    int tt = 0;
    public int inGrid(){
        try {
            for (Tile t : map.getGrid()) {
                if (x == t.x && y == t.y) {
                    return t.getIndex();
                }
            }
        }catch(java.util.ConcurrentModificationException ex){
            ex.printStackTrace();
        }
        return -1;
    }

    int gravityDelay=0;
    int r = 0;
    int z = 0;
    @Override
    public void move(Map map){

        int t;
        if((t= inGrid())>-1){
            if(map.getGameType()==GameType.SIDE_VIEW_PERSPECTIVE && ((t=inGrid())>-1) &&
                    !(map.getGrid().get(t+1).isWall()) && (!(map.getGrid().get(t).isLadder())&&
                    !(map.getGrid().get(t+1).isLadder()))){

                if(gravityDelay++>4) {
                    moveToTile(map.getGrid().get(t+1));
                    gravityDelay=0;
                }
                return;
            }


            try {
                if (dir == Sprite.moves.UP && map.getGrid().get(t + 1).isLadder()
                        && !map.getGrid().get(t).isLadder()) {
                    dir = getRandDir(t);
                    return;
                }else
                if (dir == moves.LEFT && map.getGrid().get(t - map.getRows()).isWall()) {
                    dir=getRandDir(t);
                    return;
                } else if (dir == moves.RIGHT && map.getGrid().get(t + map.getRows()).isWall()) {
                    dir=getRandDir(t);
                    return;
                } else if (dir == moves.UP && map.getGrid().get(t - 1).isWall()) {
                    dir=getRandDir(t);
                    return;
                } else if (dir == moves.DOWN && map.getGrid().get(t + 1).isWall()) {
                    dir=getRandDir(t);
                    return;
                }
            }catch(Exception ex){return;}
        }

        if(t>-1 && getAllowedMoves(t).size()>0 && (r++%4)==0) {
            dir = getRandDir(t,true);
        }

        switch(dir){
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case STOP:
                break;
        }
    }

    public void draw(CosGraphics g) {
        draw(g,x,y,width,height);
    }
    boolean isOnLadder=false;
    int anim = 0;
    int n = 0;
    int diff=0;
    public void draw(CosGraphics g, int x, int y, int width, int height) {
        try {
            if (!visible) return;
            int x_add = 8;
            int y_add = 8;
            int x_offset = 4;
            int y_offset = 4;
            if(width > map.getSize()){
                diff = width - map.getSize();
                x_offset = diff/2;
                y_offset = diff/2;
                x_add=0;
                y_add=0;
                x_add=0;
                y_add=0;
            }
            if(height > map.getSize()){
                diff = height-map.getSize();
                x_offset = diff/2;
                y_offset = diff/2;
                x_add=0;
                y_add=0;
            }
            int t = getTileIndex();
            boolean isOnLadder = (map.getGrid().get(getTileIndex()).isLadder());
            if (images.size() > 0 && images.get(0) != null) {
                if (dir == DOWN && !isOnLadder) g.drawImage(images.get(anim), x - x_offset, y - y_offset, width + x_add, height + y_add);
                if (dir == Sprite.moves.LEFT) g.drawImage(images.get(4 + anim), x - x_offset, y - y_offset, width + x_add, height + y_add);
                if (dir == Sprite.moves.RIGHT) g.drawImage(images.get(8 + anim), x - x_offset, y - y_offset, width + x_add, height + y_add);
                if (dir == UP || isOnLadder) g.drawImage(images.get(12 + anim), x - x_offset, y - y_offset, width + x_add, height + y_add);
                if (dir == STOP) g.drawImage(images.get(0), x - x_offset, y - y_offset, width + x_add, height + y_add);
                if ((n++ % aniDelay) == 0 && !gamePlay.isGameOver()) anim++;
                if (anim > 3) anim = 0;
            } else {
                g.drawRect(x, y, x + width, y + height);
            }

            if(isVisible()) {
                if (hitCounter > -1 && hitCounter < 20) {
                    g.drawImage(splat, x + (width / 2) - 10, y + (height / 2) - 10, 20, 20);
                    hitCounter++;
                    if(hitCounter>19) setVisible(false);
                } else hitCounter = -1;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }


    CosBitmap loadImage(String file, int row, int col){
        return map.getResources().loadImage(file,row,col);
    }

}
