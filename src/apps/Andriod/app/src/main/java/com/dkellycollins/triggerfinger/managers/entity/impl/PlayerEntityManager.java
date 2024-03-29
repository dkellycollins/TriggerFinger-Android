package com.dkellycollins.triggerfinger.managers.entity.impl;

import com.dkellycollins.triggerfinger.data.config.IPlayerConfig;
import com.dkellycollins.triggerfinger.data.daos.IPlayerDao;
import com.dkellycollins.triggerfinger.data.entity.IPlayer;
import com.dkellycollins.triggerfinger.managers.entity.ICollidableEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.IPlayerEntityManager;
import com.dkellycollins.triggerfinger.data.model.IVector;
import com.dkellycollins.triggerfinger.managers.entity.ITimerEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.IWeaponEntityManager;
import com.dkellycollins.triggerfinger.util.qa.Assert;

public class PlayerEntityManager implements IPlayerEntityManager {

    private final IPlayerDao _dao;
    private final IPlayerConfig _config;
    private final ICollidableEntityManager _collidableManager;
    private final IWeaponEntityManager _weaponManager;
    private final ITimerEntityManager _timerManager;

    public PlayerEntityManager(IPlayerDao dao, IPlayerConfig config, ICollidableEntityManager collidableManager, IWeaponEntityManager weaponManager, ITimerEntityManager timerManager) {
        Assert.isNotNull(dao, "dao");
        Assert.isNotNull(config, "config");
        Assert.isNotNull(collidableManager, "collidableManager");
        Assert.isNotNull(weaponManager, "weaponManger");
        Assert.isNotNull(timerManager, "timerManager");

        _dao = dao;
        _config = config;
        _collidableManager = collidableManager;
        _weaponManager = weaponManager;
        _timerManager = timerManager;
    }

    @Override
    public Iterable<IPlayer> retrieve() {
        return _dao.retrieve();
    }

    @Override
    public IPlayer retrieve(int playerId) {
        return _dao.retrieve(playerId);
    }

    @Override
    public IPlayer retrievePlayerOne() {
        return _dao.playerOne();
    }

    @Override
    public int create(IVector position) {
        int collidableId = _collidableManager.create(position, _config.getCollidableRadius());
        int weaponId = _weaponManager.create(collidableId);
        int invincibleTimerId = _timerManager.create(_config.getInvincibleTime(), false);

        return _dao.create(collidableId, weaponId, invincibleTimerId, 0, (byte)3);
    }

    @Override
    public void update(int playerId, int score, byte health) {
        IPlayer player = _dao.retrieve(playerId);

        _dao.update(playerId, player.getWeaponId(), score, health);
    }

    @Override
    public void delete(int playerId) {
        IPlayer player = _dao.retrieve(playerId);
        _dao.delete(playerId);
        _collidableManager.delete(player.getCollidableId());
    }
}
