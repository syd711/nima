package com.starsailor.editor.util;

import com.google.gson.*;
import com.starsailor.data.ShipData;

import java.lang.reflect.Type;

/**
 * GSON serialization for ShipData objects
 */
public class ShipDataSerializer implements JsonSerializer<ShipData> {

  @Override
  public JsonElement serialize(ShipData obj, Type type, JsonSerializationContext jsc) {
    Gson gson = new Gson();
    JsonObject jObj = (JsonObject) gson.toJsonTree(obj);
//    if(obj.getShieldData() != null && obj.getShieldData().isExtendParentData()) {
//      jObj.remove("shieldData");
//    }
//    if(obj.getBodyData() != null && obj.getBodyData().isExtendParentData()) {
//      jObj.remove("bodyData");
//    }
//    if(obj.getSteeringData() != null && obj.getSteeringData().isExtendParentData()) {
//      jObj.remove("steeringData");
//    }
    return jObj;
  }
}
