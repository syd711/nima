package com.starsailor.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.starsailor.Game;
import com.starsailor.actors.Selectable;
import com.starsailor.components.SelectionComponent;
import com.starsailor.util.GraphicsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * All selection based events and management
 */
public class SelectionManager implements EntityListener {
  private static SelectionManager INSTANCE = new SelectionManager();

  public List<SelectionChangeListener> selectionChangeListeners = new ArrayList<>();
  private Selectable currentSelection;

  public static SelectionManager getInstance() {
    return INSTANCE;
  }

  public void addSelectionChangeListener(SelectionChangeListener listener) {
    this.selectionChangeListeners.add(listener);
  }

  public Selectable getSelection() {
    return currentSelection;
  }

  public void setSelection(Selectable selection) {
    updateSelection(currentSelection, selection, true);
  }

  /**
   * Checks if there is a valid selection at the given coordinates.
   *
   * @param singleSelection true if this is a multi-selection
   * @return true if a selection was found
   */
  protected boolean selectAt(float targetX, float targetY, boolean singleSelection) {
    Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(Game.camera, targetX, targetY);
    Entity clickTarget = EntityManager.getInstance().getEntityAt(worldCoordinates.x, worldCoordinates.y);
    if(clickTarget instanceof Selectable) {
      updateSelection(currentSelection, (Selectable) clickTarget, singleSelection);
      return true;
    }

    return false;
  }

  /**
   * Selection update with listener notification
   */
  private void updateSelection(Selectable oldSelection, Selectable newSelection, boolean singleSelection) {
    if(singleSelection) {
      ImmutableArray<Entity> entitiesFor = EntityManager.getInstance().getEntitiesFor(SelectionComponent.class);
      for(Entity entity : entitiesFor) {
        Selectable selectable = (Selectable) entity;
        if(selectable.equals(newSelection)) {
          selectable.setSelected(!selectable.isSelected());
        }
        else {
          selectable.setSelected(false);
        }
      }
    }

    this.currentSelection = newSelection;

    for(SelectionChangeListener selectionChangeListener : selectionChangeListeners) {
      selectionChangeListener.selectionChanged(oldSelection, newSelection);
    }
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    if(currentSelection != null && currentSelection.equals(entity)) {
      setSelection(null);
    }
  }
}
