package com.starsailor.actors;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;


public interface Collidable extends Component {
    void handleCollision(Entity collider, Entity collidee, Vector2 position);
}
