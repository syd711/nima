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
    //createMap("erebos", 4, 4);
    rename("main", "erebos");
  }

  private static void rename(String oldname, String newname) {
    String filename = ASSET_MAP_DIR + newname + "/";
    File[] files = new File(filename).listFiles();
    for(File file : files) {
      String fn = newname + file.getName().substring(file.getName().indexOf("_"), file.getName().length());
      file.renameTo(new File(file.getParent(), fn));
      System.out.println("Renamed to " + fn);
    }
  }

  /**
   * Creates a new map with the give name and size
   * @param mapName the map name
   * @param x
   * @param y
   * @throws IOException
   */
  private static void createMap(String mapName, int x, int y) throws IOException {
    String template = Files.toString(new File(ASSET_MAP_DIR + "main/main_0,1.tmx"), Charset.defaultCharset());

    for(int i=0; i<x; i++) {
      for(int j=0; j<y; j++) {
        String filename = ASSET_MAP_DIR + mapName + "/" + mapName + "_" + i + "," + j + ".tmx";
        File mapFile = new File(filename);

        if(!mapFile.exists()) {
          mapFile.getParentFile().mkdirs();
          Files.write(template.getBytes(), mapFile);
          System.out.println("Created " + mapFile.getAbsolutePath());
        }
      }
    }
  }
}
