package com.crazedout.cosplay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Sprite implements Drawable, Movable, Serializable {

    protected int speed = 1;
    protected int x,y,width,height;
    protected Map map;
    protected boolean visible = true;
    protected moves REQUESTED_MOVE = moves.NAN;
    protected String id = "Sprite";
    protected List<SpriteListener> listeners = new ArrayList<>();
    protected boolean viewPortScrolling;
    protected int startTile=0;

    protected moves dir = moves.LEFT;
    public enum moves {
        STOP,LEFT,RIGHT,UP,DOWN,JUMP,NAN,VIEWPORT_HALT
    }

    public Sprite(){
    }

    public Sprite(Map map){
        this(map.getStartTile().x,map.getStartTile().y,
                map.getStartTile().size,map.getStartTile().size,map);
    }

    public Sprite(Map map, Tile t){
        this(t.x,t.y,t.size,t.size,map);
    }

    public Sprite(int x, int y, int width, int height, Map map){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height = height;
        this.map = map;
    }

    public int getStartTile(){
        return this.startTile;
    }

    public void setStartTile(int startTile){
        this.startTile=startTile;
    }

    public int snapToGrid(Map map){
        Tile t = map.getGrid().get(getTileIndex());
        moveToTile(t);
        return t.getIndex();
    }

    public Sprite.moves getDir(){
        return this.dir;
    }

    public void setViewPortScrolling(boolean b){
        this.viewPortScrolling=b;
    }

    public boolean getViewPortScrolling(){
        return this.viewPortScrolling;
    }

    public void addSpriteListener(SpriteListener l){
        this.listeners.add(l);
    }

    public void removeSpriteListener(SpriteListener l){
        this.listeners.remove(l);
    }

    public void sendSpriteAction(Action a){
        for(SpriteListener l:listeners){
            l.spriteAction(map,this, a);
        }
    }

    public abstract void hit(Object obj);

    public boolean contains(int x, int y){
        return this.inside(x,y);
    }

    protected boolean inside(int X, int Y) {
        int w = this.width;
        int h = this.height;
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


    public abstract void reloadImages(int index);
    public abstract int inGrid();

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void move(Map map){
        sendSpriteAction(new Action(Actions.SPRITE_MOVING));
    }

    public void setVisible(boolean v){
        this.visible=v;
    }

    public boolean isVisible(){
        return this.visible;
    }

    public int getTileIndex(){
        for(Tile t : map.getGrid()){
            if(t.contains(x+width/2,y+height/2)){
                return t.index;
            }
        }
        return -1;
    }

    public void setSpeed(int pixSpeed){
        speed = pixSpeed;
    }

    public int getSpeed(){
        return speed;
    }

    public void moveToTile(Tile t){
        this.x=t.x;
        this.y=t.y;
        sendSpriteAction(new Action(Actions.SPRITE_MOVING));
    }

    public void moveToTile(int index){
        Tile t = this.map.getGrid().get(index);
        this.x=t.x;
        this.y=t.y;
        sendSpriteAction(new Action(Actions.SPRITE_MOVING));
    }

    public void requestDirection(moves m){
        REQUESTED_MOVE = m;
    }

    public Sprite.moves getRequestedMove(){
        return this.REQUESTED_MOVE;
    }

    public moves getInvertedDir(){
        if(dir==moves.LEFT) return moves.RIGHT;
        else if(dir==moves.RIGHT) return moves.LEFT;
        else if(dir==moves.UP) return moves.DOWN;
        else if(dir==moves.DOWN) return moves.UP;
        else return moves.NAN;
    }

}
