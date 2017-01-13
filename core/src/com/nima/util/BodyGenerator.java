package com.nima.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.nima.Game;
import com.nima.data.RoutePoint;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*;
import static com.nima.util.Settings.MPP;


public class BodyGenerator {
  private static final String TYPE_ATTRIBUTE = "type";
  private static final String HEIGHT_ATTRIBUTE = "height";
  private static final String WIDTH_ATTRIBUTE = "width";
  private static final String X_ATTRIBUTE = "x";
  private static final String Y_ATTRIBUTE = "y";
  private static final String RADIUS_ATTRIBUTE = "radius";
  private static final String SENSOR_ATTRIBUTE = "isSensor";
  private static final String DENSITY_ATTRIBUTE = "density";
  private static final String BIT_SHIFTS_ATTRIBUTE = "bitShifts";
  private static final String FRICTION_ATTRIBUTE = "friction";
  private static final String BULLET_ATTRIBUTE = "bullet";
  private static final String FIXED_ROTATION_ATTRIBUTE = "fixedRotation";
  private static final String GRAVITY_SCALE_ATTRIBUTE = "gravityScale";

  private static final String BODY_DEF = "BodyDef";
  private static final String FIXTURES = "Fixtures";

  private static World world = Game.world;

  public static Body generateRoutePointBody(RoutePoint point) {
    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.DynamicBody;
    bdef.position.set(point.position.x * MPP, point.position.y * MPP);
    Body b = world.createBody(bdef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(10 * MPP, 10 * MPP);

    FixtureDef fdef = new FixtureDef();
    fdef.isSensor = true;
    fdef.density = 1;
    fdef.restitution = 0.9f;
    fdef.shape = shape;
    b.createFixture(fdef);
    shape.dispose();

    return b;
  }

  public static Body generateBody(Entity owner, Sprite image, FileHandle handle, short filterCategory) {
    return bodyHelper(owner, new Vector2(image.getX(), image.getY()), new Vector2(image.getWidth(), image.getHeight()), handle, filterCategory);
  }

  private static Body bodyHelper(Entity owner, Vector2 position, Vector2 dimensions, FileHandle handle, short filterCategory) {
    Body body;

    String rawJson = handle.readString();
    JsonReader jsonReader = new JsonReader();
    JsonValue root = jsonReader.parse(rawJson);

    short maskingBits = (short) ((Settings.FRIENDLY_BITS | Settings.ENEMY_BITS | Settings.NEUTRAL_BITS | Settings.LEVEL_BITS) ^ filterCategory);

    BodyDef bodyDef = new BodyDef();

    String bodyType = root.get(BODY_DEF).getString(TYPE_ATTRIBUTE);
    if(bodyType.equalsIgnoreCase(DynamicBody.toString())) {
      bodyDef.type = DynamicBody;
    }
    else if(bodyType.equalsIgnoreCase(KinematicBody.toString())) {
      bodyDef.type = KinematicBody;
    }
    else if(bodyType.equalsIgnoreCase(StaticBody.toString())) {
      bodyDef.type = StaticBody;
    }
    else {
      Gdx.app.log("WARNING", "Entity Box2D body type undefined - " + filterCategory);
    }

    JsonValue jsonBody = root.get(BODY_DEF);

    bodyDef.bullet = jsonBody.getBoolean(BULLET_ATTRIBUTE);
    bodyDef.fixedRotation = jsonBody.getBoolean(FIXED_ROTATION_ATTRIBUTE);
    bodyDef.gravityScale = jsonBody.getFloat(GRAVITY_SCALE_ATTRIBUTE);
    bodyDef.position.set((position.x + dimensions.x / 2) * MPP, (position.y + dimensions.y / 2) * MPP);

    body = world.createBody(bodyDef);

    JsonValue fixtures = root.get(FIXTURES);
    for(JsonValue fixture : fixtures) {
      String fixtureType = fixture.getString(TYPE_ATTRIBUTE);
      Shape shape;

      if(fixtureType.equalsIgnoreCase(PolygonShape.class.getSimpleName())) {
        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(fixture.getFloat(WIDTH_ATTRIBUTE) * MPP,
            fixture.getFloat(HEIGHT_ATTRIBUTE) * MPP,
            new Vector2((body.getLocalCenter().x + fixture.getFloat(X_ATTRIBUTE)) * MPP,
                (body.getLocalCenter().y + fixture.getFloat(Y_ATTRIBUTE)) * MPP), 0f);

      }
      else if(fixtureType.equalsIgnoreCase(CircleShape.class.getSimpleName())) {
        shape = new CircleShape();
        shape.setRadius(fixture.getFloat(RADIUS_ATTRIBUTE) * MPP);
        ((CircleShape) shape).setPosition(new Vector2((body.getLocalCenter().x + fixture.getFloat(X_ATTRIBUTE)) * MPP,
            (body.getLocalCenter().y + fixture.getFloat(Y_ATTRIBUTE)) * MPP));
      }
      else {
        Gdx.app.log("WARNING", "Generated body shape was invalid");
        continue;
      }

      boolean isSensor = fixture.getBoolean(SENSOR_ATTRIBUTE);

      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.shape = shape;
      fixtureDef.isSensor = isSensor;
      fixtureDef.density = fixture.getFloat(DENSITY_ATTRIBUTE);
      if(isSensor) {
        fixtureDef.filter.categoryBits = (short) (filterCategory << fixture.getShort(BIT_SHIFTS_ATTRIBUTE));
        fixtureDef.filter.maskBits = Settings.LEVEL_BITS;
      }
      else {
        fixtureDef.friction = fixture.getFloat(FRICTION_ATTRIBUTE);
        fixtureDef.filter.categoryBits = filterCategory;
        fixtureDef.filter.maskBits = maskingBits;
      }
      body.createFixture(fixtureDef).setUserData(owner);
      shape.dispose();
    }

    return body;
  }
}
