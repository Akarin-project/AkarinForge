/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapCanvas;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_12_R1.map.RenderData;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public final class CraftMapView
implements MapView {
    private final Map<CraftPlayer, RenderData> renderCache = new HashMap<CraftPlayer, RenderData>();
    private final List<MapRenderer> renderers = new ArrayList<MapRenderer>();
    private final Map<MapRenderer, Map<CraftPlayer, CraftMapCanvas>> canvases = new HashMap<MapRenderer, Map<CraftPlayer, CraftMapCanvas>>();
    protected final bev worldMap;

    public CraftMapView(bev worldMap) {
        this.worldMap = worldMap;
        this.addRenderer(new CraftMapRenderer(this, worldMap));
    }

    @Override
    public short getId() {
        String text = this.worldMap.a;
        if (text.startsWith("map_")) {
            try {
                return Short.parseShort(text.substring("map_".length()));
            }
            catch (NumberFormatException ex2) {
                throw new IllegalStateException("Map has non-numeric ID");
            }
        }
        throw new IllegalStateException("Map has invalid ID");
    }

    @Override
    public boolean isVirtual() {
        return this.renderers.size() > 0 && !(this.renderers.get(0) instanceof CraftMapRenderer);
    }

    @Override
    public MapView.Scale getScale() {
        return MapView.Scale.valueOf(this.worldMap.g);
    }

    @Override
    public void setScale(MapView.Scale scale) {
        this.worldMap.g = scale.getValue();
    }

    @Override
    public World getWorld() {
        int dimension = this.worldMap.d;
        for (World world : Bukkit.getServer().getWorlds()) {
            if (((CraftWorld)world).getHandle().dimension != dimension) continue;
            return world;
        }
        return null;
    }

    @Override
    public void setWorld(World world) {
        this.worldMap.d = (byte)((CraftWorld)world).getHandle().dimension;
    }

    @Override
    public int getCenterX() {
        return this.worldMap.b;
    }

    @Override
    public int getCenterZ() {
        return this.worldMap.c;
    }

    @Override
    public void setCenterX(int x2) {
        this.worldMap.b = x2;
    }

    @Override
    public void setCenterZ(int z2) {
        this.worldMap.c = z2;
    }

    @Override
    public List<MapRenderer> getRenderers() {
        return new ArrayList<MapRenderer>(this.renderers);
    }

    @Override
    public void addRenderer(MapRenderer renderer) {
        if (!this.renderers.contains(renderer)) {
            this.renderers.add(renderer);
            this.canvases.put(renderer, new HashMap());
            renderer.initialize(this);
        }
    }

    @Override
    public boolean removeRenderer(MapRenderer renderer) {
        if (this.renderers.contains(renderer)) {
            this.renderers.remove(renderer);
            for (Map.Entry<CraftPlayer, CraftMapCanvas> entry : this.canvases.get(renderer).entrySet()) {
                for (int x2 = 0; x2 < 128; ++x2) {
                    for (int y2 = 0; y2 < 128; ++y2) {
                        entry.getValue().setPixel(x2, y2, -1);
                    }
                }
            }
            this.canvases.remove(renderer);
            return true;
        }
        return false;
    }

    private boolean isContextual() {
        for (MapRenderer renderer : this.renderers) {
            if (!renderer.isContextual()) continue;
            return true;
        }
        return false;
    }

    public RenderData render(CraftPlayer player) {
        boolean context = this.isContextual();
        RenderData render = this.renderCache.get(context ? player : null);
        if (render == null) {
            render = new RenderData();
            this.renderCache.put(context ? player : null, render);
        }
        if (context && this.renderCache.containsKey(null)) {
            this.renderCache.remove(null);
        }
        Arrays.fill(render.buffer, 0);
        render.cursors.clear();
        Iterator<MapRenderer> iterator = this.renderers.iterator();
        while (iterator.hasNext()) {
            int i2;
            MapRenderer renderer;
            CraftMapCanvas canvas = this.canvases.get(renderer).get((renderer = iterator.next()).isContextual() ? player : null);
            if (canvas == null) {
                canvas = new CraftMapCanvas(this);
                this.canvases.get(renderer).put(renderer.isContextual() ? player : null, canvas);
            }
            canvas.setBase(render.buffer);
            try {
                renderer.render(this, canvas, player);
            }
            catch (Throwable ex2) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not render map using renderer " + renderer.getClass().getName(), ex2);
            }
            byte[] buf = canvas.getBuffer();
            for (i2 = 0; i2 < buf.length; ++i2) {
                byte color = buf[i2];
                if (color < 0 && color > -49) continue;
                render.buffer[i2] = color;
            }
            for (i2 = 0; i2 < canvas.getCursors().size(); ++i2) {
                render.cursors.add(canvas.getCursors().getCursor(i2));
            }
        }
        return render;
    }

    @Override
    public boolean isUnlimitedTracking() {
        return this.worldMap.f;
    }

    @Override
    public void setUnlimitedTracking(boolean unlimited) {
        this.worldMap.f = unlimited;
    }
}

