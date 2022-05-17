package com.eternalcode.core.teleport;

import com.eternalcode.core.chat.notification.NoticeService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class TeleportListeners implements Listener {

    private final NoticeService noticeService;
    private final TeleportService teleportService;

    public TeleportListeners(NoticeService noticeService, TeleportService teleportService) {
        this.noticeService = noticeService;
        this.teleportService = teleportService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            this.removeTeleport(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        this.removeTeleport(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.removeTeleport(player);
    }

    private void removeTeleport(Player player) {
        UUID uuid = player.getUniqueId();

        if (this.teleportService.inTeleport(uuid)) {
            this.teleportService.removeTeleport(uuid);

            this.noticeService.notice()
                    .message(messages -> messages.teleport().cancel())
                    .send();
        }
    }

}