package com.eternalcode.core.scheduler;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.scheduler.BukkitSchedulerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import panda.std.Blank;
import panda.utilities.StringUtils;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompletableTest {

    private static ServerMock server;
    private static MockPlugin plugin;
    private static BukkitSchedulerMock mockScheduler;
    private static Scheduler scheduler;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin();
        mockScheduler = server.getScheduler();
        scheduler = new BukkitSchedulerImpl(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testCompleteSync() {
        Completable<Blank> sync = scheduler.completeSync(() -> Blank.BLANK);

        mockScheduler.performOneTick();
        assertTrue(sync.isDone());
    }

    @Test
    void testCompleteAsync() {
        Completable<Blank> async = scheduler.completeAsync(() -> Blank.BLANK);
        Completable<String> sync = async.applySync(blank -> StringUtils.EMPTY);

        await().atMost(1, TimeUnit.SECONDS).until(async::isDone);
        assertFalse(sync.isDone());

        mockScheduler.performOneTick();
        assertTrue(async.isDone());
    }

    @Test
    void testCompleteAsync2() {
        Completable<String> sync = scheduler
            .completeSync(() -> StringUtils.EMPTY)
            .applySync(blank -> {
                System.out.println("Hello");

                return StringUtils.EMPTY;
            })
            .apply(blank -> StringUtils.EMPTY);

        assertFalse(sync.isDone());

        mockScheduler.performOneTick();
        assertFalse(sync.isDone());

        mockScheduler.performOneTick();
        assertTrue(sync.isDone());
    }

}
