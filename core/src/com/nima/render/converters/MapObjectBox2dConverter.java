package com.nima.render.converters;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nima.render.MapObjectConverter;
import com.nima.render.TiledMapFragment;
import com.nima.util.Settings;

import static com.nima.render.converters.MapConstants.PROPERTY_OBJECT_TYPE;

/**
 * Creates Box2d bodies from map objects depending on the map object type.
 */
public class MapObjectBox2dConverter extends MapObjectConverter {

  private final World world;

  public MapObjectBox2dConverter(World world) {
    this.world = world;
  }

  @Override
  public void init(TiledMapFragment map) {
  }

  @Override
  public boolean isApplicable(TiledMapFragment mapFragment, MapObject mapObject) {
    String type = (String) mapObject.getProperties().get(PROPERTY_OBJECT_TYPE);
    return type != null && (type.equalsIgnoreCase(MapConstants.TYPE_PLANET) || type.equalsIgnoreCase(MapConstants.TYPE_STATION));
  }

  @Override
  public void convertRectangle(TiledMapFragment mapFragment, RectangleMapObject mapObject) {
    createBody(mapObject, getRectangle(mapObject));
  }

  @Override
  public void convertPolygon(TiledMapFragment mapFragment, PolygonMapObject mapObject) {
    createBody(mapObject, getPolygon(mapObject));
  }

  @Override
  public void convertPolyline(TiledMapFragment mapFragment, PolylineMapObject mapObject) {
    createBody(mapObject, getPolyline(mapObject));
  }

  @Override
  public void convertEllipse(TiledMapFragment mapFragment, EllipseMapObject mapObject) {
    createBody(mapObject, getCircle(mapObject));
  }

  @Override
  public void destroy(TiledMapFragment mapFragment, MapObject mapObject) {
    Object body = mapObject.getProperties().get(MapConstants.PROPERTY_BOX2D_BODY);
    if(body != null) {
      world.destroyBody((Body) body);
    }
  }

  //------------------- Helper ---------------------------------

  private void createBody(MapObject mapObject, Shape shape) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.awake = false;
    bodyDef.type = BodyDef.BodyType.StaticBody;

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;

    Body body = world.createBody(bodyDef);
    body.setActive(true);
    body.createFixture(fixtureDef);
    fixtureDef.shape = null;
    fixtureDef.isSensor = true;
    shape.dispose();

    mapObject.getProperties().put(MapConstants.PROPERTY_BOX2D_BODY, body);
  }

  // -------------------- Box2d Conversion ----------------------------------

  private Shape getRectangle(RectangleMapObject rectangleObject) {
    Rectangle rectangle = rectangleObject.getRectangle();
    PolygonShape polygon = new PolygonShape();
    Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * Settings.MPP,
        (rectangle.y + rectangle.height * 0.5f) * Settings.MPP);

    polygon.setAsBox(rectangle.width * 0.5f * Settings.MPP, rectangle.height * 0.5f * Settings.MPP,
        size, 0.0f);
    return polygon;
  }

  private Shape getCircle(EllipseMapObject circleObject) {
    Ellipse ellipse = circleObject.getEllipse();
    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(ellipse.width / 2 * Settings.MPP);
    circleShape.setPosition(new Vector2((ellipse.x + ellipse.width / 2) * Settings.MPP,
        (ellipse.y + ellipse.height / 2) * Settings.MPP));
    return circleShape;
  }

  private Shape getPolygon(PolygonMapObject polygonObject) {
    PolygonShape polygon = new PolygonShape();
    float[] vertices = polygonObject.getPolygon().getTransformedVertices();
    float[] worldVertices = new float[vertices.length];
    for(int i = 0; i < vertices.length; ++i) {
      worldVertices[i] = vertices[i] * Settings.MPP;
    }

    polygon.set(worldVertices);
    return polygon;
  }

  private Shape getPolyline(PolylineMapObject polylineObject) {
    float[] vertices = polylineObject.getPolyline().getTransformedVertices();
    Vector2[] worldVertices = new Vector2[vertices.length / 2];

    for(int i = 0; i < vertices.length / 2; ++i) {
      worldVertices[i] = new Vector2();
      worldVertices[i].x = vertices[i * 2] * Settings.MPP;
      worldVertices[i].y = vertices[i * 2 + 1] * Settings.MPP;
    }

    ChainShape chain = new ChainShape();
    chain.createChain(worldVertices);
    return chain;
  }
}
