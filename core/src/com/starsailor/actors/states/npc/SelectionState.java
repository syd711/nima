package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.SpriteComponent;
import com.starsailor.managers.Sprites;

/**
 *
 */
public class SelectionState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    npc.selectionComponent.setSelected(true);
    SpriteComponent spriteComponent = ComponentFactory.addSpriteComponent(npc, Sprites.SELECTION, -1);
    spriteComponent.addSprite(Sprites.HEALTHBG, -1);
    spriteComponent.addSprite(Sprites.HEALTHFG, -1);
  }

  @Override
  public void update(NPC npc) {
    npc.getStateMachine().changeState(npc.getStateMachine().getPreviousState());
  }

  @Override
  public void exit(NPC npc) {

  }

  @Override
  public boolean onMessage(NPC npc, Telegram telegram) {
    return false;
  }
}
