package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polyline;

/**
 *
 */
public class DimensionComponent implements Component {
  public float width = 0.0f;
  public float height = 0.0f;

  public DimensionComponent(MapObject object) {
    if(object instanceof RectangleMapObject) {
      RectangleMapObject rectangle = (RectangleMapObject) object;
      width  = rectangle.getRectangle().width;
      height = rectangle.getRectangle().height;
    }
    else if(object instanceof PolylineMapObject) {
      PolylineMapObject p = (PolylineMapObject) object;
      Polyline polyline = p.getPolyline();
    }
    else if(object instanceof PolygonMapObject) {
      PolygonMapObject p = (PolygonMapObject) object;
    }
    else if(object instanceof CircleMapObject) {
      CircleMapObject circle = (CircleMapObject) object;
      Circle c = circle.getCircle();
    }
  }


  /**
   * Constructor for spines
   * @param spineComponent the spine object to create the polygon for
   */
  public DimensionComponent(SpineComponent spineComponent) {
    width = spineComponent.skeleton.getData().getWidth()*spineComponent.getScaling();
    height = spineComponent.skeleton.getData().getHeight()*spineComponent.getScaling();
  }
}
