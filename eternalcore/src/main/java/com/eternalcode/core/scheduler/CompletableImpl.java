package com.eternalcode.core.scheduler;

import panda.std.function.ThrowingSupplier;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

class CompletableImpl<T> implements Completable<T> {

    private final Scheduler scheduler;
    private final CompletableFuture<T> completableFuture;

    private CompletableImpl(Scheduler scheduler, CompletableFuture<T> completableFuture) {
        this.scheduler = scheduler;
        this.completableFuture = completableFuture;
    }

    @Override
    public T get() {
        return this.completableFuture
            .orTimeout(5, TimeUnit.MILLISECONDS)
            .join();
    }

    @Override
    public void await() {
        this.completableFuture
            .orTimeout(5, TimeUnit.MILLISECONDS)
            .join();
    }

    @Override
    public T getOrThrow() {
        try {
            return this.completableFuture.get();
        }
        catch (Exception exception) {
            throw new CompletableException(exception);
        }
    }

    @Override
    public T getOrDefault(T defaultValue) {
        return this.completableFuture.getNow(defaultValue);
    }

    @Override
    public boolean isDone() {
        return this.completableFuture.isDone();
    }

    @Override
    public Completable<T> then(Consumer<T> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.whenComplete((result, exception) -> {
            if (exception == null) {
                consumer.accept(result);
            }
        }));
    }

    @Override
    public Completable<T> thenSync(Consumer<T> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.whenCompleteAsync((result, exception) -> {
            if (exception == null) {
                this.scheduler.sync(() -> consumer.accept(result)).await();
            }
        }));
    }

    @Override
    public Completable<T> thenAsync(Consumer<T> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.whenCompleteAsync((result, exception) -> {
            if (exception == null) {
                this.scheduler.async(() -> consumer.accept(result)).await();
            }
        }));
    }

    @Override
    public Completable<T> thenError(Consumer<Throwable> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.whenComplete((result, exception) -> {
            if (exception != null) {
                consumer.accept(exception);
            }
        }));
    }

    @Override
    public Completable<T> thenErrorSync(Consumer<Throwable> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.whenCompleteAsync((result, exception) -> {
            if (exception != null) {
                this.scheduler.sync(() -> consumer.accept(exception)).await();
            }
        }));
    }

    @Override
    public Completable<T> thenErrorAsync(Consumer<Throwable> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.whenCompleteAsync((result, exception) -> {
            if (exception != null) {
                this.scheduler.async(() -> consumer.accept(exception)).await();
            }
        }));
    }

    @Override
    public <R> Completable<R> apply(Function<T, R> mapper) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.thenApply(mapper));
    }

    @Override
    public <R> Completable<R> applySync(Function<T, R> mapper) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture
            .thenApplyAsync(result -> this.scheduler.completeSync(() -> mapper.apply(result)).get()));
    }

    @Override
    public <R> Completable<R> applyAsync(Function<T, R> mapper) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture
            .thenApplyAsync(result -> this.scheduler.completeAsync(() -> mapper.apply(result)).get()));
    }

    @Override
    public Completable<T> whenComplete(BiConsumer<T, Throwable> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture.whenComplete(consumer));
    }

    @Override
    public Completable<T> whenCompleteSync(BiConsumer<T, Throwable> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture
            .whenCompleteAsync((result, exception) -> this.scheduler.sync(() -> consumer.accept(result, exception)).await()));
    }

    @Override
    public Completable<T> whenCompleteAsync(BiConsumer<T, Throwable> consumer) {
        return new CompletableImpl<>(this.scheduler, this.completableFuture
            .whenCompleteAsync((result, exception) -> this.scheduler.async(() -> consumer.accept(result, exception)).await()));
    }

    static Completable<Void> create(Scheduler scheduler, SchedulerFunction schedulerFunction, Runnable task) {
        return create(scheduler, schedulerFunction, () -> {
            task.run();
            return null;
        });
    }

    static <T> Completable<T> create(Scheduler scheduler, SchedulerFunction schedulerFunction, ThrowingSupplier<T, Exception> supplier) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        completableFuture.whenComplete((o, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        });

        schedulerFunction.accept(() -> {
            try {
                completableFuture.complete(supplier.get());
            }
            catch (Exception exception) {
                completableFuture.completeExceptionally(exception);
            }
        });

        return new CompletableImpl<>(scheduler, completableFuture);
    }

    static <T> Completable<T> createAsync(Scheduler scheduler, ThrowingSupplier<T, Exception> supplier) {
        return create(scheduler, scheduler::async, supplier);
    }

    static <T> Completable<T> createSync(Scheduler scheduler, ThrowingSupplier<T, Exception> supplier) {
        return create(scheduler, scheduler::sync, supplier);
    }

    interface SchedulerFunction {
        void accept(Runnable runnable);
    }

}
