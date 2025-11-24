package com.crazedout.cosplay;

import java.io.Serializable;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        SMALL_MEDKIT,
        BIG_MEDKIT,
        HEART,
        AMMO_1,
        AMMO_2,
        GUN,
        SHOTGUN,
        RPG,
        AXE,
        KNIFE,
        GOLD,
        USER_DEFINED
    }

    String id;
    Type type = Type.USER_DEFINED;
    int soundId=0;
    boolean active = true;

    public Item(Type type) {
        this.type=type;
    }

    public Item(Type type, String id) {
        this(type,id,0);
    }

    public Item(Type type, String id, int soundId) {
        this.type=type;
        this.id=id;
        this.soundId=soundId;
    }

    public void setActive(boolean active){
        this.active=active;
    }

    public boolean isActive(){
        return this.active;
    }

    public Type getType(){
        return this.type;
    }

    public String getItemId(){
        return this.id;
    }

    public int getSoundId(){
        return this.soundId;
    }

}
