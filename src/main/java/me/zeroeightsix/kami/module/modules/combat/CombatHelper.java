package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;


@Module.Info(name = "Combat Helper", category = Module.Category.EXPERIMENTAL, description = "Combat Helper Works By Using The Auto Functions Like AutoGap, Autototem, AutoMend, Auto Chorus")
public class CombatHelper extends Module {
        private Setting<Integer> foodLevel = register(Settings.integerBuilder("Below Hunger").withValue(15).withMinimum(1).withMaximum(20).build());
        private Setting<Integer> healthLevel = register(Settings.integerBuilder("Below Health").withValue(8).withMinimum(1).withMaximum(20).build());

        private int lastSlot = -1;
        private boolean eating = false;

        private boolean isValid(ItemStack stack, int food) {
            return (
                    (passItemCheck(stack.getItem()) && stack.getItem() instanceof ItemFood && (foodLevel.getValue() - food) >= ((ItemFood) stack.getItem()).getHealAmount(stack)) ||
                            (passItemCheck(stack.getItem()) && stack.getItem() instanceof ItemFood && (healthLevel.getValue() - (mc.player.getHealth() + mc.player.getAbsorptionAmount()) > 0f))
            );
        }

        private boolean passItemCheck(Item item) {
            if (item == Items.ROTTEN_FLESH) return false;
            if (item == Items.SPIDER_EYE) return false;
            if (item == Items.POISONOUS_POTATO) return false;
            if (item == Items.FISH && new ItemStack(Items.FISH).getItemDamage() == 3) return false;
            return true;
        }

        @Override
        public void onUpdate() {
            if (mc.player == null) return;
            if (eating && !mc.player.isHandActive()) {
                if (lastSlot != -1) {
                    mc.player.inventory.currentItem = lastSlot;
                    lastSlot = -1;
                }
                eating = false;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                return;
            }
            if (eating) return;

            FoodStats stats = mc.player.getFoodStats();
            if (isValid(mc.player.getHeldItemOffhand(), stats.getFoodLevel())) {
                mc.player.setActiveHand(EnumHand.OFF_HAND);
                eating = true;
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
            } else {
                for (int i = 0; i < 9; i++) {
                    if (isValid(mc.player.inventory.getStackInSlot(i), stats.getFoodLevel())) {
                        lastSlot = mc.player.inventory.currentItem;
                        mc.player.inventory.currentItem = i;
                        eating = true;
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                        mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                        return;
                    }
                }
            }
        }
    @Override
    public int onEnable() {
        ModuleManager.getModule("AutoTotem").enable();
        ModuleManager.getModule("OffHandGap").enable();
        ModuleManager.getModule("FastUse").enable();
        ModuleManager.getModule("AutoMend").enable();

        return 0;
    }
    @Override
    public int onDisable() {
        ModuleManager.getModule("AutoTotem").disable();
        ModuleManager.getModule("OffHandGap").disable();
        ModuleManager.getModule("FastUse").disable();
        ModuleManager.getModule("AutoMend").disable();
        return 0;
    }

}


