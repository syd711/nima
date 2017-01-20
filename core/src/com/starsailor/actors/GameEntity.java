package com.starsailor.actors;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * Improved Ashley entity.
 */
public class GameEntity extends Entity {

  public GameEntity() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Component> T getComponent (Class<T> component) {
    ImmutableArray<Component> components = this.getComponents();
    T found = null;
    for(int i = 0, n = components.size(); i < n; i++) {
      Component comp = this.getComponents().get(i);
      if(ClassReflection.isAssignableFrom(component, comp.getClass())) {
        if(found == null) {
          found = (T) comp;
        }
        else
          throw new IllegalArgumentException("Cannot get a " + ClassReflection.getSimpleName(component.getClass())
              + " component because entity has a subclass component: " + ClassReflection.getSimpleName(comp.getClass()));
      }
    }
    if(found != null) {
      return found;
    }

    return super.getComponent(component);
  }
}
