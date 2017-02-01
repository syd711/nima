package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Used for grouping
 */
public class FormationComponent implements Component, Pool.Poolable {

  public Formation<Vector2> formation;

  @Override
  public void reset() {
    this.formation = null;
  }

  public void updateFormation() {
    formation.updateSlots();
  }

  public void addMember(FormationMember member) {
    formation.addMember(member);
  }
}
