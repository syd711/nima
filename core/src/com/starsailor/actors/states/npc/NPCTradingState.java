package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Player;
import com.starsailor.managers.SteeringManager;

/**
 *
 */
public class NPCTradingState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered " + this.getClass().getSimpleName());
    SteeringManager.setFollowTargetSteering(npc.steerableComponent, Player.getInstance().steerableComponent);
  }

  @Override
  public void update(NPC npc) {
    float dst = Player.getInstance().positionComponent.getPosition().dst(npc.positionComponent.getPosition());
    if(dst < npc.getWidth()*2) {
      npc.steerableComponent.setBehavior(null);
    }
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
