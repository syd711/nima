package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.IFormationMember;
import com.starsailor.actors.IFormationOwner;
import com.starsailor.actors.Ship;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for grouping
 */
public class FormationComponent implements Component, Pool.Poolable {

  private Formation<Vector2> formation;

  private List<Ship> members = new ArrayList<>();

  @Override
  public void reset() {
    this.formation = null;
    this.members.clear();
  }

  public void updateFormation() {
    if(!members.isEmpty()) {
      formation.updateSlots();
    }
  }

  public void setFormation(Formation<Vector2> formation) {
    this.formation = formation;
  }

  public void addMember(IFormationMember member) {
    members.add((Ship) member);
    formation.addMember(member);
  }

  public void removeMember(Ship ship) {
    members.remove(ship);
    formation.removeMember(ship);
  }

  public List<Ship> getMembers() {
    return members;
  }

  public Formation<Vector2> getFormation() {
    return formation;
  }
}
