package com.crazedout.cosplay.adapter.android;

import javax.imageio.ImageIO;
import java.awt.*;

public class BitmapFactory {

    protected final static String images[] = {
            "cosplay.pack",
    };

    public static Bitmap decodeResource(Resources r, int id){
      try {
          Image i = ImageIO.read(r.getClass().getResource("/" + images[id]));
          return new Bitmap(i);
      }catch(Exception ex){
          System.out.println(images[id]);
          ex.printStackTrace();
      }
      return null;
    }

}
