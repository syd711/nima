package com.nima.render;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The TmxCacheMapLoader loads the tiles itself and
 * and creates the file handles for the tmx file.
 *
 * The actual loading of the textures is done in a post processing
 * and only if they are not already cached.
 */
public class TmxCacheMapLoader extends TmxMapLoader {
  private final static Logger LOG = Logger.getLogger(TmxCacheMapLoader.class.getName());
  private static ObjectMap<String, Texture> cachedTextures = new ObjectMap<>();

  private Array<FileHandle> textureFiles;
  private FileHandle tmxFile;
  private TmxMapLoader.Parameters parameters;
  private TiledMap map;
  private boolean dirty;
  private String filename;

  private int frameX;
  private int frameY;

  public TmxCacheMapLoader(String filename, int frameX, int frameY) {
    this.filename = filename;
    this.frameX = frameX;
    this.frameY = frameY;

    super.load(filename);

    Array<FileHandle> textureFiles = this.textureFiles;
    for(FileHandle textureFile : textureFiles) {
      String key = textureFile.path();
      if(!cachedTextures.containsKey(key)) {
        this.dirty = true;
      }
    }

    if(!isDirty()) {
      createMap();
    }
  }


  /**
   * This method must be invoked with a GL context to load the missing
   * textures for the given tmx map loader
   */
  protected void loadUncached() {
    LOG.info("Loading uncached map " + getFilename());

    TmxMapLoader.Parameters parameters = this.parameters;

    for (FileHandle textureFile : textureFiles) {
      //recheck if the file has not been loaded by GDL thread meanwhile
      String key = textureFile.path();
      if(!cachedTextures.containsKey(key)) {
        LOG.info("Loading Texture file " + textureFile.file().getAbsolutePath());
        Texture texture = new Texture(textureFile, parameters.generateMipMaps);
        texture.setFilter(parameters.textureMinFilter, parameters.textureMagFilter);
        cachedTextures.put(textureFile.path(), texture);
      }
    }

    createMap();
  }

  /**
   * Creates the map from the cached textures that have been applied before.
   */
  protected void createMap() {
    ImageResolver.DirectImageResolver imageResolver = new ImageResolver.DirectImageResolver(cachedTextures);
    map = loadTilemap(root, tmxFile, imageResolver);
    map.setOwnedResources(cachedTextures.values().toArray());
  }

  protected TiledMap getMap() {
    return map;
  }


  @Override
  public TiledMap load (String fileName, TmxMapLoader.Parameters parameters) {
    try {
      this.parameters = parameters;
      this.convertObjectToTileSpace = parameters.convertObjectToTileSpace;
      this.flipY = parameters.flipY;
      tmxFile = resolve(fileName);
      root = xml.parse(tmxFile);

      textureFiles = loadTilesets(root, tmxFile);
      textureFiles.addAll(loadImages(root, tmxFile));

      //we return null since the actual map creation is done in a post processing
      return null;
    } catch (IOException e) {
      throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
    }
  }

  public boolean isDirty() {
    return dirty;
  }

  public String getFilename() {
    return filename;
  }

  public int getFrameX() {
    return frameX;
  }

  public int getFrameY() {
    return frameY;
  }
}
