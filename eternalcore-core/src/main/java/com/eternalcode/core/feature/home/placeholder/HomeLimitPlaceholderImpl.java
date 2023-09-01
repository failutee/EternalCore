package com.eternalcode.core.feature.home.placeholder;

import com.eternalcode.core.configuration.implementation.PluginConfiguration;
import com.eternalcode.core.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;

import java.util.Map;

public class HomeLimitPlaceholderImpl implements PlaceholderReplacer {

    private final PluginConfiguration pluginConfiguration;

    public HomeLimitPlaceholderImpl(PluginConfiguration pluginConfiguration) {
        this.pluginConfiguration = pluginConfiguration;
    }

    @Override
    public String apply(String text, Player targetPlayer) {
        int maxValue = this.pluginConfiguration.homes.maxHomes.values()
            .stream()
            .max(Integer::compare)
            .orElse(0);

        return String.valueOf(maxValue);
    }
}
