package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.Map;
import com.crazedout.cosplay.Scrollable;
import com.crazedout.cosplay.DefaultSpriteWrapper;
import com.crazedout.cosplay.DefaultTileWrapper;
import com.crazedout.cosplay.pojo.ImageBitmap;
import com.crazedout.cosplay.sprites.DefaultActorSprite;
import com.crazedout.cosplay.sprites.DefaultUserSprite;
import com.crazedout.cosplay.sprites.DefaultZombieSprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class CosMapFactory {

    private CosMapFactory(){
    }

    public static void exportEditableMap(MapPanel panel, boolean exportImages){

        Map map = panel.map;

        try {
            File tmpDir = null;
            if(exportImages){
                tmpDir = Files.createTempDirectory("cosplay_").toFile();
                tmpDir.deleteOnExit();
            }


            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(CosResources.getInstance().userDir.getAbsolutePath()
                    + File.separatorChar + "maps" + File.separatorChar + "cosplay_map.cos"));
            fileChooser.setDialogTitle("Export map file");
            int userSelection = fileChooser.showSaveDialog(CosPlay.frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {

                File fileToSave = fileChooser.getSelectedFile();
                ObjectOutputStream obj = new ObjectOutputStream(
                        new FileOutputStream(fileToSave));

                obj.writeObject(panel.t.getSpeed());

                Object props[] = {map.getCols(),map.getRows(),map.getSize()};
                obj.writeObject(props);

                if(map.getBackgroundImage()==null) obj.writeObject(new String("nobgimage"));
                else {
                    obj.writeObject(map.getBackgroundImage());
                    if(exportImages) exportFile(tmpDir,(ImageBitmap)map.getBackgroundImage());
                }

                if(map.getDefaultTileImage()==null) obj.writeObject(new String("noDefaultimage"));
                else {
                    obj.writeObject(map.getDefaultTileImage());
                    if(exportImages) exportFile(tmpDir,(ImageBitmap)map.getDefaultTileImage());
                }

                if(map.getDefaultBgTileImage()==null) obj.writeObject(new String("noDefaultBgimage"));
                else {
                    obj.writeObject(map.getDefaultBgTileImage());
                    if(exportImages) exportFile(tmpDir,(ImageBitmap)map.getDefaultBgTileImage());
                }

                obj.writeObject(map.getBackgroundColor()!=null?map.getBackgroundColor():new String("noBgColor"));

                if (map instanceof Scrollable){
                    Scrollable sc = (Scrollable)map;
                    obj.writeObject(sc.getViewPort() != null ? sc.getViewPort() : new String("noViewPort"));
                }

                obj.writeObject(map.getGameType());

                obj.writeObject(map.getGrid());

                List<DefaultSpriteWrapper> sprites = new ArrayList<>();
                for(Sprite s:map.getSprites()){
                    if(s instanceof DefaultUserSprite) {
                        DefaultUserSprite us = (DefaultUserSprite) s;
                        DefaultSpriteWrapper w = new DefaultSpriteWrapper(s.getId(), s.getClass().getName(),
                                us.getImageMap(), s.getSpeed(), s.getStartTile());
                        sprites.add(w);
                    }else{
                        DefaultActorSprite us = (DefaultActorSprite)s;
                        DefaultSpriteWrapper w = new DefaultSpriteWrapper(s.getId(), s.getClass().getName(),
                                us.getImageMap(), s.getSpeed(),s.getStartTile());
                        sprites.add(w);
                    }
                }
                obj.writeObject(sprites);

                obj.close();
                if(exportImages) {
                    for (Tile t : map.getGrid()) {
                        if(t.getImage()!=null) exportFile(tmpDir,(ImageBitmap)t.getImage());
                        if(t.getDefaultBgImage()!=null) exportFile(tmpDir,(ImageBitmap)t.getDefaultBgImage());
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            CosPlay.showErrorDialog("Error", ex.getMessage());
        }
    }

    public static void exportAllGameMaps(List<CosMap> maps, boolean exportImages) throws Exception {

        File tmpDir = null;
        if(exportImages){
            tmpDir = Files.createTempDirectory("cosplay_").toFile();
            tmpDir.deleteOnExit();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(CosResources.getInstance().userDir.getAbsolutePath()
                + File.separatorChar +  "maps" + File.separatorChar + "cosplay.zip"));
        fileChooser.setDialogTitle("Export map file");
        int userSelection = fileChooser.showSaveDialog(CosPlay.frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String zipFile = fileToSave.getName();
            File indexFile = new File(tmpDir.getAbsolutePath() + File.separatorChar + "index.cos");
            FileOutputStream fis = new FileOutputStream(indexFile);
            fis.write("[CrazedoutSoft CosPlay Map (c) created at:".getBytes());
            long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(yourmilliseconds);
            fis.write((sdf.format(resultdate) + "]\n").getBytes());
            fis.write(("[Compiled on " + System.getProperty("java.version") + "]\n").getBytes());
            fis.write(("[License: " + CosPlay.getLicense() + "]\n").getBytes());
            for(int i = 0; i < maps.size(); i++){
                File f = new File(fileToSave.getParent() + File.separatorChar + maps.get(i).name + ".map");
                export(tmpDir,f,maps.get(i),true);
                fis.write((maps.get(i).name + ".map\n").getBytes());
            }
            fis.close();
            zip(tmpDir, new File(fileToSave.getParent() + File.separatorChar + zipFile));
        }
    }

    public static void export(File tmpDir, File fileToSave, Map map, boolean exportImages) throws Exception {

        ObjectOutputStream obj = new ObjectOutputStream(
                new FileOutputStream(fileToSave));

        Object props[] = {map.getCols(), map.getRows(), map.getSize()};
        obj.writeObject(props);

        if (map.getBackgroundImage() == null) obj.writeObject(new String("noBgImage"));
        else {
            obj.writeObject(map.getBackgroundImage().getName());
            if (exportImages) exportFile(tmpDir, map.getBackgroundImage());
        }

        if (map.getDefaultTileImage() == null) obj.writeObject(new String("noDefaultImage"));
        else {
            obj.writeObject(map.getDefaultTileImage().getName());
            if (exportImages) exportFile(tmpDir, map.getDefaultTileImage());
        }

        if (map.getDefaultBgTileImage() == null) obj.writeObject(new String("noDefaultBgImage"));
        else {
            obj.writeObject(map.getDefaultBgTileImage().getName());
            if (exportImages) exportFile(tmpDir, map.getDefaultBgTileImage());
        }

        obj.writeObject(map.getBackgroundColor() != null ? map.getBackgroundColor() : new String("noBgColor"));

        if (map instanceof Scrollable){
            Scrollable sc = (Scrollable)map;
            obj.writeObject(sc.getViewPort() != null ? sc.getViewPort() : new String("noViewPort"));
        }

        obj.writeObject(map.getGameType());

        List<DefaultTileWrapper> tiles = new ArrayList<>();
        for(Tile t:map.getGrid()){
            DefaultTileWrapper w = new DefaultTileWrapper();
            w.image=t.getImage()!=null?t.getImage().getName():null;
            w.defImage=t.getDefaultImage()!=null?t.getDefaultImage().getName():null;
            w.defBgImage=t.getDefaultBgImage()!=null?t.getDefaultBgImage().getName():null;
            if(w.defBgImage!=null) exportFile(tmpDir,t.getDefaultBgImage());
            w.wall=t.isWall();
            w.grave=t.isGrave();
            w.exit=t.isExit();
            w.index=t.getIndex();
            w.size=t.getSize();
            w.ladder=t.isLadder();
            w.x=t.x;
            w.y=t.y;
            w.file=t.getFile();
            if(t.getItem()!=null){
                w.itemName=t.getItem().getItemId();
                w.itemType=t.getItem().getType().name();
                w.itemSoundId=t.getItem().getSoundId();
            }
            tiles.add(w);
        }
        obj.writeObject(tiles);

        List<DefaultSpriteWrapper> sprites = new ArrayList<>();
        for(Sprite s:map.getSprites()){
            if(s instanceof DefaultUserSprite) {
                DefaultUserSprite us = (DefaultUserSprite) s;
                DefaultSpriteWrapper w = new DefaultSpriteWrapper(s.getId(), s.getClass().getName(),
                        us.getImageMap(), s.getSpeed(), s.getStartTile());
                sprites.add(w);
                if (exportImages) {
                    ImageBitmap bm = new ImageBitmap(
                            ImageIO.read(us.getClass().getResource("/sprites/" + us.getImageMap())),
                            (new File(us.getImageMap()).getName()));
                    exportSpriteFile(tmpDir, bm);
                }
            }else if(s instanceof DefaultActorSprite){
                if(s instanceof DefaultZombieSprite){
                    DefaultZombieSprite us = (DefaultZombieSprite) s;
                    DefaultSpriteWrapper w = new DefaultSpriteWrapper(s.getId(), s.getClass().getName(),
                            us.getImageMap(), s.getSpeed(), us.getStartTile(),((DefaultZombieSprite)s).getTypeIndex());
                    w.setStartTile(us.getStartTile());
                    sprites.add(w);
                    if (exportImages) {
                        for(CosBitmap bm:us.images){
                            System.out.println(bm.getName() + " " + bm.getFile() +
                                    " " + ((DefaultZombieSprite)s).getTypeIndex() + " " + w.typeIndex);
                            ImageBitmap img = new ImageBitmap(
                                    ImageIO.read(us.getClass().getResource("/sprites/" + bm.getName())),
                                    bm.getName());
                            exportSpriteFile(tmpDir,img);
                        }
                    }
                }else {
                    DefaultActorSprite us = (DefaultActorSprite) s;
                    DefaultSpriteWrapper w = new DefaultSpriteWrapper(s.getId(), s.getClass().getName(),
                            us.getImageMap(), s.getSpeed(), s.getStartTile());
                    sprites.add(w);
                    if (exportImages) {
                        ImageBitmap bm = new ImageBitmap(
                                ImageIO.read(us.getClass().getResource("/sprites/" + us.getImageMap())),
                                (new File(us.getImageMap()).getName()));
                        exportSpriteFile(tmpDir, bm);
                    }
                }
            }
        }
        obj.writeObject(sprites);
        exportSpriteFile(tmpDir,new ImageBitmap(ImageIO.read(map.getClass().getResource("/sprites/splat.png")),
                "splat.png"));
        obj.close();

        if(exportImages) {
            for (Tile t : map.getGrid()) {
                if(t.getImage()!=null) exportFile(tmpDir,t.getImage());
            }
        }
        copy(fileToSave,new FileOutputStream(tmpDir.getAbsolutePath() +  File.separatorChar + fileToSave.getName()));
        fileToSave.delete();
    }

    protected static void exportSpriteFile(File outDir, CosBitmap bm){
        String file = System.getProperty("user.home") + File.separatorChar +
                "cosplay" + File.separatorChar + "sprites" + File.separatorChar +
                (new File(bm.getFile()).getName());
        try {
            InputStream in = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(outDir.getAbsolutePath() +
                    File.separatorChar + bm.getName());
            byte[] buf = new byte[8192];
            int length;
            while ((length = in.read(buf)) != -1) {
                fos.write(buf, 0, length);
            }
            in.close();
            fos.flush();
            fos.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    protected static void exportFile(File outDir, CosBitmap bm){
        System.out.println(outDir);
        try {
            InputStream in = new FileInputStream(bm.getFile());
            FileOutputStream fos = new FileOutputStream(outDir.getAbsolutePath() +
                    File.separatorChar + bm.getName());
            byte[] buf = new byte[8192];
            int length;
            while ((length = in.read(buf)) != -1) {
                fos.write(buf, 0, length);
            }
            in.close();
            fos.flush();
            fos.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void importEditableMap(String file, MapPanel panel, Class c){

        Map map = panel.map;

        try {
            int tc=0,tr=0,ts=0;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

            Object mg = in.readObject();
            panel.t.setSpeed((Long)mg);

            mg = in.readObject();
            if(mg instanceof Object[]){
                tc = (int)((Object[])mg)[0];
                tr = (int)((Object[])mg)[1];
                ts = (int)((Object[])mg)[2];
            }
            mg = in.readObject();
            if(mg instanceof CosImageBitmap){
                CosImageBitmap bm = (CosImageBitmap)mg;
                bm.setImage(ImageIO.read(new FileInputStream(bm.getFile())));
                map.setBackgroundImage(bm);
            }
            mg = in.readObject();
            if(mg instanceof CosImageBitmap){
                CosImageBitmap bm = (CosImageBitmap)mg;
                bm.setImage(ImageIO.read(new FileInputStream(bm.getFile())));
                map.setDefaultTileImage(bm);
            }
            mg = in.readObject();
            if(mg instanceof CosImageBitmap){
                CosImageBitmap bm = (CosImageBitmap)mg;
                bm.setImage(ImageIO.read(new FileInputStream(bm.file)));
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
            List<Tile> list = (List<Tile>)mg;

            map.getGrid().clear();
            CosImageBitmap bm = null;
            for(Tile t:list){
                if(t.getDefaultBgImage()!=null){
                    try {
                        bm = new CosImageBitmap(ImageIO.read(new FileInputStream(t.getDefaultBgImage().getFile())), new File(t.getDefaultBgImage().getFile()));
                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                    t.setDefaultBgImage(bm);
                }
                if(t.getFile()!=null){
                    if(t.getFile().endsWith("blank.png")){
                        try {
                            bm = new CosImageBitmap(ImageIO.read(c.getClass().getResourceAsStream("/blank.png")), new File("/blank.png"));
                        }catch(IOException ex){
                            ex.printStackTrace();
                        }
                    }else {
                        bm = new CosImageBitmap(ImageIO.read(new FileInputStream(t.getFile())), new File(t.getFile()));
                    }
                    bm.exit=t.isExit();
                    bm.wall=t.isWall();
                    bm.grave=t.isGrave();
                    bm.ladder=t.isLadder();
                    t.setImage(bm);
                    if(map.getDefaultTileImage()!=null) t.setDefaultImage((CosImageBitmap)map.getDefaultTileImage());
                    if(map.getDefaultBgTileImage()!=null) t.setDefaultBgImage((CosImageBitmap)map.getDefaultBgTileImage());
                }
                ((CosTile)t).mark=false;
                map.getGrid().add(t);
            }

            mg = in.readObject();
            List<DefaultSpriteWrapper> sprites = (List<DefaultSpriteWrapper>)mg;
            map.getSprites().clear();
            for(DefaultSpriteWrapper w:sprites){
                map.getSprites().add(w.instantiate(map));
            }

            in.close();
            map.setGridProperties(tc,tr,ts);
        }catch(Exception ex){
            CosPlay.showErrorDialog("Error",ex.getMessage());
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            copy(in, out);
        } finally {
            in.close();
        }
    }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            copy(in, out);
        } finally {
            out.close();
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    public static void zip(File directory, File zipfile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        } finally {
            res.close();
        }
    }

    public static void unzip(File zipfile, File directory) throws IOException {
        ZipFile zfile = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> entries = zfile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File file = new File(directory, entry.getName());
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
                InputStream in = zfile.getInputStream(entry);
                try {
                    copy(in, file);
                } finally {
                    in.close();
                }
            }
        }
    }

}
