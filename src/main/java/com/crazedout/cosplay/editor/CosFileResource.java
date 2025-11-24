package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.CosBitmap;
import com.crazedout.cosplay.pojo.FileResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CosFileResource extends FileResource {

    @Override
    public InputStream getResource(String id) throws Exception {
        String userDir = System.getProperty("user.home") + File.separatorChar + "cosplay" + File.separatorChar;
        return new FileInputStream(userDir + "sprites\\" + id);
    }

    public CosBitmap loadImage(String imageFile, int row, int col) {
        String userDir = System.getProperty("user.home") + File.separatorChar +
                "cosplay" + File.separatorChar + "sprites\\" + imageFile;
        return super.loadImage(userDir,row,col);
    }

}
