package com.eternalcode.core.feature;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.List;

public interface FeatureFacade {

    List<Listener> listeners();

    void configureCommands(LiteCommandsBuilder<CommandSender> builder);

}
