package com.crazedout.cosplay.adapter.android;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Bitmap  {

    private Image image;

    public Bitmap(Image image){
        this.image = image;
    }

    public Image getImage(){
        return this.image;
    }

    public int getWidth(ImageObserver observer) {
        return this.image.getWidth(observer);
    }

    public int getHeight(ImageObserver observer) {
        return this.image.getHeight(observer);
    }
}
