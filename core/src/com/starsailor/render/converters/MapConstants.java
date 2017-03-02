package com.starsailor.render.converters;

/**
 * Constants used in the Tiled Map Editor
 */
public class MapConstants {

  public static final String PROPERTY_OBJECT_TYPE = "type";

  public static final String PROPERTY_SHIP_PROFILE = "shipProfile";
  public static final String PROPERTY_DOCKABLE = "dockable";
  public static final String PROPERTY_DOCK_TIME = "dockTime";
  public static final String PROPERTY_FRACTION = "fraction";

  //entity properties
  public static final String PROPERTY_POSITION = "position";
  public static final String PROPERTY_CENTERED_POSITION = "centeredPosition";

  //light properties
  public static final String PROPERTY_LIGHT_DISTANCE = "lightDistance";
  public static final String PROPERTY_LIGHT_DEGREE = "lightDegree";
  public static final String PROPERTY_CONE_DEGREE = "coneDegree";

  //used to store the body of an entity temporary to the map object
  public static final String PROPERTY_BOX2D_BODY = "body";

  // PointLight, DirectionalLight, PositionalLight, ConeLight
  public static final String TYPE_CONE_LIGHT = "ConeLight";
  public static final String TYPE_POINT_LIGHT = "PointLight";

  //TODO fix this
  @Deprecated
  public static final String TYPE_PLANET = "Planet";
  public static final String TYPE_STATION = "Station";
  public static final String TYPE_ROUTE_POINT = "Route";
  public static final String TYPE_ROUTE_MEMBER = "RouteMember";
  public static final String TYPE_PIRATE = "Pirate";

  // Steering Behaviours represented by states
  @Deprecated
  public static final String PROPERTY_STATE = "state";

  @Deprecated
  public static final String STATE_SEEK_AND_DESTROY = "SeekAndDestroy";
  @Deprecated
  public static final String STATE_GUARD = "Guard";
  @Deprecated
  public static final String STATE_ROUTE = "Route";

  //routing attributes
  @Deprecated
  public static final String PROPERTY_ROUTE = "route";

}
