package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;


/*
* Created by Hamburger 8/1/2020
 */

@Module.Info(name = "AutoMine", category = Module.Category.EXPERIMENTAL, description = "Auto Mine")
public class AutoMine extends Module {

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("#mine Diamond_ore iron_ore gold_ore coal_ore");
    }
    @Override
    public void onDisable() {
        mc.player.sendChatMessage("#stop");
    }

}




