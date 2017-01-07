package com.nima.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import static com.nima.util.Resources.WEAPON_PROFILES;

/**
 * Creates the weapon profile objects
 */
public class WeapenProfileFactory {
  private static final String RECHARGE_TIME_MILLIS = "rechargeTimeMillis";
  private static final String DAMAGE = "damage";
  private static final String SPEED = "speed";

  public static WeaponProfile createProfile(String name) {
    FileHandle file = Gdx.files.internal(WEAPON_PROFILES + name + ".json");
    String rawJson = file.readString();
    JsonReader jsonReader = new JsonReader();
    JsonValue root = jsonReader.parse(rawJson);

    float rechargeTimeMillis = root.getFloat(RECHARGE_TIME_MILLIS);
    float damage = root.getFloat(DAMAGE);
    float speed = root.getFloat(SPEED);

    WeaponProfile profile = new WeaponProfile();
    profile.rechargeTime = rechargeTimeMillis;
    profile.damage = damage;
    profile.speed = speed;
    return profile;
  }
}
