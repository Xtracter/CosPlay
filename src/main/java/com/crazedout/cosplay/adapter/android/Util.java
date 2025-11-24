package com.crazedout.cosplay.adapter.android;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;

public class Util {

    public static ImageObserver observer;

    public static Rect getRect(int x, int y, int width, int height){
        return new Rect(x,y,x+width,y+height);
    }

    public static RectF getRectF(int x, int y, int width, int height){
        return new RectF(x,y,x+width,y+height);
    }

    public static void drawImage(Canvas c, Bitmap bm, int x, int y, int width, int height, Paint p){
        c.g.drawImage(bm.getImage(),x,y,width,height,observer);
    }

    public static void drawImage(Canvas c, Bitmap bm, int x, int y, Paint p){
        if(bm==null) return;
        Rect rf = getRect(x,y,bm.getWidth(observer),bm.getHeight(observer));
        drawBitmap(c,bm,rf.left,rf.top,p);
    }

    /*
    public static void drawRect(Canvas c, RectF rect, Paint paint){
        c.drawRect(rect,paint);
    }*/

    public static void drawImage(Canvas c, Bitmap bm, RectF rf, Paint p){
        c.g.drawImage(bm.getImage(), (int)rf.left, (int)rf.top,
                (int)(rf.right-rf.left), (int)(rf.bottom-rf.top), observer);
    }

    public static Bitmap loadImage(View game, int img, int row, int col){
        try {
            InputStream in = observer.getClass().getResourceAsStream("/" + BitmapFactory.images[img]);
            BufferedImage i = ImageIO.read(in);
            if(row>-1 && col>-1){
                int imgWidth = 128;
                int imgHeight = 192;
                int x = col*imgWidth/4;
                int y = row*imgHeight/4;
                return new Bitmap(i.getSubimage(x, y, imgWidth / 4, imgHeight / 4));
            }else{
                return new Bitmap(i);
            }
        } catch (IOException e) {
            System.out.println("The image was not loaded.");
        }
        return null;
    }

    public static void drawBitmap(Canvas c, Bitmap bm, int x, int y, Paint paint){
        Rect rf = getRect(x,y,bm.getWidth(observer),bm.getHeight(observer));
        c.g.drawImage(bm.getImage(),rf.left,rf.top,rf.right-rf.left,rf.top - rf.bottom,observer);
    }


}
