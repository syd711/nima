package com.nima.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ShootingComponent implements Component, Poolable {
    public long lastBulletTime = 0;

    @Override
    public void reset() {
        lastBulletTime = 0;
    }
}
