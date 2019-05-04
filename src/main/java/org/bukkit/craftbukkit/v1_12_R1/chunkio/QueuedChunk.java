/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.chunkio;

class QueuedChunk {
    final int x;
    final int z;
    final aye loader;
    final amu world;
    final on provider;
    fy compound;

    public QueuedChunk(int x2, int z2, aye loader, amu world, on provider) {
        this.x = x2;
        this.z = z2;
        this.loader = loader;
        this.world = world;
        this.provider = provider;
    }

    public int hashCode() {
        return this.x * 31 + this.z * 29 ^ this.world.hashCode();
    }

    public boolean equals(Object object) {
        if (object instanceof QueuedChunk) {
            QueuedChunk other = (QueuedChunk)object;
            return this.x == other.x && this.z == other.z && this.world == other.world;
        }
        return false;
    }
}

