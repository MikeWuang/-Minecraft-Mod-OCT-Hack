package me.zeroeightsix.kami.module.modules.combat;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.init.MobEffects;
@Module.Info(name = "WeaknessNotification", category = Module.Category.COMBAT, description = "Detects when you have weakness arrows")
public class WeaknessNotification extends Module {

	public int onEnable() {
		if (mc.player.isPotionActive(MobEffects.WEAKNESS)) {
			sendRawChatMessage("I Have Weakness");

		}
		return 0;
	}

	private void sendRawChatMessage(String i_have_weakness) {
	}
}
