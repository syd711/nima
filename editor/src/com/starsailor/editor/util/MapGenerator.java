package com.starsailor.editor.util;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Used to create a new map
 */
public class MapGenerator {

  private final static String ASSET_MAP_DIR =  "core/assets/maps/";

  public static void main(String[] args) throws IOException {
    String map = Files.toString(new File(ASSET_MAP_DIR + "main/main_0,1.tmx"), Charset.defaultCharset());

    int x = 4;
    int y = 4;
    String mapName = "main";

    for(int i=0; i<x; i++) {
      for(int j=0; j<y; j++) {
        String filename = ASSET_MAP_DIR + mapName + "/" + mapName + "_" + i + "," + j + ".tmx";
        File mapFile = new File(filename);

        if(!mapFile.exists()) {
          mapFile.getParentFile().mkdirs();
          Files.write(map.getBytes(), mapFile);
          System.out.println("Created " + mapFile.getAbsolutePath());
        }


      }
    }

  }
}
