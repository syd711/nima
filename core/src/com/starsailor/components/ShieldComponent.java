package com.starsailor.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.starsailor.Game;
import com.starsailor.data.ShieldProfile;

/**
 *
 */
public class ShieldComponent implements Component , Pool.Poolable {
  public ShieldProfile shieldProfile;

  private long lastIgnitionTime = 0;

  @Override
  public void reset() {
    this.shieldProfile = null;
  }

  public boolean isCharged() {
    float current = Game.currentTimeMillis - lastIgnitionTime;
    return current > shieldProfile.rechargeTimeMillis;
  }
}
