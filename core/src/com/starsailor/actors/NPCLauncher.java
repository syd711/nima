package com.starsailor.actors;

import com.badlogic.gdx.Gdx;
import com.starsailor.actors.route.Route;
import com.starsailor.managers.EntityManager;
import com.starsailor.model.items.ShipItem;

import java.util.*;

/**
 * Adds game entities to the game
 */
public class NPCLauncher {

  public static void launch(List<NPC> items) {
    Map<String,FormationOwner> routeRabbits = new HashMap<>();

    for(NPC npc : items) {
      npc.switchToDefaultState();
      EntityManager.getInstance().add(npc);
      Gdx.app.log(NPCLauncher.class.getName(), "Added '" + npc + "'");

      ShipItem shipItem = npc.getShipItem();
      String routeName = shipItem.getRoute();
      if(routeName != null && shipItem.isFollowRouteRabbit()) {
        List<Route> entities = EntityManager.getInstance().getEntities(Route.class);
        for(Route route : entities) {
          if(route.getName().equals(routeName) && !routeRabbits.containsKey(routeName)) {
            FormationOwner formationOwner = new FormationOwner(route);
            routeRabbits.put(routeName, formationOwner);
          }
        }
      }
    }

    Collection<FormationOwner> values = routeRabbits.values();
    for(FormationOwner value : values) {
      EntityManager.getInstance().add(value);
    }
  }

  private static void launchRouteRabbit(Route route) {

  }
}
