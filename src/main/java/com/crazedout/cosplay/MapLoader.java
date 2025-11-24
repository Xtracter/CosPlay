package com.crazedout.cosplay;

import java.io.InputStream;

public interface MapLoader {

    void loadMap(Map map, String mapName, Resources res) throws Exception;

}
