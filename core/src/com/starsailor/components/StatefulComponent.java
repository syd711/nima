package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.utils.Pool;

/**
 * Interface for entities controlled by state machines.
 */
public class StatefulComponent implements Component, Pool.Poolable {
  public StateMachine stateMachine;

  @Override
  public void reset() {
    stateMachine = null;
  }
}
