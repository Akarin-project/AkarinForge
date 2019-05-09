/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1;

import java.util.HashMap;

import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.CustomTimingsHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class SpigotTimings {
    public static final CustomTimingsHandler serverTickTimer = new CustomTimingsHandler("** Full Server Tick");
    public static final CustomTimingsHandler playerListTimer = new CustomTimingsHandler("Player List");
    public static final CustomTimingsHandler commandFunctionsTimer = new CustomTimingsHandler("Command Functions");
    public static final CustomTimingsHandler connectionTimer = new CustomTimingsHandler("Connection Handler");
    public static final CustomTimingsHandler tickablesTimer = new CustomTimingsHandler("Tickables");
    public static final CustomTimingsHandler schedulerTimer = new CustomTimingsHandler("Scheduler");
    public static final CustomTimingsHandler chunkIOTickTimer = new CustomTimingsHandler("ChunkIOTick");
    public static final CustomTimingsHandler timeUpdateTimer = new CustomTimingsHandler("Time Update");
    public static final CustomTimingsHandler serverCommandTimer = new CustomTimingsHandler("Server Command");
    public static final CustomTimingsHandler worldSaveTimer = new CustomTimingsHandler("World Save");
    public static final CustomTimingsHandler entityMoveTimer = new CustomTimingsHandler("** entityMove");
    public static final CustomTimingsHandler tickEntityTimer = new CustomTimingsHandler("** tickEntity");
    public static final CustomTimingsHandler activatedEntityTimer = new CustomTimingsHandler("** activatedTickEntity");
    public static final CustomTimingsHandler tickTileEntityTimer = new CustomTimingsHandler("** tickTileEntity");
    public static final CustomTimingsHandler timerEntityBaseTick = new CustomTimingsHandler("** livingEntityBaseTick");
    public static final CustomTimingsHandler timerEntityAI = new CustomTimingsHandler("** livingEntityAI");
    public static final CustomTimingsHandler timerEntityAICollision = new CustomTimingsHandler("** livingEntityAICollision");
    public static final CustomTimingsHandler timerEntityAIMove = new CustomTimingsHandler("** livingEntityAIMove");
    public static final CustomTimingsHandler timerEntityTickRest = new CustomTimingsHandler("** livingEntityTickRest");
    public static final CustomTimingsHandler processQueueTimer = new CustomTimingsHandler("processQueue");
    public static final CustomTimingsHandler schedulerSyncTimer = new CustomTimingsHandler("** Scheduler - Sync Tasks", JavaPluginLoader.pluginParentTimer);
    public static final CustomTimingsHandler playerCommandTimer = new CustomTimingsHandler("** playerCommand");
    public static final CustomTimingsHandler entityActivationCheckTimer = new CustomTimingsHandler("entityActivationCheck");
    public static final CustomTimingsHandler checkIfActiveTimer = new CustomTimingsHandler("** checkIfActive");
    public static final HashMap<String, CustomTimingsHandler> entityTypeTimingMap = new HashMap();
    public static final HashMap<String, CustomTimingsHandler> tileEntityTypeTimingMap = new HashMap();
    public static final HashMap<String, CustomTimingsHandler> pluginTaskTimingMap = new HashMap();

    public static CustomTimingsHandler getPluginTaskTimings(BukkitTask task, long period) {
        if (!task.isSync()) {
            return null;
        }
        CraftTask ctask = (CraftTask)task;
        String plugin = task.getOwner() != null ? task.getOwner().getDescription().getFullName() : (ctask.timingName != null ? "CraftScheduler" : "Unknown");
        String taskname = ctask.getTaskName();
        String name = "Task: " + plugin + " Runnable: " + taskname;
        name = period > 0 ? name + "(interval:" + period + ")" : name + "(Single)";
        CustomTimingsHandler result = pluginTaskTimingMap.get(name);
        if (result == null) {
            result = new CustomTimingsHandler(name, schedulerSyncTimer);
            pluginTaskTimingMap.put(name, result);
        }
        return result;
    }

    public static CustomTimingsHandler getEntityTimings(Entity entity) {
        String entityType = entity.getClass().getName();
        CustomTimingsHandler result = entityTypeTimingMap.get(entityType);
        if (result == null) {
            result = new CustomTimingsHandler("** tickEntity - " + entity.getClass().getSimpleName(), activatedEntityTimer);
            entityTypeTimingMap.put(entityType, result);
        }
        return result;
    }

    public static CustomTimingsHandler getTileEntityTimings(TileEntity entity) {
        String entityType = entity.getClass().getName();
        CustomTimingsHandler result = tileEntityTypeTimingMap.get(entityType);
        if (result == null) {
            result = new CustomTimingsHandler("** tickTileEntity - " + entity.getClass().getSimpleName(), tickTileEntityTimer);
            tileEntityTypeTimingMap.put(entityType, result);
        }
        return result;
    }

    public static class WorldTimingsHandler {
        public final CustomTimingsHandler mobSpawn;
        public final CustomTimingsHandler doChunkUnload;
        public final CustomTimingsHandler doPortalForcer;
        public final CustomTimingsHandler doTickPending;
        public final CustomTimingsHandler doTickTiles;
        public final CustomTimingsHandler doVillages;
        public final CustomTimingsHandler doChunkMap;
        public final CustomTimingsHandler doChunkGC;
        public final CustomTimingsHandler doSounds;
        public final CustomTimingsHandler entityTick;
        public final CustomTimingsHandler tileEntityTick;
        public final CustomTimingsHandler tileEntityPending;
        public final CustomTimingsHandler tracker;
        public final CustomTimingsHandler doTick;
        public final CustomTimingsHandler tickEntities;
        public final CustomTimingsHandler syncChunkLoadTimer;
        public final CustomTimingsHandler syncChunkLoadDataTimer;
        public final CustomTimingsHandler syncChunkLoadStructuresTimer;
        public final CustomTimingsHandler syncChunkLoadEntitiesTimer;
        public final CustomTimingsHandler syncChunkLoadTileEntitiesTimer;
        public final CustomTimingsHandler syncChunkLoadTileTicksTimer;
        public final CustomTimingsHandler syncChunkLoadPostTimer;

        public WorldTimingsHandler(WorldServer server) {
            String name = server.worldInfo.getWorldName() + " - ";
            this.mobSpawn = new CustomTimingsHandler("** " + name + "mobSpawn");
            this.doChunkUnload = new CustomTimingsHandler("** " + name + "doChunkUnload");
            this.doTickPending = new CustomTimingsHandler("** " + name + "doTickPending");
            this.doTickTiles = new CustomTimingsHandler("** " + name + "doTickTiles");
            this.doVillages = new CustomTimingsHandler("** " + name + "doVillages");
            this.doChunkMap = new CustomTimingsHandler("** " + name + "doChunkMap");
            this.doSounds = new CustomTimingsHandler("** " + name + "doSounds");
            this.doChunkGC = new CustomTimingsHandler("** " + name + "doChunkGC");
            this.doPortalForcer = new CustomTimingsHandler("** " + name + "doPortalForcer");
            this.entityTick = new CustomTimingsHandler("** " + name + "entityTick");
            this.tileEntityTick = new CustomTimingsHandler("** " + name + "tileEntityTick");
            this.tileEntityPending = new CustomTimingsHandler("** " + name + "tileEntityPending");
            this.syncChunkLoadTimer = new CustomTimingsHandler("** " + name + "syncChunkLoad");
            this.syncChunkLoadDataTimer = new CustomTimingsHandler("** " + name + "syncChunkLoad - Data");
            this.syncChunkLoadStructuresTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Structures");
            this.syncChunkLoadEntitiesTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Entities");
            this.syncChunkLoadTileEntitiesTimer = new CustomTimingsHandler("** " + name + "chunkLoad - TileEntities");
            this.syncChunkLoadTileTicksTimer = new CustomTimingsHandler("** " + name + "chunkLoad - TileTicks");
            this.syncChunkLoadPostTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Post");
            this.tracker = new CustomTimingsHandler(name + "tracker");
            this.doTick = new CustomTimingsHandler(name + "doTick");
            this.tickEntities = new CustomTimingsHandler(name + "tickEntities");
        }
    }

}

