package me.zeroeightsix.kami.module.modules.misc;

import com.mojang.authlib.GameProfile;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created 10 August 2019 by hub
 * Updated 8 December 2019 by hub
 */
@Module.Info(name = "FakePlayer", category = Module.Category.MISC, description = "Spawns a fake Player")
public class FakePlayer extends Module {

    private Setting<SpawnMode> spawnMode = register(Settings.e("Spawn Mode", SpawnMode.SINGLE));

    private List<Integer> fakePlayerIdList = null;

    private enum SpawnMode {
        SINGLE, MULTI
    }

    private static final String[][] fakePlayerInfo =
            {
                    {"0f75a81d-70e5-43c5-b892-f33c524284f2", "popbob", "-3", "0"},
                    {"64a5c834-514b-4024-9aa4-515719f6e7fa", "iTristan", "0", "-3"},
                    {"fdee323e-7f0c-4c15-8d1c-0f277442342a", "Fit", "3", "0"},
                    {"5d4e46df-0674-4837-a07b-12b5960b2b94", "jj20051", "0", "3"},
                    {"68b1bb11-cfa3-4e16-a01d-b6ddda0a1083", "Pyrobyte", "-6", "0"},
                    {"53283da0-eda3-4671-a22f-511afb2faf87", "maggut", "0", "-6"},
                    {"63980449-98ca-48d7-882f-ed791e4fb4eb", "policemike55", "6", "0"},
                    {"66666666-6666-6666-6666-666666666607", "derp7", "0", "6"},
                    {"66666666-6666-6666-6666-666666666608", "derp8", "-9", "0"},
                    {"66666666-6666-6666-6666-666666666609", "derp9", "0", "-9"},
                    {"66666666-6666-6666-6666-666666666610", "derp10", "9", "0"},
                    {"66666666-6666-6666-6666-666666666611", "derp11", "0", "9"}
            };

    @Override
    protected void onEnable() {

        if (mc.player == null || mc.world == null) {
            this.disable();
        }

        fakePlayerIdList = new ArrayList<>();

        int entityId = -101;

        for (String[] data : fakePlayerInfo) {

            if (spawnMode.getValue().equals(SpawnMode.SINGLE)) {
                addFakePlayer(data[0], data[1], entityId, 0, 0);
                break;
            } else {
                addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            }

            entityId--;

        }
    }

    private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {

        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString(uuid), name));
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.posX = fakePlayer.posX + offsetX;
        fakePlayer.posZ = fakePlayer.posZ + offsetZ;
        mc.world.addEntityToWorld(entityId, fakePlayer);
        fakePlayerIdList.add(entityId);

    }

    @Override
    public void onUpdate() {

        if (fakePlayerIdList == null || fakePlayerIdList.isEmpty() ) {
            this.disable();
        }

    }

    @Override
    protected void onDisable() {

        if (mc.player == null || mc.world == null) {       }

        if (fakePlayerIdList != null) {
            for (int id : fakePlayerIdList) {
                mc.world.removeEntityFromWorld(id);
            }
        }

    }

}
