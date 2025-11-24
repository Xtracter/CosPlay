package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.pojo.*;
import com.crazedout.cosplay.sprites.DefaultActorSprite;
import com.crazedout.cosplay.sprites.DefaultUserSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MapPanel extends JPanel implements GamePanel {

    CosMap map;
    StatusBar statusBar;
    ImagePanel imagePanel;
    boolean showWalls;
    boolean showGrid=true;
    GameThread t = new GameThread(this,12);
    KeyControls keyControls;
    public boolean showCoreRect=false;

    public MapPanel(CosMap map){
        this.map = map;
        this.addMouseListener(new CosMouseListener(this));
        this.addMouseMotionListener(new CosMouseListener(this));
        //setToolTipText("Select a Tile and place it in the grid using SHIFT+Mouse down. Remove with CTRL+Mouse down.");
        addKeyListener((keyControls = new KeyControls()));
        keyControls.setJumpKey(KeyEvent.VK_SPACE);
        keyControls.setStopKey(KeyEvent.VK_STOP);
        keyControls.setFireKey1(KeyEvent.VK_P);
        DefaultGamePlay gamePlay = new DefaultGamePlay();
        GamePlay.setAudio(new POJOAudio(map.getResources()));
        setFocusable(true);
        grabFocus();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    if(t.isAlive()) {
                        statusBar.animate.setSelected(false);
                        t.stop();
                    }else{
                        statusBar.animate.setSelected(true);
                        t.start();
                    }
                }
            }
        });
        GamePlay.getAudio().playSound(Audio.OPEN_GATE);
    }

    public void setImagePanel(ImagePanel imagePanel){
        this.imagePanel = imagePanel;
    }
    public void setStatusBar(StatusBar statusBar){
        this.statusBar = statusBar;
    }

    public Tile getTile(int index){
        return map.grid.get(index);
    }

    public Tile getTileAt(int x, int y){
        for(Tile t:map.grid){
            if(t.contains(x,y)) return t;
        }
        return null;
    }

    @Override
    public synchronized Dimension getPreferredSize(){

        Dimension dim = null;
        if(map.getViewPort()==null) {
            int c = map.cols;
            int r = map.rows;
            int w = c * map.size;
            int h = r * map.size;
            dim = new Dimension(w+12,h+2);
        }else{
            ViewPort r = map.getViewPort();
            dim = new Dimension(r.getPixWidth(), r.getPixHeight());
        }
       return dim;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        POJOGraphics pg = new POJOGraphics(g,this);
        map.draw(pg);
        for(Tile t:map.grid){
            if(showGrid) {
                g.setColor(Color.GRAY);
                g.drawRect(t.x, t.y, t.getSize(), t.getSize());
            }
            if(t.isWall() && showWalls){
                g.setColor(Color.RED);
                g.drawRect(t.x-1,t.y-1,t.getSize()+2,t.getSize()+2);
            }
            if(t.isLadder() && showWalls){
                g.setColor(Color.YELLOW);
                g.drawRect(t.x-1,t.y-1,t.getSize()+2,t.getSize()+2);
            }
            if(t.getItem()!=null && showWalls){
                g.setColor(Color.BLUE);
                g.drawRect(t.x-1,t.y-1,t.getSize()+2,t.getSize()+2);
            }
        }
        int diff=0;
        for(Sprite s:map.sprites){
            if(s instanceof DefaultActorSprite){
            }else{
                int x_add = 8;
                int y_add = 8;
                int x_offset = 4;
                int y_offset = 4;
                if(s.getWidth() > map.getSize()){
                    diff = s.getWidth()-map.getSize();
                    x_offset = diff/2;
                    y_offset = diff/2;
                    x_add=0;
                    y_add=0;
                }
                if(s.getHeight() > map.getSize()){
                    diff = s.getHeight()-map.getSize();
                    x_offset = diff/2;
                    y_offset = diff/2;
                    x_add=0;
                    y_add=0;
                }
                g.setColor(Color.GRAY);
                DefaultUserSprite u = (DefaultUserSprite)s;
                g.drawRoundRect(u.getX()-x_offset,u.getY()-y_offset,u.getWidth()+y_add,u.getHeight()+y_add,8,8);
            }
            if(showCoreRect){
                g.setColor(Color.RED);
                g.drawRect(s.getX(),s.getY(),map.size,map.size);
            }
        }

    }
    @Override
    public void tick() {
        for(Sprite s:map.sprites){
            s.move(map);
        }
        repaint();
    }
}
