package com.eternalcode.core.scheduler;

import panda.std.function.ThrowingSupplier;

import java.time.Duration;

public interface Scheduler {

    Completable<Void> sync(Runnable task);

    Completable<Void> async(Runnable task);

    Completable<Void> laterSync(Runnable task, Duration delay);

    Completable<Void> laterAsync(Runnable task, Duration delay);

    Completable<Void> timerSync(Runnable task, Duration delay, Duration period);

    Completable<Void> timerAsync(Runnable task, Duration delay, Duration period);

    <T> Completable<T> completeSync(ThrowingSupplier<T, Exception> task);

    <T> Completable<T> completeAsync(ThrowingSupplier<T, Exception> task);

}
