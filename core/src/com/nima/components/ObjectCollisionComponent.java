package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Matthias on 18.12.2016.
 */
public class ObjectCollisionComponent implements Component {

  public boolean intersects(MapObject object) {
    if (object instanceof RectangleMapObject) {
      Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

//      Rectangle spineRectangle = new Rectangle(getX()-width/2, getY(), width, height);
//      if (Intersector.overlaps(rectangle, spineRectangle)) {
//        return true;
//      }
    }
    else if (object instanceof PolygonMapObject) {
    }
    else if (object instanceof PolylineMapObject) {
    }
    else if (object instanceof CircleMapObject) {
    }

//    for (PolygonMapObject polygonMapObject : objects.getByType(PolygonMapObject.class)) {
//      //polygonMapObject.getPolyline().g
//
//      if (Intersector.overlaps(rectangle, sprite.getBoundingRectangle())) {
//        camera.position.set(cx, cy, 0);
//        sprite.setPosition(x, y);
//        break;
//      }
//    }

// there are several other types, Rectangle is probably the most common one
//    for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
//
//      Rectangle rectangle = rectangleObject.getRectangle();
//      if (Intersector.overlaps(rectangle, sprite.getBoundingRectangle())) {
//        camera.position.set(cx, cy, 0);
//        sprite.setPosition(x, y);
//        break;
//      }
//    }
    return false;
  }
}
