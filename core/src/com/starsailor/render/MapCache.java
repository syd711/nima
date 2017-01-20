package com.starsailor.render;

import com.badlogic.gdx.Gdx;

import java.util.LinkedHashMap;

/**
 * Used to cache a loaded map.
 */
public class MapCache {
  private static MapCache INSTANCE = new MapCache();

  protected LinkedHashMap<String, TiledMapFragment> cacheMap = new LinkedHashMap<>();

  //file infos
  private String folder;
  private String prefix;

  //center position
  private int frameX = -1;
  private int frameY = -1;

  public static MapCache getInstance() {
    return INSTANCE;
  }

  private MapCache() {

  }

  //force singleton
  protected MapCache(String folder, String filePrefix) {
    this.folder = folder;
    this.prefix = filePrefix;
  }

  public TiledMapFragment initCache(String folder, String filePrefix) {
    this.folder = folder;
    this.prefix = filePrefix;

    MapFragmentLoader mapFragmentLoader = new MapFragmentLoader(this, 0, 0);
    mapFragmentLoader.setGdlThread(true);
    mapFragmentLoader.run();

    String key = keyFor(0, 0);
    return cacheMap.get(key);
  }

  /**
   * Invokes the map loader thread for the given position.
   * If the current frame hasn't changed, the loader thread is not notified.
   *
   * @param frameX current frame X
   * @param frameY current frame Y
   */
  public void updateCache(int frameX, int frameY) {
    if(this.frameX != frameX || this.frameY != frameY) {
      this.frameX = frameX;
      this.frameY = frameY;
      new MapFragmentLoader(this, frameX, frameY).start();
    }
  }

  /**
   * Checks which maps are not used anymore
   */
  public void evict(TiledMapFragment map) {
    cacheMap.remove(map.getFilename());
    Gdx.app.log(this.toString(),"Evicted " + map.getFilename());
  }

  /**
   * Returns the given frame tile from the cache.
   * If the frame is not found, an exception is thrown since
   * this method is access from the rendering and should never
   * load a map directly.
   *
   * @param x frame position X
   * @param y frame position Y
   */
  public TiledMapFragment get(int x, int y) {
    String key = keyFor(x, y);
    if(cacheMap.containsKey(key)) {
      return cacheMap.get(key);
    }

    throw new UnsupportedOperationException("Tile " + x + ", " + y + " not cached yet.");
  }

  /**
   * Helper for generating the cache key
   */
  protected String keyFor(int x, int y) {
    String key = prefix + x + "," + y + ".tmx";
    if(folder != null) {
      if(!folder.endsWith("/")) {
        folder += "/";
      }
      key = folder + key;
    }
    return key;
  }

}
