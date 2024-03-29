package com.dkellycollins.triggerfinger.managers.entity;

import com.dkellycollins.triggerfinger.data.entity.IPlayer;
import com.dkellycollins.triggerfinger.data.model.IVector;

public interface IPlayerEntityManager {

    Iterable<IPlayer> retrieve();
    IPlayer retrieve(int playerId);
    IPlayer retrievePlayerOne();

    int create(IVector position);

    void update(int playerId, int score, byte health);

    void delete(int playerId);
}
