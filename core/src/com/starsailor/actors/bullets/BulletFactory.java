package com.starsailor.actors.bullets;

import com.starsailor.actors.Ship;
import com.starsailor.data.WeaponProfile;
import com.starsailor.managers.EntityManager;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;

/**
 * Creates new bullets via reflection.
 */
public class BulletFactory {

  public static void create(Ship owner, Ship target) {
    try {
      WeaponProfile weaponProfile = owner.shootingComponent.getActiveWeaponProfile();
      String className = StringUtils.capitalize(weaponProfile.type.toString().toLowerCase()) + "Bullet";
      String fullClassName = Bullet.class.getPackage().getName() + "." + className;

      Class<?> clazz = Class.forName(fullClassName);
      Constructor<?> constructor = clazz.getConstructor(WeaponProfile.class, Ship.class, Ship.class);
      Bullet bullet = (Bullet) constructor.newInstance(weaponProfile, owner, target);
      bullet.create();

      EntityManager.getInstance().add(bullet);
      bullet.owner.shootingComponent.updateLastBulletTime();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
