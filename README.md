# nima


# Map Objects

* 1: "ground": background layer
* 2: "entities": entities on the background layer
* 3: "collision" [Object Layer]: defines the body of the entity, use circles (+shift) and rectangles here
* 4: "lights" [Object Layer]: defines fix light entities
* 5: "data" [Object Layer]: all additional data on this layer, e.g. the routing points for NPCs

## Map Object Types (Layer Depending)

There are several type of entities that can be defined on the object layers.

Layer       Attributes          Description                         Name Attribute                  Shape
========================================================================================================================
collision   type:Planet         Defines a planet                    The name of the planet          Rectangle/Circle
collision   type:Station        Defines a station                   The name of the station         Rectangle/Circle
lights      type:ConeLight      Defines a cone light spot           - ignored -                     Circle
            lightDegree:<INT>   Direction angle of the light
            coneDegree:<INT>    Angle/Width of the spot itself
            lightDistance:<INT> Size of the light, if undefined the width of the circle will be used
lights      type:PointLight     Defines a point light               - ignored -                     Circle
            lightDistance:<INT> Size of the light, if undefined the width of the circle will be used
data        type:Route          Defines a point of a route          The route the point belongs to  Circle
            dockable:<BOOLEAN>  true/false to play docking animation, used for locations
            dockTime:<INT>      Milliseconds to stack docked (hidden)
            shipType:<STRING>   Type of the ship that is on this route, only defined for 1x point.
                                Valid ship types are:
                                - merchant
                                - pirate