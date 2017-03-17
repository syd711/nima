package com.starsailor.managers;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.starsailor.actors.Selectable;
import com.starsailor.ui.stages.hud.HudStage;
import com.starsailor.ui.states.UIState;
import com.starsailor.ui.states.UIStates;

/**
 * Handling scene2d stages
 */
public class UIManager implements SelectionChangeListener {
  private static UIManager instance;
  private DefaultStateMachine stateMachine = new StackStateMachine<>();

  private HudStage hudStage;
  private Stage activeStage;

  private UIManager() {
    SelectionManager.getInstance().addSelectionChangeListener(this);
  }

  public static UIManager getInstance() {
    if(instance == null) {
      instance = new UIManager();
      instance.hudStage = new HudStage();

      instance.stateMachine.changeState(UIStates.DEFAULT_STATE);
      instance.activeStage = instance.hudStage;
    }
    return instance;
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

  public void changeState(UIState state) {
    stateMachine.changeState(state);
  }

  public void switchToDefaultState(){
    stateMachine.changeState(UIStates.DEFAULT_STATE);
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
}