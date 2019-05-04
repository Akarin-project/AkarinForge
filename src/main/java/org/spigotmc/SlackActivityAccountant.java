/*
 * Akarin Forge
 */
package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class SlackActivityAccountant {
    private double prevTickSlackWeightReciprocal = 65536.0;
    private static final double MIN_SLACK_WEIGHT = 1.52587890625E-5;
    private double averageTickNonSlackNanos = 0.0;
    private static final double AVERAGING_FACTOR = 0.375;
    private long currentActivityStartNanos;
    private static final long OFF = -1;
    private long currentActivityEndNanos;
    private double tickSlackWeight;
    private long tickSlackNanos;

    private double getSlackFraction(double slackWeight) {
        return Math.min(slackWeight * this.prevTickSlackWeightReciprocal, 1.0);
    }

    private int getEstimatedSlackNanos() {
        return (int)Math.max((long)MinecraftServer.currentTick - (long)this.averageTickNonSlackNanos, 0);
    }

    public void tickStarted() {
        this.currentActivityStartNanos = -1;
        this.tickSlackWeight = 0.0;
        this.tickSlackNanos = 0;
    }

    public void startActivity(double slackWeight) {
        long t2;
        double slackFraction0 = this.getSlackFraction(this.tickSlackWeight);
        this.tickSlackWeight += slackWeight;
        double slackFraction1 = this.getSlackFraction(this.tickSlackWeight);
        this.currentActivityStartNanos = t2 = System.nanoTime();
        this.currentActivityEndNanos = t2 + (long)((int)((slackFraction1 - slackFraction0) * (double)this.getEstimatedSlackNanos()));
    }

    private void endActivity(long endNanos) {
        this.tickSlackNanos += endNanos - this.currentActivityStartNanos;
        this.currentActivityStartNanos = -1;
    }

    public boolean activityTimeIsExhausted() {
        if (this.currentActivityStartNanos == -1) {
            return true;
        }
        long t2 = System.nanoTime();
        if (t2 <= this.currentActivityEndNanos) {
            return false;
        }
        this.endActivity(this.currentActivityEndNanos);
        return true;
    }

    public void endActivity() {
        if (this.currentActivityStartNanos == -1) {
            return;
        }
        this.endActivity(Math.min(System.nanoTime(), this.currentActivityEndNanos));
    }

    public void tickEnded(long tickNanos) {
        this.prevTickSlackWeightReciprocal = 1.0 / Math.max(this.tickSlackWeight, 1.52587890625E-5);
        long tickNonSlackNanos = tickNanos - this.tickSlackNanos;
        this.averageTickNonSlackNanos = this.averageTickNonSlackNanos * 0.625 + (double)tickNonSlackNanos * 0.375;
    }
}

