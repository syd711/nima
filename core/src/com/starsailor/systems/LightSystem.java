package com.starsailor.systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.starsailor.components.LightComponent;
import com.starsailor.components.PositionComponent;
import com.starsailor.util.Settings;

public class LightSystem extends EntitySystem {
  private ComponentMapper<LightComponent> lightsMap = ComponentMapper.getFor(LightComponent.class);
  private RayHandler rayHandler;
  private float brightness = Settings.getInstance().ambient_light_brightness;
  private float currentBrightness = Settings.getInstance().ambient_light_brightness;

  public LightSystem(RayHandler rayHandler) {
    this.rayHandler = rayHandler;
    this.rayHandler.setAmbientLight(brightness, brightness, brightness, brightness);
  }

  public void fadeOut(boolean fadeOut) {
    if(fadeOut) {
      brightness = 0f;
    }
    else {
      brightness = Settings.getInstance().ambient_light_brightness;
    }
  }

  @Override
  public void update(float deltaTime) {
    updateAmbientLight();
    Family family = Family.all(LightComponent.class).get();
    ImmutableArray<Entity> entitiesFor = getEngine().getEntitiesFor(family);
    for(Entity entity : entitiesFor) {
      process(entity);
    }
  }

  private void updateAmbientLight() {
    if(currentBrightness != brightness) {
      if(brightness < currentBrightness) {
        currentBrightness = currentBrightness - Settings.getInstance().fade_out_offset;
      }
      else {
        currentBrightness = currentBrightness + Settings.getInstance().fade_in_offset;
      }
      currentBrightness = (float) (Math.round(currentBrightness * 100.0) / 100.0);
      rayHandler.setAmbientLight(currentBrightness, currentBrightness, currentBrightness, currentBrightness);
    }
  }

  private void process(Entity entity) {
    LightComponent light = lightsMap.get(entity);
    if(brightness == 0) {
      light.setTargetValue(0f);
    }
    else {
      light.setMaxValue();
    }
    light.updateValue();
    if(light.isMoveable()) {
      PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
      light.setPosition(positionComponent.x, positionComponent.y);
    }
  }

  public boolean isOutFaded() {
    return currentBrightness == 0f;
  }
}