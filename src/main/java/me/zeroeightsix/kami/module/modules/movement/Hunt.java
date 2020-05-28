package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;


/*
* Created by Hamburger 8/1/2020
 */

@Module.Info(name = "Hunt", category = Module.Category.EXPERIMENTAL, description = "Hunts Mobs [Really Good With Aimbot And Killaura]")
public class Hunt extends Module {

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("#follow entities");
    }
    @Override
    public void onDisable() {
        mc.player.sendChatMessage("#stop");
    }

}


