package com.dkellycollins.triggerfinger.data.entity.impl;

import com.dkellycollins.triggerfinger.data.entity.IPlayer;

public class Player implements IPlayer {

    private final int _id;
    private final int _collidableId;

    private int _weaponId;

    public Player(int id, int collidableId, int weaponId) {
        _id = id;
        _collidableId = collidableId;
        _weaponId = weaponId;
    }

    @Override
    public int getCollidableId() {
        return _collidableId;
    }

    @Override
    public int getWeaponId() {
        return _weaponId;
    }

    @Override
    public int getId() {
        return _id;
    }

    public void setWeaponId(int weaponId) {
        _weaponId = weaponId;
    }
}
