package com.crazedout.cosplay.pojo;

import com.crazedout.cosplay.CosBitmap;
import com.crazedout.cosplay.Resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResource implements Resources {

    @Override
    public InputStream getResource(String id) throws Exception {
        return new FileInputStream(id);
    }

    public InputStream getMap(){
        return null;
    }

    public CosBitmap loadImage(String imageFile, int row, int col){
        try {
            InputStream in = new FileInputStream(imageFile);
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
        } catch (IOException e) {
            System.out.println("The image was not loaded.");
        }
        return null;
    }

}
