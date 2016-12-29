package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.nima.Game;
import com.nima.actors.Spine;
import com.nima.util.Settings;

/**
 * Box2d support for entities
 */
public class BodyComponent implements Component {

  public Body body;

  public BodyComponent(MapObject mapObject) {

  }

  public BodyComponent(Spine spine, float x, float y) {
    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
    def.fixedRotation = false;
    def.position.set(x, y);
    body = Game.world.createBody(def);

    PolygonShape shape = new PolygonShape();
    //calculated from center!
    shape.setAsBox(spine.skeleton.getData().getWidth() * 0.2f / 2 / Settings.PPM, spine.skeleton.getData().getHeight() * 0.2f / 2 / Settings.PPM);
    body.createFixture(shape, 0f);
    shape.dispose();
  }
}
