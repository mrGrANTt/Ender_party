package org.ven.mrg.ender_party.custom;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.ven.mrg.ender_party.Values;

public abstract class MrgRunnable extends BukkitRunnable {
    public synchronized @NotNull BukkitTask runTask() throws IllegalArgumentException, IllegalStateException {
        return super.runTask(Values.getPlg());
    }
    public synchronized @NotNull BukkitTask runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        return super.runTaskAsynchronously(Values.getPlg());
    }
    public synchronized @NotNull BukkitTask runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskLater(Values.getPlg(), delay);
    }
    public synchronized @NotNull BukkitTask runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskLaterAsynchronously(Values.getPlg(), delay);
    }
    public synchronized @NotNull BukkitTask runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskTimer(Values.getPlg(), delay, period);
    }
    public synchronized @NotNull BukkitTask runTaskTimerAsynchronously(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskTimerAsynchronously(Values.getPlg(), delay, period);
    }
}
