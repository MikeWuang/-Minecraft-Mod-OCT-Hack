package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import net.minecraft.util.text.TextComponentString;
//Last time im updating this shit its done

@Module.Info(category = Module.Category.MISC, description = "wont fall no moe", name = "AntiFall")
public class AntiFall extends Module {
	public void onUpdate() {
		if(mc.player.fallDistance >= 4) {
			mc.player.sendMessage(new TextComponentString("Test " + mc.player.fallDistance));
			ModuleManager.getModuleByName("Flight").enable();
			mc.player.capabilities.isFlying = true;
			mc.player.capabilities.allowFlying = true;
		}
		else {
			ModuleManager.getModuleByName("Flight").disable();
			mc.player.capabilities.isFlying = false;
			mc.player.capabilities.allowFlying = false;
			
		}
	}
}

