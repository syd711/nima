package com.nima.render;

import com.badlogic.gdx.Gdx;
import com.nima.util.Settings;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * The CachedMapLoader thread checks if frames that are possible
 * next frames for the 3x3 rendering are already in the cache.
 *
 * Otherwise the corresponding frames will be loaded asynchronously.
 */
public class MapFragmentLoader extends Thread {
  private final static Logger LOG = Logger.getLogger(MapFragmentLoader.class.getName());

  private Queue<TmxCacheMapLoader> mapQueue = new ConcurrentLinkedQueue<>();

  private MapCache mapCache;
  private int frameX = -1;
  private int frameY = -1;
  private boolean gdlThread = false;

  public MapFragmentLoader(MapCache mapCache, int frameX, int frameY) {
    this.mapCache = mapCache;
    this.frameX = frameX;
    this.frameY = frameY;
  }

  @Override
  public void run() {
    updateQueue();

    while(!mapQueue.isEmpty()) {
      TmxCacheMapLoader loader = mapQueue.poll();
      executeLoaderThread(loader);
    }
  }

  private void executeLoaderThread(final TmxCacheMapLoader loader) {
    if(loader.isDirty()) {
      if(gdlThread) {
        loadUncached(loader);
      }
      else {
        Gdx.app.postRunnable(new Runnable() {
          @Override
          public void run() {
            loadUncached(loader);
          }
        });
      }
    }
    else {
      LOG.info("Loading cached map " + loader.getFilename());
      doCache(loader);
    }
  }

  /**
   * Called when loading textures is required.
   */
  private void loadUncached(TmxCacheMapLoader loader) {
    loader.loadUncached();
    doCache(loader);
  }

  private void doCache(TmxCacheMapLoader loader) {
    TiledMapFragment cachedMap = new TiledMapFragment(loader);
    mapCache.cacheMap.put(loader.getFilename(), cachedMap);
  }

  /**
   * Checks which frames to load for the given frameX and frameY.
   */
  private void updateQueue() {
    int startX = frameX - 1;
    int startY = frameY - 1;

    int CACHE_SIZE = 3;

    for(int x = startX; x < (startX + CACHE_SIZE); x++) {
      if(x >= 0 && x < Settings.WORLD_WIDTH) {
        for(int y = startY; y < (startY + CACHE_SIZE); y++) {
          if(y >= 0 && y < Settings.WORLD_HEIGHT) {
            String filename = MapCache.getInstance().keyFor(x, y);
            if(!mapCache.cacheMap.containsKey(filename)) {
              TmxCacheMapLoader loader = new TmxCacheMapLoader(filename, x, y);
              mapQueue.offer(loader);
            }
          }
        }
      }
    }
  }

  public void setGdlThread(boolean gdlThread) {
    this.gdlThread = gdlThread;
  }
}
