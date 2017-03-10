package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.starsailor.util.Resources;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads all particle effects
 */
public class ParticleManager {
  private static final int POOL_SIZE = 5;
  private static ParticleManager instance = new ParticleManager();

  private Map<String, ParticleEffectPool> effects = new HashMap<>();

  public static ParticleManager getInstance() {
    return instance;
  }

  private ParticleManager() {
  }

  //load when game is created
  public void loadParticles() {
    FileHandle internal = Gdx.files.internal(Resources.PARTICLES);
    FileHandle[] particleFiles = internal.list(new FilenameFilter() {

      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".p");
      }
    });

    for(FileHandle particleFile : particleFiles) {
      ParticleEffect pe = new ParticleEffect();
      pe.load(particleFile, Gdx.files.internal(""));
      pe.scaleEffect(0.3f);

      Gdx.app.log(this.getClass().getName(), "Loaded particle " + particleFile.file().getName());

      String particleName = particleFile.name().substring(0, particleFile.name().lastIndexOf("."));
      ParticleEffectPool pool = new ParticleEffectPool(pe, 0, POOL_SIZE);
      effects.put(particleName, pool);
    }
  }

  /**
   * Returns the effect for the given particle enum
   */
  public ParticleEffectPool.PooledEffect getEffect(String particle) {
    ParticleEffectPool pool = effects.get(particle);
    if(pool == null) {
      throw new UnsupportedOperationException("No particle found for " + particle);
    }
    return pool.obtain();
  }

  public void release(String particle, ParticleEffectPool.PooledEffect effect) {
    ParticleEffectPool pool = effects.get(particle);
    pool.free(effect);
  }
}
