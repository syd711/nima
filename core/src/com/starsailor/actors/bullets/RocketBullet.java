package com.starsailor.actors.bullets;

import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponProfile;

/**
 * Concrete implementation of a weapon type.
 */
public class RocketBullet extends MissileBullet  {

  public RocketBullet(WeaponProfile weaponProfile, Ship owner, Ship target) {
    super(weaponProfile, owner, target);
  }

}
