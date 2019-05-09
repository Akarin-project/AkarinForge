package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;

public interface INetHandlerPlayClient extends INetHandler
{
    void handleSpawnObject(SPacketSpawnObject packetIn);

    void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb packetIn);

    void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity packetIn);

    void handleSpawnMob(SPacketSpawnMob packetIn);

    void handleScoreboardObjective(SPacketScoreboardObjective packetIn);

    void handleSpawnPainting(SPacketSpawnPainting packetIn);

    void handleSpawnPlayer(SPacketSpawnPlayer packetIn);

    void handleAnimation(SPacketAnimation packetIn);

    void handleStatistics(SPacketStatistics packetIn);

    void handleRecipeBook(SPacketRecipeBook packetIn);

    void handleBlockBreakAnim(SPacketBlockBreakAnim packetIn);

    void handleSignEditorOpen(SPacketSignEditorOpen packetIn);

    void handleUpdateTileEntity(SPacketUpdateTileEntity packetIn);

    void handleBlockAction(SPacketBlockAction packetIn);

    void handleBlockChange(SPacketBlockChange packetIn);

    void handleChat(SPacketChat packetIn);

    void handleTabComplete(SPacketTabComplete packetIn);

    void handleMultiBlockChange(SPacketMultiBlockChange packetIn);

    void handleMaps(SPacketMaps packetIn);

    void handleConfirmTransaction(SPacketConfirmTransaction packetIn);

    void handleCloseWindow(SPacketCloseWindow packetIn);

    void handleWindowItems(SPacketWindowItems packetIn);

    void handleOpenWindow(SPacketOpenWindow packetIn);

    void handleWindowProperty(SPacketWindowProperty packetIn);

    void handleSetSlot(SPacketSetSlot packetIn);

    void handleCustomPayload(SPacketCustomPayload packetIn);

    void handleDisconnect(SPacketDisconnect packetIn);

    void handleUseBed(SPacketUseBed packetIn);

    void handleEntityStatus(SPacketEntityStatus packetIn);

    void handleEntityAttach(SPacketEntityAttach packetIn);

    void handleSetPassengers(SPacketSetPassengers packetIn);

    void handleExplosion(SPacketExplosion packetIn);

    void handleChangeGameState(SPacketChangeGameState packetIn);

    void handleKeepAlive(SPacketKeepAlive packetIn);

    void handleChunkData(SPacketChunkData packetIn);

    void processChunkUnload(SPacketUnloadChunk packetIn);

    void handleEffect(SPacketEffect packetIn);

    void handleJoinGame(SPacketJoinGame packetIn);

    void handleEntityMovement(SPacketEntity packetIn);

    void handlePlayerPosLook(SPacketPlayerPosLook packetIn);

    void handleParticles(SPacketParticles packetIn);

    void handlePlayerAbilities(SPacketPlayerAbilities packetIn);

    void handlePlayerListItem(SPacketPlayerListItem packetIn);

    void handleDestroyEntities(SPacketDestroyEntities packetIn);

    void handleRemoveEntityEffect(SPacketRemoveEntityEffect packetIn);

    void handleRespawn(SPacketRespawn packetIn);

    void handleEntityHeadLook(SPacketEntityHeadLook packetIn);

    void handleHeldItemChange(SPacketHeldItemChange packetIn);

    void handleDisplayObjective(SPacketDisplayObjective packetIn);

    void handleEntityMetadata(SPacketEntityMetadata packetIn);

    void handleEntityVelocity(SPacketEntityVelocity packetIn);

    void handleEntityEquipment(SPacketEntityEquipment packetIn);

    void handleSetExperience(SPacketSetExperience packetIn);

    void handleUpdateHealth(SPacketUpdateHealth packetIn);

    void handleTeams(SPacketTeams packetIn);

    void handleUpdateScore(SPacketUpdateScore packetIn);

    void handleSpawnPosition(SPacketSpawnPosition packetIn);

    void handleTimeUpdate(SPacketTimeUpdate packetIn);

    void handleSoundEffect(SPacketSoundEffect packetIn);

    void handleCustomSound(SPacketCustomSound packetIn);

    void handleCollectItem(SPacketCollectItem packetIn);

    void handleEntityTeleport(SPacketEntityTeleport packetIn);

    void handleEntityProperties(SPacketEntityProperties packetIn);

    void handleEntityEffect(SPacketEntityEffect packetIn);

    void handleCombatEvent(SPacketCombatEvent packetIn);

    void handleServerDifficulty(SPacketServerDifficulty packetIn);

    void handleCamera(SPacketCamera packetIn);

    void handleWorldBorder(SPacketWorldBorder packetIn);

    void handleTitle(SPacketTitle packetIn);

    void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter packetIn);

    void handleResourcePack(SPacketResourcePackSend packetIn);

    void handleUpdateBossInfo(SPacketUpdateBossInfo packetIn);

    void handleCooldown(SPacketCooldown packetIn);

    void handleMoveVehicle(SPacketMoveVehicle packetIn);

    void handleAdvancementInfo(SPacketAdvancementInfo packetIn);

    void handleSelectAdvancementsTab(SPacketSelectAdvancementsTab packetIn);

    void func_194307_a(SPacketPlaceGhostRecipe p_194307_1_);
}