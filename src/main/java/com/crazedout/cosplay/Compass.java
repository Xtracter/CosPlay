package com.crazedout.cosplay;

public class Compass {

    public enum Direction {
        NORTH,
        NORTH_WEST,
        WEST,
        SOUTH_WEST,
        SOUTH,
        SOUTH_EAST,
        EAST,
        NORTH_EAST
    }

    public static Tile getTileAt(int originIndex, Map map, Direction dir){
        return map.getGrid().get(getIndexAt(originIndex,map,dir));
    }

    public static int getIndexAt(int originIndex, Map map, Direction dir){
        int t = -1;
        switch(dir){
            case NORTH:
                t=map.getGrid().get(originIndex-1).getIndex();
                break;
            case NORTH_EAST:
                t=map.getGrid().get(originIndex+map.getRows()-1).getIndex();
                break;
            case EAST:
                t=map.getGrid().get(originIndex+map.getRows()).getIndex();
                break;
            case SOUTH_EAST:
                t=map.getGrid().get(originIndex+map.getRows()+1).getIndex();
                break;
            case SOUTH:
                t=map.getGrid().get(originIndex+1).getIndex();
                break;
            case SOUTH_WEST:
                t=map.getGrid().get(originIndex-map.getRows()+1).getIndex();
                break;
            case WEST:
                t=map.getGrid().get(originIndex-map.getRows()).getIndex();
                break;
            case NORTH_WEST:
                t=map.getGrid().get(originIndex-map.getRows()-1).getIndex();
                break;
        }
        return t;
    }


}
