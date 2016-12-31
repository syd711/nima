package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.nima.Game;
import com.nima.actors.Spine;
import com.nima.render.MapConstants;
import com.nima.util.PolygonUtil;

/**
 * Box2d support for entities
 */
public class BodyComponent implements Component {

  private Body body;
  private Spine spine;

  public BodyComponent(MapObject object) {
    body = (Body) object.getProperties().get(MapConstants.PROPERTY_COLLISION_COMPONENT);
  }

  public BodyComponent(Spine spine) {
    this.spine = spine;
  }

  public Body getBody() {
    if(body == null) {
      body = PolygonUtil.createSpineBody(Game.world, spine);
    }
    return body;
  }
}
