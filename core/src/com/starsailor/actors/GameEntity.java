package com.starsailor.actors;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.starsailor.components.GameEntityComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Improved Ashley entity.
 */
public class GameEntity extends Entity {
  public GameEntityComponent gameEntityComponent;

  private boolean markedForDestroy;

  public GameEntity() {
    gameEntityComponent = new GameEntityComponent();
    add(gameEntityComponent);
  }

  public <T> List<T> getComponents (Class<T> component) {
    List<T> result = new ArrayList<T>();
    ImmutableArray<Component> components = this.getComponents();
    T found = null;
    for(int i = 0, n = components.size(); i < n; i++) {
      Component comp = this.getComponents().get(i);
      if(ClassReflection.isAssignableFrom(component, comp.getClass())) {
        result.add((T) comp);
      }
    }

    return result;
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

  public boolean isMarkedForDestroy() {
    return markedForDestroy;
  }

  public void markForDestroy() {
    this.markedForDestroy = true;
  }
}
