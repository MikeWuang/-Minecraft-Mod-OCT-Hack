package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.Module;


/*
* Created by Hamburger 8/1/2020
 */

@Module.Info(name = "Suicide", category = Module.Category.EXPERIMENTAL, description = "Kills self by running the /kill command")
public class Suicide extends Module {

    @Override
    public int onEnable() {
        mc.player.sendChatMessage("/kill");
        this.disable();
        return 0;
    }

}


