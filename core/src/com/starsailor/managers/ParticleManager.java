package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.components.PositionComponent;
import com.starsailor.util.Resources;

import java.util.*;

/**
 *
 */
public class ParticleManager extends ResourceManager {
  private static ParticleManager instance = new ParticleManager();
  private Map<Particles, ParticleEffectPool> effects = new HashMap<>();

  public static ParticleManager getInstance() {
    return instance;
  }

  private List<EffectKey> activeEffects = new ArrayList<>();

  private ParticleManager() {
  }

  public void loadParticles() {
    FileHandle internal = Gdx.files.internal(Resources.PARTICLES);
    FileHandle[] particleFiles = internal.list((dir, name) -> name.endsWith(".p"));

    for(FileHandle particleFile : particleFiles) {
      ParticleEffect pe = new ParticleEffect();
      pe.load(particleFile, Gdx.files.internal(""));

      Gdx.app.log(this.getClass().getName(), "Loaded particle " + particleFile.file().getName());

      String particleName = getEnumName(particleFile);
      ParticleEffectPool pool = new ParticleEffectPool(pe, 0, 5);
      effects.put(Particles.valueOf(particleName), pool);
    }
  }

  public void playEffect(Particles particle, PositionComponent positionComponent, float scaling) {
    playEffect(particle, null, positionComponent, scaling);
  }

  public void playEffect(Particles particle, Vector2 position, float scaling) {
    playEffect(particle, position, null, scaling);
  }

  public void playEffect(Particles particle, Vector2 position) {
    playEffect(particle, position, null, -1);
  }


  /**
   * Actual implementation of enqueing a particle effect for rendering.
   *
   * @param particle
   * @param position
   * @param positionComponent
   * @param scaling
   */
  private void playEffect(Particles particle, Vector2 position, PositionComponent positionComponent, float scaling) {
    ParticleEffectPool pool = effects.get(particle);
    ParticleEffectPool.PooledEffect pe = pool.obtain();
    if(position != null) {
      pe.setPosition(position.x, position.y);
    }
    else {
      pe.setPosition(positionComponent.x, positionComponent.y);
    }

    if(scaling > 0) {
      pe.scaleEffect(scaling);
    }
    else {
      pe.scaleEffect(1f);
    }

    EffectKey effect = new EffectKey();
    effect.effect = pe;
    effect.particle = particle;
    effect.positionComponent = positionComponent;
    activeEffects.add(effect);
  }

  public void render(Batch batch, float deltaTime) {
    Iterator<EffectKey> iterator = activeEffects.iterator();
    while(iterator.hasNext()) {
      EffectKey key = iterator.next();
      ParticleEffectPool.PooledEffect pe = key.effect;
      if(key.positionComponent != null) {
        pe.setPosition(key.positionComponent.x, key.positionComponent.y);
      }

      if(pe.isComplete() && key.positionComponent == null) {
        iterator.remove();

        ParticleEffectPool pool = effects.get(key.particle);
        pool.free(pe);
      }

      pe.draw(batch, deltaTime);
    }
  }

  class EffectKey {
    Particles particle;
    ParticleEffectPool.PooledEffect effect;
    PositionComponent positionComponent;
  }
}
