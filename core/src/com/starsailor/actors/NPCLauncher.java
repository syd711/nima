package com.starsailor.actors;

import com.badlogic.gdx.Gdx;
import com.starsailor.actors.route.Route;
import com.starsailor.managers.EntityManager;

import java.util.*;

/**
 * Adds game entities to the game
 */
public class NPCLauncher {

  public static void launch(List<NPC> items) {
    List<NPC> singletonList = filterNPCs(items, false);
    launchUnroutedNPCs(singletonList);

    List<NPC> memberList = filterNPCs(items, true);

    Map<String, FormationOwner> formationOwners = new HashMap<>();
    List<Route> entities = EntityManager.getInstance().getEntities(Route.class);
    for(Route route : entities) {
//      if(route.getName().equals(routeName) && !formationOwners.containsKey(routeName)) {
//        FormationOwner formationOwner = new FormationOwner(route);
//        formationOwner.put(routeName, formationOwner);
//      }
    }

    Collection<FormationOwner> values = formationOwners.values();
    for(FormationOwner value : values) {
      EntityManager.getInstance().add(value);
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
      npc.switchToDefaultState();
      EntityManager.getInstance().add(npc);
      Gdx.app.log(NPCLauncher.class.getName(), "Added '" + npc + "'");
    }
  }
}
