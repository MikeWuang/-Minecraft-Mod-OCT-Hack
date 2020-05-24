package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;


/*
* Created by Hamburger 8/1/2020
 */

@Module.Info(name = "FindHole", category = Module.Category.MOVEMENT, description = "Uses Baritone To Find Hole")
public class FindHole extends Module {

    @Override
    public int onEnable() {
        mc.player.sendChatMessage("#goto 1");
        this.disable();
        return 0;
    }

}


