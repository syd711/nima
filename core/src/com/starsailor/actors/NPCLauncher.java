package com.starsailor.actors;

import com.badlogic.gdx.Gdx;
import com.starsailor.managers.EntityManager;

import java.util.List;

/**
 * Adds game entities to the game
 */
public class NPCLauncher {

  public static void launch(List<NPC> items) {
    for(NPC npc : items) {
      npc.switchToDefaultState();
      EntityManager.getInstance().add(npc);
      Gdx.app.log(NPCLauncher.class.getName(), "Added '" + npc + "'");
    }
  }
}
