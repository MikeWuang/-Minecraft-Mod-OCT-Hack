package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;

import java.util.concurrent.TimeUnit;


/*
* Created by Hamburger 8/1/2020
 */

@Module.Info(name = "FindHole", category = Module.Category.MOVEMENT, description = "Uses Baritone To Autowalk")
public class FindHoleAPI extends Module {

    @Override
    public int onEnable() {
        mc.player.sendChatMessage("#goto 1");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mc.player.sendChatMessage(";toggle Surround");
        this.disable();
        return 0;
    }

}


