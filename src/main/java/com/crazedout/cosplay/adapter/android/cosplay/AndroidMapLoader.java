package com.crazedout.cosplay.adapter.android.cosplay;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.DefaultSpriteWrapper;
import com.crazedout.cosplay.pojo.ImageBitmap;
import com.crazedout.cosplay.pojo.ZipResource;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.List;

public class AndroidMapLoader implements MapLoader {

    public AndroidMapLoader(){
    }

    public void loadMap(Map map, String mapName, Resources res) throws Exception {

        CosBitmap defaultTile = null;
        CosBitmap defaultBgTile = null;

        int tc=0,tr=0,ts=0;
        ObjectInputStream in = new ObjectInputStream(((ZipResource)res).getMap(mapName));
        Object mg = in.readObject();
        if(mg instanceof Object[]){
            tc = (int)((Object[])mg)[0];
            tr = (int)((Object[])mg)[1];
            ts = (int)((Object[])mg)[2];
        }
        String bmstr = (String)in.readObject();
        System.out.println(bmstr);
        if(!bmstr.startsWith("noBgImage")){
            ImageBitmap bm = new ImageBitmap(ImageIO.read(res.getResource(bmstr)), new File(bmstr));
            map.setBackgroundImage(bm);
        }
        bmstr = (String)in.readObject();
        System.out.println(bmstr);
        if(!bmstr.startsWith("noDefaultImage")){
            ImageBitmap bm = new ImageBitmap(ImageIO.read(res.getResource(bmstr)), new File(bmstr));
            defaultTile=bm;
            map.setDefaultTileImage(bm);
        }
        bmstr = (String)in.readObject();
        System.out.println(bmstr);
        if(!bmstr.startsWith("noDefaultBgImage")){
            ImageBitmap bm = new ImageBitmap(ImageIO.read(res.getResource(bmstr)), new File(bmstr));
            defaultBgTile=bm;
            map.setDefaultTileBgImage(bm);
        }

        mg = in.readObject();
        if(!(mg instanceof String)){
            map.setBackgroundColor(mg);
        }

        mg = in.readObject();
        if(!(mg instanceof String)){
            if(map instanceof Scrollable) {
                ((Scrollable)map).setViewPort((ViewPort)mg);
            }
        }

        mg = in.readObject();
        List<DefaultTileWrapper> list = (List<DefaultTileWrapper>)mg;

        map.getGrid().clear();
        ImageBitmap bm = null;
        for(DefaultTileWrapper t:list){
            Tile tt = new Tile(t.x,t.y,t.size,t.index);
            tt.setDefaultImage(defaultTile);
            tt.setDefaultBgImage(defaultBgTile);
            tt.setWall(t.wall);
            tt.setExit(t.exit);
            tt.setGrave(t.grave);
            tt.setWall(t.wall);
            if(t.file!=null){
                try {
                    if(t.file.indexOf("blank.png")==-1) {
                        System.out.println(t.file);
                        bm = new ImageBitmap(ImageIO.read(res.getResource(t.file)), new File(t.file));
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                tt.setImage(bm);
                if(map.getDefaultTileImage()!=null) tt.setDefaultImage(map.getDefaultTileImage());
            }
            if(t.itemName!=null){
                Item i = new Item(Item.Type.valueOf(t.itemType));
                tt.setItem(i);
            }
            map.getGrid().add(tt);
        }

        mg = in.readObject();
        List<DefaultSpriteWrapper> sprites = (List<DefaultSpriteWrapper>)mg;
        map.getSprites().clear();
        for(DefaultSpriteWrapper w:sprites){
            map.getSprites().add(w.instantiate(map));
        }

        in.close();
        map.setGridProperties(tc,tr,ts);
    }

}
