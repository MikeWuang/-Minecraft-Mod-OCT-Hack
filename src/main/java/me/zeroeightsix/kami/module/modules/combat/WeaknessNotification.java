package me.zeroeightsix.kami.module.modules.combat;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.init.MobEffects;
@Module.Info(name = "WeaknessNotification   ", category = Module.Category.EXPERIMENTAL, description = "Detects when you have weakness arrows")
public class WeaknessNotification extends Module {

	public void onEnable() {
		if (mc.player.isPotionActive(MobEffects.WEAKNESS)) {
			sendRawChatMessage("I FUCKING Have Weakness");

		}
	}

	private void sendRawChatMessage(String i_have_weakness) {
	}
}
