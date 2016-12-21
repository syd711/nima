package com.nima.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

/**
 * Used for testing
 */
public class DebugRenderer {

  private final ShapeRenderer shapeRenderer;
  private TiledMultiMapRenderer renderer;
  private Circle circle;
  private Rectangle rectangle;
  private Polygon polygon;

  public DebugRenderer(TiledMultiMapRenderer renderer, OrthographicCamera camera) {
    this.renderer = renderer;
    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(camera.combined);
  }

  public void render() {
    shapeRenderer.setAutoShapeType(true);
    shapeRenderer.begin();
    shapeRenderer.setColor(Color.RED);

    if(polygon != null) {
      shapeRenderer.polygon(polygon.getVertices());
    }

    if(circle != null) {
      shapeRenderer.circle(circle.x, circle.y, circle.radius);
    }

    if(rectangle != null) {
      shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    shapeRenderer.end();
  }

  public void render(Circle circle) {
    this.circle = circle;
  }

  public void render(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  public void render(Polygon polygon) {
    this.polygon = polygon;
  }
}
