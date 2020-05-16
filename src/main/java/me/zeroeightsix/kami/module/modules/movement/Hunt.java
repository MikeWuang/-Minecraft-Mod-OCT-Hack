package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;


/*
* Created by Hamburger 8/1/2020
 */

@Module.Info(name = "Hunt", category = Module.Category.EXPERIMENTAL, description = "Hunts Mobs [Really Good With Aimbot And Killaura]")
public class Hunt extends Module {

    @Override
    public int onEnable() {
        mc.player.sendChatMessage("#follow entities");
        return 0;
    }
    @Override
    public int onDisable() {
        mc.player.sendChatMessage("#stop");
        return 0;
    }

}


