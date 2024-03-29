package com.dkellycollins.triggerfinger;

import android.content.Context;
import android.view.View;

import com.dkellycollins.triggerfinger.data.config.IBulletConfig;
import com.dkellycollins.triggerfinger.data.config.IEnemyConfig;
import com.dkellycollins.triggerfinger.data.config.IFontConfig;
import com.dkellycollins.triggerfinger.data.config.IPlayerConfig;
import com.dkellycollins.triggerfinger.data.config.impl.BulletConfig;
import com.dkellycollins.triggerfinger.data.config.impl.EnemyConfig;
import com.dkellycollins.triggerfinger.data.config.impl.FontConfig;
import com.dkellycollins.triggerfinger.data.config.impl.PlayerConfig;
import com.dkellycollins.triggerfinger.data.daos.IActivityDao;
import com.dkellycollins.triggerfinger.data.daos.IBitmapDao;
import com.dkellycollins.triggerfinger.data.daos.IBulletDao;
import com.dkellycollins.triggerfinger.data.daos.ICollidableDao;
import com.dkellycollins.triggerfinger.data.daos.IDeviceInfoDao;
import com.dkellycollins.triggerfinger.data.daos.IEnemyDao;
import com.dkellycollins.triggerfinger.data.daos.IPlayerDao;
import com.dkellycollins.triggerfinger.data.daos.ITimerDao;
import com.dkellycollins.triggerfinger.data.daos.ITouchPositionDao;
import com.dkellycollins.triggerfinger.data.daos.IWeaponDao;
import com.dkellycollins.triggerfinger.data.daos.impl.BitmapDao;
import com.dkellycollins.triggerfinger.data.daos.impl.BulletDao;
import com.dkellycollins.triggerfinger.data.daos.impl.CollidableDao;
import com.dkellycollins.triggerfinger.data.daos.impl.DeviceInfoDao;
import com.dkellycollins.triggerfinger.data.daos.impl.EnemyDao;
import com.dkellycollins.triggerfinger.data.daos.impl.PlayerDao;
import com.dkellycollins.triggerfinger.data.daos.impl.TimerDao;
import com.dkellycollins.triggerfinger.data.daos.impl.TouchPositionDao;
import com.dkellycollins.triggerfinger.data.daos.impl.WeaponDao;
import com.dkellycollins.triggerfinger.managers.entity.IBitmapEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.IBulletEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.ICollidableEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.IEnemyEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.IPlayerEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.ITimerEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.IWeaponEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.impl.BitmapEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.impl.BulletEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.impl.CollidableEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.impl.EnemyEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.impl.PlayerEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.impl.TimerEntityManager;
import com.dkellycollins.triggerfinger.managers.entity.impl.WeaponEntityManager;
import com.dkellycollins.triggerfinger.managers.events.IEventDispatcher;
import com.dkellycollins.triggerfinger.managers.events.IEventInterceptor;
import com.dkellycollins.triggerfinger.managers.events.impl.EventDispatcher;
import com.dkellycollins.triggerfinger.managers.events.impl.interceptors.collisions.BulletEnemyCollisionInterceptor;
import com.dkellycollins.triggerfinger.managers.events.impl.interceptors.collisions.PlayerEnemyCollisionInterceptor;
import com.dkellycollins.triggerfinger.managers.state.IStateManager;
import com.dkellycollins.triggerfinger.managers.state.impl.BulletStateManager;
import com.dkellycollins.triggerfinger.managers.state.impl.CollisionStateManager;
import com.dkellycollins.triggerfinger.managers.state.impl.EnemyStateManager;
import com.dkellycollins.triggerfinger.managers.state.impl.PlayerStateManager;
import com.dkellycollins.triggerfinger.managers.state.impl.TimerStateManager;
import com.dkellycollins.triggerfinger.managers.state.impl.WeaponStateManager;
import com.dkellycollins.triggerfinger.managers.view.IViewManager;
import com.dkellycollins.triggerfinger.managers.view.impl.EnemyViewManager;
import com.dkellycollins.triggerfinger.managers.view.impl.PlayerViewManager;
import com.dkellycollins.triggerfinger.managers.view.impl.gui.ScoreViewManager;
import com.dkellycollins.triggerfinger.managers.view.impl.debug.BulletHitboxViewManager;
import com.dkellycollins.triggerfinger.managers.view.impl.debug.EnemyHitboxViewManager;
import com.dkellycollins.triggerfinger.managers.view.impl.debug.PlayerHitboxViewManager;
import com.dkellycollins.triggerfinger.util.logger.ILogWriter;
import com.dkellycollins.triggerfinger.util.logger.ILogger;
import com.dkellycollins.triggerfinger.util.logger.impl.Logger;
import com.dkellycollins.triggerfinger.util.logger.impl.SystemLogWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Module {

    private final List<IViewManager> _viewManagers;
    private final List<IStateManager> _stateManagers;
    private final List<IActivityDao> _activityDaos;
    private final ILogger _logger;

    public Module(View activeView, Context context) {

        //Util
        List<ILogWriter> logWriters = new ArrayList<>();
        logWriters.add(new SystemLogWriter());
        _logger = new Logger(logWriters);
        Random random = new Random();

        //Config
        IBulletConfig bulletConfig = new BulletConfig(context);
        IEnemyConfig enemyConfig = new EnemyConfig(context);
        IPlayerConfig playerConfig = new PlayerConfig(context);
        IFontConfig fontConfig = new FontConfig(context);

        //Data
        ITouchPositionDao touchPositionDao = new TouchPositionDao(activeView);
        IDeviceInfoDao deviceInfoDao = new DeviceInfoDao(activeView);
        ICollidableDao collidableDao = new CollidableDao();
        IPlayerDao playerDao = new PlayerDao();
        IEnemyDao enemyDao = new EnemyDao();
        ITimerDao timerDao = new TimerDao();
        IWeaponDao weaponDao = new WeaponDao();
        IBulletDao bulletDao = new BulletDao();
        IBitmapDao bitmapDao = new BitmapDao(context);

        _activityDaos = new ArrayList<>();
        _activityDaos.add(collidableDao);
        _activityDaos.add(playerDao);
        _activityDaos.add(enemyDao);
        _activityDaos.add(timerDao);
        _activityDaos.add(weaponDao);
        _activityDaos.add(bulletDao);

        //Entity managers.
        ICollidableEntityManager collidableEntityManager = new CollidableEntityManager(collidableDao);
        ITimerEntityManager timerEntityManager = new TimerEntityManager(timerDao);
        IWeaponEntityManager weaponEntityManager = new WeaponEntityManager(weaponDao, bulletConfig, timerEntityManager);
        IBulletEntityManager bulletEntityManager = new BulletEntityManager(bulletDao, bulletConfig, collidableEntityManager);
        IPlayerEntityManager playerEntityManager = new PlayerEntityManager(playerDao, playerConfig, collidableEntityManager, weaponEntityManager, timerEntityManager);
        IEnemyEntityManager enemyEntityManager = new EnemyEntityManager(enemyDao, enemyConfig, collidableEntityManager);
        IBitmapEntityManager bitmapEnityManager = new BitmapEntityManager(bitmapDao);

        //Interceptors
        List<IEventInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new BulletEnemyCollisionInterceptor(bulletEntityManager, enemyEntityManager, playerEntityManager));
        //interceptors.add(new BulletPlayerCollisionInterceptor(bulletEntityManager, playerEntityManager));
        interceptors.add(new PlayerEnemyCollisionInterceptor(playerEntityManager, enemyEntityManager, timerEntityManager));

        //Dispatchers
        IEventDispatcher dispatcher = new EventDispatcher(interceptors);

        //State managers.
        _stateManagers = new ArrayList<IStateManager>();
        _stateManagers.add(new CollisionStateManager(collidableEntityManager, dispatcher));
        _stateManagers.add(new PlayerStateManager(playerEntityManager, collidableEntityManager, touchPositionDao, weaponEntityManager, timerEntityManager));
        _stateManagers.add(new EnemyStateManager(enemyEntityManager, enemyConfig, collidableEntityManager, timerEntityManager, deviceInfoDao, random));
        _stateManagers.add(new TimerStateManager(timerEntityManager));
        _stateManagers.add(new WeaponStateManager(weaponEntityManager, timerEntityManager, bulletEntityManager, collidableEntityManager));
        _stateManagers.add(new BulletStateManager(bulletEntityManager, collidableEntityManager, deviceInfoDao));

        //View managers.
        _viewManagers = new ArrayList<IViewManager>();
        //_viewManagers.add(new BulletViewManager(bulletConfig, bulletEntityManager, collidableEntityManager, bitmapEnityManager));
        _viewManagers.add(new PlayerViewManager(playerEntityManager, playerConfig, collidableEntityManager, bitmapEnityManager));
        _viewManagers.add(new EnemyViewManager(enemyConfig, bitmapEnityManager, enemyEntityManager, collidableEntityManager));
        _viewManagers.add(new PlayerHitboxViewManager(playerEntityManager, collidableEntityManager));
        _viewManagers.add(new EnemyHitboxViewManager(enemyEntityManager, collidableEntityManager));
        _viewManagers.add(new BulletHitboxViewManager(bulletEntityManager, collidableEntityManager));
        _viewManagers.add(new ScoreViewManager(context, fontConfig, playerEntityManager));

        Collections.sort(_viewManagers, new Comparator<IViewManager>() {
            @Override
            public int compare(IViewManager lhs, IViewManager rhs) {
                return lhs.getLayer().compareTo(rhs.getLayer());
            }
        });
    }

    public List<IStateManager> getStateManagers() {
        return _stateManagers;
    }

    public List<IViewManager> getViewManagers() {
        return _viewManagers;
    }

    public List<IActivityDao> getActivityDaos() { return _activityDaos; }

    public ILogger getLogger() {
        return _logger;
    }

}
