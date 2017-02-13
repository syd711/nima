package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for grouping
 */
public class FormationComponent implements Component, Pool.Poolable {

  public Formation<Vector2> formation;
  public List<NPC> members = new ArrayList<>();
  public Ship formationOwner;

  @Override
  public void reset() {
    this.formation = null;
    this.members.clear();
  }

  public void updateFormation() {
    formation.updateSlots();
  }

  public void addMember(FormationMember member) {
    members.add((NPC) member);
    formation.addMember(member);
  }

  public void removeMember(Ship ship) {
    members.remove(ship);
    formation.removeMember(ship);
  }

  public List<Ship> getMembers() {
    List<Ship> result = new ArrayList<>();
    result.addAll(formationOwner.formationComponent.members);
    result.add(formationOwner);
    return result;
  }
}
