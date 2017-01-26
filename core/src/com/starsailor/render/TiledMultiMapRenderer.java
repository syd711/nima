package com.starsailor.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.starsailor.Game;
import com.starsailor.util.Settings;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

public class TiledMultiMapRenderer extends OrthogonalTiledMapRenderer {
  public static TiledDebugRenderer debugRenderer;

  public int actorFrameX = 0;
  public int actorFrameY = 0;

  private int frameTilesX = 0;
  private int frameTilesY = 0;

  //variables updated for each render frame/region
  private int frameNumberX;
  private int frameNumberY;
  private TiledMap frameMap;

  private List<MapChangeListener> mapChangeListeners = new ArrayList<>();
  private boolean dirty = true;
  private List<TiledMapFragment> currentMaps = new ArrayList<>();

  private List<MapObjectConverter> objectConverters = new ArrayList<>();

  private String mapFolder;
  private String mapPrefix;

  private List<TiledMapFragment> actualMaps = new ArrayList<>();

  public TiledMultiMapRenderer(String mapFolder, String mapPrefix, SpriteBatch batch) {
    super(null, batch);
    this.mapFolder = mapFolder;
    this.mapPrefix = mapPrefix;

    TiledMapFragment tiledMapFragment = MapCache.getInstance().initCache(mapFolder, mapPrefix);
    setMap(tiledMapFragment.getMap());

    debugRenderer = new TiledDebugRenderer(Game.camera);

    TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get(0);
    this.frameTilesX = groundLayer.getWidth();
    this.frameTilesY = groundLayer.getHeight();
  }

  public void fullScan(int maxX, int maxY) {
    Gdx.app.log(this.toString(), "Executing full map scan");
    for(int x=0; x<maxX; x++) {
      for(int y=0; y<maxY; y++) {
        MapCache mapCache = new MapCache(mapFolder, mapPrefix);
        MapFragmentLoader mapFragmentLoader = new MapFragmentLoader(mapCache, x, y);
        mapFragmentLoader.setLoadNeighbours(false);
        mapFragmentLoader.setGdlThread(true);
        mapFragmentLoader.run();
        TiledMapFragment tiledMapFragment = mapCache.get(x, y);

        for(MapObjectConverter objectConverter : objectConverters) {
          List<MapObject> mapObjects = tiledMapFragment.getMapObjects();
          for(MapObject mapObject : mapObjects) {
            if(objectConverter.isApplicable(tiledMapFragment, mapObject)) {
              objectConverter.convertMapObject(tiledMapFragment, mapObject);
            }
          }
        }
      }
    }
  }


  public void removeAllObjectConverters() {
    this.objectConverters.clear();
  }

  public void addMapObjectConverter(MapObjectConverter mapObjectConverter) {
    this.objectConverters.add(mapObjectConverter);
  }

  public void addMapChangeListener(MapChangeListener listener) {
    this.mapChangeListeners.add(listener);
  }

  public void setActorFrame(int x, int y) {
    if(this.actorFrameX != x || this.actorFrameY != y) {
      dirty = true;
    }
    this.actorFrameX = x;
    this.actorFrameY = y;
  }

  public void preRender() {
    MapCache.getInstance().updateCache(actorFrameX, actorFrameY);
    beginRender();
  }

  @Override
  public void render() {
    if(Settings.getInstance().debug) {
      Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    int startX = actorFrameX - 1;
    int startY = actorFrameY - 1;

    MapLayers layers = getMap().getLayers();
    for(MapLayer layer : layers) {
      String layerName = layer.getName();

      //render layer by layer for all frames
      for(int x = startX; x < startX + 3; x++) {
        if(x >= 0 && x < Settings.WORLD_WIDTH) {
          for(int y = startY; y < startY + 3; y++) {
            if(y >= 0 && y < Settings.WORLD_HEIGHT) {
              frameNumberX = x;
              frameNumberY = y;
              //get the map for the current frame
              TiledMapFragment tiledMapFragment = MapCache.getInstance().get(x, y);
              if(!actualMaps.contains(tiledMapFragment)) {
                actualMaps.add(tiledMapFragment);
              }


              frameMap = tiledMapFragment.getMap();
              MapLayer l = frameMap.getLayers().get(layerName);

              if(l != null && l instanceof TiledMapTileLayer) {
                renderTileLayer((TiledMapTileLayer) l);
              }
            }
          }
        }
      }
    } //end layer rendering

    if(Settings.getInstance().debug) {
      debugRenderer.render();
    }
  }

  public void postRender() {
    endRender();

    //listener handling
    if(dirty) {
      updateListeners(actualMaps);
      dirty = false;
    }

    actualMaps.clear();
  }

  /**
   * Compares the map that have been rendered
   * with the latest rendering result. Only called when dirty
   * @param actualMaps the maps that have been rendered during the last call
   */
  private void updateListeners(List<TiledMapFragment> actualMaps) {
    for(TiledMapFragment cachedMap : currentMaps) {
      if(!actualMaps.contains(cachedMap)) {
        for(MapChangeListener mapChangeListener : mapChangeListeners) {
          mapChangeListener.mapRemoved(cachedMap);
          Gdx.app.log(this.toString(),"Removed map " + cachedMap.getFilename());
        }

        //destroy all map objects after notifying listeners
        for(MapObjectConverter objectConverter : objectConverters) {
          List<MapObject> mapObjects = cachedMap.getMapObjects();
          for(MapObject mapObject : mapObjects) {
            objectConverter.destroy(cachedMap, mapObject);
          }
        }

        //TODO no real caching here
        MapCache.getInstance().evict(cachedMap);
      }
    }

    for(TiledMapFragment cachedMap : actualMaps) {
      if(!currentMaps.contains(cachedMap)) {
        //convert all map objects before notifying listeners
        for(MapObjectConverter objectConverter : objectConverters) {
          List<MapObject> mapObjects = cachedMap.getMapObjects();
          for(MapObject mapObject : mapObjects) {
            if(objectConverter.isApplicable(cachedMap, mapObject)) {
              objectConverter.convertMapObject(cachedMap, mapObject);
            }
          }
        }

        for(MapChangeListener mapChangeListener : mapChangeListeners) {
          mapChangeListener.mapAdded(cachedMap);
          Gdx.app.log(this.toString(),"Added map " + cachedMap.getFilename());
        }
      }
    }

    this.currentMaps.clear();
    this.currentMaps.addAll(actualMaps);
  }

  @Override
  public void renderTileLayer(TiledMapTileLayer l) {
    TiledMapTileLayer layer = (TiledMapTileLayer) frameMap.getLayers().get(l.getName());

    //TODO only render tiles inside the view
    final Color batchColor = batch.getColor();
    final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

    final int layerWidth = layer.getWidth();
    final int layerHeight = layer.getHeight();

    final float layerTileWidth = layer.getTileWidth() * unitScale;
    final float layerTileHeight = layer.getTileHeight() * unitScale;

    float y = frameTilesY * layerTileHeight + frameNumberY * Settings.FRAME_PIXELS_Y;
    final float[] vertices = this.vertices;

    //start rendering rows from top to bottom
    for(int row = layerHeight; row >= 0; row--) {
      float x = frameNumberX * Settings.FRAME_PIXELS_X;
      for(int col = 0; col < layerWidth; col++) {
        final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
        if(cell == null) {
          x += layerTileWidth;
          continue;
        }
        final TiledMapTile tile = cell.getTile();

        renderTile(color, y, vertices, x, cell, tile);
        x += layerTileWidth;
      }
      y -= layerTileHeight;
    }
  }

  /**
   * Rendering of a single tile cell
   */
  private void renderTile(float color, float y, float[] vertices, float x, TiledMapTileLayer.Cell cell, TiledMapTile tile) {
    if(tile != null) {
      final boolean flipX = cell.getFlipHorizontally();
      final boolean flipY = cell.getFlipVertically();
      final int rotations = cell.getRotation();

      TextureRegion region = tile.getTextureRegion();

      float x1 = x + tile.getOffsetX() * unitScale;
      float y1 = y + tile.getOffsetY() * unitScale; //no need to add more since y is always the upper pixel value for the frame
      float x2 = x1 + region.getRegionWidth() * unitScale;
      float y2 = y1 + region.getRegionHeight() * unitScale;

      float u1 = region.getU();
      float v1 = region.getV2();
      float u2 = region.getU2();
      float v2 = region.getV();

      vertices[X1] = x1;
      vertices[Y1] = y1;
      vertices[C1] = color;
      vertices[U1] = u1;
      vertices[V1] = v1;

      vertices[X2] = x1;
      vertices[Y2] = y2;
      vertices[C2] = color;
      vertices[U2] = u1;
      vertices[V2] = v2;

      vertices[X3] = x2;
      vertices[Y3] = y2;
      vertices[C3] = color;
      vertices[U3] = u2;
      vertices[V3] = v2;

      vertices[X4] = x2;
      vertices[Y4] = y1;
      vertices[C4] = color;
      vertices[U4] = u2;
      vertices[V4] = v1;

      if(flipX) {
        float temp = vertices[U1];
        vertices[U1] = vertices[U3];
        vertices[U3] = temp;
        temp = vertices[U2];
        vertices[U2] = vertices[U4];
        vertices[U4] = temp;
      }
      if(flipY) {
        float temp = vertices[V1];
        vertices[V1] = vertices[V3];
        vertices[V3] = temp;
        temp = vertices[V2];
        vertices[V2] = vertices[V4];
        vertices[V4] = temp;
      }
      if(rotations != 0) {
        switch(rotations) {
          case TiledMapTileLayer.Cell.ROTATE_90: {
            float tempV = vertices[V1];
            vertices[V1] = vertices[V2];
            vertices[V2] = vertices[V3];
            vertices[V3] = vertices[V4];
            vertices[V4] = tempV;

            float tempU = vertices[U1];
            vertices[U1] = vertices[U2];
            vertices[U2] = vertices[U3];
            vertices[U3] = vertices[U4];
            vertices[U4] = tempU;
            break;
          }
          case TiledMapTileLayer.Cell.ROTATE_180: {
            float tempU = vertices[U1];
            vertices[U1] = vertices[U3];
            vertices[U3] = tempU;
            tempU = vertices[U2];
            vertices[U2] = vertices[U4];
            vertices[U4] = tempU;
            float tempV = vertices[V1];
            vertices[V1] = vertices[V3];
            vertices[V3] = tempV;
            tempV = vertices[V2];
            vertices[V2] = vertices[V4];
            vertices[V4] = tempV;
            break;
          }
          case TiledMapTileLayer.Cell.ROTATE_270: {
            float tempV = vertices[V1];
            vertices[V1] = vertices[V4];
            vertices[V4] = vertices[V3];
            vertices[V3] = vertices[V2];
            vertices[V2] = tempV;

            float tempU = vertices[U1];
            vertices[U1] = vertices[U4];
            vertices[U4] = vertices[U3];
            vertices[U3] = vertices[U2];
            vertices[U2] = tempU;
            break;
          }
        }
      }
      batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
    }
  }

}