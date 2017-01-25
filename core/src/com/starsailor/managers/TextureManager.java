package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.starsailor.util.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TextureManager extends ResourceManager {
  private static TextureManager instance = new TextureManager();

  public static TextureManager getInstance() {
    return instance;
  }

  private Map<Textures, Texture> textures = new HashMap<>();

  private TextureManager() {
  }

  public void loadTextures() {
    FileHandle internal = Gdx.files.internal(Resources.TEXTURES);
    FileHandle[] particleFiles = internal.list((dir, name) -> name.endsWith(".png"));

    for(FileHandle file : particleFiles) {
      Texture texture = new Texture(file);
      String name = getEnumName(file);
      textures.put(Textures.valueOf(name), texture);

      Gdx.app.log(this.getClass().getName(), "Loaded texture " + file.file().getName());
    }
  }

  public Texture getTexture(Textures texture) {
    return textures.get(texture);
  }
}
