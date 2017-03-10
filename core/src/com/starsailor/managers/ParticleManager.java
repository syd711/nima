package com.starsailor.managers;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads all particle effects
 */
public class ParticleManager {
  private static final int POOL_SIZE = 15;
  private static ParticleManager instance = new ParticleManager();

  private Map<String, ParticleEffectPool> effects = new HashMap<>();

  public static ParticleManager getInstance() {
    return instance;
  }

  private ParticleManager() {
  }

  /**
   * Returns the effect for the given particle enum
   */
  public ParticleEffectPool.PooledEffect getEffect(String particle) {
    if(!effects.containsKey(particle)) {
      ParticleEffect pe = ResourceManager.getInstance().getParticleEffect(particle);
      ParticleEffectPool pool = new ParticleEffectPool(pe, POOL_SIZE, POOL_SIZE);
      effects.put(particle, pool);
    }

    ParticleEffectPool pool = effects.get(particle);
    return pool.obtain();
  }

  public void release(String particle, ParticleEffectPool.PooledEffect effect) {
    ParticleEffectPool pool = effects.get(particle);
    pool.free(effect);
  }
}
