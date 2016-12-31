package com.nima.render;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nima.util.PolygonUtil;
import com.nima.util.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The cached map with additional information to optimize caching
 */
public class CachedTiledMap {
  private static final Logger LOG = Logger.getLogger(CachedTiledMap.class.getName());

  private boolean rendered;
  private TiledMap map;

  private int frameNumberX;
  private int frameNumberY;

  private String filename;
  private World world;

  private List<MapObject> mapObjects = new ArrayList<>();

  protected CachedTiledMap(TmxCacheMapLoader loader, World world) {
    this.world = world;
    this.map = loader.getMap();
    this.filename = loader.getFilename();
    this.frameNumberX = loader.getFrameX();
    this.frameNumberY = loader.getFrameY();
    renderObjects();
  }

  public List<MapObject> getMapObjects() {
    return mapObjects;
  }

  private void renderObjects() {
    if(!rendered) {
      rendered = true;

      float xOffset = frameNumberX * Settings.FRAME_PIXELS_X;
      float yOffset = frameNumberY * Settings.FRAME_PIXELS_Y;

      for(MapLayer mapLayer : map.getLayers()) {
        MapObjects objects = mapLayer.getObjects();
        for(MapObject object : objects) {
          if (object instanceof TextureMapObject){
            continue;
          }

          renderObject(object, xOffset, yOffset);
          mapObjects.add(object);
        }
      }

      LOG.info("Rendered objects of frame " + frameNumberX + ","  + frameNumberY);
    }
  }

  /**
   * Update the position of the objects, depending
   * on the active frame.
   */
  private void renderObject(MapObject object, float xOffset, float yOffset) {
      LevelGeometry geometry = null;
      Shape shape = null;
      BodyDef bodyDef = new BodyDef();
      bodyDef.awake = false;
      bodyDef.type = BodyDef.BodyType.StaticBody;

      if (object instanceof RectangleMapObject) {
        RectangleMapObject mapObject = (RectangleMapObject) object;
        Rectangle r = mapObject.getRectangle();
        r.setPosition(xOffset + r.getX(), yOffset + r.getY());

        //add position
        Vector2 position = r.getPosition(new Vector2());
        object.getProperties().put(MapConstants.PROPERTY_POSITION, position);

        //add centered position
        Vector2 centeredPosition = new Vector2(position.x+r.width/2, position.y+r.height/2);
        object.getProperties().put(MapConstants.PROPERTY_CENTERED_POSITION, centeredPosition);

        //collision component
        geometry = getRectangle((RectangleMapObject)object);
        shape = geometry.getShape();
      }
      else if (object instanceof PolygonMapObject) {
        PolygonMapObject mapObject = (PolygonMapObject) object;
        Polygon polygon = mapObject.getPolygon();
        polygon.setPosition(xOffset + polygon.getX(), yOffset + polygon.getY());

        //add position
        Vector2 position = new Vector2(polygon.getX(), polygon.getY());
        object.getProperties().put(MapConstants.PROPERTY_POSITION, position);

        //add centered position
        Vector2 centeredPosition = PolygonUtil.getCenter(polygon);
        object.getProperties().put(MapConstants.PROPERTY_CENTERED_POSITION, centeredPosition);

        //collision component
        geometry = getPolygon((PolygonMapObject)object);
        shape = geometry.getShape();
      }
      else if (object instanceof PolylineMapObject) {
//        PolylineMapObject mapObject = (PolylineMapObject) object;
//        Polygon polygon = mapObject.getPolygon();
//        polygon.setPosition(xOffset + polygon.getX(), yOffset + polygon.getY());
//
//        //add position
//        Vector2 position = new Vector2(polygon.getX(), polygon.getY());
//        object.getProperties().put(MapConstants.PROPERTY_POSITION, position);
//
//        //add centered position
//        Vector2 centeredPosition = PolygonUtil.getCenter(polygon);
//        object.getProperties().put(MapConstants.PROPERTY_CENTERED_POSITION, centeredPosition);


        //collision component
        geometry = getPolyline((PolylineMapObject)object);
        shape = geometry.getShape();
      }
      else if (object instanceof EllipseMapObject) {
        EllipseMapObject mapObject = (EllipseMapObject) object;
        Ellipse circle = mapObject.getEllipse();
        circle.setPosition(xOffset + circle.x, yOffset + circle.y);

        //add position
        Vector2 position = new Vector2(circle.x, circle.y);
        object.getProperties().put(MapConstants.PROPERTY_POSITION, position);

        //add centered position
        Vector2 centeredPosition = new Vector2(circle.x + circle.width / 2, circle.y + circle.height / 2);
        object.getProperties().put(MapConstants.PROPERTY_CENTERED_POSITION, centeredPosition);

        //collision component
        geometry = getCircle((EllipseMapObject)object);
        shape = geometry.getShape();
      }
      else {
        LOG.warning("Unrecognized shape " + object.toString());
      }

      if(shape != null) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
//      fixtureDef.filter.categoryBits = Settings.LEVEL_BITS;
//      fixtureDef.filter.maskBits = (short) (Settings.FRIENDLY_BITS |
//          Settings.ENEMY_BITS |
//          Settings.NEUTRAL_BITS |
//          Settings.FOOT_SENSOR |
//          Settings.RIGHT_WALL_SENSOR |
//          Settings.LEFT_WALL_SENSOR);

//        bodyDef.position.x = x;
//        bodyDef.position.y = y;
        Body body = world.createBody(bodyDef);
        body.setActive(false);
        body.createFixture(fixtureDef);
        fixtureDef.shape = null;
        shape.dispose();
        object.getProperties().put(MapConstants.PROPERTY_COLLISION_COMPONENT, body);
      }
  }



  private LevelGeometry getRectangle(RectangleMapObject rectangleObject) {
    Rectangle rectangle = rectangleObject.getRectangle();
    PolygonShape polygon = new PolygonShape();
    Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * Settings.MPP,
        (rectangle.y + rectangle.height * 0.5f ) * Settings.MPP);

    polygon.setAsBox(rectangle.width * 0.5f * Settings.MPP, rectangle.height * 0.5f * Settings.MPP,
        size, 0.0f);
    return new LevelGeometry(polygon);
  }

  private LevelGeometry getCircle(EllipseMapObject circleObject) {
    Ellipse ellipse = circleObject.getEllipse();
    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(ellipse.width/2 * Settings.MPP);
    circleShape.setPosition(new Vector2((ellipse.x+ellipse.width/2) * Settings.MPP,
        (ellipse.y+ellipse.height/2) * Settings.MPP));
    return new LevelGeometry(circleShape);
  }

  private LevelGeometry getPolygon(PolygonMapObject polygonObject) {
    PolygonShape polygon = new PolygonShape();
    float[] vertices = polygonObject.getPolygon().getTransformedVertices();
    float[] worldVertices = new float[vertices.length];
    for (int i = 0; i < vertices.length; ++i) {
      worldVertices[i] = vertices[i] * Settings.MPP;
    }

    polygon.set(worldVertices);
    return new LevelGeometry(polygon);
  }

  private LevelGeometry getPolyline(PolylineMapObject polylineObject) {
    float[] vertices = polylineObject.getPolyline().getTransformedVertices();
    Vector2[] worldVertices = new Vector2[vertices.length / 2];

    for (int i = 0; i < vertices.length / 2; ++i) {
      worldVertices[i] = new Vector2();
      worldVertices[i].x = vertices[i * 2] * Settings.MPP;
      worldVertices[i].y = vertices[i * 2 + 1] * Settings.MPP;
    }

    ChainShape chain = new ChainShape();
    chain.createChain(worldVertices);
    return new LevelGeometry(chain);
  }

  public TiledMap getMap() {
    return map;
  }

  public String getFilename() {
    return filename;
  }

  @Override
  public boolean equals(Object obj) {
    return getFilename().equals(((CachedTiledMap)obj).getFilename());
  }

  public void destroy() {
    int count = 0;
    for(MapObject mapObject : mapObjects) {
      Body body = (Body) mapObject.getProperties().get(MapConstants.PROPERTY_COLLISION_COMPONENT);
      if(body != null) {
        world.destroyBody(body);
        count++;
      }
    }
    LOG.info("Destroyed " + count + " bodies of " + filename);
  }
}
