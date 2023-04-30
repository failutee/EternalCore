package com.eternalcode.core.feature.poll;

import com.eternalcode.core.feature.FeatureFacade;
import com.eternalcode.core.notification.NoticeService;
import com.eternalcode.core.scheduler.Scheduler;
import com.eternalcode.core.user.UserManager;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.List;

public class PollFacade implements FeatureFacade {

    private final UserManager userManager;
    private final NoticeService noticeService;
    private final PollManager pollManager;

    public PollFacade(UserManager userManager, NoticeService noticeService, Scheduler scheduler) {
        this.userManager = userManager;
        this.noticeService = noticeService;
        this.pollManager = new PollManager(noticeService, scheduler);
    }

    @Override
    public List<Listener> listeners() {
        return List.of(new PollCreateController(this.noticeService, this.pollManager, this.userManager));
    }

    @Override
    public void configureCommands(LiteCommandsBuilder<CommandSender> builder) {
        builder.commandInstance(new PollCommand(this.noticeService, this.pollManager));
    }

}
