package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.*;
import com.nima.Main;

/**
 * Component responsible for handling the collision of entities
 */
public class CollisionComponent implements Component {
  private Polygon polygon;
  private MapObject mapObject;
  private SpineComponent spineComponent;
  private Circle circle;

  public CollisionComponent(SpineComponent spineComponent) {
    this.spineComponent = spineComponent;

    float w = spineComponent.skeleton.getData().getWidth() * spineComponent.getScaling();
    float h = spineComponent.skeleton.getData().getHeight() * spineComponent.getScaling();
    float x = spineComponent.skeleton.getX();
    float y = spineComponent.skeleton.getY();
    polygon = new Polygon(new float[]{0, 0, w, 0, w, h, 0, h});
    polygon.setPosition(x-w/2, y);

    Main.DEBUG_RENDERER.render(polygon);
  }

  /**
   * Constructor for tiled map objects.
   * The map objects have a fix position, so we can immediately calculate
   * the polygon for it.
   *
   * @param object the map object to create the polygon for
   */
  public CollisionComponent(MapObject object) {
    this.mapObject = object;

    if(mapObject instanceof RectangleMapObject) {
      RectangleMapObject rectangle = (RectangleMapObject) mapObject;
      float w = rectangle.getRectangle().width;
      float h = rectangle.getRectangle().height;
      float x = rectangle.getRectangle().x;
      float y = rectangle.getRectangle().y;
      polygon = new Polygon(new float[]{0, 0, w, 0, w, h, 0, h});
      polygon.setPosition(x, y);
      Main.DEBUG_RENDERER.render(rectangle.getRectangle());
    }
    else if(mapObject instanceof PolygonMapObject) {
      PolygonMapObject p = (PolygonMapObject) mapObject;
      polygon = p.getPolygon();
    }
    else if(mapObject instanceof EllipseMapObject) {
      EllipseMapObject e = (EllipseMapObject) mapObject;
      Ellipse ellipse = e.getEllipse();
      float radius = ellipse.width/2;
      circle = new Circle(ellipse.x+radius, ellipse.y+radius, radius);
      Main.DEBUG_RENDERER.render(circle);
    }
  }

  public void updatePosition() {
    if(this.spineComponent != null) {
      float w = spineComponent.skeleton.getData().getWidth() * spineComponent.getScaling();
      polygon.setPosition(this.spineComponent.skeleton.getX()-w/2, this.spineComponent.skeleton.getY());
      polygon.setRotation(this.spineComponent.getRotation());
    }
  }

  public boolean isMapObject() {
    return this.mapObject != null;
  }

  /**
   * Checks the collision of an entity with the owner
   * of the Dimension Component.
   * We know that his method is alway called from a spine or sprite, so
   * the source is always a polygon!
   */
  public boolean collidesWith(Entity entity, CollisionComponent collisionComponent) {
    if(collisionComponent.polygon != null) {
      return Intersector.intersectPolygons(polygon, collisionComponent.polygon, new Polygon());
    }
    else if(collisionComponent.circle != null) {
      Circle c = collisionComponent.circle;
      float []vertices=polygon.getTransformedVertices();
      Vector2 center=new Vector2(c.x, c.y);
      float squareRadius=c.radius*c.radius;
      for (int i=0;i<vertices.length;i+=2){
        if (i==0){
          if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length-2], vertices[vertices.length-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
            return true;
        } else {
          if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
            return true;
        }
      }
    }
    return false;
  }
}
