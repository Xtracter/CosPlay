package com.crazedout.cosplay;

public interface CosGraphics {

    void setColor(Object c);
    void setStyle(Object style);
    void drawImage(CosBitmap bm, int x, int y, int width, int height);
    void drawRect(int x, int y, int width, int height);
    void fillRect(int x, int y, int width, int height);
    void drawString(String s, int x, int y);
    void fillCircle(int x, int y, int rad);
    void translate(int x, int y);
}
