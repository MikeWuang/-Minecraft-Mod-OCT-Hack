package me.zeroeightsix.kami.module.modules.hidden;

import me.zeroeightsix.kami.module.Module;


/*
 * Created by Hamburger Edited By Occult
 */

@Module.Info(name = "PopbobSexDupe              ", category = Module.Category.HIDDEN, description = "Ezz dupe")
public class PopbobSexDupe extends Module {

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("/kill");
        this.disable();
    }

}
