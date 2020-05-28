package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;


@Module.Info(name = "Baritone AutoWalk", category = Module.Category.MOVEMENT, description = "Uses Baritone To Autowalk")
public class BaritoneAutoWalk extends Module {

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("#thisway 100000");
        mc.player.sendChatMessage("#path");
    }    @Override
    public void onDisable() {
        mc.player.sendChatMessage("#stop");
    }

}


