package com.starsailor.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.util.Resources;

import java.util.*;

/**
 *
 */
public class ParticleManager {

  private static ParticleManager instance = new ParticleManager();
  private Map<Particle, ParticleEffect> effects = new HashMap<>();

  public static ParticleManager getInstance() {
    return instance;
  }

  private List<ParticleEffect> activeEffects = new ArrayList<>();

  private ParticleManager() {
  }

  public void loadParticles() {
    FileHandle internal = Gdx.files.internal(Resources.PARTICLES);
    FileHandle[] particleFiles = internal.list((dir, name) -> name.endsWith(".p"));

    for(FileHandle particleFile : particleFiles) {
      ParticleEffect pe = new ParticleEffect();
      pe.load(particleFile, Gdx.files.internal(""));
      String particleName = particleFile.name().toUpperCase().substring(0, particleFile.name().lastIndexOf("."));
      effects.put(Particle.valueOf(particleName), pe);
      Gdx.app.log(this.getClass().getName(), "Loaded particle " + particleFile.file().getName());
    }

  }

  public void queueEffect(Particle particle, Vector2 position) {
    ParticleEffect pe = effects.get(particle);
    pe.getEmitters().first().setPosition(position.x, position.y);
    pe.start();
    activeEffects.add(pe);
  }

  public void render(Batch batch, float deltaTime) {
    Iterator<ParticleEffect> iterator = activeEffects.iterator();
    while(iterator.hasNext()) {
      ParticleEffect pe = iterator.next();
      if(pe != null) {
        if(pe.isComplete()) {
          iterator.remove();
        }
        pe.draw(batch, deltaTime);
      }
    }
  }
}
