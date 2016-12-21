package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.nima.util.Settings;

/**
 *
 */
public class PositionComponent implements Component {
  public float x = 0.0f;
  public float y = 0.0f;

  public PositionComponent() {
  }

  public PositionComponent(MapObject object) {
    if(object instanceof RectangleMapObject) {
      RectangleMapObject rectangle = (RectangleMapObject) object;
      x = rectangle.getRectangle().getX();
      y = rectangle.getRectangle().getY();
    }
    else if(object instanceof PolylineMapObject) {
      PolylineMapObject p = (PolylineMapObject) object;
      Polyline polyline = p.getPolyline();
      x = polyline.getX();
      y = polyline.getY();
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
    }
  }

  public void translate(float x, float y) {
    this.x += x;
    this.y += y;
  }

  public void center() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    x = Settings.START_FRAME_X * Settings.FRAME_PIXELS_X + (w / 2);
    y = Settings.START_FRAME_Y * Settings.FRAME_PIXELS_Y + (h / 2);

    x = 0;
    y = 0;
  }
}
