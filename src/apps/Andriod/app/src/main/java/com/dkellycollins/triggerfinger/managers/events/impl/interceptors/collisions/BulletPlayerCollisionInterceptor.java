package com.dkellycollins.triggerfinger.managers.events.impl.interceptors.collisions;

import com.dkellycollins.triggerfinger.data.entity.IBullet;
import com.dkellycollins.triggerfinger.data.entity.IPlayer;
import com.dkellycollins.triggerfinger.managers.entity.IBulletEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.IPlayerEntityManager;
import com.dkellycollins.triggerfinger.managers.events.IEventInterceptor;
import com.dkellycollins.triggerfinger.managers.events.IEvent;
import com.dkellycollins.triggerfinger.managers.events.impl.events.CollisionEvent;
import com.dkellycollins.triggerfinger.util.qa.Assert;

public class BulletPlayerCollisionInterceptor implements IEventInterceptor {

    private final IBulletEntityManager _bulletManager;
    private final IPlayerEntityManager _playerManager;

    public BulletPlayerCollisionInterceptor(IBulletEntityManager bulletManager, IPlayerEntityManager playerManager) {
        Assert.isNotNull(bulletManager, "bullerManager");
        Assert.isNotNull(playerManager, "playerManager");

        _bulletManager = bulletManager;
        _playerManager = playerManager;
    }

    @Override
    public boolean handlesMessage(IEvent event) {
        return event instanceof CollisionEvent;
    }

    @Override
    public void invoke(IEvent event) {
        CollisionEvent m = (CollisionEvent) event;

        IPlayer player = getPlayer(m.getItem1(), m.getItem2());
        IBullet bullet = getBullet(m.getItem1(), m.getItem2());

        if(player != null && bullet != null) {
            onCollision(bullet, player);
        }
    }

    public void onCollision(IBullet bullet, IPlayer player) {
        _bulletManager.delete(bullet.getId());
    }

    private IPlayer getPlayer(int item1, int item2) {
        IPlayer player = _playerManager.retrievePlayerOne();

        if(player.getCollidableId() == item1 || player.getCollidableId() == item2) {
            return player;
        }
        return null;
    }

    private IBullet getBullet(int item1, int item2) {
        IBullet bullet = _bulletManager.retrieveByCollidable(item1);
        if(bullet == null) {
            bullet = _bulletManager.retrieveByCollidable(item2);
        }

        return bullet;
    }
}
