package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public int dimension; //FML byte -> int
    public boolean trackingPosition;
    public boolean unlimitedTracking;
    public byte scale;
    public byte[] colors = new byte[16384];
    public List<MapData.MapInfo> playersArrayList = Lists.<MapData.MapInfo>newArrayList();
    public final Map<EntityPlayer, MapData.MapInfo> playersHashMap = Maps.<EntityPlayer, MapData.MapInfo>newHashMap(); // Akarin
    public Map<String, MapDecoration> mapDecorations = Maps.<String, MapDecoration>newLinkedHashMap();
    // CraftBukkit start
    public final CraftMapView mapView;
    private CraftServer server;
    private UUID uniqueId = null;
    // CraftBukkit end

    public MapData(String mapname)
    {
        super(mapname);
        // CraftBukkit start
        mapView = new CraftMapView(this);
        server = (CraftServer) org.bukkit.Bukkit.getServer();
        // CraftBukkit end
    }

    public void calculateMapCenter(double x, double z, int mapScale)
    {
        int i = 128 * (1 << mapScale);
        int j = MathHelper.floor((x + 64.0D) / (double)i);
        int k = MathHelper.floor((z + 64.0D) / (double)i);
        this.xCenter = j * i + i / 2 - 64;
        this.zCenter = k * i + i / 2 - 64;
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        // CraftBukkit start
        byte dimension = nbt.getByte("dimension");

        if (dimension >= 10) {
            long least = nbt.getLong("UUIDLeast");
            long most = nbt.getLong("UUIDMost");

            if (least != 0L && most != 0L) {
                this.uniqueId = new UUID(most, least);

                CraftWorld world = (CraftWorld) server.getWorld(this.uniqueId);
                // Check if the stored world details are correct.
                if (world == null) {
                    /* All Maps which do not have their valid world loaded are set to a dimension which hopefully won't be reached.
                       This is to prevent them being corrupted with the wrong map data. */
                    dimension = 127;
                } else {
                    dimension = (byte) world.getHandle().dimension;
                }
            }
        }

        this.dimension = dimension;
        // CraftBukkit end
        this.xCenter = nbt.getInteger("xCenter");
        this.zCenter = nbt.getInteger("zCenter");
        this.scale = nbt.getByte("scale");
        this.scale = (byte)MathHelper.clamp(this.scale, 0, 4);

        if (nbt.hasKey("trackingPosition", 1))
        {
            this.trackingPosition = nbt.getBoolean("trackingPosition");
        }
        else
        {
            this.trackingPosition = true;
        }

        this.unlimitedTracking = nbt.getBoolean("unlimitedTracking");
        int i = nbt.getShort("width");
        int j = nbt.getShort("height");

        if (i == 128 && j == 128)
        {
            this.colors = nbt.getByteArray("colors");
        }
        else
        {
            byte[] abyte = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            int k = (128 - i) / 2;
            int l = (128 - j) / 2;

            for (int i1 = 0; i1 < j; ++i1)
            {
                int j1 = i1 + l;

                if (j1 >= 0 || j1 < 128)
                {
                    for (int k1 = 0; k1 < i; ++k1)
                    {
                        int l1 = k1 + k;

                        if (l1 >= 0 || l1 < 128)
                        {
                            this.colors[l1 + j1 * 128] = abyte[k1 + i1 * i];
                        }
                    }
                }
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        // CraftBukkit start
        if (this.dimension >= 10) {
            if (this.uniqueId == null) {
                for (org.bukkit.World world : server.getWorlds()) {
                    CraftWorld cWorld = (CraftWorld) world;
                    if (cWorld.getHandle().dimension == this.dimension) {
                        this.uniqueId = cWorld.getUID();
                        break;
                    }
                }
            }
            /* Perform a second check to see if a matching world was found, this is a necessary
               change incase Maps are forcefully unlinked from a World and lack a UID.*/
            if (this.uniqueId != null) {
                compound.setLong("UUIDLeast", this.uniqueId.getLeastSignificantBits());
                compound.setLong("UUIDMost", this.uniqueId.getMostSignificantBits());
            }
        }
        // CraftBukkit end
        compound.setInteger("xCenter", this.xCenter);
        compound.setInteger("zCenter", this.zCenter);
        compound.setByte("scale", this.scale);
        compound.setShort("width", (short)128);
        compound.setShort("height", (short)128);
        compound.setByteArray("colors", this.colors);
        compound.setBoolean("trackingPosition", this.trackingPosition);
        compound.setBoolean("unlimitedTracking", this.unlimitedTracking);
        return compound;
    }

    public void updateVisiblePlayers(EntityPlayer player, ItemStack mapStack)
    {
        if (!this.playersHashMap.containsKey(player))
        {
            MapData.MapInfo mapdata$mapinfo = new MapData.MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }

        if (!player.inventory.hasItemStack(mapStack))
        {
            this.mapDecorations.remove(player.getName());
        }

        for (int i = 0; i < this.playersArrayList.size(); ++i)
        {
            MapData.MapInfo mapdata$mapinfo1 = this.playersArrayList.get(i);

            if (!mapdata$mapinfo1.player.isDead && (mapdata$mapinfo1.player.inventory.hasItemStack(mapStack) || mapStack.isOnItemFrame()))
            {
                if (!mapStack.isOnItemFrame() && mapdata$mapinfo1.player.dimension == this.dimension && this.trackingPosition)
                {
                    this.updateDecorations(MapDecoration.Type.PLAYER, mapdata$mapinfo1.player.world, mapdata$mapinfo1.player.getName(), mapdata$mapinfo1.player.posX, mapdata$mapinfo1.player.posZ, (double)mapdata$mapinfo1.player.rotationYaw);
                }
            }
            else
            {
                this.playersHashMap.remove(mapdata$mapinfo1.player);
                this.playersArrayList.remove(mapdata$mapinfo1);
            }
        }

        if (mapStack.isOnItemFrame() && this.trackingPosition)
        {
            EntityItemFrame entityitemframe = mapStack.getItemFrame();
            BlockPos blockpos = entityitemframe.getHangingPosition();
            this.updateDecorations(MapDecoration.Type.FRAME, player.world, "frame-" + entityitemframe.getEntityId(), (double)blockpos.getX(), (double)blockpos.getZ(), (double)(entityitemframe.facingDirection.getHorizontalIndex() * 90));
        }

        if (mapStack.hasTagCompound() && mapStack.getTagCompound().hasKey("Decorations", 9))
        {
            NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);

            for (int j = 0; j < nbttaglist.tagCount(); ++j)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);

                if (!this.mapDecorations.containsKey(nbttagcompound.getString("id")))
                {
                    this.updateDecorations(MapDecoration.Type.byIcon(nbttagcompound.getByte("type")), player.world, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
                }
            }
        }
    }

    public static void addTargetDecoration(ItemStack map, BlockPos target, String decorationName, MapDecoration.Type type)
    {
        NBTTagList nbttaglist;

        if (map.hasTagCompound() && map.getTagCompound().hasKey("Decorations", 9))
        {
            nbttaglist = map.getTagCompound().getTagList("Decorations", 10);
        }
        else
        {
            nbttaglist = new NBTTagList();
            map.setTagInfo("Decorations", nbttaglist);
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setByte("type", type.getIcon());
        nbttagcompound.setString("id", decorationName);
        nbttagcompound.setDouble("x", (double)target.getX());
        nbttagcompound.setDouble("z", (double)target.getZ());
        nbttagcompound.setDouble("rot", 180.0D);
        nbttaglist.appendTag(nbttagcompound);

        if (type.hasMapColor())
        {
            NBTTagCompound nbttagcompound1 = map.getOrCreateSubCompound("display");
            nbttagcompound1.setInteger("MapColor", type.getMapColor());
        }
    }

    private void updateDecorations(MapDecoration.Type type, World worldIn, String decorationName, double worldX, double worldZ, double rotationIn)
    {
        int i = 1 << this.scale;
        float f = (float)(worldX - (double)this.xCenter) / (float)i;
        float f1 = (float)(worldZ - (double)this.zCenter) / (float)i;
        byte b0 = (byte)((int)((double)(f * 2.0F) + 0.5D));
        byte b1 = (byte)((int)((double)(f1 * 2.0F) + 0.5D));
        int j = 63;
        byte b2;

        if (f >= -63.0F && f1 >= -63.0F && f <= 63.0F && f1 <= 63.0F)
        {
            rotationIn = rotationIn + (rotationIn < 0.0D ? -8.0D : 8.0D);
            b2 = (byte)((int)(rotationIn * 16.0D / 360.0D));

            if (worldIn.provider.shouldMapSpin(decorationName, worldX, worldZ, rotationIn))
            {
                int l = (int)(worldIn.getWorldTime() / 10L);
                b2 = (byte)(l * l * 34187121 + l * 121 >> 15 & 15);
            }
        }
        else
        {
            if (type != MapDecoration.Type.PLAYER)
            {
                this.mapDecorations.remove(decorationName);
                return;
            }

            int k = 320;

            if (Math.abs(f) < 320.0F && Math.abs(f1) < 320.0F)
            {
                type = MapDecoration.Type.PLAYER_OFF_MAP;
            }
            else
            {
                if (!this.unlimitedTracking)
                {
                    this.mapDecorations.remove(decorationName);
                    return;
                }

                type = MapDecoration.Type.PLAYER_OFF_LIMITS;
            }

            b2 = 0;

            if (f <= -63.0F)
            {
                b0 = -128;
            }

            if (f1 <= -63.0F)
            {
                b1 = -128;
            }

            if (f >= 63.0F)
            {
                b0 = 127;
            }

            if (f1 >= 63.0F)
            {
                b1 = 127;
            }
        }

        this.mapDecorations.put(decorationName, new MapDecoration(type, b0, b1, b2));
    }

    @Nullable
    public Packet<?> getMapPacket(ItemStack mapStack, World worldIn, EntityPlayer player)
    {
        MapData.MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
        return mapdata$mapinfo == null ? null : mapdata$mapinfo.getPacket(mapStack);
    }

    public void updateMapData(int x, int y)
    {
        super.markDirty();

        for (MapData.MapInfo mapdata$mapinfo : this.playersArrayList)
        {
            mapdata$mapinfo.update(x, y);
        }
    }

    public MapData.MapInfo getMapInfo(EntityPlayer player)
    {
        MapData.MapInfo mapdata$mapinfo = this.playersHashMap.get(player);

        if (mapdata$mapinfo == null)
        {
            mapdata$mapinfo = new MapData.MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }

        return mapdata$mapinfo;
    }

    public class MapInfo
    {
        public final EntityPlayer player;
        private boolean isDirty = true;
        private int minX;
        private int minY;
        private int maxX = 127;
        private int maxY = 127;
        private int tick;
        public int step;

        public MapInfo(EntityPlayer player)
        {
            this.player = player;
        }

        @Nullable
        public Packet<?> getPacket(ItemStack stack)
        {
         // CraftBukkit start
            if (!this.isDirty && this.tick % 5 != 0) { this.tick++; return null; }
            org.bukkit.craftbukkit.map.RenderData render = MapData.this.mapView.render((org.bukkit.craftbukkit.entity.CraftPlayer) this.player.getBukkitEntity());

            java.util.Collection<MapDecoration> icons = new java.util.ArrayList<MapDecoration>();

            for ( org.bukkit.map.MapCursor cursor : render.cursors) {

                if (cursor.isVisible()) {
                    icons.add(new MapDecoration(MapDecoration.Type.byIcon(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection()));
                }
            }
            // Akarin end
            if (this.isDirty)
            {
                this.isDirty = false;
                return new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, icons, render.buffer, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY); // Akarin
            }
            else
            {
                return this.tick++ % 5 == 0 ? new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, icons, render.buffer, 0, 0, 0, 0) : null; // Akarin
            }
        }

        public void update(int x, int y)
        {
            if (this.isDirty)
            {
                this.minX = Math.min(this.minX, x);
                this.minY = Math.min(this.minY, y);
                this.maxX = Math.max(this.maxX, x);
                this.maxY = Math.max(this.maxY, y);
            }
            else
            {
                this.isDirty = true;
                this.minX = x;
                this.minY = y;
                this.maxX = x;
                this.maxY = y;
            }
        }
    }
}