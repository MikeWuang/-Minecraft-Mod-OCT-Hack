package me.zeroeightsix.kami.module.modules.combat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import me.zeroeightsix.kami.event.events.EntityEvent;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.event.KamiEvent;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.module.Module;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

// Rina.
@Module.Info(name = "AntiKnockback", description = "Modify knockback to 0.", category = Module.Category.MOVEMENT)
public class AntiKnockback extends Module {
	@EventHandler
	private Listener<PacketEvent.Receive> packetEventListener = new Listener<>(event -> {
		if (event.getEra() == KamiEvent.Era.PRE) {
			if (event.getPacket() instanceof SPacketEntityVelocity) {
				SPacketEntityVelocity knockback = (SPacketEntityVelocity) event.getPacket();

				if (knockback.getEntityID() == mc.player.entityId) {
					event.cancel();

					knockback.motionX *= 0.0f;
					knockback.motionY *= 0.0f;
					knockback.motionZ *= 0.0f;
				}
			} else if (event.getPacket() instanceof SPacketExplosion) {
				event.cancel();

				SPacketExplosion knockback = (SPacketExplosion) event.getPacket();

				knockback.motionX *= 0.0f;
				knockback.motionY *= 0.0f;
				knockback.motionZ *= 0.0f;
			}
		}
	});

	@EventHandler
	private Listener<EntityEvent.EntityCollision> entityCollisionListener = new Listener<>(event -> {
		if (event.getEntity() == mc.player) {
			event.cancel();

			return;
		}
	});
}