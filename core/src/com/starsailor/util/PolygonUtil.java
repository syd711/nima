package com.starsailor.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Creating polygon and transform them
 * for the given coordinates.
 */
public class PolygonUtil {

  public static void selectionCircle(Vector2 pos) {
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.circle(pos.x, pos.y, 32);
    shapeRenderer.setColor(Color.BLACK);
    shapeRenderer.end();
  }

  public static Polygon clickPolygon(Vector2 clickPoint) {
    float w = 10;
    float h = 10;
    float x = clickPoint.x - 5;
    float y = clickPoint.y - 5;
    float[] vertices = createVertices(w, h, x, y);
    Polygon polygon = new Polygon(vertices);
    polygon.setPosition(0, 0);
    return polygon;
  }

  /**
   * There is no actual center of a polygon, so we simply
   * use the first and the middle vertice and use the distance between them
   */
  public static Vector2 getCenter(Polygon polygon) {
    float[] vertices = polygon.getVertices();
    int index = Math.round(vertices.length / 2);

    Vector2 point1 = new Vector2(vertices[0], vertices[1]);
    Vector2 point2 = new Vector2(vertices[index + 1], vertices[index + 2]);
    float distance = point1.dst(point2);

    return new Vector2(polygon.getX() + distance / 2, polygon.getY() + distance / 2);
  }


  /**
   * Helper for creating a vertice
   */
  private static float[] createVertices(float w, float h, float x, float y) {
    return new float[]{x, y, w + x, y, w + x, h + y, x, h + y};
  }
}
