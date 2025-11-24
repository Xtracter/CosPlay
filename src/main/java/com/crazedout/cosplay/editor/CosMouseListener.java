package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.Item;
import com.crazedout.cosplay.Sprite;
import com.crazedout.cosplay.Tile;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CosMouseListener extends MouseAdapter {

    MapPanel mapPanel;
    CosTile markedTile;

    public CosMouseListener(MapPanel mapPanel){
        this.mapPanel = mapPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        mapPanel.grabFocus();

        if (!mapPanel.t.isAlive() && e.getButton()==3){
            Sprite sp = null;
            for (Sprite s : mapPanel.map.getSprites()) {
                if (s.contains(e.getX(), e.getY())) {
                    sp = s;
                }
            }
            if(sp!=null){
                SpriteEditor.getInstance().pop(mapPanel,sp,e.getX(),e.getY());
                return;
            }
            Tile tt = null;
            for(Tile t:mapPanel.map.getGrid()){
                if(t.contains(e.getX(),e.getY())){
                    if(t.getItem()!=null) tt=t;
                }
            }
            if(tt!=null){
                ItemEditor.getInstance().edit(mapPanel,(CosImageBitmap)tt.getImage());
            }
        }

        if((e.getModifiers() & MouseEvent.SHIFT_MASK)!=0){
            CosTile t = (CosTile)mapPanel.getTileAt(e.getX(),e.getY());
            mapPanel.statusBar.setTile(t);
            markedTile=t;
            if(markedTile.getImage()==null) {
                markedTile.setImage(mapPanel.imagePanel.selectedImage);
            }else{
                markedTile.setDefaultBgImage(mapPanel.imagePanel.selectedImage);
            }
            mapPanel.repaint();
        }
        else if((e.getModifiers() & MouseEvent.CTRL_MASK)!=0){
            CosTile t = (CosTile)mapPanel.getTileAt(e.getX(),e.getY());
            mapPanel.statusBar.setTile(t);
            markedTile=t;
            markedTile.setImage(null);
            markedTile.setWall(false);
            markedTile.setItem(null);
            markedTile.setLadder(false);
            mapPanel.repaint();
        }else{
            CosTile t = (CosTile)mapPanel.getTileAt(e.getX(),e.getY());
            if(t!=null) {
                mapPanel.statusBar.setTile(t);
                markedTile=t;
                mapPanel.repaint();
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){
        super.mouseReleased(e);
        if(markedTile!=null){
            ((CosTile)markedTile).mark=false;
            markedTile=null;
            mapPanel.statusBar.setTile(markedTile);
            mapPanel.repaint();
        }
        if((e.getModifiers() & MouseEvent.SHIFT_MASK)!=0) {
        }
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if((e.getModifiers() & MouseEvent.SHIFT_MASK)!=0){
            CosTile t = (CosTile)mapPanel.getTileAt(e.getX(),e.getY());
            mapPanel.statusBar.setTile(t);
            markedTile=t;
            if(markedTile!=null) markedTile.setImage(mapPanel.imagePanel.selectedImage);
            mapPanel.repaint();
        }
        if((e.getModifiers() & MouseEvent.CTRL_MASK)!=0){
            CosTile t = (CosTile)mapPanel.getTileAt(e.getX(),e.getY());
            mapPanel.statusBar.setTile(t);
            markedTile=t;
            markedTile.setImage(null);
            markedTile.setWall(false);
            markedTile.setItem(null);
            markedTile.setLadder(false);
            mapPanel.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e){
        CosTile t = (CosTile)mapPanel.getTileAt(e.getX(),e.getY());
        mapPanel.statusBar.setTile(t);
    }

}
