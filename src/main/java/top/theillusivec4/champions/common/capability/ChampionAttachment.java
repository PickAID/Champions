package top.theillusivec4.champions.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.event.ChampionEventsHandler;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.registry.ModAttachments;
import top.theillusivec4.champions.common.util.ChampionHelper;

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

  public static Optional<IChampion> getAttachment(final Entity entity) {
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
    public @NotNull LivingEntity getLivingEntity() {
      return this.champion;
    }

    public static class Server implements IChampion.Server {

      private final Map<ResourceLocation, CompoundTag> data = new HashMap<>();
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
        this.data.put(ResourceLocation.parse(identifier), data);
      }

      @Override
      public CompoundTag getData(String identifier) {
        return this.data.getOrDefault(ResourceLocation.parse(identifier), new CompoundTag());
      }
    }

    public static class Client implements IChampion.Client {

      private final List<IAffix> affixes = new ArrayList<>();
      private final Map<ResourceLocation, IAffix> idToAffix = new HashMap<>();
      private final Map<ResourceLocation, CompoundTag> data = new HashMap<>();
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
      public void setAffixes(Set<ResourceLocation> affixes) {
        this.affixes.clear();

        for (ResourceLocation affix : affixes) {
          Champions.API.getAffix(affix).ifPresent(val -> {
            this.affixes.add(val);
            this.idToAffix.put(val.getIdentifier(), val);
          });
        }
      }

      @Override
      public Optional<IAffix> getAffix(String id) {
        return Optional.ofNullable(this.idToAffix.get(ResourceLocation.parse(id)));
      }

      @Override
      public void setData(String identifier, CompoundTag data) {
        this.data.put(ResourceLocation.parse(identifier), data);
      }

      @Override
      public CompoundTag getData(String identifier) {
        return this.data.getOrDefault(ResourceLocation.parse(identifier), new CompoundTag());
      }
    }
  }

  public static class Provider implements INBTSerializable<CompoundTag> {

    private final IChampion champion;

    Provider(final LivingEntity livingEntity) {
      this.champion = new Champion(livingEntity);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
      CompoundTag compoundNBT = new CompoundTag();
      IChampion.Server champion = this.champion.getServer();
      champion.getRank().ifPresent(rank -> compoundNBT.putInt(TIER_TAG, rank.getTier()));
      List<IAffix> affixes = champion.getAffixes();
      ListTag list = new ListTag();
      affixes.forEach(affix -> {
        CompoundTag tag = new CompoundTag();
        String id = affix.getIdentifier().toString();
        tag.putString(ID_TAG, id);
        tag.put(DATA_TAG, champion.getData(id));
        list.add(tag);
      });
      compoundNBT.put(AFFIX_TAG, list);
      return compoundNBT;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
      IChampion.Server champion = this.champion.getServer();

      if (nbt.contains(TIER_TAG)) {
        int tier = nbt.getInt(TIER_TAG);
        champion.setRank(RankManager.getRank(tier));
      }

      if (nbt.contains(AFFIX_TAG)) {
        ListTag list = nbt.getList(AFFIX_TAG, CompoundTag.TAG_COMPOUND);
        List<IAffix> affixes = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
          CompoundTag tag = list.getCompound(i);
          String id = tag.getString(ID_TAG);
          Champions.API.getAffix(id).ifPresent(affix -> {
            affixes.add(affix);

            if (tag.contains(DATA_TAG)) {
              champion.setData(id, tag.getCompound(DATA_TAG));
            }
          });
        }
        champion.setAffixes(affixes);
      }
    }
  }
}
