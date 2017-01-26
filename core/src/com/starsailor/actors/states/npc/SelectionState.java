package com.starsailor.actors.states.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.actors.NPC;
import com.starsailor.components.ComponentFactory;
import com.starsailor.components.SpriteComponent;
import com.starsailor.managers.Textures;

/**
 *
 */
public class SelectionState implements State<NPC> {
  @Override
  public void enter(NPC npc) {
    npc.selectionComponent.setSelected(true);
    SpriteComponent spriteComponent = ComponentFactory.addSpriteComponent(npc, Textures.SELECTION, -1);
    spriteComponent.addSprite(Textures.HEALTHBG);
    spriteComponent.addSprite(Textures.HEALTHFG);

    spriteComponent.addSprite(Textures.SHIELDBG);
    spriteComponent.addSprite(Textures.SHIELDFG);
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
