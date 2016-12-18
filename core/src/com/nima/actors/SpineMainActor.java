package com.nima.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.nima.render.ActorBasedTiledMultiMapRenderer;
import com.nima.util.Settings;

/**
 * Our main character!
 */
public class SpineMainActor extends SpineActor {
  private BitmapFont font = new BitmapFont();

  private MapObject intersectingObject;

  private float lastX;
  private float lastY;

  public SpineMainActor(ActorBasedTiledMultiMapRenderer renderer, String spineName, String defaultAnimation, float scale) {
    super(renderer, spineName,  defaultAnimation, scale);
  }

  @Override
  public void render() {
    super.render();
    intersectingObject = null;
    if(Settings.DEBUG) {
      font.draw(renderer.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), getX()-30, getY());
    }
  }

  public boolean moveBy(int x, int y) {
    if(intersectingObject == null) {
      lastX = getX();
      lastY = getY();
      return super.translate(x, y);
    }

    setPosition(lastX, lastY);
    return false;
  }

  @Override
  public boolean intersects(MapObject object) {
    if (object instanceof RectangleMapObject) {
      Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

      Rectangle spineRectangle = new Rectangle(getX()-width/2, getY(), width, height);
      if (Intersector.overlaps(rectangle, spineRectangle)) {
        intersectingObject = object;
        return true;
      }
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
