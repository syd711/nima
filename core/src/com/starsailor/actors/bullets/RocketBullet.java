package com.starsailor.actors.bullets;

import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponData;

/**
 * Concrete implementation of a weapon type.
 */
public class RocketBullet extends MissileBullet  {

  public RocketBullet(WeaponData weaponData, Ship owner, Ship target) {
    super(weaponData, owner, target);
  }

}
