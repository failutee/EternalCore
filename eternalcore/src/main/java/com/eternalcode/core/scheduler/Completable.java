package com.eternalcode.core.scheduler;

import org.jetbrains.annotations.Blocking;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Completable<T> {

    @Blocking
    T get();

    @Blocking
    void await();

    T getOrThrow();

    T getOrDefault(T defaultValue);

    boolean isDone();

    Completable<T> then(Consumer<T> consumer);

    Completable<T> thenSync(Consumer<T> consumer);

    Completable<T> thenAsync(Consumer<T> consumer);

    Completable<T> thenError(Consumer<Throwable> consumer);

    Completable<T> thenErrorSync(Consumer<Throwable> consumer);

    Completable<T> thenErrorAsync(Consumer<Throwable> consumer);

    <R> Completable<R> apply(Function<T, R> mapper);

    <R> Completable<R> applySync(Function<T, R> mapper);

    <R> Completable<R> applyAsync(Function<T, R> mapper);

    Completable<T> whenComplete(BiConsumer<T, Throwable> consumer);

    Completable<T> whenCompleteSync(BiConsumer<T, Throwable> consumer);

    Completable<T> whenCompleteAsync(BiConsumer<T, Throwable> consumer);

}
