package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.util.Resources;

import java.util.*;

/**
 *
 */
public class ParticleManager {

  private static ParticleManager instance = new ParticleManager();
  private Map<Particle, ParticleEffectPool> effects = new HashMap<>();

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
      String particleName = particleFile.name().toUpperCase().substring(0, particleFile.name().lastIndexOf("."));
      Gdx.app.log(this.getClass().getName(), "Loaded particle " + particleFile.file().getName());

      ParticleEffectPool pool = new ParticleEffectPool(pe, 0, 5);
      effects.put(Particle.valueOf(particleName), pool);
    }

  }

  public void queueEffect(Particle particle, Vector2 position) {
    ParticleEffectPool pool = effects.get(particle);
    ParticleEffectPool.PooledEffect pe = pool.obtain();
    pe.setPosition(position.x, position.y);

    EffectKey effect = new EffectKey();
    effect.effect = pe;
    effect.particle = particle;
    activeEffects.add(effect);
  }

  public void render(Batch batch, float deltaTime) {
    Iterator<EffectKey> iterator = activeEffects.iterator();
    while(iterator.hasNext()) {
      EffectKey key = iterator.next();
      ParticleEffectPool.PooledEffect pe = key.effect;
      if(pe != null) {
        if(pe.isComplete()) {
          iterator.remove();

          ParticleEffectPool pool = effects.get(key.particle);
          pool.free(pe);
        }
        pe.draw(batch, deltaTime);
      }
    }
  }

  class EffectKey {
    Particle particle;
    ParticleEffectPool.PooledEffect effect;
  }
}
