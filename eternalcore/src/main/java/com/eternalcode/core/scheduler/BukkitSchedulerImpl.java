package com.eternalcode.core.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import panda.std.function.ThrowingSupplier;

import java.time.Duration;

public class BukkitSchedulerImpl implements Scheduler {

    private final Plugin plugin;
    private final BukkitScheduler rootScheduler;

    public BukkitSchedulerImpl(Plugin plugin) {
        this.plugin = plugin;
        this.rootScheduler = plugin.getServer().getScheduler();
    }

    @Override
    public Completable<Void> sync(Runnable task) {
        return this.of(task, runnable -> this.rootScheduler.runTask(this.plugin, runnable));
    }

    @Override
    public Completable<Void> async(Runnable task) {
        return this.of(task, runnable -> this.rootScheduler.runTaskAsynchronously(this.plugin, runnable));
    }

    @Override
    public Completable<Void> laterSync(Runnable task, Duration delay) {
        return this.of(task, runnable -> this.rootScheduler.runTaskLater(this.plugin, runnable, this.toTick(delay)));
    }

    @Override
    public Completable<Void> laterAsync(Runnable task, Duration delay) {
        return this.of(task, runnable -> this.rootScheduler.runTaskLaterAsynchronously(this.plugin, runnable, this.toTick(delay)));
    }

    @Override
    public Completable<Void> timerSync(Runnable task, Duration delay, Duration period) {
        return this.of(task, runnable -> this.rootScheduler.runTaskTimer(this.plugin, runnable, this.toTick(delay), this.toTick(period)));
    }

    @Override
    public Completable<Void> timerAsync(Runnable task, Duration delay, Duration period) {
        return this.of(task, runnable -> this.rootScheduler.runTaskTimerAsynchronously(this.plugin, runnable, this.toTick(delay), this.toTick(period)));
    }

    @Override
    public <T> Completable<T> completeSync(ThrowingSupplier<T, Exception> task) {
        return CompletableImpl.createSync(this, task);
    }

    @Override
    public <T> Completable<T> completeAsync(ThrowingSupplier<T, Exception> task) {
        return CompletableImpl.createAsync(this, task);
    }

    private long toTick(Duration duration) {
        return duration.toMillis() / 50L;
    }

    private Completable<Void> of(Runnable task, CompletableImpl.SchedulerFunction scheduler) {
        return CompletableImpl.create(this, scheduler, task);
    }

}
