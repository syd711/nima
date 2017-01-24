package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

  public void loadSprites() {
    FileHandle internal = Gdx.files.internal(Resources.TEXTURES);
    FileHandle[] particleFiles = internal.list((dir, name) -> name.endsWith(".png"));

    for(FileHandle file : particleFiles) {
      Texture texture = new Texture(file);
      String name = getEnumName(file);
      textures.put(Textures.valueOf(name), texture);

      Gdx.app.log(this.getClass().getName(), "Loaded texture " + file.file().getName());
    }
  }

  public Sprite createSprite(Textures sprite) {
    Texture texture = textures.get(sprite);
    return new Sprite(texture);
  }

  public Texture getTexture(Textures texture) {
    return textures.get(texture);
  }
}
