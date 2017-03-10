package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.starsailor.util.Resources;

import java.io.File;
import java.io.FilenameFilter;
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
  private Map<String,String> assetPathMapper = new HashMap<>();


  //force singleton
  private ResourceManager() {
  }

  public static ResourceManager getInstance() {
    return instance;
  }

  public void loadAssets() {
    assetManager = new AssetManager();

    loadAsset(Resources.TEXTURES, Texture.class, ".png");
    loadAsset(Resources.SPINES, TextureAtlas.class, ".atlas");

    assetManager.finishLoading();
  }

  public <T> T getAsset(String name, Class<T> assetType) {
    return getMappedAsset(name, assetType);
  }

  public <T> List<T> getAssets(String prefix, Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    Array<String> assetNames = assetManager.getAssetNames();
    for(String assetName : assetNames) {
      if(assetName.startsWith(prefix)) {
        result.add(getMappedAsset(assetName, clazz));
      }
    }
    return result;
  }

  public void dispose() {
    assetManager.dispose();
  }

  //--------------- Helper --------------------------------------------------

  public <T> T getMappedAsset(String name, Class<T> assetType) {
    String fullPath = assetPathMapper.get(name);
    return assetManager.get(fullPath, assetType);
  }

  /**
   * Recursive loading of all assets in the given directory
   * @param dir the base directory to start with
   * @param assetClass the type of asset to load
   * @param suffix the filename suffix.
   */
  private void loadAsset(String dir, Class assetClass, String suffix) {
    FileHandle internal = Gdx.files.internal(dir);
    FileHandle[] assetFiles = internal.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(suffix);
      }
    });

    for(FileHandle file : assetFiles) {
      assetManager.load(file.path(), assetClass);

      String simpleName = file.name().substring(0, file.name().lastIndexOf("."));
      assetPathMapper.put(simpleName, file.path());
      Gdx.app.log(this.getClass().getName(), "Loaded asset " + file.path());
    }

    FileHandle[] subDirs = internal.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return new File(dir, name).isDirectory();
      }
    });

    for(FileHandle subDir : subDirs) {
      loadAsset(subDir.path(), assetClass, suffix);
    }
  }
}
