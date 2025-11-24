package com.crazedout.cosplay.sprites;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.pojo.ImageBitmap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.crazedout.cosplay.Actions.*;
import static com.crazedout.cosplay.Sprite.moves.*;

public class DefaultUserSprite extends Sprite implements GamePlayListener, UserSprite {

    private static final long serialVersionUID = 1L;

    int aniDelay=12;
    String defaultImageMap = "tva.png";
    List imageCache = new ArrayList<>();
    CosBitmap splat;
    List<CosBitmap> images = new ArrayList<>();

    protected GamePlay gamePlay = new GamePlay(){
        public void spriteAction(Map map, Sprite sp, Action action){
        };
    };

    public DefaultUserSprite(Map map){
        this(map.getStartTile().x,map.getStartTile().y,
                map.getStartTile().getSize(),map.getStartTile().getSize(),map);
    }

    public DefaultUserSprite(Map map, Tile t){
        this(t.x,t.y,t.getSize(),t.getSize(),map);
    }

    public DefaultUserSprite(int x, int y, int width, int height, Map map){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height = height;
        this.map = map;
        setSpeed(1);
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

    List<Shot> shots = new ArrayList<>();
    public void fire(){
        if(dir==LEFT || dir==RIGHT || dir == UP || dir==DOWN) {
            Shot s = new Shot(x + (width / 2), y + (height / 2), 8, 4, dir, toString());
            shots.add(s);
        }
        sendSpriteAction(new Action(Actions.SHOTGUN));
    }

    public List<Shot> getShots(){
        return this.shots;
    }

    public void setWidth(int w){
        this.width=w;
    }

    public void setHeight(int h){
        this.height=h;
    }

    public void setSpeed(int speed){
        this.speed=speed;
    }

    public String getImageMap(){
        return this.defaultImageMap;
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


    @Override
    public void gamePlayChanged(GamePlay gp) {
        this.gamePlay = gp;
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

    int hitCounter=-1;
    public void hit(Object obj){
        if(obj instanceof Shot) {
            hitCounter = 0;
            sendSpriteAction(new Action(HIT_BY_SHOT,obj));
            dir = STOP;
        }else if(obj instanceof Sprite){
            sendSpriteAction(new Action(HIT_BY_SPRITE,obj));
            dir = STOP;
        }else if(obj instanceof Item){
            sendSpriteAction(new Action(HIT_ITEM, obj));
        }
    }

    protected boolean canJump(moves dir, int tindex){

        if(!Compass.getTileAt(tindex,map,Compass.Direction.SOUTH).isWall() &&
                !Compass.getTileAt(tindex,map,Compass.Direction.SOUTH).isLadder()) return false;
        if(Compass.getTileAt(tindex,map,Compass.Direction.NORTH).isWall()) return false;

        switch(dir){
            case RIGHT:
                if(Compass.getTileAt(tindex,map,Compass.Direction.NORTH_EAST).isWall()) return false;
                if(map.getGrid().get(Compass.getIndexAt(tindex,map, Compass.Direction.EAST)+map.getRows()).isWall()
                        && !(map.getGrid().get(Compass.getIndexAt(tindex,map, Compass.Direction.EAST)).isWall())){
                    return false;
                }
                break;
            case LEFT:
                if(Compass.getTileAt(tindex,map,Compass.Direction.NORTH_WEST).isWall()) return false;
                if(map.getGrid().get(Compass.getIndexAt(tindex,map, Compass.Direction.WEST)-map.getRows()).isWall()
                        && !(map.getGrid().get(Compass.getIndexAt(tindex,map, Compass.Direction.WEST)).isWall())){
                    return false;
                }
                break;
        }
        return true;
    }

    int jumpIter=0;
    int jumpTile=0;
    int jumpDelay=0;
    public void jump(){
        jumpDelay=0;
        if(jumpIter<6) {
            switch (dir) {
                case RIGHT:
                    y -= map.getSize() / 6;
                    x += map.getSize() / 6;
                    if(x>=(map.getViewPort().getPixWidth()/2) && x <= (map.getCols()*map.getSize())-(map.getViewPort().getPixWidth()/2)) {
                        viewPortOffset.x -= map.getSize() / 6;
                    }
                    break;
                case LEFT:
                    y -= map.getSize() / 6;
                    x -= map.getSize() / 6;
                    if(viewPortOffset.x<=0 &&
                            x <= (map.getCols()*map.getSize())-(map.getViewPort().getPixWidth()/2)) viewPortOffset.x+=map.getSize() / 6;
                    break;
                default:
            }
            if (jumpIter == 5) {
                jumpTile=snapToGrid(map);
                if(map.getGrid().get(jumpTile+1).getItem()!=null){
                    sendSpriteAction(new Action(Actions.HIT_ITEM));
                    map.getGrid().get(jumpTile+1).setImage(null);
                }
                if(map.getGrid().get(jumpTile+1).isWall()){
                    jumpIter=0;
                    return;
                }
            }
        }else if(jumpIter<12){
            switch (dir) {
                case RIGHT:
                    y += map.getSize() / 6;
                    x += map.getSize() / 6;
                    if(x>=(map.getViewPort().getPixWidth()/2) && x <= (map.getCols()*map.getSize())-(map.getViewPort().getPixWidth()/2)) {
                        viewPortOffset.x -= map.getSize() / 6;
                    }
                    break;
                case LEFT:
                    y += map.getSize() / 6;
                    x -= map.getSize() / 6;
                    if(viewPortOffset.x<=0 &&
                            x <= (map.getCols()*map.getSize())-(map.getViewPort().getPixWidth()/2)) viewPortOffset.x+=map.getSize() / 6;
                    break;
                default:
            }
            if (jumpIter == 11) {
                snapToGrid(map);
            }
        }else if(jumpIter>=12){
            snapToGrid(map);
            jumpIter=0;
            return;
        }
        jumpIter++;
    }

    @Override
    public void requestDirection(moves m){
        if(!(map.getGameType()==GameType.SIDE_VIEW_PERSPECTIVE) && m==JUMP) return;
        REQUESTED_MOVE = m;
    }
    Offset offset = new Offset(0,0);
    int gravityDelay=0;
    boolean viewPortScrolling=false;
    public void move(Map map){

        int t = 0;
        Sprite.moves old_dir = dir;
        if(jumpIter>0) {
            jump();
            return;
        }
        t = inGrid();
        if(jumpIter==0 && REQUESTED_MOVE==JUMP){
            if(canJump(dir,getTileIndex())) {
                snapToGrid(map);
                REQUESTED_MOVE = dir;
                jump();
            } else {
                REQUESTED_MOVE=NAN;
                dir = old_dir;
            }
        }

        if(map.getGameType()==GameType.SIDE_VIEW_PERSPECTIVE && ((t=inGrid())>-1) &&
                !(map.getGrid().get(t+1).isWall()) && (!(map.getGrid().get(t).isLadder())&&
                !(map.getGrid().get(t+1).isLadder()))){
            if(gravityDelay++>3) {
                moveToTile(map.getGrid().get(t+1));
                gravityDelay=0;
            }
            return;
        }

        if((REQUESTED_MOVE != dir && REQUESTED_MOVE != Sprite.moves.NAN
                ) &&
                (t= inGrid())>-1) {
            dir = REQUESTED_MOVE;
            REQUESTED_MOVE = Sprite.moves.NAN;
        }

        if((t=inGrid())>-1){
            try {
                if(map.getGameType()==GameType.SIDE_VIEW_PERSPECTIVE && (dir==UP || dir==DOWN)){
                    if(!map.getGrid().get(t).isLadder() && !map.getGrid().get(t+1).isLadder()) {
                        dir = old_dir;
                        return;
                    }
                }
                if(dir == STOP){
                    return;
                }
                if (dir == Sprite.moves.UP && map.getGrid().get(t + 1).isLadder()
                        && !map.getGrid().get(t).isLadder()) {
                    dir = old_dir;
                    return;
                }else
                    if (dir == Sprite.moves.LEFT && map.getGrid().get(t - map.getRows()).isWall()) {
                    dir = old_dir;
                    return;
                } else if (dir == Sprite.moves.RIGHT &&
                        map.getGrid().get(t + map.getRows()).isWall()) {
                    dir = old_dir;
                    return;
                } else if (dir == UP && map.getGrid().get(t - 1).isWall()) {
                    dir = old_dir;
                    return;
                } else if (dir == DOWN && map.getGrid().get(t + 1).isWall()) {
                    dir = old_dir;
                    return;
                }
            }catch(Exception ex){
                return;
            }
        }

        switch (dir) {
            case LEFT:
                if(map.getViewPort()!=null) {
                    if(viewPortOffset.x<=0 && x <= (map.getCols()*map.getSize())-(map.getViewPort().getPixWidth()/2)){
                        viewPortOffset.x+=getSpeed();
                        map.setOffset(viewPortOffset);
                        System.out.println(viewPortOffset + " " + x);
                    }
                    x -= getSpeed();
                }else {
                    x -= getSpeed();
                }
                break;
            case RIGHT:
                if(map.getViewPort()!=null) {
                    if(x>=(map.getViewPort().getPixWidth()/2) &&
                            x <= (map.getCols()*map.getSize())-(map.getViewPort().getPixWidth()/2)) {
                        viewPortOffset.x-=getSpeed();
                        map.setOffset(viewPortOffset);
                        System.out.println(viewPortOffset + " " + x + " " + (map.getViewPort().getPixWidth()/2));
                    }
                    x += getSpeed();
                }else {
                    x += getSpeed();
                }
                break;
            case UP:
                y -= getSpeed();
                break;
            case DOWN:
                y += getSpeed();
                break;
            case STOP:
                break;
        }
        super.move(map);
    }

    Offset viewPortOffset = new Offset(0,0);

    @Override
    public void draw(CosGraphics g) {
        this.draw(g,x,y,width,height);
    }

    int anim = 0;
    int n = 0;
    int diff=0;
    public void draw(CosGraphics g, int x, int y, int width, int height){
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
                else if (dir == LEFT) g.drawImage(images.get(4 + anim), x - x_offset, y - y_offset, width + x_add, height + y_add);
                else if (dir == RIGHT) g.drawImage(images.get(8 + anim), x - x_offset, y - y_offset, width + x_add, height + y_add);
                else if (dir == UP || isOnLadder) g.drawImage(images.get(12 + anim), x - x_offset, y - y_offset, width + x_add, height + y_add);
                else if (dir == STOP) g.drawImage(images.get(0), x - x_offset, y - y_offset, width + x_add, height + y_add);
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
        for(Shot s:shots){
            g.setColor(Color.BLACK);
            g.fillCircle(s.x, s.y, s.size);
            s.advance();
        }
    }

    CosBitmap loadImage(String file, int row, int col){
        return map.getResources().loadImage(file,row,col);
    }

}
