package com.starsailor.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.IOException;

/**
 * The TmxCacheMapLoader loads the tiles itself and
 * and creates the file handles for the tmx file.
 *
 * The actual loading of the textures is done in a post processing
 * and only if they are not already cached.
 */
public class TmxCacheMapLoader extends TmxMapLoader {
  private static ObjectMap<String, Texture> cachedTextures = new ObjectMap<>();

  private Array<FileHandle> textureFiles;
  private FileHandle tmxFile;
  private TmxMapLoader.Parameters parameters;
  private TiledMap map;

  private int frameX;
  private int frameY;

  public TmxCacheMapLoader(int frameX, int frameY) {
    this.frameX = frameX;
    this.frameY = frameY;

    TmxMapLoader.Parameters par = new TmxMapLoader.Parameters();
    load(TmxSettings.keyFor(frameX, frameY), par);

    TmxMapLoader.Parameters parameters = this.parameters;
    for (FileHandle textureFile : textureFiles) {
      //recheck if the file has not been loaded by GDL thread meanwhile
      String key = textureFile.path();
      if(!cachedTextures.containsKey(key)) {
        Gdx.app.log(this.toString(),"Loading Texture file " + textureFile.file().getAbsolutePath());
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
  private void createMap() {
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

      //we return null since the actual map creation is done in a post processing, see createMap()
      return null;
    } catch (IOException e) {
      throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
    }
  }

  public int getFrameX() {
    return frameX;
  }

  public int getFrameY() {
    return frameY;
  }
}
