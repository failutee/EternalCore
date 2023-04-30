package com.eternalcode.core.feature.ignore;

import panda.std.Blank;
import panda.std.reactive.Completable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class IgnoreRepositoryInMemory implements IgnoreRepository {

    private final Map<UUID, Map<UUID, Boolean>> ignored = new HashMap<>();

    @Override
    public Completable<Boolean> isIgnored(UUID by, UUID target) {
        return Completable.completed(this.ignored.getOrDefault(by, new HashMap<>()).getOrDefault(target, false));
    }

    @Override
    public Completable<Blank> ignore(UUID by, UUID target) {
        this.ignored.computeIfAbsent(by, uuid -> new HashMap<>()).put(target, true);
        return Completable.completed(Blank.BLANK);
    }

    @Override
    public Completable<Blank> ignoreAll(UUID by) {
        return this.ignore(by, IGNORE_ALL);
    }

    @Override
    public Completable<Blank> unIgnore(UUID by, UUID target) {
        Map<UUID, Boolean> ignoredBy = this.ignored.get(by);

        if (ignoredBy != null) {
            ignoredBy.remove(target);
        }

        return Completable.completed(Blank.BLANK);
    }

    @Override
    public Completable<Blank> unIgnoreAll(UUID by) {
        Map<UUID, Boolean> ignoredBy = this.ignored.get(by);

        if (ignoredBy != null) {
            ignoredBy.clear();
        }

        return Completable.completed(Blank.BLANK);
    }

}
