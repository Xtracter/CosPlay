package com.crazedout.cosplay.pojo;

import com.crazedout.cosplay.CosBitmap;
import com.crazedout.cosplay.Resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResource implements Resources {

    private ZipFile zipFile;

    public ZipResource(String file) throws IOException {
        zipFile = new ZipFile(file);
    }

    public List<String> getMapNames(){
        int n = 0;
        List<String> names = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(entry.getName().endsWith(".map")) {
                names.add(entry.getName());
            }
        }
        return names;
    }

    public InputStream getMap(String name) throws Exception {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(entry.getName().equals(name)) {
                return zipFile.getInputStream(entry);
            }
        }
        return null;
    }


    @Override
    public InputStream getResource(String name) throws Exception {
        File f = new File(name);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(entry.getName().equals(f.getName())) {
                return zipFile.getInputStream(entry);
            }
        }
        return null;
    }

    public CosBitmap loadImage(String imageFile, int row, int col){
        try {
            InputStream in = getResource(imageFile);
            BufferedImage i = ImageIO.read(in);
            if(row>-1 && col>-1){
                int imgWidth = 128;
                int imgHeight = 192;
                int x = col*imgWidth/4;
                int y = row*imgHeight/4;
                return new ImageBitmap(i.getSubimage(x, y, imgWidth / 4, imgHeight / 4), new File(imageFile));
            }else{
                return new ImageBitmap(i,new File(imageFile));
            }
        } catch (Exception e) {
            System.out.println("The image was not loaded: " + imageFile);
        }
        return null;
    }
}
