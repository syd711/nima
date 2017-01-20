package com.starsailor.actors;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;


public interface Collidable extends Component {
    void handleCollision(Entity collider, Entity collidee);
}
