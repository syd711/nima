package com.starsailor.actors;

import com.badlogic.gdx.math.Vector2;
import com.starsailor.data.ShipProfile;

/**
 * Guarding another NPC while alive
 */
public class GuardingNPC extends NPC {
  public NPC guardedNPC;

  private Vector2 position;

  /**
   * Constructor used for ships that guard a routing ship
   * @param shipProfile
   * @param guardedNPC
   */
  public GuardingNPC(ShipProfile shipProfile, NPC guardedNPC, Behaviours behaviour, Vector2 position) {
    super(shipProfile, behaviour);
    this.behaviour = behaviour;
    this.guardedNPC = guardedNPC;
    this.position = position;
  }

  @Override
  public void createComponents(ShipProfile profile) {
    super.createComponents(profile);
    this.positionComponent.setPosition(position);
  }
}