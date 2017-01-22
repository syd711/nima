package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class ParticleManager {

  private static ParticleManager instance = new ParticleManager();
  private ParticleEffect pe;
  private FileHandle internal;

  public static ParticleManager getInstance() {
    return instance;
  }

  private ParticleManager() {
  }

  public void queueEffect(String name, Vector2 position) {
    pe = new ParticleEffect();
    internal = Gdx.files.internal("particles/explosion.p");
    pe.load(internal, Gdx.files.internal(""));
    pe.getEmitters().first().setPosition(position.x, position.y);
    pe.start();
  }

  public void render(Batch batch, float deltaTime) {
    if(pe != null) {
      if (pe.isComplete()) {
        pe.dispose();
      }
      pe.draw(batch, deltaTime);
    }

  }
}
