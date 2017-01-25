package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.managers.Particles;

/**
 *
 */
public class ParticleComponent implements Component, Pool.Poolable{

  public ParticleEffectPool.PooledEffect effect;
  public Particles particle;

  public boolean enabled;

  @Override
  public void reset() {
    this.effect = null;
  }
}
