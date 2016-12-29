package com.nima;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nima.util.GraphicsUtil;

/**
 * Created by Matthias on 29.12.2016.
 */
public class Box2dTestApplication extends ApplicationAdapter {

  private World world;
  private Box2DDebugRenderer box2DDebugRenderer;

  TestSteeringEntity entity, target;
  private OrthographicCamera camera;

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();


    //camera
    camera = new OrthographicCamera();
//    camera.zoom = 1.5f;
    camera.setToOrtho(false, w, h);
    camera.update();

    world = new World(new Vector2(0, 0), false);
    box2DDebugRenderer = new Box2DDebugRenderer();

    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
    def.fixedRotation = false;
    def.position.set(600, 600);
    Body body = world.createBody(def);

    PolygonShape shape = new PolygonShape();
    //calculated from center!
    shape.setAsBox(10, 10);
    body.createFixture(shape, 1f);
    shape.dispose();

    entity = new TestSteeringEntity(body, 30);

    def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
    def.fixedRotation = false;
    def.position.set(150, 150);
    body = world.createBody(def);

    shape = new PolygonShape();
    //calculated from center!
    shape.setAsBox(20, 20);
    body.createFixture(shape, 1f);
    shape.dispose();

    target = new TestSteeringEntity(body, 30);

    Arrive<Vector2> arrive = new Arrive<>(entity, target);
    arrive.setTimeToTarget(0.1f);
    arrive.setArrivalTolerance(2f);
    arrive.setDecelerationRadius(10);

    entity.setBehavior(arrive);
  }



  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    box2DDebugRenderer.render(world, camera.combined);

    int x= 0, y = 0;
    int delta = 10;
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      x+= delta;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      x-= delta;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      y+= delta;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      y-= delta;
    }

    if(x != 0) {
      Vector2 vel = target.getBody().getLinearVelocity();
      target.getBody().setLinearVelocity(x*10, vel.y);
    }
    if(y != 0) {
      Vector2 vel = target.getBody().getLinearVelocity();
      target.getBody().setLinearVelocity(vel.y, y*10);
    }

    entity.update(Gdx.graphics.getDeltaTime());

  }

  class TestSteeringEntity implements Steerable<Vector2> {

    private boolean tagged;
    private float boundingRadius;
    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private float maxAngularAcceleration;

    private SteeringBehavior<Vector2> behavior;
    private SteeringAcceleration<Vector2> steeringOutput;

    public Body getBody() {
      return body;
    }

    private Body body;

    public TestSteeringEntity(Body body, float boundingRadius) {
      this.body = body;
      this.boundingRadius = boundingRadius;

      this.maxLinearSpeed = 1500;
      this.maxLinearAcceleration = 1000;
      this.maxAngularSpeed = 30;
      this.maxAngularAcceleration = 1500;

      this.tagged = false;

      this.steeringOutput = new SteeringAcceleration<>(new Vector2());
    }

    public void update(float delta) {
      if(behavior != null) {
        behavior.calculateSteering(steeringOutput);
        applySteering(delta);
      }
    }

    private void applySteering(float delta) {
      boolean anyAccelerations = false;
      if(!steeringOutput.isZero()) {
        Vector2 force = steeringOutput.linear.scl(50);
        body.applyForceToCenter(force, true);
        anyAccelerations = true;
      }

      if(anyAccelerations) {
        Vector2 velocity = body.getLinearVelocity();
        float currentSpeedSquare = velocity.len2();
        if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
          body.setLinearVelocity(velocity.scl((float) (maxLinearSpeed / Math.sqrt(currentSpeedSquare))));
        }
      }
    }

    @Override
    public Vector2 getLinearVelocity() {
      return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
      return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
      return boundingRadius;
    }

    @Override
    public boolean isTagged() {
      return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
      this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
      return 1;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
      return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
      this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
      return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
      this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
      return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
      this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
      return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
      this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
      return body.getPosition();
    }

    @Override
    public float getOrientation() {
      return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector2 vector) {
      return GraphicsUtil.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
      return GraphicsUtil.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
      return null;
    }


    public SteeringBehavior<Vector2> getBehavior() {
      return behavior;
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior) {
      this.behavior = behavior;
    }

    public void setSteeringOutput(SteeringAcceleration<Vector2> steeringOutput) {
      this.steeringOutput = steeringOutput;
    }

    public SteeringAcceleration<Vector2> getSteeringOutput() {
      return steeringOutput;
    }
  }
}
