package com.dkellycollins.triggerfinger.managers.state.impl;

import com.dkellycollins.triggerfinger.data.entity.ICollidable;
import com.dkellycollins.triggerfinger.data.model.IPosition;
import com.dkellycollins.triggerfinger.data.model.IVector;
import com.dkellycollins.triggerfinger.managers.entity.ICollidableEntityManager;
import com.dkellycollins.triggerfinger.managers.events.dispatchers.ICollisionDispatcher;
import com.dkellycollins.triggerfinger.managers.state.IStateManager;
import com.dkellycollins.triggerfinger.util.ArrayUtil;
import com.dkellycollins.triggerfinger.util.MathUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollisionStateManager implements IStateManager {

    private final ICollidableEntityManager _collidableManager;
    private final ICollisionDispatcher _dispatcher;

    public CollisionStateManager(ICollidableEntityManager collidableManager, ICollisionDispatcher dispatcher) {
        _collidableManager = collidableManager;
        _dispatcher = dispatcher;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(long deltaTime) {

        List<ICollidable> collidables = ArrayUtil.asArrayList(_collidableManager.retrieve());

        for(int i = 0; i < collidables.size(); i++) {
            ICollidable item1 = collidables.get(i);
            for(int j = i + 1; j < collidables.size(); j++) {
                ICollidable item2 = collidables.get(j);

                if(isCollision(item1, item2)) {
                    _dispatcher.onCollision(item1.getId(), item2.getId());
                }
            }
        }
    }

    private boolean isCollision(ICollidable item1, ICollidable item2) {
        IVector item1Pos = item1.getCenter();
        IVector item2Pos = item2.getCenter();
        float d2 = MathUtil.distanceSquared(item1Pos.getX(), item2Pos.getX(), item1Pos.getY(), item2Pos.getY());
        float d = (float)Math.sqrt(d2);

        float r = item1.getRadius() + item2.getRadius();

        return d <= r;
    }
}