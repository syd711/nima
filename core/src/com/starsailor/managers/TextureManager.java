package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.starsailor.util.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class TextureManager extends ResourceManager {
  private static TextureManager instance = new TextureManager();

  public static TextureManager getInstance() {
    return instance;
  }

  private Map<String, Texture> textures = new HashMap<>();

  private TextureManager() {
  }

  public void loadTextures() {
    loadTexturesFor(Resources.TEXTURES);
    loadTexturesFor(Resources.WEAPON_TEXTURES);
  }

  private void loadTexturesFor(String path) {
    FileHandle internal = Gdx.files.internal(path);
    FileHandle[] particleFiles = internal.list((dir, name) -> name.endsWith(".png"));

    for(FileHandle file : particleFiles) {
      Texture texture = new Texture(file);
      String name = file.name().substring(0, file.name().lastIndexOf("."));
      textures.put(name, texture);

      Gdx.app.log(this.getClass().getName(), "Loaded texture " + file.file().getName());
    }
  }

  public Texture getTexture(String name) {
    return textures.get(name);
  }

  public List<Texture> getTextures(String texturePrefix) {
    List<Texture> result = new ArrayList<>();
    int index = 0;
    while(textures.containsKey(texturePrefix + "_" + index)) {
      result.add(textures.get(texturePrefix + "_" + index));
      index++;
    }
    return result;
  }
}
