package com.starsailor.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.starsailor.util.Settings;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

public class TiledMultiMapRenderer extends OrthogonalTiledMapRenderer {
  private final static String MAP_DIR = "maps/";

  private int actorFragmentX = 0;
  private int actorFragmentY = 0;

  private int frameTilesX = 0;
  private int frameTilesY = 0;

  private int framePixelsX = 0;
  private int framePixelsY = 0;

  private int worldPixelsX = 0;
  private int worldPixelsY = 0;

  private int worldWidth = 0;
  private int worldHeight = 0;

  //variables updated for each render frame/region
  private int frameNumberX;
  private int frameNumberY;
  private TiledMap frameMap;

  private List<MapObjectConverter> objectConverters = new ArrayList<>();
  private List<ParallaxLayer> parallaxLayers = new ArrayList<>();

  private Map<String, TiledMapFragment> currentMaps = new HashMap<>();
  private List<TiledMapFragment> dirtyList = new ArrayList<>();

  private String mapName;

  public TiledMultiMapRenderer(String name, SpriteBatch batch) {
    super(null, batch);
    this.mapName = name;

    initSettings();
  }

  private File fileFor(int x, int y) {
    return new File(MAP_DIR + mapName, mapName + "_" + x + "," + y + ".tmx");
  }

  /**
   * Parallax layers are added from bottom to top.
   *
   * @param resource the resource key for the layer
   */
  public void addParallaxLayer(String resource) {
    parallaxLayers.add(new ParallaxLayer(this, resource));
  }

  /**
   * Executese a full scan for this map loaded
   */
  public void fullScan() {
    Gdx.app.log(this.toString(), "Executing full map scan");

    for(int x = 0; x < worldWidth; x++) {
      for(int y = 0; y < worldHeight; y++) {
        File file = fileFor(x, y);
        Gdx.app.log(this.toString(), "Scanning " + file.getAbsolutePath());
        TiledMapFragment tiledMapFragment = new TiledMapFragment(this, file, x, y);

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

  public void setActorFragment(float posX, float posY) {
    actorFragmentX = (int) (posX / framePixelsX);
    actorFragmentY = (int) (posY / framePixelsY);
  }

  // --------------------- Main Render -------------------------------------

  @Override
  public void render() {
    //render the parallax layers first
    for(ParallaxLayer parallaxLayer : parallaxLayers) {
      parallaxLayer.render(getBatch());
    }

    beginRender();

    if(Settings.getInstance().debug) {
      Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    int startX = actorFragmentX - 1;
    int startY = actorFragmentY - 1;

    List<TiledMapFragment> usedFragments = new ArrayList<>();
    MapLayers layers = getMap().getLayers();
    for(MapLayer layer : layers) {
      String layerName = layer.getName();

      //render layer by layer for all frames
      for(int x = startX; x < startX + 3; x++) {
        if(x >= 0 && x < worldWidth) {
          for(int y = startY; y < startY + 3; y++) {
            if(y >= 0 && y < worldHeight) {
              frameNumberX = x;
              frameNumberY = y;

              TiledMapFragment mapFragment = getMapFragment(x, y);
              frameMap = mapFragment.getMap();
              if(!usedFragments.contains(mapFragment)) {
                usedFragments.add(mapFragment);
              }

              MapLayer l = frameMap.getLayers().get(layerName);

              if(l != null && l instanceof TiledMapTileLayer) {
                renderTileLayer((TiledMapTileLayer) l);
              }
//              else if(l != null && l instanceof TiledMapImageLayer){
//                super.renderImageLayer((TiledMapImageLayer) l);
//              }
            }
          }
        }
      }
    } //end layer rendering

    endRender();

    //listener handling
    updateListeners(usedFragments);
  }


  // --------------- Map Conversion ------------------------------------
  public void removeAllObjectConverters() {
    this.objectConverters.clear();
  }

  public void addMapObjectConverter(MapObjectConverter mapObjectConverter) {
    this.objectConverters.add(mapObjectConverter);
  }


  /**
   * Checks if map or object converts have to be called.
   * @param usedFragments
   */
  private void updateListeners(List<TiledMapFragment> usedFragments) {
    //remove and destroy unused map fragments first
    Map<String,TiledMapFragment> clone = new HashMap<>(currentMaps);
    for(TiledMapFragment mapFragment : clone.values()) {
      //the given map was not used during the rendering process
      if(!usedFragments.contains(mapFragment)) {
        //destroy all map objects after notifying listeners
        for(MapObjectConverter objectConverter : objectConverters) {
          List<MapObject> mapObjects = mapFragment.getMapObjects();
          for(MapObject mapObject : mapObjects) {
            objectConverter.destroy(mapFragment, mapObject);
          }
        }

        //remove map from loaded maps
        currentMaps.remove(mapFragment.toString());
      }
    }

    //check if the map converters have been triggered yet
    for(TiledMapFragment mapFragment : usedFragments) {
      if(mapFragment.isDirty()) {
        mapFragment.setDirty(false);
        //convert all map objects before notifying listeners
        for(MapObjectConverter objectConverter : objectConverters) {
          List<MapObject> mapObjects = mapFragment.getMapObjects();
          for(MapObject mapObject : mapObjects) {
            if(objectConverter.isApplicable(mapFragment, mapObject)) {
              objectConverter.convertMapObject(mapFragment, mapObject);
            }
          }
          objectConverter.finalizeConverter();
        }
      }
    }
  }

  //-------------------- the actual rendering ----------------------------------------

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

    float y = frameTilesY * layerTileHeight + frameNumberY * framePixelsY;
    final float[] vertices = this.vertices;

    //start rendering rows from top to bottom
    for(int row = layerHeight; row >= 0; row--) {
      float x = frameNumberX * framePixelsX;
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

  public int getFramePixelsX() {
    return framePixelsX;
  }

  public int getFramePixelsY() {
    return framePixelsY;
  }

  public int getWorldPixelsX() {
    return worldPixelsX;
  }

  public int getWorldPixelsY() {
    return worldPixelsY;
  }

  //-------------------- Helper------------------------------------


  /**
   * Loads the map for the given coordinates if not loaded yet.
   */
  private TiledMapFragment getMapFragment(int x, int y) {
    //get the map for the current frame
    File mapFile = fileFor(x, y);

    if(!currentMaps.containsKey(mapFile.getName())) {
      TiledMapFragment tiledMapFragment = new TiledMapFragment(this, mapFile, x, y);
      currentMaps.put(mapFile.getName(), tiledMapFragment);
      Gdx.app.log(this.toString(), "Loaded map " + tiledMapFragment);
    }

    return currentMaps.get(mapFile.getName());
  }

  /**
   * Sets all pixels sizes for calculations once the map is loaded.
   */
  private void initSettings() {
    TiledMapFragment loader = new TiledMapFragment(this, fileFor(0, 0), 0, 0);
    setMap(loader.getMap());

    MapProperties mapProperties = getMap().getProperties();
    int width = (int) mapProperties.get("width");
    int height = (int) mapProperties.get("height");
    int tileWidth = (int) mapProperties.get("tilewidth");
    int tileHeight = (int) mapProperties.get("tileheight");

    //well, frame pixels area easy
    framePixelsX = width * tileWidth;
    framePixelsY = height * tileHeight;

    //calculate galaxy size next
    File mapFolder = new File(MAP_DIR + mapName);
    File[] mapFiles = mapFolder.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".tmx");
      }
    });

    for(File map : mapFiles) {
      String name = map.getName();
      int x = Integer.parseInt(name.substring(name.indexOf("_") + 1, map.getName().indexOf(",")));
      if(x > worldWidth) {
        worldWidth = x;
      }

      int y = Integer.parseInt(name.substring(name.indexOf(",") + 1, map.getName().indexOf(".")));
      if(y > worldHeight) {
        worldHeight = y;
      }
    }

    worldWidth += 1;
    worldHeight += 1;

    //galaxy pixels
    worldPixelsX = framePixelsX * worldWidth;
    worldPixelsY = framePixelsY * worldHeight;

    TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get(0);
    this.frameTilesX = groundLayer.getWidth();
    this.frameTilesY = groundLayer.getHeight();
  }
}