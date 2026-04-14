package top.theillusivec4.champions.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionsPayloads {
	public static final CustomPacketPayload.Type<ChampionsBossEventPayload> BOSS_EVENT = register("champions_boss_event");

	private ChampionsPayloads() {
	}

	public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> register(String name) {
		return new CustomPacketPayload.Type<>(ChampionsUtil.id(name));
	}
}
