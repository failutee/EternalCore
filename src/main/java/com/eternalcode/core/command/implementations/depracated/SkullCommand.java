/*
 * Copyright (c) 2022. EternalCode.pl
 */

package com.eternalcode.core.command.implementations.depracated;

import com.eternalcode.core.EternalCore;
import com.eternalcode.core.builders.ItemBuilder;
import net.dzikoysk.funnycommands.commands.CommandInfo;
import net.dzikoysk.funnycommands.stereotypes.FunnyCommand;
import net.dzikoysk.funnycommands.stereotypes.FunnyComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.eternalcode.core.command.Valid.when;

@FunnyComponent
public class SkullCommand {

    @FunnyCommand(
        name = "skull",
        permission = "eternalcore.command.skull",
        usage = "&8» &cPoprawne użycie &7/skull <player>",
        aliases = "głowa, glowa",
        playerOnly = true,
        acceptsExceeded = true
    )

    public void execute(EternalCore eternalCore, Player player, String[] args, CommandInfo commandInfo) {
        when(args.length != 1, commandInfo.getUsageMessage());
        Bukkit.getScheduler().runTaskAsynchronously(eternalCore, () -> {
            ItemStack item = new ItemBuilder(Material.PLAYER_HEAD)
                .displayName(args[0])
                .skullOwner(args[0])
                .build();

            player.getInventory().addItem(item);
        });
    }
}