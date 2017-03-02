package com.starsailor.editor.util;

import com.google.gson.*;
import com.starsailor.model.GameData;

import java.lang.reflect.Type;

/**
 * GSON serialization for ShipData objects
 */
public class GameDataSerializer implements JsonSerializer<GameData> {

  @Override
  public JsonElement serialize(GameData obj, Type type, JsonSerializationContext context) {
    Gson gson = new Gson();
    JsonObject jObj = (JsonObject) gson.toJsonTree(obj);

//    if(obj instanceof ShipData) {
//      ShipData shipData = (ShipData) obj;
//
//      if(shipData.getShieldData() != null && shipData.getShieldData().isExtendParentData()) {
//        jObj.remove("shieldData");
//      }
//      if(shipData.getBodyData() != null && shipData.getBodyData().isExtendParentData()) {
//        jObj.remove("bodyData");
//      }
//      if(shipData.getSteeringData() != null && shipData.getSteeringData().isExtendParentData()) {
//        jObj.remove("steeringData");
//      }
//
//      JsonElement children = jObj.get("children");
//      jObj.remove("children");
//      jObj.add("children", context.serialize(shipData.getChildren(), GameData.class));
//    }
    return jObj;
  }
}
