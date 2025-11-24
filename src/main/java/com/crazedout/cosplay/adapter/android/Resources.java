package com.crazedout.cosplay.adapter.android;

public class Resources {

    View view;
    DisplayMetrics displayMetrics;

    public Resources(View view){
        this.view = view;
        displayMetrics = new DisplayMetrics(view);
    }

    public DisplayMetrics getDisplayMetrics(){
        return displayMetrics;
    }

}
