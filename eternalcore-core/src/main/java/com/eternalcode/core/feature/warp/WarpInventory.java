package com.eternalcode.core.feature.warp;

import com.eternalcode.commons.adventure.AdventureUtil;
import com.eternalcode.core.configuration.contextual.ConfigItem;
import com.eternalcode.core.feature.language.Language;
import com.eternalcode.core.injector.annotations.Inject;
import com.eternalcode.core.injector.annotations.component.Service;
import com.eternalcode.core.translation.Translation;
import com.eternalcode.core.translation.Translation.WarpSection.WarpInventorySection;
import com.eternalcode.core.translation.TranslationManager;
import dev.triumphteam.gui.builder.item.BaseItemBuilder;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Service
public class WarpInventory {

    private final TranslationManager translationManager;
    private final WarpManager warpManager;
    private final Server server;
    private final MiniMessage miniMessage;
    private final WarpTeleportService warpTeleportService;

    @Inject
    WarpInventory(
        TranslationManager translationManager,
        WarpManager warpManager,
        Server server,
        MiniMessage miniMessage,
        WarpTeleportService warpTeleportService
    ) {
        this.translationManager = translationManager;
        this.warpManager = warpManager;
        this.server = server;
        this.miniMessage = miniMessage;
        this.warpTeleportService = warpTeleportService;
    }

    private Gui createInventory(Language language) {
        Translation translation = this.translationManager.getMessages(language);
        Translation.WarpSection.WarpInventorySection warpSection = translation.warp().warpInventory();

        Gui gui = Gui.gui()
            .title(this.miniMessage.deserialize(warpSection.title()))
            .rows(warpSection.rows())
            .disableAllInteractions()
            .create();

        this.createWarpItems(warpSection, gui);
        this.createBorder(warpSection, gui);
        this.createDecorations(warpSection, gui);

        return gui;
    }

    private void createBorder(WarpInventorySection warpSection, Gui gui) {
        if (warpSection.border().enabled()) {
            WarpInventorySection.BorderSection borderSection = warpSection.border();

            ItemBuilder borderItem = ItemBuilder.from(borderSection.material());

            if (!borderSection.name().isBlank()) {
                borderItem.name(AdventureUtil.resetItalic(this.miniMessage.deserialize(borderSection.name())));
            }

            if (!borderSection.lore().isEmpty()) {
                borderItem.lore(borderSection.lore()
                    .stream()
                    .map(entry -> AdventureUtil.resetItalic(this.miniMessage.deserialize(entry)))
                    .toList());
            }

            GuiItem guiItem = new GuiItem(borderItem.build());

            switch (borderSection.fillType()) {
                case BORDER -> gui.getFiller().fillBorder(guiItem);
                case ALL -> gui.getFiller().fill(guiItem);
                case TOP -> gui.getFiller().fillTop(guiItem);
                case BOTTOM -> gui.getFiller().fillBottom(guiItem);
                default -> throw new IllegalStateException("Unexpected value: " + borderSection.fillType());
            }
        }
    }

    private void createDecorations(WarpInventorySection warpSection, Gui gui) {
        for (ConfigItem item : warpSection.decorationItems().items()) {
            BaseItemBuilder baseItemBuilder = this.createItem(item);
            GuiItem guiItem = baseItemBuilder.asGuiItem();

            guiItem.setAction(event -> {
                Player player = (Player) event.getWhoClicked();

                if (item.commands.isEmpty()) {
                    return;
                }

                for (String command : item.commands) {
                    this.server.dispatchCommand(player, command);
                }

                player.closeInventory();
            });

            gui.setItem(item.slot(), guiItem);
        }
    }

    private void createWarpItems(WarpInventorySection warpSection, Gui gui) {
        warpSection.items().values().forEach(item -> {
            Optional<Warp> warpOptional = this.warpManager.findWarp(item.warpName());

            if (warpOptional.isEmpty()) {
                return;
            }

            Warp warp = warpOptional.get();
            ConfigItem warpItem = item.warpItem();

            BaseItemBuilder baseItemBuilder = this.createItem(warpItem);
            GuiItem guiItem = baseItemBuilder.asGuiItem();

            guiItem.setAction(event -> {
                Player player = (Player) event.getWhoClicked();

                player.closeInventory();
                this.warpTeleportService.teleport(player, warp);
            });

            gui.setItem(warpItem.slot(), guiItem);
        });
    }

    private BaseItemBuilder createItem(ConfigItem item) {
        Component name = AdventureUtil.resetItalic(this.miniMessage.deserialize(item.name()));

        List<Component> lore = item.lore()
            .stream()
            .map(entry -> AdventureUtil.resetItalic(this.miniMessage.deserialize(entry)))
            .toList();

        if (item.material() == Material.PLAYER_HEAD && !item.texture().isEmpty()) {
            return ItemBuilder.skull()
                .name(name)
                .lore(lore)
                .texture(item.texture())
                .glow(item.glow());
        }

        return ItemBuilder.from(item.material())
            .name(name)
            .lore(lore)
            .glow(item.glow());
    }

    public void openInventory(Player player, Language language) {
        this.createInventory(language).open(player);
    }
}
