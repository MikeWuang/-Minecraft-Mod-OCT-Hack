package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;

@Module.Info(name = "Strafe",
   description = "Automatically makes the player sprint",
   category = Module.Category.HIDDEN,
   showOnArray = Module.ShowOnArray.OFF
)
public class Strafe extends Module {
   public void onUpdate() {
      try {
         if (!mc.player.collidedHorizontally && mc.player.moveForward > 0.0F) {
            mc.player.setSprinting(true);
         } else {
            mc.player.setSprinting(false);
         }
      } catch (Exception var2) {
      }

   }
}
