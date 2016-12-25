package com.nima.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used for testing
 */
public class TiledDebugRenderer {

  private final ShapeRenderer shapeRenderer;
  private final OrthographicCamera camera;
  private List<Circle> circles = new ArrayList<>();
  private List<Rectangle> rectangles = new ArrayList<>();
  private List<Polygon> polygons = new ArrayList<>();

  private Map<String,Polygon> mappedPolygon = new HashMap<>();

  public TiledDebugRenderer(OrthographicCamera camera) {
    shapeRenderer = new ShapeRenderer();
    this.camera = camera;
  }

  public void render() {
    shapeRenderer.setAutoShapeType(true);
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(Color.RED);

    for(Polygon polygon : polygons) {
      shapeRenderer.polygon(polygon.getTransformedVertices());
    }

    for(Polygon polygon : mappedPolygon.values()) {
      shapeRenderer.polygon(polygon.getTransformedVertices());
    }


    for(Circle circle : circles) {
      shapeRenderer.circle(circle.x, circle.y, circle.radius);
    }

    for(Rectangle rectangle : rectangles) {
      shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    shapeRenderer.end();
  }

  public void render(Circle circle) {
    this.circles.add(circle);
  }

  public void render(Rectangle rectangle) {
    this.rectangles.add(rectangle);
  }

  public void render(Polygon polygon) {
    this.polygons.add(polygon);
  }

  public void render(String key, Polygon polygon) {
    this.mappedPolygon.put(key, polygon);
  }

  public void render(List<Polygon> polygons) {
    this.polygons.addAll(polygons);
  }
}
