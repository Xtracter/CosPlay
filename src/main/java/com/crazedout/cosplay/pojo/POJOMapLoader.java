package com.crazedout.cosplay.pojo;

import com.crazedout.cosplay.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class POJOMapLoader implements MapLoader {

    public POJOMapLoader(){
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
        if(!bmstr.startsWith("noBgImage")){
            ImageBitmap bm = new ImageBitmap(ImageIO.read(res.getResource(bmstr)), new File(bmstr));
            map.setBackgroundImage(bm);
        }
        bmstr = (String)in.readObject();
        if(!bmstr.startsWith("noDefaultImage")){
            ImageBitmap bm = new ImageBitmap(ImageIO.read(res.getResource(bmstr)), new File(bmstr));
            defaultTile=bm;
            map.setDefaultTileImage(bm);
        }
        bmstr = (String)in.readObject();
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
        map.setGameType((GameType)mg);

        mg = in.readObject();
        List<DefaultTileWrapper> list = (List<DefaultTileWrapper>)mg;

        map.getGrid().clear();
        ImageBitmap bm = null;
        for(DefaultTileWrapper t:list){
            Tile tt = new Tile(t.x,t.y,t.size,t.index);
            tt.setDefaultImage(defaultTile);
            tt.setWall(t.wall);
            tt.setExit(t.exit);
            tt.setGrave(t.grave);
            tt.setWall(t.wall);
            tt.setLadder(t.ladder);
            if(t.defBgImage!=null){
                try {
                    if(t.defBgImage.indexOf("blank.png")==-1) {
                        bm = new ImageBitmap(ImageIO.read(res.getResource(t.defBgImage)), new File(t.defBgImage));
                        tt.setDefaultBgImage(bm);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }else{
                tt.setDefaultBgImage(defaultBgTile);
            }
            if(t.file!=null){
                try {
                    if(t.file.indexOf("blank.png")==-1) {
                        File f = new File(t.file);
                        InputStream zin = res.getResource(f.getName());
                        bm = new ImageBitmap(ImageIO.read(zin), new File(f.getName()));
                        zin.close();
                    }
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
                tt.setImage(bm);
                if(map.getDefaultTileImage()!=null) tt.setDefaultImage(map.getDefaultTileImage());
            }
            if(t.itemName!=null){
                Item i = new Item(Item.Type.valueOf(t.itemType),t.itemName,t.itemSoundId);
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
