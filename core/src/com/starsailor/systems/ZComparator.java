package com.starsailor.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.starsailor.components.PositionComponent;

import java.util.Comparator;


public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

    @Override
    public int compare(Entity e1, Entity e2) {
        return (int)Math.signum(posMap.get(e1).z - posMap.get(e2).z);
    }
}
