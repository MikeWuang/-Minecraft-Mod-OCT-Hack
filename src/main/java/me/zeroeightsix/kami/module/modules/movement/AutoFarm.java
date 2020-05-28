package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;


/*
* Created by Hamburger 8/1/2020
 */

@Module.Info(name = "AutoFarm", category = Module.Category.EXPERIMENTAL, description = "Automatically Farms For You")
public class AutoFarm extends Module {

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("#farm");
    }
    @Override
    public void onDisable() {
        mc.player.sendChatMessage("#stop");
    }

}


