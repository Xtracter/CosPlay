package com.crazedout.cosplay;

import java.io.InputStream;

public interface Resources {

    InputStream getResource(String id) throws Exception;
    CosBitmap loadImage(String imageFile, int row, int col);

}
