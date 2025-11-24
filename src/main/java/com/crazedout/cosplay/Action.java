package com.crazedout.cosplay;

public class Action {

    protected Actions action;
    protected Object obj;

    public Action(Actions a){
        this(a,null);
    }


    public Action(Actions a, Object obj){
        this.action=a;
        this.obj=obj;
    }

    public Actions getAction(){
        return this.action;
    }

    public Object getObject(){
        return this.obj;
    }


}
