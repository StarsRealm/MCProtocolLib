package org.geysermc.mcprotocollib.protocol.data.game.level.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LevelEventType implements LevelEvent {
    SOUND_DISPENSER_DISPENSE(1000),
    SOUND_DISPENSER_FAIL(1001),
    SOUND_DISPENSER_PROJECTILE_LAUNCH(1002),
    SOUND_FIREWORK_SHOOT(1004),
    SOUND_EXTINGUISH_FIRE(1009),
    SOUND_PLAY_JUKEBOX_SONG(1010),
    SOUND_STOP_JUKEBOX_SONG(1011),
    SOUND_GHAST_WARNING(1015),
    SOUND_GHAST_FIREBALL(1016),
    SOUND_DRAGON_FIREBALL(1017),
    SOUND_BLAZE_FIREBALL(1018),
    SOUND_ZOMBIE_WOODEN_DOOR(1019),
    SOUND_ZOMBIE_IRON_DOOR(1020),
    SOUND_ZOMBIE_DOOR_CRASH(1021),
    SOUND_WITHER_BLOCK_BREAK(1022),
    SOUND_WITHER_BOSS_SPAWN(1023), // Global level event
    SOUND_WITHER_BOSS_SHOOT(1024),
    SOUND_BAT_LIFTOFF(1025),
    SOUND_ZOMBIE_INFECTED(1026),
    SOUND_ZOMBIE_CONVERTED(1027),
    SOUND_DRAGON_DEATH(1028), // Global level event
    SOUND_ANVIL_BROKEN(1029),
    SOUND_ANVIL_USED(1030),
    SOUND_ANVIL_LAND(1031),
    SOUND_PORTAL_TRAVEL(1032),
    SOUND_CHORUS_GROW(1033),
    SOUND_CHORUS_DEATH(1034),
    SOUND_BREWING_STAND_BREW(1035),
    SOUND_END_PORTAL_SPAWN(1038), // Global level event
    SOUND_PHANTOM_BITE(1039),
    SOUND_ZOMBIE_TO_DROWNED(1040),
    SOUND_HUSK_TO_ZOMBIE(1041),
    SOUND_GRINDSTONE_USED(1042),
    SOUND_PAGE_TURN(1043),
    SOUND_SMITHING_TABLE_USED(1044),
    SOUND_POINTED_DRIPSTONE_LAND(1045),
    SOUND_DRIP_LAVA_INTO_CAULDRON(1046),
    SOUND_DRIP_WATER_INTO_CAULDRON(1047),
    SOUND_SKELETON_TO_STRAY(1048),
    SOUND_CRAFTER_CRAFT(1049),
    SOUND_CRAFTER_FAIL(1050),
    SOUND_WIND_CHARGE_SHOOT(1051),

    COMPOSTER_FILL(1500),
    LAVA_FIZZ(1501),
    REDSTONE_TORCH_BURNOUT(1502),
    END_PORTAL_FRAME_FILL(1503),
    DRIPSTONE_DRIP(1504),
    PARTICLES_AND_SOUND_PLANT_GROWTH(1505),

    PARTICLES_SHOOT_SMOKE(2000),
    PARTICLES_DESTROY_BLOCK(2001),
    PARTICLES_SPELL_POTION_SPLASH(2002),
    PARTICLES_EYE_OF_ENDER_DEATH(2003),
    PARTICLES_MOBBLOCK_SPAWN(2004),
    PARTICLES_DRAGON_FIREBALL_SPLASH(2006),
    PARTICLES_INSTANT_POTION_SPLASH(2007),
    PARTICLES_DRAGON_BLOCK_BREAK(2008),
    PARTICLES_WATER_EVAPORATING(2009),
    PARTICLES_SHOOT_WHITE_SMOKE(2010),
    PARTICLES_BEE_GROWTH(2011),
    PARTICLES_TURTLE_EGG_PLACEMENT(2012),
    PARTICLES_SMASH_ATTACK(2013),

    ANIMATION_END_GATEWAY_SPAWN(3000),
    ANIMATION_DRAGON_SUMMON_ROAR(3001),
    PARTICLES_ELECTRIC_SPARK(3002),
    PARTICLES_AND_SOUND_WAX_ON(3003),
    PARTICLES_WAX_OFF(3004),
    PARTICLES_SCRAPE(3005),
    PARTICLES_SCULK_CHARGE(3006),
    PARTICLES_SCULK_SHRIEK(3007),
    PARTICLES_AND_SOUND_BRUSH_BLOCK_COMPLETE(3008),
    PARTICLES_EGG_CRACK(3009),
    PARTICLES_TRIAL_SPAWNER_SPAWN(3011),
    PARTICLES_TRIAL_SPAWNER_SPAWN_MOB_AT(3012),
    PARTICLES_TRIAL_SPAWNER_DETECT_PLAYER(3013),
    ANIMATION_TRIAL_SPAWNER_EJECT_ITEM(3014),
    ANIMATION_VAULT_ACTIVATE(3015),
    ANIMATION_VAULT_DEACTIVATE(3016),
    ANIMATION_VAULT_EJECT_ITEM(3017),
    ANIMATION_SPAWN_COBWEB(3018),
    PARTICLES_TRIAL_SPAWNER_DETECT_PLAYER_OMINOUS(3019),
    PARTICLES_TRIAL_SPAWNER_BECOME_OMINOUS(3020),
    PARTICLES_TRIAL_SPAWNER_SPAWN_ITEM(3021);

    private final int id;

    @Override
    public int getId() {
        return id;
    }
}
