package top.theillusivec4.champions.network;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.server.boss.ChampionsServerBossEvent;
import top.theillusivec4.champions.util.ChampionsStreamCodecs;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.UUID;

public record ChampionsBossEventPayload(UUID id, Operation operation) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<ChampionsBossEventPayload> TYPE = ChampionsUtil.payload("champion_boss_event");
  public static final StreamCodec<RegistryFriendlyByteBuf, ChampionsBossEventPayload> STREAM_CODEC = StreamCodec.composite(
    UUIDUtil.STREAM_CODEC, ChampionsBossEventPayload::id,
    Operation.STREAM_CODEC, ChampionsBossEventPayload::operation,
    ChampionsBossEventPayload::new
  );

  public static ChampionsBossEventPayload createAddPacket(ChampionsServerBossEvent event) {
    return new ChampionsBossEventPayload(
      event.getId(),
      new AddOperation(
        event.getName(),
        event.getProgress(),
        event.getLevel(),
        event.getColor(),
        event.getAffixes()
      )
    );
  }

  public static ChampionsBossEventPayload createRemovePacket(ChampionsServerBossEvent event) {
    return new ChampionsBossEventPayload(
      event.getId(),
      RemoveOperation.INSTANCE
    );
  }

  public static ChampionsBossEventPayload createUpdateProgress(ChampionsServerBossEvent event) {
    return new ChampionsBossEventPayload(
      event.getId(),
      new UpdateProgress(
        event.getProgress()
      )
    );
  }

  public static ChampionsBossEventPayload createUpdateName(ChampionsServerBossEvent event) {
    return new ChampionsBossEventPayload(
      event.getId(),
      new UpdateName(
        event.getName()
      )
    );
  }

  public static ChampionsBossEventPayload createUpdateLevel(ChampionsServerBossEvent event) {
    return new ChampionsBossEventPayload(
      event.getId(),
      new UpdateLevel(
        event.getLevel()
      )
    );
  }

  public static ChampionsBossEventPayload createUpdateColor(ChampionsServerBossEvent event) {
    return new ChampionsBossEventPayload(
      event.getId(),
      new UpdateColor(
        event.getColor()
      )
    );
  }

  public static ChampionsBossEventPayload createUpdateAffixes(ChampionsServerBossEvent event) {
    return new ChampionsBossEventPayload(
      event.getId(),
      new UpdateAffixes(
        event.getAffixes()
      )
    );
  }

  public void dispatch(UUID uuid, Handler handler) {
    this.operation.dispatch(uuid, handler);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public enum OperationType implements StringRepresentable {
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

  public interface Handler {
    void add(UUID id, Component name, float progress, int level, TextColor color, AffixContainer affixes);

    void remove(UUID id);

    void updateProgress(UUID id, float progress);

    void updateLevel(UUID id, int level);

    void updateColor(UUID id, TextColor color);

    void updateName(UUID id, Component name);

    void updateAffixes(UUID id, AffixContainer affixes);
  }

  public sealed interface Operation permits AddOperation, RemoveOperation, UpdateAffixes, UpdateColor, UpdateLevel, UpdateName, UpdateProgress {
    StreamCodec<RegistryFriendlyByteBuf, Operation> STREAM_CODEC = OperationType.STREAM_CODEC.dispatch(Operation::type, OperationType::streamCodec);

    OperationType type();

    void dispatch(UUID id, Handler handler);
  }

  private record AddOperation(
    Component name,
    float progress,
    int level,
    TextColor color,
    AffixContainer affixes
  ) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, AddOperation> STREAM_CODEC = StreamCodec.composite(
      ComponentSerialization.STREAM_CODEC, AddOperation::name,
      ByteBufCodecs.FLOAT, AddOperation::progress,
      ByteBufCodecs.INT, AddOperation::level,
      ChampionsStreamCodecs.TEXT_COLOR, AddOperation::color,
      AffixContainer.STREAM_CODEC, AddOperation::affixes,
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

  }

  private record UpdateColor(TextColor color) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateColor> STREAM_CODEC = StreamCodec.composite(
      ChampionsStreamCodecs.TEXT_COLOR, UpdateColor::color,
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

  }

  private record UpdateAffixes(AffixContainer affixes) implements Operation {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAffixes> STREAM_CODEC = StreamCodec.composite(
      AffixContainer.STREAM_CODEC, UpdateAffixes::affixes,
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

  }

}
