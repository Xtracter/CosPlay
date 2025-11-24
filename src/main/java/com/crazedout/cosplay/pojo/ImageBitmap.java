package com.crazedout.cosplay.pojo;

import com.crazedout.cosplay.CosBitmap;

import java.awt.*;
import java.io.File;
import java.io.Serializable;

public class ImageBitmap implements CosBitmap, Serializable {

    protected transient Image image;
    protected String name;
    protected String file;

    public ImageBitmap(Image i, File file){
        this.image=i;
        this.name = file.getName();
        this.file = file.getAbsolutePath();
    }

    public ImageBitmap(Image i, String file){
        this.image=i;
        this.name = file;
        this.file = file;
    }

    public Image getImage(){
        return this.image;
    }

    public void setImage(Object image){
        this.image = (Image)image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
