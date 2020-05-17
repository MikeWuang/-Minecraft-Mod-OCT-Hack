package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.module.Module;

import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.Item;

// Rina.
@Module.Info(name = "FastCE", description = "Fast Crystal and Exp Bottles.", category = Module.Category.COMBAT)
public class FastCE extends Module {
	private Setting<Boolean> crystal = register(Settings.b("Crystal", true));
	private Setting<Boolean> expbottle = register(Settings.b("Exp Bottles", true));

	@Override
	public void onUpdate() {
		Item itemMainHand     = mc.player.getHeldItemMainhand().getItem();
		Item itemONotMainHand = mc.player.getHeldItemOffhand().getItem();

		boolean expInMainHand    = itemMainHand instanceof ItemExpBottle;
		boolean expNotInMainHand = itemONotMainHand instanceof ItemExpBottle;

		boolean crystalInMainHand    = itemMainHand instanceof ItemEndCrystal;
		boolean crystalNotInMainHand = itemONotMainHand instanceof ItemEndCrystal;

		if (crystal.getValue()) {
			if (crystalInMainHand | crystalNotInMainHand) {
				mc.rightClickDelayTimer = 0;
			}
		}

		if (expbottle.getValue()) {
			if (expInMainHand | expNotInMainHand) {
				mc.rightClickDelayTimer = 0;
			}
		}
	}
}