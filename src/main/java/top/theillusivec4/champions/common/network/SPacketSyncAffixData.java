package top.theillusivec4.champions.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;

import java.util.function.Supplier;

public class SPacketSyncAffixData {

    private final int entityId;
    private final ResourceLocation id;
    private final CompoundNBT data;

    public SPacketSyncAffixData(int entityId, ResourceLocation id, CompoundNBT data) {
        this.entityId = entityId;
        this.data = data;
        this.id = id;
    }

    public static void encode(SPacketSyncAffixData msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeResourceLocation(msg.id);
        buf.writeNbt(msg.data);
    }

    public static SPacketSyncAffixData decode(PacketBuffer buf) {
        return new SPacketSyncAffixData(buf.readInt(), buf.readResourceLocation(), buf.readNbt());
    }

    public static void handle(SPacketSyncAffixData msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().level;

            if (world != null) {
                Entity entity = world.getEntity(msg.entityId);
                ChampionCapability.getCapability(entity).ifPresent(champion -> {
                    IChampion.Client clientChampion = champion.getClient();
                    clientChampion.getAffix(msg.id.toString())
                            .ifPresent(affix -> affix.readSyncTag(champion, msg.data));
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

