package com.nima.actors;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.nima.systems.states.AttackState;
import com.nima.util.GraphicsUtil;

import static com.nima.util.Settings.MPP;

/**
 * Common superclass for all NPC.
 * We assume that they are instances of Spine.
 */
public class NPC extends Spine {

  public StateMachine<NPC, AttackState> attackStateMachine;

  public NPC(Player player, String path, String defaultAnimation, float jsonScaling, float x, float y) {
    super(path, defaultAnimation, jsonScaling, x, y);

    Vector2 screenCenter = GraphicsUtil.getScreenCenter(getHeight());
    positionComponent.x = screenCenter.x + 360;
    positionComponent.y = screenCenter.y + 60;

    bodyComponent.body.setTransform(positionComponent.x * MPP, positionComponent.y * MPP, 0);
    bodyComponent.body.setLinearDamping(4f);

    speedComponent.setIncreaseBy(0.2f);
    speedComponent.setDecreaseBy(0.2f);


    this.attackStateMachine = new DefaultStateMachine<>(this, AttackState.SLEEP);
  }

  @Override
  public void update() {
    super.update();
    this.attackStateMachine.update();
  }
}
