package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.Module;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Module.Info(
   name = "AutoInsult",
   description = "WinDaArguement",
   category = Module.Category.EXPERIMENTAL
)
public class AutoInsult extends Module {
   protected int onEnable() {
      List myList = Arrays.asList("Who? U NIGGA", "You Just got Raped By OTC Hack!", "LoL I Didn't die you did!", "Someone Sounds Mad", " Someone Sounds Angry", "Be Quiet Kid Tts Past Your Bedtime!", "You just got fucked by OCTHack", "Sorry i dont speak 漢字漢字漢字漢字漢字漢字漢字", "Cuck", "Your Just Mad OCThack Is On Top", "You PVP Like a FUCKING NIGGA", "You Smell Like a Burnt Jew");
      Random r = new Random();
      int randomitem = r.nextInt(myList.size());
      String randomElement = (String)myList.get(randomitem);
      mc.player.sendChatMessage(randomElement);
      super.toggle();
       return randomitem;
   }
}
