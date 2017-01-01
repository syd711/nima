package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.Game;
import com.nima.actors.Spine;
import com.nima.render.MapConstants;
import com.nima.util.SpineUtil;

/**
 * Box2d support for entities
 */
public class BodyComponent implements Component {

  public Body body;

  public BodyComponent(MapObject object) {
    body = (Body) object.getProperties().get(MapConstants.PROPERTY_COLLISION_COMPONENT);
  }

  public BodyComponent(Spine spine) {
    body = SpineUtil.createSpineBody(Game.world, spine);
  }
}
