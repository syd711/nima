package com.starsailor.actors;

import com.badlogic.gdx.Gdx;
import com.starsailor.managers.EntityManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Adds game entities to the game
 */
public class NPCLauncher {

  public static void launch(List<NPC> items) {
    List<NPC> singletonList = filterNPCs(items, false);
    launchUnroutedNPCs(singletonList);

    List<NPC> memberList = filterNPCs(items, true);
    for(NPC npc : memberList) {
      FormationOwner formationOwner = npc.getRoute().getOrCreateFormationMember(npc.getShipItem().getRouteIndex());
      formationOwner.addMember(npc);
      EntityManager.getInstance().add(npc);
    }
  }

  private static List<NPC> filterNPCs(List<NPC> items, boolean followRabbit) {
    List<NPC> result = new ArrayList<>(items);
    Iterator<NPC> iterator = result.iterator();
    while(iterator.hasNext()) {
      NPC next = iterator.next();
      if(next.getShipItem().getRouteIndex() == 0 && followRabbit) {
        iterator.remove();
      }
      else if(next.getShipItem().getRouteIndex() > 0 && !followRabbit) {
        iterator.remove();
      }
    }
    return result;
  }

  private static void launchUnroutedNPCs(List<NPC> npcs) {
    for(NPC npc : npcs) {
      EntityManager.getInstance().add(npc);
      Gdx.app.log(NPCLauncher.class.getName(), "Added '" + npc + "'");
    }
  }
}
