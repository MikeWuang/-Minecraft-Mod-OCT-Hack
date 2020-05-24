package me.zeroeightsix.kami.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;


import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;

import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;

import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.KamiTessellator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Module.Info(name = "YoinkCA", category = Module.Category.COMBAT, description = "EZ")
public class AYoinkCA extends Module {
	private Setting<Boolean> place;

	private Setting<Boolean> raytrace;
	private Setting<Integer> attackSpeed;
	private Setting<Integer> placeDelay;
	private Setting<Boolean> autoSwitch;

	private Setting<Integer> facePlace;
	private Setting<Integer> multiPlaceSpeed;

	private Setting<Integer> enemyRange;
	private Setting<Boolean> antiStuck;
	private Setting<Boolean> multiPlace;
	private Setting<Boolean> alert;
	private Setting<Boolean> antiSui;

	private Setting<Integer> minDamage;

	private Setting<Integer> placeRange;
	private Setting<Integer> breakRange;
	private BlockPos render;
	public boolean isActive = false;
	private Entity renderEnt;
	private long placeSystemTime;
	private long breakSystemTime;
	private int newSlot;
	private long antiStuckSystemTime;
	private static boolean togglePitch;
	private boolean switchCooldown;
	private static double yaw;
	private static double pitch;
	private int placements;
	private static boolean isSpoofingAngles;
	private long chatSystemTime;
	private long multiPlaceSystemTime;
	private EntityPlayer target;

	@EventHandler
	private Listener<PacketEvent.Send> packetListener;

	public AYoinkCA() {

		this.place = this.register(Settings.b("Place?", true));

		this.raytrace = this.register(Settings.b("RayTrace", false));

		this.minDamage = this.register((Setting<Integer>) Settings.integerBuilder("MinimumDPS").withMinimum(0).withMaximum(16).withValue(4).build());
		this.facePlace = this.register((Setting<Integer>) Settings.integerBuilder("FacePlace").withMinimum(0).withMaximum(16).withValue(7).build());


		this.multiPlaceSpeed = this.register((Setting<Integer>) Settings.integerBuilder("YoinkSpeed").withMinimum(1).withMaximum(10).withValue(2).build());

		this.placeRange = this.register((Setting<Integer>) Settings.integerBuilder("PlaceRange").withMinimum(1).withMaximum(6).withValue(6).build());
		this.breakRange = this.register((Setting<Integer>) Settings.integerBuilder("BreakRange").withMinimum(1).withMaximum(6).withValue(6).build());
		this.autoSwitch = this.register(Settings.b("AutoSwitching", true));
		this.antiStuck = this.register(Settings.b("AntiStuck", true));


		this.multiPlace = this.register(Settings.b("MultiPlace", false));
		this.alert = this.register(Settings.b("ChatShit", true));



		this.antiSui = this.register(Settings.b("AntiSuicide", true));
		this.attackSpeed = this.register((Setting<Integer>) Settings.integerBuilder("DPS").withMinimum(0).withMaximum(20).withValue(17).build());
		this.placeDelay = this.register((Setting<Integer>) Settings.integerBuilder("PlaceSpeed").withMinimum(0).withMaximum(50).withValue(0).build());
		this.enemyRange = this.register((Setting<Integer>) Settings.integerBuilder("PlayerRange").withMinimum(1).withMaximum(13).withValue(9).build());


		this.placeSystemTime = -1L;
		this.breakSystemTime = -1L;

		this.chatSystemTime = -1L;
		this.multiPlaceSystemTime = -1L;
		this.antiStuckSystemTime = -1L;

		this.switchCooldown = false;

		this.placements = 0;
		final Packet[] packet = new Packet[1];
		this.packetListener = new Listener<PacketEvent.Send>(event -> {
			packet[0] = event.getPacket();
			if (packet[0] instanceof CPacketPlayer && AYoinkCA.isSpoofingAngles) {
				((CPacketPlayer) packet[0]).yaw = (float) AYoinkCA.yaw;
				((CPacketPlayer) packet[0]).pitch = (float) AYoinkCA.pitch;
			}
		}, (Predicate<PacketEvent.Send>[]) new Predicate[0]);
	}

	@Override
	public void onUpdate() {
		final EntityEnderCrystal crystal = (EntityEnderCrystal) AYoinkCA.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> AYoinkCA.mc.player.getDistance(c))).orElse(null);
		if (crystal != null && AYoinkCA.mc.player.getDistance((Entity) crystal) <= this.breakRange.getValue()) {
			if (System.nanoTime() / 1000000L - this.breakSystemTime >= 420 - this.attackSpeed.getValue() * 20) {
				this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer) AYoinkCA.mc.player);
				AYoinkCA.mc.playerController.attackEntity((EntityPlayer) AYoinkCA.mc.player, (Entity) crystal);
				AYoinkCA.mc.player.swingArm(EnumHand.MAIN_HAND);
				this.breakSystemTime = System.nanoTime() / 1000000L;
			}
			if (this.multiPlace.getValue()) {
				if (System.nanoTime() / 1000000L - this.multiPlaceSystemTime >= 20 * this.multiPlaceSpeed.getValue() && System.nanoTime() / 1000000L - this.antiStuckSystemTime <= 400 + (400 - this.attackSpeed.getValue() * 20)) {
					this.multiPlaceSystemTime = System.nanoTime() / 1000000L;
					return;
				}
			} else if (System.nanoTime() / 1000000L - this.antiStuckSystemTime <= 400 + (400 - this.attackSpeed.getValue() * 20)) {
				return;
			}
		} else {
			resetRotation();
		}
		int crystalSlot = (AYoinkCA.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AYoinkCA.mc.player.inventory.currentItem : -1;
		if (crystalSlot == -1) {
			for (int l = 0; l < 9; ++l) {
				if (AYoinkCA.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
					crystalSlot = l;
					break;
				}
			}
		}
		boolean offhand = false;
		if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
			offhand = true;
		} else if (crystalSlot == -1) {
			return;
		}
		Entity ent = null;
		Entity lastTarget = null;

		BlockPos finalPos = null;
		final List<BlockPos> blocks = this.findCrystalBlocks();
		final List<Entity> entities = new ArrayList<Entity>();
		entities.addAll((Collection<? extends Entity>) AYoinkCA.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
		double damage = 0.5;
		for (final Entity entity2 : entities) {
			if (entity2 != AYoinkCA.mc.player) {
				if (((EntityLivingBase) entity2).getHealth() <= 0.0f) {
					continue;

				}
				if (AYoinkCA.mc.player.getDistanceSq(entity2) > this.enemyRange.getValue() * this.enemyRange.getValue()) {
					continue;
				}
				for (final BlockPos blockPos : blocks) {
					if (!canBlockBeSeen(blockPos) && AYoinkCA.mc.player.getDistanceSq(blockPos) > 25.0 && this.raytrace.getValue()) {
						continue;
					}
					final double b = entity2.getDistanceSq(blockPos);
					if (b > 56.2) {
						continue;
					}
					final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
					if (d < this.minDamage.getValue() && ((EntityLivingBase) entity2).getHealth() + ((EntityLivingBase) entity2).getAbsorptionAmount() > this.facePlace.getValue()) {
						continue;
					}
					if (d <= damage) {
						continue;
					}
					final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity) AYoinkCA.mc.player);
					if (this.antiSui.getValue()) {
						if (AYoinkCA.mc.player.getHealth() + AYoinkCA.mc.player.getAbsorptionAmount() - self <= 7.0) {
							continue;
						}
						if (self > d) {
							continue;
						}
					}
					damage = d;
					finalPos = blockPos;
					ent = entity2;
					lastTarget = entity2;

				}
			}
		}
		if (damage == 0.5) {
			this.render = null;
			this.renderEnt = null;
			resetRotation();
			return;
		}
		this.render = finalPos;
		this.renderEnt = ent;

		if (this.place.getValue()) {
			if (!offhand && AYoinkCA.mc.player.inventory.currentItem != crystalSlot) {
				if (this.autoSwitch.getValue()) {
					AYoinkCA.mc.player.inventory.currentItem = crystalSlot;
					resetRotation();
					this.switchCooldown = true;
				}
				return;
			}
			this.lookAtPacket(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5, (EntityPlayer) AYoinkCA.mc.player);
			final RayTraceResult result = AYoinkCA.mc.world.rayTraceBlocks(new Vec3d(AYoinkCA.mc.player.posX, AYoinkCA.mc.player.posY + AYoinkCA.mc.player.getEyeHeight(), AYoinkCA.mc.player.posZ), new Vec3d(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5));
			EnumFacing f;
			if (result == null || result.sideHit == null) {
				f = EnumFacing.UP;
			} else {
				f = result.sideHit;
			}
			if (this.switchCooldown) {
				this.switchCooldown = false;
				return;
			}
			if (System.nanoTime() / 1000000L - this.placeSystemTime >= this.placeDelay.getValue() * 2) {
				AYoinkCA.mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItemOnBlock(finalPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
				++this.placements;
				this.antiStuckSystemTime = System.nanoTime() / 1000000L;
				this.placeSystemTime = System.nanoTime() / 1000000L;
			}
		}
		if (AYoinkCA.isSpoofingAngles) {
			if (AYoinkCA.togglePitch) {
				final EntityPlayerSP player = AYoinkCA.mc.player;
				player.rotationPitch += (float) 4.0E-4;
				AYoinkCA.togglePitch = false;
			} else {
				final EntityPlayerSP player2 = AYoinkCA.mc.player;
				player2.rotationPitch -= (float) 4.0E-4;
				AYoinkCA.togglePitch = true;
			}
		}
	}
	private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
		final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
		setYawAndPitch((float)v[0], (float)v[1]);
	}

	private boolean canPlaceCrystal(final BlockPos blockPos) {
		final BlockPos boost = blockPos.add(0, 1, 0);
		final BlockPos boost2 = blockPos.add(0, 2, 0);
		return (AYoinkCA.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AYoinkCA.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AYoinkCA.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AYoinkCA.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AYoinkCA.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AYoinkCA.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
	}

	public static BlockPos getPlayerPos() {
		return new BlockPos(Math.floor(AYoinkCA.mc.player.posX), Math.floor(AYoinkCA.mc.player.posY), Math.floor(AYoinkCA.mc.player.posZ));
	}

	private List<BlockPos> findCrystalBlocks() {
		NonNullList positions = NonNullList.create();
		positions.addAll((Collection)this.getSphere(AYoinkCA.getPlayerPos(), this.placeRange.getValue().floatValue(), this.placeRange.getValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
		return (List<BlockPos>)positions;
	}

	public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
		final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
		final int cx = loc.getX();
		final int cy = loc.getY();
		final int cz = loc.getZ();
		for (int x = cx - (int)r; x <= cx + r; ++x) {
			for (int z = cz - (int)r; z <= cz + r; ++z) {
				for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
					final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
					if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
						final BlockPos l = new BlockPos(x, y + plus_y, z);
						circleblocks.add(l);
					}
				}
			}
		}
		return circleblocks;
	}

	public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
		final float doubleExplosionSize = 12.0f;
		final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
		final Vec3d vec3d = new Vec3d(posX, posY, posZ);
		final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
		final double v = (1.0 - distancedsize) * blockDensity;
		final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
		double finald = 1.0;
		if (entity instanceof EntityLivingBase) {
			finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World) AYoinkCA.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
		}
		return (float)finald;
	}

	public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer ep = (EntityPlayer)entity;
			final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
			damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
			final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
			final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
			damage *= 1.0f - f / 25.0f;
			if (entity.isPotionActive(Potion.getPotionById(11))) {
				damage -= damage / 4.0f;
			}
			return damage;
		}
		damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
		return damage;
	}

	private static float getDamageMultiplied(final float damage) {
		final int diff = AYoinkCA.mc.world.getDifficulty().getId();
		return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
	}

	public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
		return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
	}

	public static boolean canBlockBeSeen(final BlockPos blockPos) {
		return AYoinkCA.mc.world.rayTraceBlocks(new Vec3d(AYoinkCA.mc.player.posX, AYoinkCA.mc.player.posY + AYoinkCA.mc.player.getEyeHeight(), AYoinkCA.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()), false, true, false) == null;
	}

	private static void setYawAndPitch(final float yaw1, final float pitch1) {
		AYoinkCA.yaw = yaw1;
		AYoinkCA.pitch = pitch1;
		AYoinkCA.isSpoofingAngles = true;
	}

	private static void resetRotation() {
		if (AYoinkCA.isSpoofingAngles) {
			AYoinkCA.yaw = AYoinkCA.mc.player.rotationYaw;
			AYoinkCA.pitch = AYoinkCA.mc.player.rotationPitch;
			AYoinkCA.isSpoofingAngles = false;
		}
	}

	@Override
	protected int onEnable() {

		if (this.alert.getValue() && AYoinkCA.mc.world != null) {
			sendRawChatMessage("\u00A7aAutoCrystal ON");
		}
		return 0;
	}

	public int onDisable() {
		if (this.alert.getValue() && AYoinkCA.mc.world != null) {
			sendRawChatMessage("\u00A7cAutoCrystal" + ChatFormatting.RED.toString() + "OFF");
		}
		this.render = null;
		resetRotation();
		return 0;
	}

	private void sendRawChatMessage(String s) {
	}

	static {
		AYoinkCA.togglePitch = false;
	}
}
