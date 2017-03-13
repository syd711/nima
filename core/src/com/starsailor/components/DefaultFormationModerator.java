package com.starsailor.components;

import com.badlogic.gdx.ai.fma.FormationMotionModerator;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.starsailor.actors.GameEntity;
import com.starsailor.actors.IFormationOwner;

/**
 * Created by Matthias on 07.03.2017.
 */
public class DefaultFormationModerator extends FormationMotionModerator {

  private final IFormationOwner formationOwner;
  private final GameEntity formationOwnerAsEntity;

  private Vector2 lastPosition;
  private final SteerableComponent steerableComponent;
  private final BodyComponent bodyComponent;

  public DefaultFormationModerator(IFormationOwner formationOwner) {
    this.formationOwner = formationOwner;
    this.formationOwnerAsEntity = (GameEntity) formationOwner;
    steerableComponent = formationOwnerAsEntity.getComponent(SteerableComponent.class);
    bodyComponent = formationOwnerAsEntity.getComponent(BodyComponent.class);
  }

  @Override
  public void updateAnchorPoint(Location anchor) {
    float maxMemberDistance = formationOwner.getMaxMemberDistance();
    Body body = bodyComponent.body;

    if(maxMemberDistance > steerableComponent.getBoundingRadius() * 2) {
      if(lastPosition != null) {
        body.setTransform(lastPosition, body.getAngle());
      }
    }
    else {
      lastPosition = new Vector2(body.getPosition());
    }
  }
}
