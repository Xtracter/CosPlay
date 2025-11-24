package com.crazedout.cosplay.adapter.android.cosplay;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.adapter.android.Activity;
import com.crazedout.cosplay.adapter.android.View;
import com.crazedout.cosplay.pojo.KeyControls;
import com.crazedout.cosplay.pojo.ZipResource;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static com.crazedout.cosplay.Sprite.moves.STOP;

public class TestGame extends View implements GamePanel, GridListener {

    Activity act;
    DefaultMap map;
    AndroidMapLoader loader;
    GameThread runner;

    public TestGame(Activity act, int x, int y){
        super(x,y);
        this.act=act;
        setFocusable(true);
        grabFocus();
        this.setCosPack("C:\\Users\\fredr\\cosplay\\cosplay.pack");
        setBackground(Color.BLACK);
    }

    public void setCosPack(String file){
        try {
            ZipResource res = new ZipResource(file);
            List<String> maps = res.getMapNames();
            map = new DefaultMap(res);
            ((DefaultMap)map).addGridListener(this);
            loader = new AndroidMapLoader();
            loader.loadMap(map, maps.get(0), res);
            Sprite sp = null;//((MapAdapter)map).getFirstKeyControlled();
            sp.requestDirection(STOP);
            KeyControls keyControls = new KeyControls(sp, this, map);
            keyControls.setStopKey(KeyEvent.VK_SPACE);
            addKeyListener(keyControls);
            runner = new GameThread(this,24);
            runner.start();
            if(map.getBackgroundColor()!=null) {
                setBackground((Color)map.getBackgroundColor());
            }
            map.setGridProperties(map.getCols(),map.getRows(),27);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    Size dim;
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if((dim=map.getPreferredOffset())!=null) {
            g.translate(dim.width,dim.height);
        }
        AndroidGraphics pg = new AndroidGraphics(g,this);
        if(this.map!=null){
            this.map.draw(pg);
        }
    }


    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void tick() {
        for(Sprite s:map.getSprites()){
            s.move(map);
        }
        repaint();
    }

    @Override
    public void gridChanged() {

    }
}
