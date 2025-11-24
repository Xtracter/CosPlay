package com.crazedout.cosplay;

import java.util.ArrayList;
import java.util.List;

public class DefaultMap implements Map, SpriteListener, Scrollable {

    List<Tile> grid = new ArrayList<>();
    List<Sprite> sprites = new ArrayList<>();
    int cols, rows, size;
    CosBitmap bgImage, defaultImage, defaultBgImage;
    Object bgColor;
    Resources res;
    int START_TILE = 143;
    ViewPort viewPort;
    List<GridListener> listeners = new ArrayList<>();
    Size offsetDim = null;
    GameType gameType = GameType.UP_DOWN_PERSPECTIVE;
    Offset offset = new Offset(0,0);

    public DefaultMap(int cols, int rows, int size) {
        this.rows = rows;
        this.cols = cols;
        this.size = size;
    }

    public void addGridListener(GridListener l){
        listeners.add(l);
    }

    public void removeGridListener(GridListener l){
        listeners.remove(l);
    }

    public DefaultMap(Resources res) {
        this.res = res;
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
    public Rectangle getBounds(){
        int x = grid.get(0).x;
        int y = grid.get(0).y;
        return new Rectangle(x, y,
                grid.get(grid.size()-1).x-x,grid.get(grid.size()-1).y-y);

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
    public void initSprites(){
        for(Sprite s:sprites){
            s.moveToTile(s.getStartTile());
        }
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
    public List<Sprite> getSprites() {
        return this.sprites;
    }

    @Override
    public Resources getResources() {
        return this.res;
    }

    @Override
    public void setBackgroundColor(Object c) {
        this.bgColor = c;
    }

    @Override
    public Object getBackgroundColor() {
        return this.bgColor;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public CosBitmap getBackgroundImage() {
        return bgImage;
    }

    @Override
    public CosBitmap getDefaultTileImage() {
        return defaultImage;
    }

    @Override
    public void setBackgroundImage(CosBitmap bm) {
        this.bgImage = bm;
    }

    @Override
    public void setDefaultTileImage(CosBitmap bm) {
        this.defaultImage = bm;
    }

    @Override
    public void setDefaultTileBgImage(CosBitmap bm) {
        this.defaultBgImage = bm;
    }

    @Override
    public CosBitmap getDefaultBgTileImage() {
        return this.defaultBgImage;
    }

    @Override
    public Tile getStartTile() {
        return grid.get(START_TILE);
    }

    public void reInitGrid(int size){
        int index = 0;
        for(int c = 0; c < cols; c++){
            for(int r = 0; r < rows; r++){
                Tile t = grid.get(index);
                t.x=(c*size);
                t.y=(r*size);
                t.size=size;
                index++;
            }
        }
        for(Sprite s:getSprites()){
            s.snapToGrid(this);
        }
    }


    @Override
    public void setGridProperties(int cols, int rows, int size) {
        int old_width = cols*size;
        this.cols=cols;
        this.rows=rows;

        if(size!=this.size){
            if(size%2>0) size--;
            reInitGrid(size);
            if((old_width - (cols*size))>5) {
                offsetDim = new Size((old_width - (cols * size)), 0);
            }
        }
        this.size=size;
        if(viewPort!=null) {
            for (Tile t : getGrid()) {
                t.x -= viewPort.getPixLeft();
                t.y -= viewPort.getPixTop();
            }
        }
        for(GridListener l:listeners){
            l.gridChanged();
        }
    }

    @Override
    public Size getPreferredOffset() {
        return offsetDim;
    }

    @Override
    public List<Tile> getGrid() {
        return this.grid;
    }

    @Override
    public void draw(CosGraphics g, int x, int y, int width, int height){
        this.draw(g);
    }

    @Override
    public void draw(CosGraphics g){

        if(bgColor!=null){
            g.setColor(bgColor);
            g.fillRect(0,0,cols*size,rows*size);
        }
        if(bgImage!=null){
            g.drawImage(bgImage,0,0,cols*size,rows*size);
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

    @Override
    public void setViewPort(ViewPort vp){
        this.viewPort = vp;
    }

    @Override
    public ViewPort getViewPort() {
        return viewPort;
    }

    @Override
    public void setOffset(Offset offset) {
        this.offset = offset;
    }
}
