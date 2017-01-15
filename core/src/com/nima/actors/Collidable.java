package com.nima.actors;

import com.badlogic.ashley.core.Entity;


public interface Collidable {
    void handleCollision(Entity collider, Entity collidee);
}
