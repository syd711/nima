package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.nima.actors.Spine;

/**
 * The size of map entities
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
    else if(object instanceof PolygonMapObject) {
      PolygonMapObject p = (PolygonMapObject) object;

    }
    else if(object instanceof EllipseMapObject) {
      EllipseMapObject circle = (EllipseMapObject) object;
      Ellipse ellipse = circle.getEllipse();
      width = ellipse.width;
      height = ellipse.height;
    }
  }


  /**
   * Constructor for spines
   * @param spine the spine object to create the polygon for
   */
  public DimensionComponent(Spine spine) {
    ScalingComponent scalingComponent = spine.getComponent(ScalingComponent.class);
    width = spine.skeleton.getData().getWidth()*scalingComponent.getScaling();
    height = spine.skeleton.getData().getHeight()*scalingComponent.getScaling();
  }
}
