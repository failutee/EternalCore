package com.eternalcode.core.database;

import com.eternalcode.core.chat.feature.ignore.IgnoreRepository;
import com.eternalcode.core.home.Home;
import com.eternalcode.core.home.HomeRepository;
import com.eternalcode.core.scheduler.Completable;
import com.eternalcode.core.scheduler.Scheduler;
import com.eternalcode.core.user.User;
import panda.std.Blank;
import panda.std.Option;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public final class NoneRepository implements HomeRepository, IgnoreRepository {

    private final Scheduler scheduler;

    public NoneRepository(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Completable<Option<Home>> getHome(UUID uuid) {
        return this.scheduler.completeSync(Option::none);
    }

    @Override
    public Completable<Option<Home>> getHome(User user, String name) {
        return this.scheduler.completeSync(Option::none);
    }

    @Override
    public Completable<Blank> saveHome(Home home) {
        return this.scheduler.completeSync(Blank::new);
    }

    @Override
    public Completable<Integer> deleteHome(UUID uuid) {
        return this.scheduler.completeSync(() -> 0);
    }

    @Override
    public Completable<Integer> deleteHome(User user, String name) {
        return this.scheduler.completeSync(() -> 0);
    }

    @Override
    public Completable<Set<Home>> getHomes() {
        return this.scheduler.completeSync(Collections::emptySet);
    }

    @Override
    public Completable<Set<Home>> getHomes(User user) {
        return this.scheduler.completeSync(Collections::emptySet);
    }

    @Override
    public Completable<Boolean> isIgnored(UUID by, UUID target) {
        return this.scheduler.completeSync(() -> false);
    }

    @Override
    public void ignore(UUID by, UUID target) {}

    @Override
    public void unIgnore(UUID by, UUID target) {}

}
