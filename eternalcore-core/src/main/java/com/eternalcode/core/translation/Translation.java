package com.eternalcode.core.translation;

import com.eternalcode.core.configuration.contextual.ConfigItem;
import com.eternalcode.core.feature.language.Language;
import com.eternalcode.core.feature.warp.WarpInventoryItem;
import com.eternalcode.multification.notice.Notice;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Translation {

    Language getLanguage();

    // argument section
    ArgumentSection argument();

    interface ArgumentSection {
        Notice missingPlayerName();
        Notice permissionMessage();
        Notice usageMessage();
        Notice usageMessageHead();
        Notice usageMessageEntry();
        Notice offlinePlayer();
        Notice onlyPlayer();
        Notice numberBiggerThanOrEqualZero();
        Notice noItem();
        Notice noArgument();
        Notice noDamaged();
        Notice noDamagedItems();
        Notice noEnchantment();
        Notice noValidEnchantmentLevel();
        Notice invalidTimeFormat();
        Notice worldDoesntExist();
        Notice incorrectNumberOfChunks();
        Notice incorrectLocation();
    }

    // format section
    Format format();

    interface Format {
        String enable();
        String disable();
    }

    // HelpOp Section
    HelpOpSection helpOp();

    interface HelpOpSection {
        Notice format();
        Notice send();
        Notice helpOpDelay();
    }

    // AdminChat Section
    AdminChatSection adminChat();

    interface AdminChatSection {
        Notice format();
    }

    SudoSection sudo();

    interface SudoSection {
        Notice sudoMessageSpy();
        Notice sudoMessage();
    }

    // Teleport Section
    TeleportSection teleport();

    interface TeleportSection {
        // teleport
        Notice teleportedToPlayer();
        Notice teleportedPlayerToPlayer();
        Notice teleportedToHighestBlock();

        // Task
        Notice teleportTimerFormat();
        Notice teleported();
        Notice teleporting();
        Notice teleportTaskCanceled();
        Notice teleportTaskAlreadyExist();

        // Coordinates XYZ
        Notice teleportedToCoordinates();
        Notice teleportedSpecifiedPlayerToCoordinates();

        // Back
        Notice teleportedToLastLocation();
        Notice teleportedSpecifiedPlayerLastLocation();
        Notice lastLocationNoExist();

        // teleport to random player command
        Notice randomPlayerNotFound();
        Notice teleportedToRandomPlayer();
    }

    // Random Teleport Section
    RandomTeleportSection randomTeleport();

    interface RandomTeleportSection {
        Notice randomTeleportStarted();
        Notice randomTeleportFailed();
        Notice teleportedToRandomLocation();
        Notice teleportedSpecifiedPlayerToRandomLocation();
        Notice randomTeleportDelay();
    }

    // Chat Section
    ChatSection chat();

    interface ChatSection {
        Notice disabled();
        Notice enabled();
        Notice cleared();
        Notice alreadyDisabled();
        Notice alreadyEnabled();
        Notice slowModeSet();
        Notice slowModeOff();
        Notice slowMode();
        Notice disabledChatInfo();
        Notice commandNotFound();
        String alertMessageFormat();
        Notice tellrawInfo();
        Notice tellrawAllInfo();
        Notice tellrawSaved();
        Notice tellrawNoSaved();
        Notice tellrawMultipleSent();
        Notice tellrawCleared();
        Notice alertQueueAdded();
        Notice alertQueueRemoved();
        Notice alertQueueCleared();
        Notice alertQueueEmpty();
        Notice alertQueueSent();
    }

    // Warp Section
    WarpSection warp();

    interface WarpSection {
        Notice warpAlreadyExists();
        Notice notExist();
        Notice create();
        Notice remove();
        Notice available();
        Notice itemAdded();
        Notice noWarps();
        Notice itemLimit();
        Notice noPermission();
        Notice addPermissions();
        Notice removePermission();
        Notice permissionDoesNotExist();
        Notice permissionAlreadyExist();
        Notice noPermissionsProvided();
        Notice missingWarpArgument();
        Notice missingPermissionArgument();

        WarpInventorySection warpInventory();

        interface WarpInventorySection {
            String title();

            Map<String, WarpInventoryItem> items();
            void setItems(Map<String, WarpInventoryItem> items);

            default void addItem(String name, WarpInventoryItem item) {
                Map<String, WarpInventoryItem> items = new HashMap<>(this.items());
                items.put(name, item);

                this.setItems(items);
            }

            default WarpInventoryItem removeItem(String name) {
                Map<String, WarpInventoryItem> items = new HashMap<>(this.items());
                WarpInventoryItem removed = items.remove(name);

                this.setItems(items);
                return removed;
            }

            BorderSection border();
            DecorationItemsSection decorationItems();

            interface BorderSection {
                boolean enabled();

                Material material();

                FillType fillType();

                String name();

                List<String> lore();

                enum FillType {
                    TOP, BOTTOM, BORDER, ALL
                }
            }

            interface DecorationItemsSection {
                List<ConfigItem> items();
            }
        }
    }

    // Home section
    HomeSection home();

    interface HomeSection {
        Notice homeList();
        Notice create();
        Notice delete();
        Notice limit();
        Notice overrideHomeLocation();
        Notice noHomesOwned();
        String noHomesOwnedPlaceholder();

        Notice overrideHomeLocationAsAdmin();
        Notice playerNoOwnedHomes();
        Notice createAsAdmin();
        Notice deleteAsAdmin();
        Notice homeListAsAdmin();
    }

    // tpa section
    TpaSection tpa();

    interface TpaSection {
        Notice tpaSelfMessage();
        Notice tpaAlreadySentMessage();
        Notice tpaSentMessage();
        Notice tpaReceivedMessage();
        Notice tpaTargetIgnoresYou();

        Notice tpaDenyNoRequestMessage();
        Notice tpaDenyDoneMessage();
        Notice tpaDenyReceivedMessage();
        Notice tpaDenyAllDenied();

        Notice tpaAcceptMessage();
        Notice tpaAcceptNoRequestMessage();
        Notice tpaAcceptReceivedMessage();
        Notice tpaAcceptAllAccepted();
    }

    // private section
    PrivateChatSection privateChat();

    interface PrivateChatSection {
        Notice noReply();
        Notice privateMessageYouToTarget();
        Notice privateMessageTargetToYou();

        Notice socialSpyMessage();
        Notice socialSpyEnable();
        Notice socialSpyDisable();

        Notice ignorePlayer();
        Notice ignoreAll();
        Notice unIgnorePlayer();
        Notice unIgnoreAll();
        Notice alreadyIgnorePlayer();
        Notice notIgnorePlayer();
        Notice cantIgnoreYourself();
        Notice cantUnIgnoreYourself();
    }

    // afk section
    AfkSection afk();

    interface AfkSection {
        Notice afkOn();
        Notice afkOff();
        Notice afkDelay();
        String afkKickReason();

        String afkEnabledPlaceholder();
        String afkDisabledPlaceholder();
    }

    // event section
    EventSection event();

    interface EventSection {
        List<Notice> deathMessage();
        List<Notice> unknownDeathCause();
        List<Notice> joinMessage();
        List<Notice> quitMessage();
        List<Notice> firstJoinMessage();

        Map<EntityDamageEvent.DamageCause, List<Notice>> deathMessageByDamageCause();

        Notice welcome();
    }

    // inventory section
    InventorySection inventory();

    interface InventorySection {
        Notice inventoryClearMessage();
        Notice inventoryClearMessageBy();
        String disposalTitle();
    }

    // player section
    PlayerSection player();

    interface PlayerSection {
        // feed
        Notice feedMessage();
        Notice feedMessageBy();

        // heal
        Notice healMessage();
        Notice healMessageBy();

        // kill
        Notice killedMessage();

        // speed
        Notice speedBetweenZeroAndTen();
        Notice speedTypeNotCorrect();

        Notice speedWalkSet();
        Notice speedFlySet();

        Notice speedWalkSetBy();
        Notice speedFlySetBy();

        // godmode
        Notice godEnable();
        Notice godDisable();
        Notice godSetEnable();
        Notice godSetDisable();

        // fly
        Notice flyEnable();
        Notice flyDisable();
        Notice flySetEnable();
        Notice flySetDisable();

        // ping
        Notice pingMessage();
        Notice pingOtherMessage();

        // gamemode
        Notice gameModeNotCorrect();
        Notice gameModeMessage();
        Notice gameModeSetMessage();

        // online
        Notice onlinePlayersCountMessage();
        Notice onlinePlayersMessage();

        // slot-bypass
        List<String> fullServerSlots();

        // whois
        List<String> whoisCommand();

        // butcher
        Notice butcherCommand();
        Notice safeChunksMessage();
    }

    // spawn section
    SpawnSection spawn();

    interface SpawnSection {
        // spawn
        Notice spawnSet();
        Notice spawnNoSet();

        Notice spawnTeleportedBy();
        Notice spawnTeleportedOther();
    }

    // item section
    ItemSection item();

    interface ItemSection {
        // item name & lore
        Notice itemClearNameMessage();
        Notice itemClearLoreMessage();

        Notice itemChangeNameMessage();
        Notice itemChangeLoreMessage();

        // item flags
        Notice itemFlagRemovedMessage();
        Notice itemFlagAddedMessage();
        Notice itemFlagClearedMessage();

        // give
        Notice giveReceived();
        Notice giveGiven();
        Notice giveNoSpace();
        Notice giveNotItem();

        // others
        Notice repairMessage();
        Notice repairAllMessage();
        Notice repairDelayMessage();
        Notice skullMessage();
        Notice enchantedMessage();
        Notice enchantedMessageFor();
        Notice enchantedMessageBy();
    }

    // time and weather
    TimeAndWeatherSection timeAndWeather();

    interface TimeAndWeatherSection {
        Notice timeSetDay();
        Notice timeSetNight();

        Notice timeSet();
        Notice timeAdd();

        Notice weatherSetRain();
        Notice weatherSetSun();
        Notice weatherSetThunder();
    }

    // language section
    LanguageSection language();

    interface LanguageSection {
        Notice languageChanged();

        List<ConfigItem> decorationItems();
    }

    // container section
    ContainerSection container();

    interface ContainerSection {
        Notice genericContainerOpened();
        Notice genericContainerOpenedBy();
        Notice genericContainerOpenedFor();
    }

    AutoMessageSection autoMessage();

    interface AutoMessageSection {
        Collection<Notice> messages();

        Notice enabled();
        Notice disabled();
    }

    JailSection jailSection();

    interface JailSection {
        Notice jailLocationSet();
        Notice jailLocationRemove();
        Notice jailLocationNotSet();
        Notice jailLocationOverride();

        Notice jailDetainBroadcast();
        Notice jailDetainPrivate();
        Notice jailDetainCountdown();
        Notice jailDetainOverride();
        Notice jailDetainAdmin();

        Notice jailReleaseBroadcast();
        Notice jailReleasePrivate();
        Notice jailReleaseAll();
        Notice jailReleaseNoPlayers();
        Notice jailIsNotPrisoner();

        Notice jailListHeader();
        Notice jailListEmpty();
        Notice jailListPlayerEntry();

        Notice jailCannotUseCommand();
    }

}
