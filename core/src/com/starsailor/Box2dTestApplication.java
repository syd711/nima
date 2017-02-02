package com.starsailor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.FreeSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.starsailor.util.box2d.Box2dLocation;
import com.starsailor.util.GraphicsUtil;

import java.util.ArrayList;
import java.util.List;

public class Box2dTestApplication extends ApplicationAdapter {

  private World world;
  private Box2DDebugRenderer box2DDebugRenderer;

  TestSteeringEntity entity;


  private Formation<Vector2> formation;

  List<TestSteeringEntity> targets = new ArrayList<>();
  private OrthographicCamera camera;

  @Override
  public void create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.update();

    world = new World(new Vector2(0, 0), false);
    box2DDebugRenderer = new Box2DDebugRenderer();

    BodyDef def = new BodyDef();
    def.type = BodyDef.BodyType.DynamicBody;
    def.fixedRotation = false;
    def.position.set(400, 400);
    Body body = world.createBody(def);

    PolygonShape shape = new PolygonShape();
    //calculated from center!
    shape.setAsBox(10, 10);
    body.createFixture(shape, 1f);
    shape.dispose();

    entity = new TestSteeringEntity(body, 30);

    //////////// Creation formation
    FreeSlotAssignmentStrategy<Vector2> freeSlotAssignmentStrategy = new FreeSlotAssignmentStrategy<>();
    DefensiveCircleFormationPattern<Vector2> defensiveCirclePattern = new DefensiveCircleFormationPattern<>(100);
    formation = new Formation<>(entity, defensiveCirclePattern, freeSlotAssignmentStrategy);

    ////////// Create formation entities
    for(int i = 0; i < 3; i++) {
      def = new BodyDef();
      def.type = BodyDef.BodyType.DynamicBody;
      def.fixedRotation = false;
      def.position.set(150 + i * 50, 150 + i * 50);
      body = world.createBody(def);
      body.setLinearDamping(0.5f);

      shape = new PolygonShape();
      //calculated from center!
      shape.setAsBox(20, 20);
      body.createFixture(shape, 0.01f);
      shape.dispose();

      TestSteeringEntity target = new TestSteeringEntity(body, 130);
      targets.add(target);
      formation.addMember(target);

      Arrive<Vector2> arrive = new Arrive<>(target, target.getTargetLocation());
      arrive.setTimeToTarget(0.1f);
      arrive.setArrivalTolerance(2f);
      arrive.setDecelerationRadius(10);

      target.setBehavior(arrive);
    }

  }


  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    box2DDebugRenderer.render(world, camera.combined);

    int x = 0, y = 0;
    int delta = 10;
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      x += delta;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      x -= delta;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      y += delta;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      y -= delta;
    }

    if(x != 0) {
      Vector2 vel = entity.getBody().getLinearVelocity();
      entity.getBody().setLinearVelocity(x * 10, vel.y);
    }
    if(y != 0) {
      Vector2 vel = entity.getBody().getLinearVelocity();
      entity.getBody().setLinearVelocity(vel.y, y * 10);
    }

    for(TestSteeringEntity target : targets) {
      target.update(Gdx.graphics.getDeltaTime());
    }

    entity.update(Gdx.graphics.getDeltaTime());
    formation.updateSlots();
  }

  public static class TestSteeringEntity implements Steerable<Vector2>, FormationMember<Vector2> {

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

    private Box2dLocation location;

    public TestSteeringEntity(Body body, float boundingRadius) {
      location = new Box2dLocation(new Vector2());
      this.body = body;
      this.boundingRadius = boundingRadius;

//      "maxLinearSpeed" : 2,
//          "maxLinearAcceleration" : 6,
//          "maxAngularSpeed" : 2,
//          "maxAngularAcceleration" : 2
      this.maxLinearSpeed = 6;
      this.maxLinearAcceleration = 6;
      this.maxAngularSpeed = 2;
      this.maxAngularAcceleration = 2;

      this.tagged = false;

      this.steeringOutput = new SteeringAcceleration<>(new Vector2());
    }

    public void update(float delta) {
      if(behavior != null) {
        behavior.calculateSteering(steeringOutput);
        applySteering(delta);
      }
    }

    private void applySteering(float deltaTime) {

      boolean anyAccelerations = false;

      // Update position and linear velocity.
      if(!steeringOutput.linear.isZero()) {
        // this method internally scales the force by deltaTime
        body.applyForceToCenter(steeringOutput.linear, true);
        anyAccelerations = true;
      }

      // Update orientation and angular velocity
      if(true) {
        if(steeringOutput.angular != 0) {
          // this method internally scales the torque by deltaTime
          body.applyTorque(steeringOutput.angular, true);
          anyAccelerations = true;
        }
      }
      else {
        // If we haven't got any velocity, then we can do nothing.
        Vector2 linVel = getLinearVelocity();
        if(!linVel.isZero(getZeroLinearSpeedThreshold())) {
          float newOrientation = vectorToAngle(linVel);
          body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
          body.setTransform(body.getPosition(), newOrientation);
        }
      }

      if(anyAccelerations) {

        // Cap the linear speed
        Vector2 velocity = body.getLinearVelocity();
        float currentSpeedSquare = velocity.len2();
        float maxLinearSpeed = getMaxLinearSpeed();
        if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
          body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
        }

        // Cap the angular speed
        float maxAngVelocity = getMaxAngularSpeed();
        if(body.getAngularVelocity() > maxAngVelocity) {
          body.setAngularVelocity(maxAngVelocity);
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

    @Override
    public Location<Vector2> getTargetLocation() {
      return location;
    }
  }
}
