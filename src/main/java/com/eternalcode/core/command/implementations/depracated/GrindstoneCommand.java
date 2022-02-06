/*
 * Copyright (c) 2022. EternalCode.pl
 */

package com.eternalcode.core.command.implementations.depracated;

import net.dzikoysk.funnycommands.stereotypes.FunnyCommand;
import net.dzikoysk.funnycommands.stereotypes.FunnyComponent;
import org.bukkit.entity.Player;

@FunnyComponent
public class GrindstoneCommand {
    @FunnyCommand(
        name = "grindstone",
        permission = "eternalcore.command.grindstone",
        usage = "&8» &cPoprawne użycie &7/grindstone <player>",
        acceptsExceeded = true
    )

    public void execute(Player player) {
        player.openGrindstone(null, true);
    }
}
