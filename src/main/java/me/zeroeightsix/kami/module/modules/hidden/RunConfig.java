package me.zeroeightsix.kami.module.modules.hidden;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.modules.gui.Gui;
import me.zeroeightsix.kami.module.modules.chat.CustomChat;
import me.zeroeightsix.kami.module.modules.client.*;
import me.zeroeightsix.kami.module.modules.misc.DiscordRPC;
import me.zeroeightsix.kami.module.modules.render.TabFriends;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

import static me.zeroeightsix.kami.KamiMod.MODULE_MANAGER;

/**
 * @author S-B99
 * Horribly designed class for uh, running things only once.
 */
@Module.Info(name = "RunConfig", category = Module.Category.HIDDEN, showOnArray = Module.ShowOnArray.OFF, description = "Default manager for first runs")
public class RunConfig extends Module {
    private Setting<Boolean> hasRunCapes = register(Settings.b("Capes", false));
    private Setting<Boolean> hasRunDiscordSettings = register(Settings.b("DiscordRPC", false));
    private Setting<Boolean> hasRunFixGui = register(Settings.b("FixGui", false));
    private Setting<Boolean> hasRunTabFriends = register(Settings.b("TabFriends", false));
    private Setting<Boolean> hasRunCustomChat = register(Settings.b("CustomChat", false));
    private Setting<Boolean> hasRunTooltips = register(Settings.b("Tooltips", false));

    public int onEnable() {
        MODULE_MANAGER.getModule(ActiveModules.class).enable();
        MODULE_MANAGER.getModule(CommandConfig.class).enable();
        MODULE_MANAGER.getModule(InfoOverlay.class).enable();
        MODULE_MANAGER.getModule(InventoryViewer.class).enable();

        if (!hasRunCapes.getValue()) {
            MODULE_MANAGER.getModule(Gui.class).enable();
            hasRunCapes.setValue(true);
        }
        if (!hasRunDiscordSettings.getValue()) {
            MODULE_MANAGER.getModule(DiscordRPC.class).enable();
            hasRunDiscordSettings.setValue(true);
        }
        if (!hasRunFixGui.getValue()) {
            MODULE_MANAGER.getModule(FixGui.class).enable();
            hasRunFixGui.setValue(true);
        }
        if (!hasRunTabFriends.getValue()) {
            MODULE_MANAGER.getModule(TabFriends.class).enable();
            hasRunTabFriends.setValue(true);
        }
        if (!hasRunCustomChat.getValue()) {
            MODULE_MANAGER.getModule(CustomChat.class).enable();
            hasRunCustomChat.setValue(true);
        }
        if (!hasRunTooltips.getValue()) {
            MODULE_MANAGER.getModule(Tooltips.class).enable();
            hasRunTooltips.setValue(true);
        }

        disable();
        return 0;
    }
}
