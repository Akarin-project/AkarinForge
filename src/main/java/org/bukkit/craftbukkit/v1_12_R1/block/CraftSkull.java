/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.mojang.authlib.GameProfile
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftSkull
extends CraftBlockEntityState<awd>
implements Skull {
    private static final int MAX_OWNER_LENGTH = 16;
    private GameProfile profile;
    private SkullType skullType;
    private byte rotation;

    public CraftSkull(Block block) {
        super(block, awd.class);
    }

    public CraftSkull(Material material, awd te2) {
        super(material, te2);
    }

    @Override
    public void load(awd skull) {
        super.load(skull);
        this.profile = skull.a();
        this.skullType = CraftSkull.getSkullType(skull.f());
        this.rotation = (byte)skull.f;
    }

    static SkullType getSkullType(int id2) {
        switch (id2) {
            default: {
                return SkullType.SKELETON;
            }
            case 1: {
                return SkullType.WITHER;
            }
            case 2: {
                return SkullType.ZOMBIE;
            }
            case 3: {
                return SkullType.PLAYER;
            }
            case 4: {
                return SkullType.CREEPER;
            }
            case 5: 
        }
        return SkullType.DRAGON;
    }

    static int getSkullType(SkullType type) {
        switch (type) {
            default: {
                return 0;
            }
            case WITHER: {
                return 1;
            }
            case ZOMBIE: {
                return 2;
            }
            case PLAYER: {
                return 3;
            }
            case CREEPER: {
                return 4;
            }
            case DRAGON: 
        }
        return 5;
    }

    static byte getBlockFace(BlockFace rotation) {
        switch (rotation) {
            case NORTH: {
                return 0;
            }
            case NORTH_NORTH_EAST: {
                return 1;
            }
            case NORTH_EAST: {
                return 2;
            }
            case EAST_NORTH_EAST: {
                return 3;
            }
            case EAST: {
                return 4;
            }
            case EAST_SOUTH_EAST: {
                return 5;
            }
            case SOUTH_EAST: {
                return 6;
            }
            case SOUTH_SOUTH_EAST: {
                return 7;
            }
            case SOUTH: {
                return 8;
            }
            case SOUTH_SOUTH_WEST: {
                return 9;
            }
            case SOUTH_WEST: {
                return 10;
            }
            case WEST_SOUTH_WEST: {
                return 11;
            }
            case WEST: {
                return 12;
            }
            case WEST_NORTH_WEST: {
                return 13;
            }
            case NORTH_WEST: {
                return 14;
            }
            case NORTH_NORTH_WEST: {
                return 15;
            }
        }
        throw new IllegalArgumentException("Invalid BlockFace rotation: " + (Object)((Object)rotation));
    }

    static BlockFace getBlockFace(byte rotation) {
        switch (rotation) {
            case 0: {
                return BlockFace.NORTH;
            }
            case 1: {
                return BlockFace.NORTH_NORTH_EAST;
            }
            case 2: {
                return BlockFace.NORTH_EAST;
            }
            case 3: {
                return BlockFace.EAST_NORTH_EAST;
            }
            case 4: {
                return BlockFace.EAST;
            }
            case 5: {
                return BlockFace.EAST_SOUTH_EAST;
            }
            case 6: {
                return BlockFace.SOUTH_EAST;
            }
            case 7: {
                return BlockFace.SOUTH_SOUTH_EAST;
            }
            case 8: {
                return BlockFace.SOUTH;
            }
            case 9: {
                return BlockFace.SOUTH_SOUTH_WEST;
            }
            case 10: {
                return BlockFace.SOUTH_WEST;
            }
            case 11: {
                return BlockFace.WEST_SOUTH_WEST;
            }
            case 12: {
                return BlockFace.WEST;
            }
            case 13: {
                return BlockFace.WEST_NORTH_WEST;
            }
            case 14: {
                return BlockFace.NORTH_WEST;
            }
            case 15: {
                return BlockFace.NORTH_NORTH_WEST;
            }
        }
        throw new AssertionError(rotation);
    }

    @Override
    public boolean hasOwner() {
        return this.profile != null;
    }

    @Override
    public String getOwner() {
        return this.hasOwner() ? this.profile.getName() : null;
    }

    @Override
    public boolean setOwner(String name) {
        if (name == null || name.length() > 16) {
            return false;
        }
        GameProfile profile = MinecraftServer.getServerInst().aB().a(name);
        if (profile == null) {
            return false;
        }
        if (this.skullType != SkullType.PLAYER) {
            this.skullType = SkullType.PLAYER;
        }
        this.profile = profile;
        return true;
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (this.profile != null) {
            if (this.profile.getId() != null) {
                return Bukkit.getOfflinePlayer(this.profile.getId());
            }
            if (this.profile.getName() != null) {
                return Bukkit.getOfflinePlayer(this.profile.getName());
            }
        }
        return null;
    }

    @Override
    public void setOwningPlayer(OfflinePlayer player) {
        Preconditions.checkNotNull((Object)player, (Object)"player");
        if (this.skullType != SkullType.PLAYER) {
            this.skullType = SkullType.PLAYER;
        }
        this.profile = new GameProfile(player.getUniqueId(), player.getName());
    }

    @Override
    public BlockFace getRotation() {
        return CraftSkull.getBlockFace(this.rotation);
    }

    @Override
    public void setRotation(BlockFace rotation) {
        this.rotation = CraftSkull.getBlockFace(rotation);
    }

    @Override
    public SkullType getSkullType() {
        return this.skullType;
    }

    @Override
    public void setSkullType(SkullType skullType) {
        this.skullType = skullType;
        if (skullType != SkullType.PLAYER) {
            this.profile = null;
        }
    }

    @Override
    public void applyTo(awd skull) {
        super.applyTo(skull);
        if (this.skullType == SkullType.PLAYER) {
            skull.a(this.profile);
        } else {
            skull.a(CraftSkull.getSkullType(this.skullType));
        }
        skull.b(this.rotation);
    }

}

