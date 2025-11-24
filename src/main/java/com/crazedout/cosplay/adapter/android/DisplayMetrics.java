package com.crazedout.cosplay.adapter.android;

public class DisplayMetrics {

    View view;
    public int widthPixels;
    public int heightPixels;

    public DisplayMetrics(View view){
        this.view = view;
        this.widthPixels = view.widthPixels;
        this.heightPixels = view.heightPixels;
    }

}
