package com.crazedout.cosplay;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public interface Map extends Drawable, Serializable {

    GameType getGameType();
    void setGameType(GameType gameType);
    int getCols();
    int getRows();
    int getSize();
    CosBitmap getBackgroundImage();
    CosBitmap getDefaultTileImage();
    CosBitmap getDefaultBgTileImage();
    Object getBackgroundColor();
    void setBackgroundColor(Object c);
    void setBackgroundImage(CosBitmap bm);
    void setDefaultTileImage(CosBitmap bm);
    void setDefaultTileBgImage(CosBitmap bm);
    void setGridProperties(int cols, int rows, int size);
    Rectangle getBounds();
    Size getPreferredOffset();
    List<Tile> getGrid();
    Resources getResources();
    Tile getStartTile();
    List<Sprite> getSprites();
    Sprite getUserSprite();
    void initSprites();
    ViewPort getViewPort();
    void setOffset(Offset offset);

}
