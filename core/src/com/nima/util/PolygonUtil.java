package com.nima.util;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.nima.components.SpineComponent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Creating polygon and transform them
 * for the given coordinates.
 */
public class PolygonUtil {

  public static Polygon rectangle2Polygon(float w, float h, float x, float y) {
    Polygon polygon = new Polygon(new float[]{x, y, w+x, y, w+x, h+y, x, h+y});
    polygon.setPosition(x, y);
    return polygon;
  }

  private static float[] createVertices(float w, float h, float x, float y) {
    return new float[]{x, y, w+x, y, w+x, h+y, x, h+y};
  }

  public static Polygon spine2Polygon(SpineComponent spine) {
    float w = spine.skeleton.getData().getWidth() * spine.getScaling();
    float h = spine.skeleton.getData().getHeight() * spine.getScaling();
    float x = spine.skeleton.getX();
//    float x = spine.skeleton.getX()-w/2;
    float y = spine.skeleton.getY();
    Polygon polygon = new Polygon();
//    polygon.setPosition(x-w/2, y);
    polygon.setPosition(0, 0);
    polygon.setOrigin(x, y);
    polygon.setVertices(createVertices(w, h, x, y));
    polygon.setRotation(spine.getRotation());
    return polygon;
  }

  public static float[] convertSpineVertices(float[] vertices) {
    if(vertices.length%5 != 0) {
      throw new UnsupportedOperationException("Vertices have wrong size");
    }

    List<Float> converted = new ArrayList<>();
    int index = 0;
    while(index < vertices.length) {
      converted.add(vertices[index]);
      converted.add(vertices[index+1]);
      index+=5;
    }

    return ArrayUtils.toPrimitive(converted.toArray(new Float[converted.size()]));
  }

  public static Polygon rectangle2Polygon(Rectangle rectangle) {
    float w = rectangle.width;
    float h = rectangle.height;
    float x = rectangle.x;
    float y = rectangle.y;
    return rectangle2Polygon(w, h, x, y);
  }
}