package com.starsailor.actors.states.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.actors.bullets.Bullet;
import com.starsailor.data.ShipProfile;

/**
 *
 */
public class AttackedState implements State<NPC> {
  private Bullet bullet;

  public AttackedState(Bullet bullet) {
    this.bullet = bullet;
  }

  @Override
  public void enter(NPC npc) {
    Gdx.app.log(getClass().getName(), npc + " entered AttackedState");
    npc.setStateVisible(true);

    //attack is finished, return back to previous state
    if(bullet.isFriendlyFire()) {
      npc.switchToDefaultState();
      return;
    }

    ShipProfile.Types type = npc.shipProfile.getType();
    switch(type) {
      case MERCHANT: {
        npc.getStateMachine().changeState(new FleeFromAttackerState(bullet));
        break;
      }
      default: {
        npc.getStateMachine().changeState(new AttackState(bullet.owner));
        break;
      }
    }
  }

  @Override
  public void update(NPC npc) {
  }

  @Override
  public void exit(NPC npc) {
  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
