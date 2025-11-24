package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.Rectangle;
import com.crazedout.cosplay.pojo.ImageBitmap;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.crazedout.cosplay.Sprite.moves.*;

public class CosMap implements Map, SpriteListener, Scrollable {

    public final static int DEFAULT_COLS = 16;
    public final static int DEFAULT_ROWS = 17;
    public final static int DEFAULT_SIZE = 32;

    String name = "Map";
    int rows,cols,size;
    int xmargin = 0;
    int ymargin = 0;
    ImageBitmap bgImage,defaultImage,defaultBgImage;
    Object bgColor;
    List<Tile> grid;
    File bgImageFile;
    List<GridListener> listeners = new ArrayList<>();
    List<Sprite> sprites = new ArrayList<>();
    Resources res = new CosFileResource();
    int START_TILE = 100;
    ViewPort viewPort;
    Offset offset;
    GameType gameType = GameType.UP_DOWN_PERSPECTIVE;

    public CosMap(){
        this(DEFAULT_COLS,DEFAULT_ROWS,DEFAULT_SIZE);
    }

    public CosMap(int cols, int rows, int size){
        grid = new ArrayList<>();
        this.rows=rows;
        this.cols=cols;
        this.size=size;
        this.START_TILE = 143;
        initGrid();
    }

    @Override
    public Rectangle getBounds(){
        //return new Rectangle(grid.get(0).x,grid.get(0).y)
        return null;
    }


    @Override
    public Sprite getUserSprite(){
        for(Sprite s:sprites){
            if(s instanceof UserSprite){
                return s;
            }
        }
        return null;
    }

    @Override
    public void initSprites(){
        for(Sprite s:sprites){
            s.moveToTile(s.getStartTile());
        }
    }

    @Override
    public void setGameType(GameType gameType){
        this.gameType=gameType;
    }

    @Override
    public GameType getGameType(){
        return this.gameType;
    }

    @Override
    public Size getPreferredOffset(){
        return new Size(0,0);
    }

    @Override
    public void setViewPort(ViewPort vp){
        this.viewPort = vp;
    }

    public ViewPort getViewPort(){
        return this.viewPort;
    }

    @Override
    public void spriteAction(Map map, Sprite sprite, Action action){
    }

    protected boolean moveGrid(Sprite.moves dir, int speed){

        switch(dir) {
            case DOWN:
                for (Tile t : getGrid()) {
                    t.y-=speed;
                }
            break;
            case UP:
                for (Tile t : getGrid()) {
                    t.y+=speed;
                }
                break;
            case LEFT:
                for (Tile t : getGrid()) {
                    t.x+=speed;
                }
                break;
            case RIGHT:
                for (Tile t : getGrid()) {
                    t.x-=speed;
                }
                break;
        }
        return true;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public List<Sprite> getSprites(){
        return this.sprites;
    }

    public void addSprite(Sprite s){
        double si = size;
        double sp = s.getSpeed();
        double res = size/sp;
        double r = res - (int)res;
        if(r>0) throw new BadGridSizeException("Map size:" + size + " and sprite pix speed:" +
                s.getSpeed() +" must be devisible");
        else sprites.add(s);
    }

    @Override
    public Tile getStartTile(){
        return grid.get(START_TILE);
    }

    @Override
    public Resources getResources(){
        return res;
    }

    public void setBackgroundColor(Object c){
        this.bgColor=c;
    }

    @Override
    public Object getBackgroundColor(){
        return this.bgColor;
    }

    public void addGridListener(GridListener l){
        this.listeners.add(l);
    }

    void sendGridHasChanged(){
        for(GridListener l:listeners){
            l.gridChanged();
        }
    }

    public void update(GameType gameType, int cols, int rows, int size){
        this.gameType=gameType;
        if(this.cols==cols && this.rows==rows && this.size==size) return;
        this.rows=rows;
        this.cols=cols;
        this.size=size;
        initGrid();
    }

    public void setBgImage(File file){
        try {
            this.bgImage = CosResources.loadFile(file);
            this.bgImageFile=file;
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void setDefaultImage(ImageBitmap bm){
        this.defaultImage = bm;
        for(Tile t:grid){
            t.setDefaultImage(bm);
        }
    }

    public ImageBitmap getDefaultImage(){
        return this.defaultImage;
    }

    public void initGrid(){
        if(grid!=null) grid.clear();
        int n = 0;
        for(int c = 0; c < cols; c++){
            for(int r = 0; r < rows; r++){
                CosTile t = new CosTile(xmargin + (c*size), ymargin+(r*size),size,n++);
                if(c==0 || c==cols-1) t.setWall(true);
                if(r==0 || r==rows-1) t.setWall(true);
                grid.add(t);
            }
        }
    }

    public void clear(){
        bgImage=null;
        sprites.clear();
        bgColor=null;
        initGrid();
    }

    @Override
    public List<Tile> getGrid() {
        return this.grid;
    }

    public int getCols(){
        return cols;
    }

    public int getRows(){
        return rows;
    }

    public int getSize(){
        return size;
    }

    @Override
    public ImageBitmap getBackgroundImage() {
        return this.bgImage;
    }

    @Override
    public ImageBitmap getDefaultTileImage() {
        return this.defaultImage;
    }

    @Override
    public void setBackgroundImage(CosBitmap bm) {
        this.bgImage=(ImageBitmap)bm;
    }

    @Override
    public void setDefaultTileImage(CosBitmap bm) {
        this.defaultImage=(ImageBitmap)bm;
    }

    public void setDefaultTileBgImage(CosBitmap bm) {
        this.defaultBgImage=(ImageBitmap)bm;
        for(Tile t:grid){
            t.setDefaultBgImage(bm);
        }
    }

    @Override
    public CosBitmap getDefaultBgTileImage() {
        return this.defaultBgImage;
    }

    @Override
    public void setGridProperties(int cols, int rows, int size){
        this.cols=cols;
        this.rows=rows;
        this.size=size;
        sendGridHasChanged();
    }

    public void setOffset(Offset offset){
        this.offset=offset;
    }

    @Override
    public void draw(CosGraphics g, int x, int y, int width, int height) {
        this.draw(g);
    }

    @Override
    public void draw(CosGraphics g){

        if(offset!=null){
            g.translate(offset.x,offset.y);
        }

        if(bgColor!=null){
            g.setColor(bgColor);
            g.fillRect(0,0,size*cols,size*rows);
        }

        if(bgImage!=null){
            g.drawImage(bgImage,0,
                    0,size*cols,size*rows);
        }
        if(grid!=null){
            for(Tile t:grid){
                t.draw(g);
            }
        }
        for (Sprite s : getSprites()) {
            s.draw(g);
        }
    }

}
