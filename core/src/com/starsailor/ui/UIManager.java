package com.starsailor.ui;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.actors.Selectable;
import com.starsailor.managers.SelectionChangeListener;
import com.starsailor.managers.SelectionManager;
import com.starsailor.ui.stages.hud.HudStage;
import com.starsailor.ui.stages.location.LocationStage;
import com.starsailor.ui.states.UIState;
import com.starsailor.ui.states.UIStates;

/**
 * Handling scene2d stages
 */
public class UIManager implements SelectionChangeListener {
  private static UIManager instance = new UIManager();
  private DefaultStateMachine stateMachine = new StackStateMachine<>();

  private HudStage hudStage;
  private LocationStage locationStage;
  private Stage activeStage;

  private UIManager() {
    SelectionManager.getInstance().addSelectionChangeListener(this);
  }

  public static UIManager getInstance() {
    return instance;
  }

  public void init() {
    hudStage = new HudStage();
    locationStage = new LocationStage();
    stateMachine.changeState(UIStates.DEFAULT_STATE);
    activeStage = instance.hudStage;
  }

  /**
   * Central update method for all states and stages.
   */
  public void update(float deltaTime) {
    stateMachine.update();
    if(activeStage != null) {
      activeStage.draw();
      activeStage.act(deltaTime);
    }
  }

  public HudStage getHudStage() {
    return hudStage;
  }

  public LocationStage getLocationStage() {
    return locationStage;
  }

  public void changeState(UIState state) {
    stateMachine.changeState(state);
  }

  public void switchToHudState(){
    activeStage = hudStage;
    stateMachine.changeState(UIStates.DEFAULT_STATE);
  }

  public void switchToLocationState() {
    activeStage = locationStage;
    stateMachine.changeState(UIStates.LOCATION_STATE);
  }

  @Override
  public void selectionChanged(Selectable oldSelection, Selectable newSelection) {
    if(newSelection != null) {
      stateMachine.changeState(UIStates.SHIP_SELECTION_STATE);
    }
    else {
      stateMachine.changeState(UIStates.DEFAULT_STATE);
    }
  }

  public boolean isInHudState() {
    return activeStage.equals(hudStage);
  }

  public void log(StringBuilder builder) {
    builder.append("= UI State ====================================================================================\n");
    builder.append("Active : " + activeStage + "\n");
    builder.append("State : " + stateMachine.getCurrentState() + "\n");
    builder.append("-----------------------------------------------------------------------------------------------\n");
  }
}
