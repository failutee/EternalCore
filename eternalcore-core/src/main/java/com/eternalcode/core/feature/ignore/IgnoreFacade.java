package com.eternalcode.core.feature.ignore;

import com.eternalcode.core.database.DatabaseManager;
import com.eternalcode.core.feature.FeatureFacade;
import com.eternalcode.core.notification.NoticeService;
import com.eternalcode.core.scheduler.Scheduler;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import panda.std.reactive.Completable;

import java.util.List;
import java.util.UUID;

public class IgnoreFacade implements FeatureFacade {

    private final NoticeService noticeService;
    private final IgnoreRepository ignoreRepository;

    public IgnoreFacade(NoticeService noticeService, DatabaseManager databaseManager, Scheduler scheduler) {
        this.noticeService = noticeService;
        this.ignoreRepository = IgnoreRepositoryOrmLite.create(databaseManager, scheduler);
    }

    public Completable<Boolean> isIgnored(UUID by, UUID target) {
        return this.ignoreRepository.isIgnored(by, target);
    }

    @Override
    public List<Listener> listeners() {
        return List.of();
    }

    @Override
    public void configureCommands(LiteCommandsBuilder<CommandSender> builder) {
        builder.commandInstance(
            new IgnoreCommand(this.ignoreRepository, this.noticeService),
            new UnIgnoreCommand(this.ignoreRepository, this.noticeService)
        );
    }

}
