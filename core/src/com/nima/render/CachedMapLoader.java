package com.nima.render;

import com.badlogic.gdx.Gdx;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * The CachedMapLoader thread checks if frames that are possible
 * next frames for the 3x3 rendering are already in the cache.
 *
 * Otherwise the corresponding frames will be loaded asynchronously.
 */
public class CachedMapLoader extends Thread {
  private final static Logger LOG = Logger.getLogger(CachedMapLoader.class.getName());

  private Queue<TmxCacheMapLoader> mapQueue = new ConcurrentLinkedQueue<>();

  private MapCache mapCache;
  private int frameX = -1;
  private int frameY = -1;
  private boolean gdlThread = false;

  public CachedMapLoader(MapCache mapCache, int frameX, int frameY) {
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
    CachedTiledMap cachedMap = new CachedTiledMap(loader);
    mapCache.cacheMap.put(loader.getFilename(), cachedMap);
  }

  /**
   * Checks which frames to load for the given frameX and frameY.
   */
  private void updateQueue() {
    int startX = frameX - 2;
    int startY = frameY - 2;

    for(int x = startX; x < (startX + 5); x++) {
      if(x >= 0) {
        for(int y = startY; y < (startY + 5); y++) {
          if(y >= 0) {
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
