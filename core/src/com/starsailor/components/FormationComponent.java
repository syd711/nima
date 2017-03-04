package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.actors.FormationOwner;
import com.starsailor.actors.NPC;
import com.starsailor.actors.Ship;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for grouping
 */
public class FormationComponent implements Component, Pool.Poolable {

  private Formation<Vector2> formation;

  private List<NPC> members = new ArrayList<>();
  private FormationOwner formationOwner;
  @Override
  public void reset() {
    this.formation = null;
    this.members.clear();
    this.formationOwner = null;
  }

  public void updateFormation() {
    if(!members.isEmpty()) {
      formation.updateSlots();
    }
  }

  public void setFormation(Formation<Vector2> formation) {
    this.formation = formation;
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
    FormationComponent formationComponent = formationOwner.getComponent(FormationComponent.class);
    List<Ship> result = new ArrayList<>();
    result.addAll(formationComponent.members);
    return result;
  }

  @Nullable
  public Ship getNearestMemberTo(Ship ship) {
    List<Ship> members = getMembers();
    Ship nearest = null;
    for(Ship member : members) {
      if(nearest == null) {
        nearest = member;
        continue;
      }

      if(nearest.getDistanceTo(ship) > member.getDistanceTo(ship)) {
        nearest = member;
      }
    }
    return nearest;
  }
}
