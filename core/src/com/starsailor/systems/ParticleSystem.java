package com.starsailor.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.starsailor.components.ParticleComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.managers.EntityManager;
import com.starsailor.managers.ParticleManager;
import com.starsailor.managers.Particles;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rendering system for particle effects
 */
public class ParticleSystem extends AbstractIteratingSystem implements EntityListener {

  private Batch batch;

  private Map<Particles, ParticleComponent> effects = new LinkedHashMap<>();

  public ParticleSystem(Batch spriteBatch) {
    super(Family.all(ParticleComponent.class).get());
    this.batch = spriteBatch;
    EntityManager.getInstance().addEntityListener(this);
  }

  @Override
  public void process(Entity entity, float deltaTime) {
    ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
    PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

    // We can't draw the particle effect immediately since the draw
    // call maybe is only executed once before the entity is destroyed.
    if(particleComponent.enabled) {
      ParticleEffectPool.PooledEffect pe = particleComponent.effect;
      pe.setPosition(positionComponent.x, positionComponent.y);
      effects.put(particleComponent.particle, particleComponent);
    }

    Iterator<Map.Entry<Particles, ParticleComponent>> iterator = effects.entrySet().iterator();
    while(iterator.hasNext()) {
      Map.Entry<Particles, ParticleComponent> next = iterator.next();
      ParticleComponent pComponent = next.getValue();
      ParticleEffectPool.PooledEffect effect = pComponent.effect;
      effect.draw(batch, deltaTime);

      //release particle if it has a limited play time
      if(effect.isComplete()) {
        iterator.remove();
        ParticleManager.getInstance().release(next.getKey(), effect);
      }
    }
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
    if(particleComponent != null && effects.containsValue(particleComponent)) {

      //remove continuous particle effects
      ParticleEffectPool.PooledEffect effect = particleComponent.effect;
      ParticleEmitter particleEmitter = effect.getEmitters().get(0);
      if(particleEmitter.isContinuous()) {
        Particles particle = particleComponent.particle;
        effects.remove(particle);
        ParticleManager.getInstance().release(particle, effect);
      }
    }
  }
}
