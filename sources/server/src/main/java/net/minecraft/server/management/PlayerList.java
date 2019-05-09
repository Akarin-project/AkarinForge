package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import io.akarin.forge.PlayerDataFixer;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

public abstract class PlayerList
{
    public static final File FILE_PLAYERBANS = new File("banned-players.json");
    public static final File FILE_IPBANS = new File("banned-ips.json");
    public static final File FILE_OPS = new File("ops.json");
    public static final File FILE_WHITELIST = new File("whitelist.json");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private final MinecraftServer mcServer;
    public final List<EntityPlayerMP> playerEntityList = Lists.<EntityPlayerMP>newCopyOnWriteArrayList(); // Akarin
    private final Map<UUID, EntityPlayerMP> uuidToPlayerMap = Maps.<UUID, EntityPlayerMP>newHashMap();
    private final UserListBans bannedPlayers;
    private final UserListIPBans bannedIPs;
    private final UserListOps ops;
    private final UserListWhitelist whiteListedPlayers;
    private final Map<UUID, StatisticsManagerServer> playerStatFiles;
    private final Map<UUID, PlayerAdvancements> advancements;
    public IPlayerFileData playerDataManager; // Akarin
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;
    // Akarin start
    private CraftServer cserver;
    // Akarin end

    public PlayerList(MinecraftServer server)
    {
        this.cserver = server.server = new CraftServer(server, this);
        
        server.console = new com.destroystokyo.paper.console.TerminalConsoleCommandSender(); // Paper
        // CraftBukkit end
        this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
        this.bannedIPs = new UserListIPBans(FILE_IPBANS);
        this.ops = new UserListOps(FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
        this.playerStatFiles = Maps.<UUID, StatisticsManagerServer>newHashMap();
        this.advancements = Maps.<UUID, PlayerAdvancements>newHashMap();
        this.mcServer = server;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }

    public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn, NetHandlerPlayServer nethandlerplayserver)
    {
        GameProfile gameprofile = playerIn.getGameProfile();
        PlayerProfileCache playerprofilecache = this.mcServer.getPlayerProfileCache();
        GameProfile gameprofile1 = playerprofilecache.getProfileByUUID(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();
        playerprofilecache.addEntry(gameprofile);
        NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
        // CraftBukkit start - Better rename detection
        if (nbttagcompound != null && nbttagcompound.hasKey("bukkit")) {
            NBTTagCompound bukkit = nbttagcompound.getCompoundTag("bukkit");
            s = bukkit.hasKey("lastKnownName", 8) ? bukkit.getString("lastKnownName") : s;
        }
        // CraftBukkit end
        playerIn.setWorld(this.mcServer.getWorld(playerIn.dimension));

        World playerWorld = this.mcServer.getWorld(playerIn.dimension);
        if (playerWorld == null)
        {
            playerIn.dimension = 0;
            playerWorld = this.mcServer.getWorld(0);
            BlockPos spawnPoint = playerWorld.provider.getRandomizedSpawnPoint();
            playerIn.setPosition(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        }

        playerIn.setWorld(playerWorld);
        playerIn.interactionManager.setWorld((WorldServer)playerIn.world);
        String s1 = "local";

        if (netManager.getRemoteAddress() != null)
        {
            s1 = netManager.getRemoteAddress().toString();
        }

        PlayerDataFixer.checkLocation(playerIn); // Akarin
        //LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", playerIn.getName(), s1, Integer.valueOf(playerIn.getEntityId()), Double.valueOf(playerIn.posX), Double.valueOf(playerIn.posY), Double.valueOf(playerIn.posZ)); // Akarin
        WorldServer worldserver = this.mcServer.getWorld(playerIn.dimension);
        WorldInfo worldinfo = worldserver.getWorldInfo();
        this.setPlayerGameTypeBasedOnOther(playerIn, (EntityPlayerMP)null, worldserver);
        playerIn.connection = nethandlerplayserver;
        net.minecraftforge.fml.common.FMLCommonHandler.instance().fireServerConnectionEvent(netManager);
        nethandlerplayserver.sendPacket(new SPacketJoinGame(playerIn.getEntityId(), playerIn.interactionManager.getGameType(), worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimension(), worldserver.getDifficulty(), this.getMaxPlayers(), worldinfo.getTerrainType(), worldserver.getGameRules().getBoolean("reducedDebugInfo")));
        playerIn.getBukkitEntity().sendSupportedChannels(); // CraftBukkit
        nethandlerplayserver.sendPacket(new SPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(this.getServerInstance().getServerModName())));
        nethandlerplayserver.sendPacket(new SPacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        nethandlerplayserver.sendPacket(new SPacketPlayerAbilities(playerIn.capabilities));
        nethandlerplayserver.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
        this.updatePermissionLevel(playerIn);
        playerIn.getStatFile().markAllDirty();
        playerIn.getRecipeBook().init(playerIn);
        this.sendScoreboard((ServerScoreboard)worldserver.getScoreboard(), playerIn);
        this.mcServer.refreshStatusNextTick();
        // Akarin start
        String joinMessage = playerIn.getName().equalsIgnoreCase(s) ? "\u00a7e" + I18n.translateToLocalFormatted("multiplayer.player.joined", playerIn.getDisplayNameString()) : "\u00a7e" + I18n.translateToLocalFormatted("multiplayer.player.joined.renamed", playerIn.getDisplayName(), s);
        this.playerLoggedIn(playerIn, joinMessage);
        /*
        TextComponentTranslation textcomponenttranslation;

        if (playerIn.getName().equalsIgnoreCase(s))
        {
            textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined", new Object[] {playerIn.getDisplayName()});
        }
        else
        {
            textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined.renamed", new Object[] {playerIn.getDisplayName(), s});
        }

        textcomponenttranslation.getStyle().setColor(TextFormatting.YELLOW);
        this.sendMessage(textcomponenttranslation);
        this.playerLoggedIn(playerIn);
        */
        // Akarin end
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver);

        if (!this.mcServer.getResourcePackUrl().isEmpty())
        {
            playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }

        for (PotionEffect potioneffect : playerIn.getActivePotionEffects())
        {
            nethandlerplayserver.sendPacket(new SPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }

        if (nbttagcompound != null && nbttagcompound.hasKey("RootVehicle", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("RootVehicle");
            Entity entity1 = AnvilChunkLoader.readWorldEntity(nbttagcompound1.getCompoundTag("Entity"), worldserver, true);

            if (entity1 != null)
            {
                UUID uuid = nbttagcompound1.getUniqueId("Attach");

                if (entity1.getUniqueID().equals(uuid))
                {
                    playerIn.startRiding(entity1, true);
                }
                else
                {
                    for (Entity entity : entity1.getRecursivePassengers())
                    {
                        if (entity.getUniqueID().equals(uuid))
                        {
                            playerIn.startRiding(entity, true);
                            break;
                        }
                    }
                }

                if (!playerIn.isRiding())
                {
                    LOGGER.warn("Couldn't reattach entity to player");
                    worldserver.removeEntityDangerously(entity1);

                    for (Entity entity2 : entity1.getRecursivePassengers())
                    {
                        worldserver.removeEntityDangerously(entity2);
                    }
                }
            }
        }

        playerIn.addSelfToInternalCraftingInventory();
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerLoggedIn(playerIn);
        PlayerList.LOGGER.info(playerIn.getName() + "[" + s1 + "] logged in with entity id " + playerIn.getEntityId() + " at ([" + playerIn.world.worldInfo.getWorldName() + "]" + playerIn.posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")"); // Akarin
    }

    public void sendScoreboard(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn) // Akarin
    {
        Set<ScoreObjective> set = Sets.<ScoreObjective>newHashSet();

        for (ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams())
        {
            playerIn.connection.sendPacket(new SPacketTeams(scoreplayerteam, 0));
        }

        for (int i = 0; i < 19; ++i)
        {
            ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);

            if (scoreobjective != null && !set.contains(scoreobjective))
            {
                for (Packet<?> packet : scoreboardIn.getCreatePackets(scoreobjective))
                {
                    playerIn.connection.sendPacket(packet);
                }

                set.add(scoreobjective);
            }
        }
    }

    public void setPlayerManager(WorldServer[] worldServers)
    {
        if (playerDataManager != null) return; // CraftBukkit
        this.playerDataManager = worldServers[0].getSaveHandler().getPlayerNBTManager();
        worldServers[0].getWorldBorder().addListener(new IBorderListener()
        {
            public void onSizeChanged(WorldBorder border, double newSize)
            {
                PlayerList.this.sendAll(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_SIZE), border.world); // Akarin
            }
            public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time)
            {
                PlayerList.this.sendAll(new SPacketWorldBorder(border, SPacketWorldBorder.Action.LERP_SIZE), border.world); // Akarin
            }
            public void onCenterChanged(WorldBorder border, double x, double z)
            {
                PlayerList.this.sendAll(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_CENTER), border.world); // Akarin
            }
            public void onWarningTimeChanged(WorldBorder border, int newTime)
            {
                PlayerList.this.sendAll(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_WARNING_TIME), border.world); // Akarin
            }
            public void onWarningDistanceChanged(WorldBorder border, int newDistance)
            {
                PlayerList.this.sendAll(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_WARNING_BLOCKS), border.world); // Akarin
            }
            public void onDamageAmountChanged(WorldBorder border, double newAmount)
            {
            }
            public void onDamageBufferChanged(WorldBorder border, double newSize)
            {
            }
        });
    }

    public void preparePlayer(EntityPlayerMP playerIn, @Nullable WorldServer worldIn)
    {
        WorldServer worldserver = playerIn.getServerWorld();

        if (worldIn != null)
        {
            worldIn.getPlayerChunkMap().removePlayer(playerIn);
        }

        worldserver.getPlayerChunkMap().addPlayer(playerIn);
        worldserver.getChunkProvider().provideChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);

        if (worldIn != null)
        {
            CriteriaTriggers.CHANGED_DIMENSION.trigger(playerIn, worldIn.provider.getDimensionType(), worldserver.provider.getDimensionType());

            if (worldIn.provider.getDimensionType() == DimensionType.NETHER && playerIn.world.provider.getDimensionType() == DimensionType.OVERWORLD && playerIn.getEnteredNetherPosition() != null)
            {
                CriteriaTriggers.NETHER_TRAVEL.trigger(playerIn, playerIn.getEnteredNetherPosition());
            }
        }
    }

    public int getEntityViewDistance()
    {
        return PlayerChunkMap.getFurthestViewableBlock(this.getViewDistance());
    }

    @Nullable
    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn)
    {
        NBTTagCompound nbttagcompound = this.mcServer.worlds[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound nbttagcompound1;

        if (playerIn.getName().equals(this.mcServer.getServerOwner()) && nbttagcompound != null)
        {
            nbttagcompound1 = nbttagcompound;
            playerIn.readFromNBT(nbttagcompound);
            LOGGER.debug("loading single player");
            net.minecraftforge.event.ForgeEventFactory.firePlayerLoadingEvent(playerIn, this.playerDataManager, playerIn.getUniqueID().toString());
        }
        else
        {
            nbttagcompound1 = this.playerDataManager.readPlayerData(playerIn);
        }

        return nbttagcompound1;
    }

    public NBTTagCompound getPlayerNBT(EntityPlayerMP player)
    {
        // Hacky method to allow loading the NBT for a player prior to login
        NBTTagCompound nbttagcompound = this.mcServer.worlds[0].getWorldInfo().getPlayerNBTTagCompound();
        if (player.getName().equals(this.mcServer.getServerOwner()) && nbttagcompound != null)
        {
            return nbttagcompound;
        }
        else
        {
            return ((net.minecraft.world.storage.SaveHandler)this.playerDataManager).getPlayerNBT(player);
        }
    }

    protected void writePlayerData(EntityPlayerMP playerIn)
    {
        if (playerIn.connection == null) return;

        this.playerDataManager.writePlayerData(playerIn);
        StatisticsManagerServer statisticsmanagerserver = this.playerStatFiles.get(playerIn.getUniqueID());

        if (statisticsmanagerserver != null)
        {
            statisticsmanagerserver.saveStatFile();
        }

        PlayerAdvancements playeradvancements = this.advancements.get(playerIn.getUniqueID());

        if (playeradvancements != null)
        {
            playeradvancements.save();
        }
    }

    public void playerLoggedIn(EntityPlayerMP playerIn)
    {
        this.playerEntityList.add(playerIn);
        this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
        this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] {playerIn})); // Akarin
        WorldServer worldserver = this.mcServer.getWorld(playerIn.dimension);

        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            playerIn.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] {this.playerEntityList.get(i)}));
        }

        net.minecraftforge.common.chunkio.ChunkIOExecutor.adjustPoolSize(this.getCurrentPlayerCount());
        worldserver.spawnEntity(playerIn);
        this.preparePlayer(playerIn, (WorldServer)null);
    }

    // Akarin start
    public void playerLoggedIn(EntityPlayerMP playerIn, String joinMessage)
    {
        this.playerEntityList.add(playerIn);
        this.uuidToPlayerMap.put(playerIn.getUniqueID(), playerIn);
        WorldServer worldserver = this.mcServer.getWorld(playerIn.dimension);
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(cserver.getPlayer(playerIn), joinMessage);
        cserver.getPluginManager().callEvent(playerJoinEvent);

        if (!playerIn.connection.netManager.isChannelOpen()) {
            return;
        }

        joinMessage = playerJoinEvent.getJoinMessage();

        if (joinMessage != null && joinMessage.length() > 0) {
            for (ITextComponent line : org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage.fromString(joinMessage)) {
                mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChat(line));
            }
        }

        ChunkIOExecutor.adjustPoolSize(getCurrentPlayerCount());
        SPacketPlayerListItem packet = new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, playerIn);

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayerMP entityplayer1 = this.playerEntityList.get(i);

            if (entityplayer1.getBukkitEntity().canSee(playerIn.getBukkitEntity())) {
                entityplayer1.connection.sendPacket(packet);
            }

            if (!playerIn.getBukkitEntity().canSee(entityplayer1.getBukkitEntity())) {
                continue;
            }

            playerIn.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] { entityplayer1}));
        }
        playerIn.sentListPacket = true;
        
        playerIn.connection.sendPacket(new SPacketEntityMetadata(playerIn.getEntityId(), playerIn.dataManager, true));
        net.minecraftforge.common.chunkio.ChunkIOExecutor.adjustPoolSize(this.getCurrentPlayerCount());
        
        if (playerIn.world == worldserver && !worldserver.playerEntities.contains(playerIn)) {
            worldserver.spawnEntity(playerIn);
            this.preparePlayer(playerIn, (WorldServer) null);
        }
    }
    // Akarin end

    public void serverUpdateMovingPlayer(EntityPlayerMP playerIn)
    {
        playerIn.getServerWorld().getPlayerChunkMap().updateMovingPlayer(playerIn);
    }

    public String playerLoggedOut(EntityPlayerMP playerIn) // Akarin
    {
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerLoggedOut(playerIn);
        WorldServer worldserver = playerIn.getServerWorld();
        playerIn.addStat(StatList.LEAVE_GAME);
        // CraftBukkit start - Quitting must be before we do final save of data, in case plugins need to modify it
        org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory.handleInventoryCloseEvent(playerIn);

        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(cserver.getPlayer(playerIn), "\u00a7e" + playerIn.getDisplayNameString() + " left the game");
        cserver.getPluginManager().callEvent(playerQuitEvent);
        playerIn.getBukkitEntity().disconnect(playerQuitEvent.getQuitMessage());
        
        //playerIn.onUpdateEntity();// SPIGOT-924
        // CraftBukkit end
        this.writePlayerData(playerIn);

        if (playerIn.isRiding())
        {
            Entity entity = playerIn.getLowestRidingEntity();

            if (entity.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1)
            {
                LOGGER.debug("Removing player mount");
                playerIn.dismountRidingEntity();
                worldserver.removeEntityDangerously(entity);

                for (Entity entity1 : entity.getRecursivePassengers())
                {
                    worldserver.removeEntityDangerously(entity1);
                }

                worldserver.getChunkFromChunkCoords(playerIn.chunkCoordX, playerIn.chunkCoordZ).markDirty();
            }
        }
        net.minecraftforge.common.chunkio.ChunkIOExecutor.adjustPoolSize(this.getCurrentPlayerCount());

        worldserver.removeEntity(playerIn);
        worldserver.getPlayerChunkMap().removePlayer(playerIn);
        playerIn.getAdvancements().dispose();
        this.playerEntityList.remove(playerIn);
        UUID uuid = playerIn.getUniqueID();
        EntityPlayerMP entityplayermp = this.uuidToPlayerMap.get(uuid);

        if (entityplayermp == playerIn)
        {
            this.uuidToPlayerMap.remove(uuid);
            this.playerStatFiles.remove(uuid);
            this.advancements.remove(uuid);
        }

        // Akarin start
        //  this.sendAll(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { entityplayer}));
        SPacketPlayerListItem packet = new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, playerIn);
        for (int i = 0; i < playerEntityList.size(); i++) {
            EntityPlayerMP entityplayer2 = this.playerEntityList.get(i);

            if (entityplayer2.getBukkitEntity().canSee(playerIn.getBukkitEntity())) {
                entityplayer2.connection.sendPacket(packet);
            } else {
                entityplayer2.getBukkitEntity().removeDisconnectingPlayer(playerIn.getBukkitEntity());
            }
        }
        // This removes the scoreboard (and player reference) for the specific player in the manager
        cserver.getScoreboardManager().removePlayer(playerIn.getBukkitEntity());

        ChunkIOExecutor.adjustPoolSize(this.getCurrentPlayerCount()); // CraftBukkit

        return playerQuitEvent.getQuitMessage(); // CraftBukkit
    }

    public EntityPlayerMP allowUserToConnect(NetHandlerLoginServer loginlistener, GameProfile gameprofile, String hostname) {
        // Moved from processLogin
        UUID uuid = EntityPlayer.getUUID(gameprofile);
        ArrayList arraylist = Lists.newArrayList();

        EntityPlayerMP entityplayer;

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            entityplayer = this.playerEntityList.get(i);
            if (entityplayer.getUniqueID().equals(uuid)) {
                arraylist.add(entityplayer);
            }
        }

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayerMP) iterator.next();
            writePlayerData(entityplayer);
            entityplayer.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.duplicate_login"));
        }

        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome.
        SocketAddress socketaddress = loginlistener.networkManager.getRemoteAddress();

        EntityPlayerMP entity = new EntityPlayerMP(mcServer, mcServer.getWorld(0), gameprofile, new PlayerInteractionManager(mcServer.getWorld(0)));
        Player player = entity.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(player, hostname, ((java.net.InetSocketAddress) socketaddress).getAddress(), ((java.net.InetSocketAddress) loginlistener.networkManager.getRawAddress()).getAddress());
        String s;

        if (getBannedPlayers().isBanned(gameprofile) && !getBannedPlayers().getEntry(gameprofile).hasBanExpired()) {
            UserListBansEntry gameprofilebanentry = this.bannedPlayers.getEntry(gameprofile);

            s = "You are banned from this server!\nReason: " + gameprofilebanentry.getBanReason();
            if (gameprofilebanentry.banEndDate != null) {
                s = s + "\nYour ban will be removed on " + DATE_FORMAT.format(gameprofilebanentry.getBanEndDate());
            }
            
            if (!gameprofilebanentry.hasBanExpired())
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s); // Spigot
        } else if (!this.canJoin(gameprofile)) { // Paper
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, org.spigotmc.SpigotConfig.whitelistMessage);
        } else if (getBannedIPs().isBanned(socketaddress) && !getBannedIPs().getBanEntry(socketaddress).hasBanExpired()) {
            UserListIPBansEntry ipbanentry = this.bannedIPs.getBanEntry(socketaddress);

            s = "Your IP address is banned from this server!\nReason: " + ipbanentry.getBanReason();
            if (ipbanentry.banEndDate != null) {
                s = s + "\nYour ban will be removed on " + DATE_FORMAT.format(ipbanentry.getBanEndDate());
            }
            
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s);
        } else {
            if (this.playerEntityList.size() >= this.maxPlayers && !this.bypassesPlayerLimit(gameprofile)) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, org.spigotmc.SpigotConfig.serverFullMessage); // Spigot
            }
        }

        cserver.getPluginManager().callEvent(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            loginlistener.disconnect(event.getKickMessage());
            return null;
        }
        return entity;
    }
    
    public EntityPlayerMP processLogin(GameProfile profile, EntityPlayerMP player) {
        return player;
    }
    // Akarin end

    public String allowUserToConnect(SocketAddress address, GameProfile profile)
    {
        if (this.bannedPlayers.isBanned(profile))
        {
            UserListBansEntry userlistbansentry = (UserListBansEntry)this.bannedPlayers.getEntry(profile);
            String s1 = "You are banned from this server!\nReason: " + userlistbansentry.getBanReason();

            if (userlistbansentry.getBanEndDate() != null)
            {
                s1 = s1 + "\nYour ban will be removed on " + DATE_FORMAT.format(userlistbansentry.getBanEndDate());
            }

            return s1;
        }
        else if (!this.canJoin(profile))
        {
            return "You are not white-listed on this server!";
        }
        else if (this.bannedIPs.isBanned(address))
        {
            UserListIPBansEntry userlistipbansentry = this.bannedIPs.getBanEntry(address);
            String s = "Your IP address is banned from this server!\nReason: " + userlistipbansentry.getBanReason();

            if (userlistipbansentry.getBanEndDate() != null)
            {
                s = s + "\nYour ban will be removed on " + DATE_FORMAT.format(userlistipbansentry.getBanEndDate());
            }

            return s;
        }
        else
        {
            return this.playerEntityList.size() >= this.maxPlayers && !this.bypassesPlayerLimit(profile) ? "The server is full!" : null;
        }
    }

    public EntityPlayerMP createPlayerForUser(GameProfile profile)
    {
        UUID uuid = EntityPlayer.getUUID(profile);
        List<EntityPlayerMP> list = Lists.<EntityPlayerMP>newArrayList();

        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);

            if (entityplayermp.getUniqueID().equals(uuid))
            {
                list.add(entityplayermp);
            }
        }

        EntityPlayerMP entityplayermp2 = this.uuidToPlayerMap.get(profile.getId());

        if (entityplayermp2 != null && !list.contains(entityplayermp2))
        {
            list.add(entityplayermp2);
        }

        for (EntityPlayerMP entityplayermp1 : list)
        {
            entityplayermp1.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.duplicate_login", new Object[0]));
        }

        PlayerInteractionManager playerinteractionmanager;

        if (this.mcServer.isDemo())
        {
            playerinteractionmanager = new DemoPlayerInteractionManager(this.mcServer.getWorld(0));
        }
        else
        {
            playerinteractionmanager = new PlayerInteractionManager(this.mcServer.getWorld(0));
        }

        return new EntityPlayerMP(this.mcServer, this.mcServer.getWorld(0), profile, playerinteractionmanager);
    }

    // Akarin start
    public EntityPlayerMP moveToWorld(EntityPlayerMP entityplayer, int i, boolean flag, Location location, boolean avoidSuffocation) {
        entityplayer.dismountRidingEntity();
        entityplayer.getServerWorld().getEntityTracker().removePlayerFromTrackers(entityplayer);
        entityplayer.getServerWorld().getPlayerChunkMap().removePlayer(entityplayer);
        this.playerEntityList.remove(entityplayer);
        this.mcServer.getWorld(entityplayer.dimension).removeEntityDangerously(entityplayer);
        BlockPos blockposition = entityplayer.getBedLocation();
        boolean flag1 = entityplayer.isSpawnForced();

        EntityPlayerMP entityplayer1 = entityplayer;
        org.bukkit.World fromWorld = entityplayer.getBukkitEntity().getWorld();
        entityplayer.queuedEndExit = false;

        entityplayer1.connection = entityplayer.connection;
        entityplayer1.copyFrom(entityplayer, flag);
        entityplayer1.setEntityId(entityplayer.getEntityId());
        entityplayer1.setCommandStats(entityplayer);
        entityplayer1.setPrimaryHand(entityplayer.getPrimaryHand());
        Iterator iterator = entityplayer.getTags().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            entityplayer1.addTag(s);
        }

        BlockPos blockposition1;

        if (location == null) {
            boolean isBedSpawn = false;
            CraftWorld cworld = (CraftWorld) this.mcServer.server.getWorld(entityplayer.spawnWorld);
            if (cworld != null && blockposition != null) {
                blockposition1 = EntityPlayer.getBedSpawnLocation(cworld.getHandle(), blockposition, flag1);
                if (blockposition1 != null) {
                    isBedSpawn = true;
                    location = new Location(cworld, blockposition1.getX() + 0.5F, blockposition1.getY() + 0.1F, blockposition1.getZ() + 0.5F);
                } else {
                    entityplayer1.setSpawnPoint(null, true);
                    entityplayer1.connection.sendPacket(new SPacketChangeGameState(0, 0.0F));
                }
            }

            if (location == null) {
                cworld = (CraftWorld) this.mcServer.server.getWorlds().get(0);
                blockposition = entityplayer1.getSpawnPoint(this.mcServer, cworld.getHandle());
                location = new Location(cworld, blockposition.getX() + 0.5F, blockposition.getY() + 0.1F, blockposition.getZ() + 0.5F);
            }

            Player respawnPlayer = cserver.getPlayer(entityplayer1);
            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, location, isBedSpawn);
            cserver.getPluginManager().callEvent(respawnEvent);
            
            if (entityplayer.connection.isDisconnected()) {
                return entityplayer;
            }

            location = respawnEvent.getRespawnLocation();
            entityplayer.reset();
        } else {
            location.setWorld(mcServer.getWorld(i).getWorld());
        }
        WorldServer worldserver = ((CraftWorld) location.getWorld()).getHandle();
        entityplayer1.forceSetPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        worldserver.getChunkProvider().provideChunk((int) entityplayer1.posX >> 4, (int) entityplayer1.posZ >> 4);

        while (avoidSuffocation && !worldserver.getCollisionBoxes(entityplayer1, entityplayer1.getEntityBoundingBox()).isEmpty() && entityplayer1.posY < 256.0D) {
            entityplayer1.setPosition(entityplayer1.posX, entityplayer1.posY + 1.0D, entityplayer1.posZ);
        }
        byte actualDimension = (byte) (worldserver.getWorld().getEnvironment().getId());
        // Force the client to refresh their chunk cache
        if (fromWorld.getEnvironment() == worldserver.getWorld().getEnvironment()) {
            entityplayer1.connection.sendPacket(new SPacketRespawn((byte) (actualDimension >= 0 ? -1 : 0), worldserver.getDifficulty(), worldserver.getWorldInfo().getTerrainType(), entityplayer.interactionManager.getGameType()));
        }

        entityplayer1.connection.sendPacket(new SPacketRespawn(actualDimension, worldserver.getDifficulty(), worldserver.getWorldInfo().getTerrainType(), entityplayer1.interactionManager.getGameType()));
        entityplayer1.setWorld(worldserver);
        entityplayer1.isDead = false;
        entityplayer1.connection.teleport(new Location(worldserver.getWorld(), entityplayer1.posX, entityplayer1.posY, entityplayer1.posZ, entityplayer1.rotationYaw, entityplayer1.rotationPitch));
        entityplayer1.setSneaking(false);
        blockposition1 = worldserver.getSpawnPoint();
        entityplayer1.connection.sendPacket(new SPacketSpawnPosition(blockposition1));
        entityplayer1.connection.sendPacket(new SPacketSetExperience(entityplayer1.experience, entityplayer1.experienceTotal, entityplayer1.experienceLevel));
        this.updateTimeAndWeatherForPlayer(entityplayer1, worldserver);
        this.updatePermissionLevel(entityplayer1);
        if (!entityplayer.connection.isDisconnected()) {
            worldserver.getPlayerChunkMap().addPlayer(entityplayer1);
            worldserver.spawnEntity(entityplayer1);
            this.playerEntityList.add(entityplayer1);
            this.uuidToPlayerMap.put(entityplayer1.getUniqueID(), entityplayer1);
        }
        
        entityplayer1.setHealth(entityplayer1.getHealth());
        // Added from changeDimension
        syncPlayerInventory(entityplayer); // Update health, etc...
        entityplayer.sendPlayerAbilities();
        for (Object o1 : entityplayer.getActivePotionEffects()) {
            PotionEffect mobEffect = (PotionEffect) o1;
            entityplayer.connection.sendPacket(new SPacketEntityEffect(entityplayer.getEntityId(), mobEffect));
        }

        // Fire advancement trigger
        CriteriaTriggers.CHANGED_DIMENSION.trigger(entityplayer, ((CraftWorld) fromWorld).getHandle().provider.getDimensionType(), worldserver.provider.getDimensionType());
        if (((CraftWorld) fromWorld).getHandle().provider.getDimensionType() == DimensionType.NETHER && worldserver.provider.getDimensionType() == DimensionType.OVERWORLD && entityplayer.getEnteredNetherPosition() != null) {
            CriteriaTriggers.NETHER_TRAVEL.trigger(entityplayer, entityplayer.getEnteredNetherPosition());
        }

        // Don't fire on respawn
        if (fromWorld != location.getWorld()) {
            PlayerChangedWorldEvent event = new PlayerChangedWorldEvent(entityplayer.getBukkitEntity(), fromWorld);
            mcServer.server.getPluginManager().callEvent(event);
        }

        // Save player file again if they were disconnected
        if (entityplayer.connection.isDisconnected()) {
            this.writePlayerData(entityplayer);
        }
        // CraftBukkit end
        return entityplayer1;
    }
    
    public void changeDimension(EntityPlayerMP entityplayer, int i, TeleportCause cause) {
        WorldServer exitWorld = null;
        if (entityplayer.dimension < CraftWorld.CUSTOM_DIMENSION_OFFSET) { // plugins must specify exit from custom Bukkit worlds
            // only target existing worlds (compensate for allow-nether/allow-end as false)
            for (WorldServer world : this.mcServer.worlds) {
                if (world.dimension == i) {
                    exitWorld = world;
                }
            }
        }

        Location enter = entityplayer.getBukkitEntity().getLocation();
        Location exit = null;
        boolean useTravelAgent = false; // don't use agent for custom worlds or return from THE_END
        if (exitWorld != null) {
            if ((cause == TeleportCause.END_PORTAL) && (i == 0)) {
                // THE_END -> NORMAL; use bed if available, otherwise default spawn
                exit = entityplayer.getBukkitEntity().getBedSpawnLocation();
                if (exit == null || ((CraftWorld) exit.getWorld()).getHandle().dimension != 0) {
                    BlockPos randomSpawn = entityplayer.getSpawnPoint(mcServer, exitWorld);
                    exit = new Location(exitWorld.getWorld(), randomSpawn.getX(), randomSpawn.getY(), randomSpawn.getZ());
                } else {
                    exit = exit.add(0.5F, 0.1F, 0.5F); // SPIGOT-3879
                }
            } else {
                // NORMAL <-> NETHER or NORMAL -> THE_END
                exit = this.calculateTarget(enter, exitWorld);
                useTravelAgent = true;
            }
        }

        TravelAgent agent = exit != null ? (TravelAgent) ((CraftWorld) exit.getWorld()).getHandle().getDefaultTeleporter() : org.bukkit.craftbukkit.v1_12_R1.CraftTravelAgent.DEFAULT; // return arbitrary TA to compensate for implementation dependent plugins
        PlayerPortalEvent event = new PlayerPortalEvent(entityplayer.getBukkitEntity(), enter, exit, agent, cause);
        event.useTravelAgent(useTravelAgent);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled() || event.getTo() == null) {
            return;
        }

        exit = event.useTravelAgent() ? event.getPortalTravelAgent().findOrCreate(event.getTo()) : event.getTo();
        if (exit == null) {
            return;
        }
        exitWorld = ((CraftWorld) exit.getWorld()).getHandle();

        org.bukkit.event.player.PlayerTeleportEvent tpEvent = new org.bukkit.event.player.PlayerTeleportEvent(entityplayer.getBukkitEntity(), enter, exit, cause);
        Bukkit.getServer().getPluginManager().callEvent(tpEvent);
        if (tpEvent.isCancelled() || tpEvent.getTo() == null) {
            return;
        }

        Vector velocity = entityplayer.getBukkitEntity().getVelocity();
        exitWorld.getDefaultTeleporter().adjustExit(entityplayer, exit, velocity);

        entityplayer.invulnerableDimensionChange = true;
        this.moveToWorld(entityplayer, exitWorld.dimension, true, exit, true); // SPIGOT-3864
        if (entityplayer.motionX != velocity.getX() || entityplayer.motionY != velocity.getY() || entityplayer.motionZ != velocity.getZ()) {
            entityplayer.getBukkitEntity().setVelocity(velocity);
        }
    }
    // Akarin end
    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd)
    {
        World world = mcServer.getWorld(dimension);
        if (world == null)
        {
            dimension = playerIn.getSpawnDimension();
        }
        else if (!world.provider.canRespawnHere())
        {
            dimension = world.provider.getRespawnDimension(playerIn);
        }
        if (mcServer.getWorld(dimension) == null) dimension = 0;

        playerIn.getServerWorld().getEntityTracker().removePlayerFromTrackers(playerIn);
        playerIn.getServerWorld().getEntityTracker().untrack(playerIn);
        playerIn.getServerWorld().getPlayerChunkMap().removePlayer(playerIn);
        this.playerEntityList.remove(playerIn);
        this.mcServer.getWorld(playerIn.dimension).removeEntityDangerously(playerIn);
        BlockPos blockpos = playerIn.getBedLocation(dimension);
        boolean flag = playerIn.isSpawnForced(dimension);
        playerIn.dimension = dimension;
        PlayerInteractionManager playerinteractionmanager;

        if (this.mcServer.isDemo())
        {
            playerinteractionmanager = new DemoPlayerInteractionManager(this.mcServer.getWorld(playerIn.dimension));
        }
        else
        {
            playerinteractionmanager = new PlayerInteractionManager(this.mcServer.getWorld(playerIn.dimension));
        }

        EntityPlayerMP entityplayermp = new EntityPlayerMP(this.mcServer, this.mcServer.getWorld(playerIn.dimension), playerIn.getGameProfile(), playerinteractionmanager);
        entityplayermp.connection = playerIn.connection;
        entityplayermp.copyFrom(playerIn, conqueredEnd);
        entityplayermp.dimension = dimension;
        entityplayermp.setEntityId(playerIn.getEntityId());
        entityplayermp.setCommandStats(playerIn);
        entityplayermp.setPrimaryHand(playerIn.getPrimaryHand());

        for (String s : playerIn.getTags())
        {
            entityplayermp.addTag(s);
        }

        WorldServer worldserver = this.mcServer.getWorld(playerIn.dimension);
        this.setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, worldserver);

        if (blockpos != null)
        {
            BlockPos blockpos1 = EntityPlayer.getBedSpawnLocation(this.mcServer.getWorld(playerIn.dimension), blockpos, flag);

            if (blockpos1 != null)
            {
                entityplayermp.setLocationAndAngles((double)((float)blockpos1.getX() + 0.5F), (double)((float)blockpos1.getY() + 0.1F), (double)((float)blockpos1.getZ() + 0.5F), 0.0F, 0.0F);
                entityplayermp.setSpawnPoint(blockpos, flag);
            }
            else
            {
                entityplayermp.connection.sendPacket(new SPacketChangeGameState(0, 0.0F));
            }
        }

        worldserver.getChunkProvider().provideChunk((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);

        while (!worldserver.getCollisionBoxes(entityplayermp, entityplayermp.getEntityBoundingBox()).isEmpty() && entityplayermp.posY < 256.0D)
        {
            entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0D, entityplayermp.posZ);
        }

        entityplayermp.connection.sendPacket(new SPacketRespawn(entityplayermp.dimension, entityplayermp.world.getDifficulty(), entityplayermp.world.getWorldInfo().getTerrainType(), entityplayermp.interactionManager.getGameType()));
        BlockPos blockpos2 = worldserver.getSpawnPoint();
        entityplayermp.connection.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        entityplayermp.connection.sendPacket(new SPacketSpawnPosition(blockpos2));
        entityplayermp.connection.sendPacket(new SPacketSetExperience(entityplayermp.experience, entityplayermp.experienceTotal, entityplayermp.experienceLevel));
        this.updateTimeAndWeatherForPlayer(entityplayermp, worldserver);
        this.updatePermissionLevel(entityplayermp);
        worldserver.getPlayerChunkMap().addPlayer(entityplayermp);
        worldserver.spawnEntity(entityplayermp);
        this.playerEntityList.add(entityplayermp);
        this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
        entityplayermp.addSelfToInternalCraftingInventory();
        entityplayermp.setHealth(entityplayermp.getHealth());
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerRespawnEvent(entityplayermp, conqueredEnd);
        return entityplayermp;
    }

    public void updatePermissionLevel(EntityPlayerMP player)
    {
        GameProfile gameprofile = player.getGameProfile();
        int i = this.canSendCommands(gameprofile) ? this.ops.getPermissionLevel(gameprofile) : 0;
        i = this.mcServer.isSinglePlayer() && this.mcServer.worlds[0].getWorldInfo().areCommandsAllowed() ? 4 : i;
        i = this.commandsAllowedForAll ? 4 : i;
        this.sendPlayerPermissionLevel(player, i);
    }

    public void changePlayerDimension(EntityPlayerMP player, int dimensionIn)
    {
        transferPlayerToDimension(player, dimensionIn, mcServer.getWorld(dimensionIn).getDefaultTeleporter());
    }

    // TODO: Remove (1.13)
    public void transferPlayerToDimension(EntityPlayerMP player, int dimensionIn, net.minecraft.world.Teleporter teleporter)
    {
        transferPlayerToDimension(player, dimensionIn, (net.minecraftforge.common.util.ITeleporter) teleporter);
    }

    public void transferPlayerToDimension(EntityPlayerMP player, int dimensionIn, net.minecraftforge.common.util.ITeleporter teleporter)
    {
        int i = player.dimension;
        WorldServer worldserver = this.mcServer.getWorld(player.dimension);
        player.dimension = dimensionIn;
        WorldServer worldserver1 = this.mcServer.getWorld(player.dimension);
        player.connection.sendPacket(new SPacketRespawn(player.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), player.interactionManager.getGameType())); // Forge: Use new dimensions information
        this.updatePermissionLevel(player);
        worldserver.removeEntityDangerously(player);
        player.isDead = false;
        this.transferEntityToWorld(player, i, worldserver, worldserver1, teleporter);
        this.preparePlayer(player, worldserver);
        player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.interactionManager.setWorld(worldserver1);
        player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
        this.updateTimeAndWeatherForPlayer(player, worldserver1);
        this.syncPlayerInventory(player);

        for (PotionEffect potioneffect : player.getActivePotionEffects())
        {
            player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        // Fix MC-88179: on non-death SPacketRespawn, also resend attributes
        net.minecraft.entity.ai.attributes.AttributeMap attributemap = (net.minecraft.entity.ai.attributes.AttributeMap) player.getAttributeMap();
        java.util.Collection<net.minecraft.entity.ai.attributes.IAttributeInstance> watchedAttribs = attributemap.getWatchedAttributes();
        if (!watchedAttribs.isEmpty()) player.connection.sendPacket(new net.minecraft.network.play.server.SPacketEntityProperties(player.getEntityId(), watchedAttribs));
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, i, dimensionIn);
    }

    public void transferEntityToWorld(Entity entityIn, int lastDimension, WorldServer oldWorldIn, WorldServer toWorldIn)
    {
        transferEntityToWorld(entityIn, lastDimension, oldWorldIn, toWorldIn, toWorldIn.getDefaultTeleporter());
    }

    // TODO: Remove (1.13)
    public void transferEntityToWorld(Entity entityIn, int lastDimension, WorldServer oldWorldIn, WorldServer toWorldIn, net.minecraft.world.Teleporter teleporter)
    {
        transferEntityToWorld(entityIn, lastDimension, oldWorldIn, toWorldIn, (net.minecraftforge.common.util.ITeleporter) teleporter);
    }

    public void transferEntityToWorld(Entity entityIn, int lastDimension, WorldServer oldWorldIn, WorldServer toWorldIn, net.minecraftforge.common.util.ITeleporter teleporter)
    {
        // Akarin start
        Location exit = calculateTarget(entityIn.getBukkitEntity().getLocation(), toWorldIn);
        repositionEntity(entityIn, exit, true);
    }

    public Location calculateTarget(Location enter, World target) {
        WorldServer worldserver = ((CraftWorld) enter.getWorld()).getHandle();
        WorldServer worldserver1 = target.getWorld().getHandle();
        int i = worldserver.dimension;

        double y = enter.getY();
        float yaw = enter.getYaw();
        float pitch = enter.getPitch();
        double d0 = enter.getX();
        double d1 = enter.getZ();
         double d2 = 8.0D;

        worldserver.profiler.startSection("moving");
        if (worldserver1.dimension == -1) {
            d0 = MathHelper.clamp(d0 / d2, worldserver1.getWorldBorder().minX()+ 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
            d1 = MathHelper.clamp(d1 / d2, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
        } else if (worldserver1.dimension == 0) {
            d0 = MathHelper.clamp(d0 * d2, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
            d1 = MathHelper.clamp(d1 * d2, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
        } else {
            BlockPos blockposition;

            if (i == 1) {
                // use default NORMAL world spawn instead of target
                worldserver1 = this.mcServer.worlds[0];
                blockposition = worldserver1.getSpawnPoint();
            } else {
                blockposition = worldserver1.getSpawnCoordinate();
            }

            d0 = blockposition.getX();
            y = blockposition.getY();
            d1 = blockposition.getZ();
        }

        worldserver.profiler.endSection();
        if (i != 1) {
            worldserver.profiler.startSection("placing");
            d0 = MathHelper.clamp((int) d0, -29999872, 29999872);
            d1 = MathHelper.clamp((int) d1, -29999872, 29999872);

            worldserver.profiler.endSection();
        }

        return new Location(worldserver1.getWorld(), d0, y, d1, yaw, pitch);
    }

    public void repositionEntity(Entity entity, Location exit, boolean portal) {
        WorldServer worldserver = (WorldServer) entity.world;
        WorldServer worldserver1 = ((CraftWorld) exit.getWorld()).getHandle();
        int i = worldserver.dimension;

        worldserver.profiler.startSection("moving");
        entity.setLocationAndAngles(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
        if (entity.isEntityAlive()) {
            worldserver.updateEntityWithOptionalForce(entity, false);
        }

        worldserver.profiler.endSection();
        if (i != 1) {
            worldserver.profiler.startSection("placing");
            if (entity.isEntityAlive()) {
                if (portal) {
                    Vector velocity = entity.getBukkitEntity().getVelocity();
                    worldserver1.getDefaultTeleporter().adjustExit(entity, exit, velocity);
                    entity.setLocationAndAngles(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
                    if (entity.motionX != velocity.getX() || entity.motionY != velocity.getY() || entity.motionZ != velocity.getZ()) {
                        entity.getBukkitEntity().setVelocity(velocity);
                    }
                }
                worldserver1.updateEntityWithOptionalForce(entity, false);
            }

            worldserver.profiler.endSection();
        }

        entity.setWorld(worldserver1);
        // Akarin end
    }

    public void onTick()
    {
        if (++this.playerPingIndex > 600)
        {
            // CraftBukkit start
            for (int i = 0; i < this.playerEntityList.size(); ++i) {
                final EntityPlayerMP target = this.playerEntityList.get(i);

                target.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_LATENCY, Iterables.filter(this.playerEntityList, new Predicate<EntityPlayerMP>() {
                    @Override
                    public boolean apply(EntityPlayerMP input) {
                        return target.getBukkitEntity().canSee(input.getBukkitEntity());
                    }
                })));
            }
            // CraftBukkit end
            this.playerPingIndex = 0;
        }
    }
    // CraftBukkit start - add a world/entity limited version
    public void sendAll(Packet packet, EntityPlayer entityhuman) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayerMP entityplayer =  this.playerEntityList.get(i);
            if (entityhuman != null && entityhuman instanceof EntityPlayerMP && !entityplayer.getBukkitEntity().canSee(((EntityPlayerMP) entityhuman).getBukkitEntity())) {
                continue;
            }
            this.playerEntityList.get(i).connection.sendPacket(packet);
        }
    }

    public void sendAll(Packet packet, World world) {
        for (int i = 0; i < world.playerEntities.size(); ++i) {
            ((EntityPlayerMP) world.playerEntities.get(i)).connection.sendPacket(packet);
        }

    }
    // CraftBukkit end

    public void sendPacketToAllPlayers(Packet<?> packetIn)
    {
        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            (this.playerEntityList.get(i)).connection.sendPacket(packetIn);
        }
    }

    public void sendPacketToAllPlayersInDimension(Packet<?> packetIn, int dimension)
    {
        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);

            if (entityplayermp.dimension == dimension)
            {
                entityplayermp.connection.sendPacket(packetIn);
            }
        }
    }

    public void sendMessageToAllTeamMembers(EntityPlayer player, ITextComponent message)
    {
        Team team = player.getTeam();

        if (team != null)
        {
            for (String s : team.getMembershipCollection())
            {
                EntityPlayerMP entityplayermp = this.getPlayerByUsername(s);

                if (entityplayermp != null && entityplayermp != player)
                {
                    entityplayermp.sendMessage(message);
                }
            }
        }
    }

    public void sendMessageToTeamOrAllPlayers(EntityPlayer player, ITextComponent message)
    {
        Team team = player.getTeam();

        if (team == null)
        {
            this.sendMessage(message);
        }
        else
        {
            for (int i = 0; i < this.playerEntityList.size(); ++i)
            {
                EntityPlayerMP entityplayermp = this.playerEntityList.get(i);

                if (entityplayermp.getTeam() != team)
                {
                    entityplayermp.sendMessage(message);
                }
            }
        }
    }

    public String getFormattedListOfPlayers(boolean includeUUIDs)
    {
        String s = "";
        List<EntityPlayerMP> list = Lists.newArrayList(this.playerEntityList);

        for (int i = 0; i < list.size(); ++i)
        {
            if (i > 0)
            {
                s = s + ", ";
            }

            s = s + ((EntityPlayerMP)list.get(i)).getName();

            if (includeUUIDs)
            {
                s = s + " (" + ((EntityPlayerMP)list.get(i)).getCachedUniqueIdString() + ")";
            }
        }

        return s;
    }

    public String[] getOnlinePlayerNames()
    {
        String[] astring = new String[this.playerEntityList.size()];

        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            astring[i] = ((EntityPlayerMP)this.playerEntityList.get(i)).getName();
        }

        return astring;
    }

    public GameProfile[] getOnlinePlayerProfiles()
    {
        GameProfile[] agameprofile = new GameProfile[this.playerEntityList.size()];

        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            agameprofile[i] = ((EntityPlayerMP)this.playerEntityList.get(i)).getGameProfile();
        }

        return agameprofile;
    }

    public UserListBans getBannedPlayers()
    {
        return this.bannedPlayers;
    }

    public UserListIPBans getBannedIPs()
    {
        return this.bannedIPs;
    }

    public void addOp(GameProfile profile)
    {
        int i = this.mcServer.getOpPermissionLevel();
        this.ops.addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel(), this.ops.bypassesPlayerLimit(profile)));
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(profile.getId()), i);
        // CraftBukkit start
        Player player = mcServer.server.getPlayer(profile.getId());
        if (player != null) {
           player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    public void removeOp(GameProfile profile)
    {
        this.ops.removeEntry(profile);
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(profile.getId()), 0);
        // CraftBukkit start
        Player player = mcServer.server.getPlayer(profile.getId());
        if (player != null) {
            player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    private void sendPlayerPermissionLevel(EntityPlayerMP player, int permLevel)
    {
        if (player != null && player.connection != null)
        {
            byte b0;

            if (permLevel <= 0)
            {
                b0 = 24;
            }
            else if (permLevel >= 4)
            {
                b0 = 28;
            }
            else
            {
                b0 = (byte)(24 + permLevel);
            }

            player.connection.sendPacket(new SPacketEntityStatus(player, b0));
        }
    }

    public boolean canJoin(GameProfile profile)
    {
        return !this.whiteListEnforced || this.ops.hasEntry(profile) || this.whiteListedPlayers.hasEntry(profile);
    }

    public boolean canSendCommands(GameProfile profile)
    {
        return this.ops.hasEntry(profile) || this.mcServer.isSinglePlayer() && this.mcServer.worlds[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName()) || this.commandsAllowedForAll;
    }

    @Nullable
    public EntityPlayerMP getPlayerByUsername(String username)
    {
        for (EntityPlayerMP entityplayermp : this.playerEntityList)
        {
            if (entityplayermp.getName().equalsIgnoreCase(username))
            {
                return entityplayermp;
            }
        }

        return null;
    }

    public void sendToAllNearExcept(@Nullable EntityPlayer except, double x, double y, double z, double radius, int dimension, Packet<?> packetIn)
    {
        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            EntityPlayerMP entityplayermp = this.playerEntityList.get(i);
            // CraftBukkit start - Test if player receiving packet can see the source of the packet
            if (except != null && entityplayermp != null && entityplayermp instanceof EntityPlayerMP && !((CraftPlayer) entityplayermp.getBukkitEntity()).canSee(((EntityPlayerMP) except).getBukkitEntity())) {
               continue;
            }
            // CraftBukkit end

            if (entityplayermp != except && entityplayermp.dimension == dimension)
            {
                double d0 = x - entityplayermp.posX;
                double d1 = y - entityplayermp.posY;
                double d2 = z - entityplayermp.posZ;

                if (d0 * d0 + d1 * d1 + d2 * d2 < radius * radius)
                {
                    entityplayermp.connection.sendPacket(packetIn);
                }
            }
        }
    }

    public void saveAllPlayerData()
    {
        for (int i = 0; i < this.playerEntityList.size(); ++i)
        {
            this.writePlayerData(this.playerEntityList.get(i));
        }
    }

    public void addWhitelistedPlayer(GameProfile profile)
    {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(profile));
    }

    public void removePlayerFromWhitelist(GameProfile profile)
    {
        this.whiteListedPlayers.removeEntry(profile);
    }

    public UserListWhitelist getWhitelistedPlayers()
    {
        return this.whiteListedPlayers;
    }

    public String[] getWhitelistedPlayerNames()
    {
        return this.whiteListedPlayers.getKeys();
    }

    public UserListOps getOppedPlayers()
    {
        return this.ops;
    }

    public String[] getOppedPlayerNames()
    {
        return this.ops.getKeys();
    }

    public void reloadWhitelist()
    {
    }

    public void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn)
    {
        WorldBorder worldborder = this.mcServer.worlds[0].getWorldBorder();
        playerIn.connection.sendPacket(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.INITIALIZE));
        playerIn.connection.sendPacket(new SPacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
        BlockPos blockpos = worldIn.getSpawnPoint();
        playerIn.connection.sendPacket(new SPacketSpawnPosition(blockpos));

        if (worldIn.isRaining())
        {
            // CraftBukkit start - handle player weather
            playerIn.setPlayerWeather(org.bukkit.WeatherType.DOWNFALL, false);
            playerIn.updateWeather(-worldIn.rainingStrength, worldIn.rainingStrength, -worldIn.thunderingStrength, worldIn.thunderingStrength);
            // CraftBukkit end
        }
    }

    public void syncPlayerInventory(EntityPlayerMP playerIn)
    {
        playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
        playerIn.getBukkitEntity().updateScaledHealth(); // CraftBukkit - Update scaled health on respawn and worldchange
        playerIn.connection.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
    }

    public int getCurrentPlayerCount()
    {
        return this.playerEntityList.size();
    }

    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public String[] getAvailablePlayerDat()
    {
        return this.mcServer.worlds[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }

    public void setWhiteListEnabled(boolean whitelistEnabled)
    {
        this.whiteListEnforced = whitelistEnabled;
    }

    public List<EntityPlayerMP> getPlayersMatchingAddress(String address)
    {
        List<EntityPlayerMP> list = Lists.<EntityPlayerMP>newArrayList();

        for (EntityPlayerMP entityplayermp : this.playerEntityList)
        {
            if (entityplayermp.getPlayerIP().equals(address))
            {
                list.add(entityplayermp);
            }
        }

        return list;
    }

    public int getViewDistance()
    {
        return this.viewDistance;
    }

    public MinecraftServer getServerInstance()
    {
        return this.mcServer;
    }

    public NBTTagCompound getHostPlayerData()
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void setGameType(GameType gameModeIn)
    {
        this.gameType = gameModeIn;
    }

    private void setPlayerGameTypeBasedOnOther(EntityPlayerMP target, EntityPlayerMP source, World worldIn)
    {
        if (source != null)
        {
            target.interactionManager.setGameType(source.interactionManager.getGameType());
        }
        else if (this.gameType != null)
        {
            target.interactionManager.setGameType(this.gameType);
        }

        target.interactionManager.initializeGameType(worldIn.getWorldInfo().getGameType());
    }

    @SideOnly(Side.CLIENT)
    public void setCommandsAllowedForAll(boolean p_72387_1_)
    {
        this.commandsAllowedForAll = p_72387_1_;
    }

    public void removeAllPlayers()
    {
        // Akarin start - disconnect safely
        for (EntityPlayerMP player : this.playerEntityList) {
            player.connection.disconnect(this.mcServer.server.getShutdownMessage());
        }
    }
    
    public void sendMessage(ITextComponent[] iChatBaseComponents) {
        for (ITextComponent component : iChatBaseComponents) {
            sendMessage(component, true);
        }
    }
    // Akarin end

    public void sendMessage(ITextComponent component, boolean isSystem)
    {
        this.mcServer.sendMessage(component);
        ChatType chattype = isSystem ? ChatType.SYSTEM : ChatType.CHAT;
        // CraftBukkit start - we run this through our processor first so we can get web links etc
        this.sendPacketToAllPlayers(new SPacketChat(CraftChatMessage.fixComponent(component), chattype));
        // CraftBukkit end
    }

    public void sendMessage(ITextComponent component)
    {
        this.sendMessage(component, true);
    }

    public StatisticsManagerServer getPlayerStatsFile(EntityPlayer playerIn)
    {
        UUID uuid = playerIn.getUniqueID();
        StatisticsManagerServer statisticsmanagerserver = uuid == null ? null : (StatisticsManagerServer)this.playerStatFiles.get(uuid);

        if (statisticsmanagerserver == null)
        {
            File file1 = new File(this.mcServer.getWorld(0).getSaveHandler().getWorldDirectory(), "stats");
            File file2 = new File(file1, uuid + ".json");

            if (!file2.exists())
            {
                File file3 = new File(file1, playerIn.getName() + ".json");

                if (file3.exists() && file3.isFile())
                {
                    file3.renameTo(file2);
                }
            }

            statisticsmanagerserver = new StatisticsManagerServer(this.mcServer, file2);
            statisticsmanagerserver.readStatFile();
            this.playerStatFiles.put(uuid, statisticsmanagerserver);
        }

        return statisticsmanagerserver;
    }

    public PlayerAdvancements getPlayerAdvancements(EntityPlayerMP p_192054_1_)
    {
        UUID uuid = p_192054_1_.getUniqueID();
        PlayerAdvancements playeradvancements = this.advancements.get(uuid);

        if (playeradvancements == null)
        {
            File file1 = new File(this.mcServer.getWorld(0).getSaveHandler().getWorldDirectory(), "advancements");
            File file2 = new File(file1, uuid + ".json");
            playeradvancements = new PlayerAdvancements(this.mcServer, file2, p_192054_1_);
            this.advancements.put(uuid, playeradvancements);
        }

        playeradvancements.setPlayer(p_192054_1_);
        return playeradvancements;
    }

    public void setViewDistance(int distance)
    {
        this.viewDistance = distance;

        if (this.mcServer.worlds != null)
        {
            for (WorldServer worldserver : this.mcServer.worlds)
            {
                if (worldserver != null)
                {
                    worldserver.getPlayerChunkMap().setPlayerViewRadius(distance);
                    worldserver.getEntityTracker().setViewDistance(distance);
                }
            }
        }
    }

    public List<EntityPlayerMP> getPlayers()
    {
        return this.playerEntityList;
    }

    public EntityPlayerMP getPlayerByUUID(UUID playerUUID)
    {
        return this.uuidToPlayerMap.get(playerUUID);
    }

    public boolean bypassesPlayerLimit(GameProfile profile)
    {
        return false;
    }

    public void reloadResources()
    {
        // CraftBukkit start
        for (EntityPlayerMP player : playerEntityList) {
            player.getAdvancements().reload();
            player.getAdvancements().flushDirty(player); // CraftBukkit - trigger immediate flush of advancements
        }
        // CraftBukkit end
    }

    @SideOnly(Side.SERVER)
    public boolean isWhiteListEnabled()
    {
        return this.whiteListEnforced;
    }
}