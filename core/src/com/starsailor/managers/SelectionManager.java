package com.starsailor.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.NPC;
import com.starsailor.components.SelectionComponent;
import com.starsailor.util.GraphicsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * All selection based events and management
 */
public class SelectionManager {
  private static SelectionManager INSTANCE = new SelectionManager();

  public List<SelectionChangeListener> selectionChangeListeners = new ArrayList();
  private GameEntity lastSelection;
  private GameEntity currentSelection;


  public static SelectionManager getInstance() {
    return INSTANCE;
  }

  public void addSelectionChangeListener(SelectionChangeListener listener) {
    this.selectionChangeListeners.add(listener);
  }

  /**
   * Checks if there is a valid selection at the given coordinates.
   * @param singleSelection true if this is a multi-selection
   * @return true if a selection was found
   */
  public boolean selectAt(float targetX, float targetY, boolean singleSelection) {
    Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(Game.camera, targetX, targetY);
    GameEntity clickTarget = (GameEntity) EntityManager.getInstance().getEntityAt(worldCoordinates.x, worldCoordinates.y);
    if(clickTarget instanceof NPC) {
      currentSelection = clickTarget;
      if(singleSelection) {
        ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(SelectionComponent.class);
        for(Entity entity : entitiesFor) {
          if(!entity.equals(clickTarget))
            entity.getComponent(SelectionComponent.class).selected = false;
        }
      }

      boolean selection = ((NPC)currentSelection).toggleSelection();
      if(!selection) {
        currentSelection = null;
      }

      for(SelectionChangeListener selectionChangeListener : selectionChangeListeners) {
        selectionChangeListener.selectionChanged(lastSelection, currentSelection);
      }

      this.lastSelection = currentSelection;
    }

    return false;
  }

  public GameEntity getSelection() {
    return currentSelection;
  }
}
