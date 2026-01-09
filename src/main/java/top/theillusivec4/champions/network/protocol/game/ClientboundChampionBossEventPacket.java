package top.theillusivec4.champions.network.protocol.game;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.network.protocol.CustomPacketPayloads;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.List;
import java.util.UUID;

public final class ClientboundChampionBossEventPacket implements CustomPacketPayload {
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundChampionBossEventPacket> STREAM_CODEC = StreamCodec.composite(
    UUIDUtil.STREAM_CODEC, ClientboundChampionBossEventPacket::getId,
    Operation.STREAM_CODEC, ClientboundChampionBossEventPacket::getOperation,
    ClientboundChampionBossEventPacket::new
  );
  private final UUID id;
  private final Operation operation;

  public static ClientboundChampionBossEventPacket createAddPacket(ServerChampionBossEvent event) {
    return new ClientboundChampionBossEventPacket(
      event.getId(),
      new ClientboundChampionBossEventPacket.AddOperation(
        event.getName(),
        event.getProgress(),
        event.getLevel(),
        event.getColor(),
        event.getAffixes()
      )
    );
  }

  public static ClientboundChampionBossEventPacket createRemovePacket(ServerChampionBossEvent event) {
    return new ClientboundChampionBossEventPacket(
      event.getId(),
      RemoveOperation.INSTANCE
    );
  }

  public static ClientboundChampionBossEventPacket createUpdateName(ServerChampionBossEvent event) {
    return new ClientboundChampionBossEventPacket(
      event.getId(),
      new UpdateName(event.getName())
    );
  }

  public static ClientboundChampionBossEventPacket createUpdateProgress(ServerChampionBossEvent event) {
    return new ClientboundChampionBossEventPacket(
      event.getId(),
      new UpdateProgress(event.getProgress())
    );
  }

  public static ClientboundChampionBossEventPacket createUpdateLevel(ServerChampionBossEvent event) {
    return new ClientboundChampionBossEventPacket(
      event.getId(),
      new UpdateLevel(event.getLevel())
    );
  }

  public static ClientboundChampionBossEventPacket createUpdateColor(ServerChampionBossEvent event) {
    return new ClientboundChampionBossEventPacket(
      event.getId(),
      new UpdateColor(event.getColor())
    );
  }

  public static ClientboundChampionBossEventPacket createUpdateAffixes(ServerChampionBossEvent event) {
    return new ClientboundChampionBossEventPacket(
      event.getId(),
      new UpdateAffixes(event.getAffixes())
    );
  }

  private ClientboundChampionBossEventPacket(UUID id, Operation operation) {
    this.id = id;
    this.operation = operation;
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return CustomPacketPayloads.CLIENTBOUND_CHAMPION_BOSS_EVENT;
  }

  public void dispatch(UUID id, Handler handler) {
    this.operation.dispatch(id, handler);
  }

  public UUID getId() {
    return id;
  }

  private Operation getOperation() {
    return operation;
  }


  private enum OperationType implements StringRepresentable {
    ADD("add", AddOperation.STREAM_CODEC),
    REMOVE("remove", RemoveOperation.STREAM_CODEC),
    UPDATE_PROGRESS("update_progress", UpdateProgress.STREAM_CODEC),
    UPDATE_NAME("update_name", UpdateName.STREAM_CODEC),
    UPDATE_LEVEL("update_level", UpdateLevel.STREAM_CODEC),
    UPDATE_COLOR("update_color", UpdateColor.STREAM_CODEC),
    UPDATE_AFFIXES("update_affixes", UpdateAffixes.STREAM_CODEC);

    public static final Codec<OperationType> CODEC = StringRepresentable.fromEnum(OperationType::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, OperationType> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    private final String name;
    private final StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec;

    OperationType(String name, StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec) {
      this.name = name;
      this.streamCodec = streamCodec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return streamCodec;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }

  private interface Operation {
    StreamCodec<RegistryFriendlyByteBuf, Operation> STREAM_CODEC = OperationType.STREAM_CODEC
      .dispatch(Operation::type, OperationType::streamCodec);

    OperationType type();

    void dispatch(UUID id, Handler handler);

    StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec();
  }

  public interface Handler {
    void add(UUID id, Component name, float progress, int level, int color, List<Holder<Affix>> affixes);

    void remove(UUID id);

    void updateProgress(UUID id, float progress);

    void updateLevel(UUID id, int level);

    void updateColor(UUID id, int color);

    void updateName(UUID id, Component name);

    void updateAffixes(UUID id, List<Holder<Affix>> affixes);
  }

  private record AddOperation(
    Component name,
    float progress,
    int level,
    int color,
    List<Holder<Affix>> affixes
  ) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, AddOperation> STREAM_CODEC = StreamCodec.composite(
      ComponentSerialization.STREAM_CODEC, AddOperation::name,
      ByteBufCodecs.FLOAT, AddOperation::progress,
      ByteBufCodecs.INT, AddOperation::level,
      ByteBufCodecs.INT, AddOperation::color,
      Affix.STREAM_CODEC.apply(ByteBufCodecs.list()), AddOperation::affixes,
      AddOperation::new
    );

    @Override
    public OperationType type() {
      return OperationType.ADD;
    }

    @Override
    public void dispatch(UUID id, Handler handler) {
      handler.add(id, this.name, this.progress, this.level, this.color, this.affixes);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return STREAM_CODEC;
    }
  }

  private record RemoveOperation() implements Operation {
    public static final RemoveOperation INSTANCE = new RemoveOperation();
    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveOperation> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public OperationType type() {
      return OperationType.REMOVE;
    }

    @Override
    public void dispatch(UUID id, Handler handler) {
      handler.remove(id);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return STREAM_CODEC;
    }
  }

  private record UpdateProgress(float progress) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateProgress> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT, UpdateProgress::progress,
      UpdateProgress::new
    );

    @Override
    public OperationType type() {
      return OperationType.UPDATE_PROGRESS;
    }

    @Override
    public void dispatch(UUID id, Handler handler) {
      handler.updateProgress(id, this.progress);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return STREAM_CODEC;
    }
  }

  private record UpdateLevel(int level) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateLevel> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT, UpdateLevel::level,
      UpdateLevel::new
    );

    @Override
    public OperationType type() {
      return OperationType.UPDATE_LEVEL;
    }

    @Override
    public void dispatch(UUID id, Handler handler) {
      handler.updateLevel(id, this.level);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return STREAM_CODEC;
    }
  }

  private record UpdateColor(int color) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateColor> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT, UpdateColor::color,
      UpdateColor::new
    );

    @Override
    public OperationType type() {
      return OperationType.UPDATE_COLOR;
    }

    @Override
    public void dispatch(UUID id, Handler handler) {
      handler.updateColor(id, this.color);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return STREAM_CODEC;
    }
  }

  private record UpdateName(Component name) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateName> STREAM_CODEC = StreamCodec.composite(
      ComponentSerialization.STREAM_CODEC, UpdateName::name,
      UpdateName::new
    );

    @Override
    public OperationType type() {
      return OperationType.UPDATE_NAME;
    }

    @Override
    public void dispatch(UUID id, Handler handler) {
      handler.updateName(id, this.name);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return STREAM_CODEC;
    }
  }

  private record UpdateAffixes(List<Holder<Affix>> affixes) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAffixes> STREAM_CODEC = StreamCodec.composite(
      Affix.STREAM_CODEC.apply(ByteBufCodecs.list()), UpdateAffixes::affixes,
      UpdateAffixes::new
    );

    @Override
    public OperationType type() {
      return OperationType.UPDATE_AFFIXES;
    }

    @Override
    public void dispatch(UUID id, Handler handler) {
      handler.updateAffixes(id, this.affixes);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends Operation> streamCodec() {
      return Operation.STREAM_CODEC;
    }
  }
}
