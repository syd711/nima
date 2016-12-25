package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class PositionComponent implements Component {
  public float x = 0.0f;
  public float y = 0.0f;

  private Vector2 center = new Vector2();

  public PositionComponent() {
  }

  public PositionComponent(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public PositionComponent(MapObject object) {
    if(object instanceof RectangleMapObject) {
      RectangleMapObject rectangle = (RectangleMapObject) object;
      x = rectangle.getRectangle().getX();
      y = rectangle.getRectangle().getY();
      center.set(x+rectangle.getRectangle().width/2, y+rectangle.getRectangle().height/2);
    }
    else if(object instanceof PolygonMapObject) {
      PolygonMapObject p = (PolygonMapObject) object;
      Polygon polygon = p.getPolygon();
      x = polygon.getX();
      y = polygon.getY();
    }
    else if(object instanceof EllipseMapObject) {
      EllipseMapObject ellipseMapObject = (EllipseMapObject) object;
      x = ellipseMapObject.getEllipse().x;
      y = ellipseMapObject.getEllipse().y;
//      center.set(x+rectangle.getRectangle().width/2, y+rectangle.getRectangle().height/2);
    }
  }

  public void translate(float x, float y) {
    this.x += x;
    this.y += y;
  }

  public Vector2 getCenter() {
    return center;
  }
}
