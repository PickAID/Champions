package top.theillusivec4.champions.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.event.ChampionEventsHandler;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.registry.ModAttachments;
import top.theillusivec4.champions.common.util.ChampionHelper;

import javax.annotation.Nullable;
import java.util.*;

public class ChampionAttachment {

  private static final String AFFIX_TAG = "affixes";
  private static final String TIER_TAG = "tier";
  private static final String DATA_TAG = "data";
  private static final String ID_TAG = "identifier";

  public static void register() {
    NeoForge.EVENT_BUS.register(new AttachmentEventHandler());
    NeoForge.EVENT_BUS.register(new ChampionEventsHandler());
  }

  public static Provider createProvider(final LivingEntity livingEntity) {
    return new Provider(livingEntity);
  }

  public static Optional<IChampion> getAttachment(@Nullable final Entity entity) {
    if (!ChampionHelper.isValidChampionEntity(entity)) {
      return Optional.empty();
    }
    return Optional.ofNullable(entity.getData(ModAttachments.CHAMPION_ATTACHMENT).champion);
  }

  public static class Champion implements IChampion {

    private final LivingEntity champion;
    private final Client client;
    private final Server server;

    private Champion(final LivingEntity livingEntity) {
      this.champion = livingEntity;
      this.client = new Client();
      this.server = new Server();
    }

    @Override
    public Client getClient() {
      return this.client;
    }

    @Override
    public Server getServer() {
      return this.server;
    }

    @Override
    public LivingEntity getLivingEntity() {
      return this.champion;
    }

    public static class Server implements IChampion.Server {

      private final Map<Identifier, CompoundTag> data = new HashMap<>();
      private Rank rank = null;
      private List<IAffix> affixes = new ArrayList<>();

      @Override
      public Optional<Rank> getRank() {
        return Optional.ofNullable(rank);
      }

      @Override
      public void setRank(Rank rank) {
        this.rank = rank;
      }

      @Override
      public List<IAffix> getAffixes() {
        return Collections.unmodifiableList(this.affixes);
      }

      @Override
      public void setAffixes(List<IAffix> affixes) {
        this.affixes = affixes;
      }

      @Override
      public void setData(String identifier, CompoundTag data) {
        this.data.put(Identifier.parse(identifier), data);
      }

      @Override
      public CompoundTag getData(String identifier) {
        return this.data.getOrDefault(Identifier.parse(identifier), new CompoundTag());
      }
    }

    public static class Client implements IChampion.Client {

      private final List<IAffix> affixes = new ArrayList<>();
      private final Map<Identifier, IAffix> idToAffix = new HashMap<>();
      private final Map<Identifier, CompoundTag> data = new HashMap<>();
      private Tuple<Integer, String> rank = null;

      @Override
      public Optional<Tuple<Integer, String>> getRank() {
        return Optional.ofNullable(rank);
      }

      @Override
      public void setRank(Tuple<Integer, String> rank) {
        this.rank = rank;
      }

      @Override
      public List<IAffix> getAffixes() {
        return Collections.unmodifiableList(this.affixes);
      }

      @Override
      public void setAffixes(Set<Identifier> affixes) {
        this.affixes.clear();

        for (Identifier affix : affixes) {
          Champions.API.getAffix(affix).ifPresent(val -> {
            this.affixes.add(val);
            this.idToAffix.put(val.getIdentifier(), val);
          });
        }
      }

      @Override
      public Optional<IAffix> getAffix(String id) {
        return Optional.ofNullable(this.idToAffix.get(Identifier.parse(id)));
      }

      @Override
      public void setData(String identifier, CompoundTag data) {
        this.data.put(Identifier.parse(identifier), data);
      }

      @Override
      public CompoundTag getData(String identifier) {
        return this.data.getOrDefault(Identifier.parse(identifier), new CompoundTag());
      }
    }
  }

  public static class Provider implements ValueIOSerializable {

    private final IChampion champion;

    Provider(final LivingEntity livingEntity) {
      this.champion = new Champion(livingEntity);
    }

    @Override
    public void serialize(ValueOutput output) {
      IChampion.Server champion = this.champion.getServer();
      champion.getRank().ifPresent(rank -> output.putInt(TIER_TAG, rank.getTier()));
      List<IAffix> affixes = champion.getAffixes();
      ValueOutput.TypedOutputList<CompoundTag> affixList = output.list(AFFIX_TAG, CompoundTag.CODEC);
      affixes.forEach(affix -> {
        CompoundTag tag = new CompoundTag();
        String id = affix.getIdentifier().toString();
        tag.putString(ID_TAG, id);
        tag.put(DATA_TAG, champion.getData(id));
        affixList.add(tag);
      });

    }

    @Override
    public void deserialize(ValueInput input) {
      IChampion.Server champion = this.champion.getServer();

      input.getInt(TIER_TAG).ifPresent(valueInput -> champion.setRank(RankManager.getRank(valueInput)));
      // 读取 affixes
      input.list(AFFIX_TAG, CompoundTag.CODEC).ifPresent(affixList -> {
        List<IAffix> affixes = new ArrayList<>();

        // 遍历 TypedInputList<CompoundTag>
        for (CompoundTag tag : affixList) {
          String id = tag.getStringOr(ID_TAG, "");
          Champions.API.getAffix(id).ifPresent(affix -> {
            affixes.add(affix);
            if (tag.contains(DATA_TAG)) {
              champion.setData(id, tag.getCompound(DATA_TAG).orElseThrow());
            }
          });
        }

        champion.setAffixes(affixes);
      });
    }
  }
}
