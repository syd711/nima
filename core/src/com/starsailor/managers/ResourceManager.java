package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.google.common.io.Files;
import com.starsailor.util.Resources;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility superclass for resource based manager singletons.
 */
public class ResourceManager {

  private static ResourceManager instance = new ResourceManager();

  private AssetManager assetManager;

  private Map<String,String> textureMapping = new HashMap<>();
  private Map<String,String> textureAtlasMapping = new HashMap<>();

  private Map<String,String> spineJsonMapping = new HashMap<>();

  //force singleton
  private ResourceManager() {
  }

  public static ResourceManager getInstance() {
    return instance;
  }

  public void loadAssets() {
    assetManager = new AssetManager();

    loadAsset(Resources.TEXTURES, Texture.class, ".png", textureMapping);
    loadAsset(Resources.SPINES, TextureAtlas.class, ".atlas", textureAtlasMapping);
    loadSpines(Resources.SPINES);

    assetManager.finishLoading();
  }

  public <T> T getAsset(String name, Class<T> assetType) {
    return assetManager.get(name, assetType);
  }

  public TextureAtlas getTextureAtlasAsset(String name) {
    return assetManager.get(textureAtlasMapping.get(name), TextureAtlas.class);
  }

  public SkeletonData getSkeletonData(String name, float jsonScaling) {
    TextureAtlas atlas = ResourceManager.getInstance().getTextureAtlasAsset(name);
    // This loads skeleton JSON data, which is stateless.
    SkeletonJson json = new SkeletonJson(atlas);
    json.setScale(jsonScaling); // Load the skeleton at x% the size it was in Spine.

    String jsonData = spineJsonMapping.get(name);
    return json.readSkeletonData(name, jsonData);
  }


  public Texture getTextureAsset(String name) {
    return assetManager.get(textureMapping.get(name), Texture.class);
  }

  public List<Texture> getTextureAssets(String prefix) {
    List<Texture> result = new ArrayList<>();
    Array<String> assetNames = assetManager.getAssetNames();
    for(String assetName : assetNames) {
      if(assetName.startsWith(prefix)) {
        result.add(getTextureAsset(assetName));
      }
    }
    return result;
  }

  public void dispose() {
    assetManager.dispose();
  }

  //--------------- Helper --------------------------------------------------

  private void loadSpines(String spines) {
    try {
      List<FileHandle> files = new ArrayList<>();
      findAssets(spines, ".json", files);

      for(FileHandle file : files) {
        String skeletonData = Files.toString(file.file(), Charset.defaultCharset());
        spineJsonMapping.put(file.nameWithoutExtension(), skeletonData);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Recursive loading of all assets in the given directory
   * @param dir the base directory to start with
   * @param assetClass the type of asset to load
   * @param suffix the filename suffix.
   * @param mapping
   */
  private void loadAsset(String dir, Class assetClass, String suffix, Map<String, String> mapping) {
    List<FileHandle> files = new ArrayList<>();
    findAssets(dir, suffix, files);

    for(FileHandle file : files) {
      assetManager.load(file.path(), assetClass);
      mapping.put(file.nameWithoutExtension(), file.path());
      Gdx.app.log(this.getClass().getName(), "Loaded asset " + file.path());
    }
  }

  /**
   * Recursive searching for all assets in the given directory
   * @param dir the base directory to start with
   * @param suffix the filename suffix.
   */
  private void findAssets(String dir, String suffix, List<FileHandle> files) {
    FileHandle internal = Gdx.files.internal(dir);
    FileHandle[] assetFiles = internal.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(suffix);
      }
    });

    for(FileHandle file : assetFiles) {
      files.add(file);
    }

    FileHandle[] subDirs = internal.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return new File(dir, name).isDirectory();
      }
    });

    for(FileHandle subDir : subDirs) {
      findAssets(subDir.path(), suffix, files);
    }
  }
}
