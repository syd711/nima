package com.starsailor.actors;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.math.Vector2;

/**
 * The core methods for providing formations
 */
public interface IFormationMember<T> extends FormationMember<Vector2> {

  void setFormationOwner(IFormationOwner formationOwner);
}
