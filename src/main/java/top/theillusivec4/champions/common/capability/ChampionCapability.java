package top.theillusivec4.champions.common.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.event.ChampionEventsHandler;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ChampionCapability {

	@CapabilityInject(IChampion.class)
    public static Capability<IChampion> CHAMPION_CAP;

    public static final ResourceLocation ID = Utils.getLocation("champion");

    private static final String AFFIX_TAG = "affixes";
    private static final String TIER_TAG = "tier";
    private static final String DATA_TAG = "data";
    private static final String ID_TAG = "identifier";

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new CapabilityEventHandler());
        MinecraftForge.EVENT_BUS.register(new ChampionEventsHandler());
    }

    public static Provider createProvider(final LivingEntity livingEntity) {
        return new Provider(livingEntity);
    }

    public static LazyOptional<IChampion> getCapability(@Nullable final LivingEntity entity) {
        return getCapability((Entity) entity);
    }

    public static LazyOptional<IChampion> getCapability(@Nullable final Entity entity) {

        if (!ChampionHelper.isValidChampionEntity(entity)) {
            return LazyOptional.empty();
        }
        return entity.getCapability(CHAMPION_CAP);
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

        @Nonnull
        @Override
        public LivingEntity getLivingEntity() {
            return this.champion;
        }

        public static class Server implements IChampion.Server {

            private final Map<String, CompoundNBT> data = new HashMap<>();
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
            public void setData(String identifier, CompoundNBT data) {
                this.data.put(identifier, data);
            }

            @Override
            public CompoundNBT getData(String identifier) {
                return this.data.getOrDefault(identifier, new CompoundNBT());
            }
        }

        public static class Client implements IChampion.Client {

            private final List<IAffix> affixes = new ArrayList<>();
            private final Map<ResourceLocation, IAffix> idToAffix = new HashMap<>();
            private final Map<ResourceLocation, CompoundNBT> data = new HashMap<>();
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
                return Optional.ofNullable(this.idToAffix.get(ResourceLocation.tryParse(id)));
            }

            @Override
            public void setData(String identifier, CompoundNBT data) {
                this.data.put(ResourceLocation.tryParse(identifier), data);
            }

            @Override
            public CompoundNBT getData(String identifier) {
                return this.data.getOrDefault(ResourceLocation.tryParse(identifier), new CompoundNBT());
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<INBT> {

        final LazyOptional<IChampion> optional;
        final IChampion data;

        Provider(final LivingEntity livingEntity) {
            this.data = new Champion(livingEntity);
            this.optional = LazyOptional.of(() -> data);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(final Capability<T> cap,
                                                 @Nullable final Direction side) {
            return cap == CHAMPION_CAP ? optional.cast() : LazyOptional.empty();
        }

        @Override
        public INBT serializeNBT() {
            CompoundNBT compoundNBT = new CompoundNBT();
            IChampion.Server champion = data.getServer();
            champion.getRank().ifPresent(rank -> compoundNBT.putInt(TIER_TAG, rank.getTier()));
            List<IAffix> affixes = champion.getAffixes();
            ListNBT list = new ListNBT();
            affixes.forEach(affix -> {
	            CompoundNBT tag = new CompoundNBT();
                String id = affix.getIdentifier().toString();
                tag.putString(ID_TAG, id);
                tag.put(DATA_TAG, champion.getData(id));
                list.add(tag);
            });
            compoundNBT.put(AFFIX_TAG, list);
            return compoundNBT;
        }

        @Override
        public void deserializeNBT(final INBT nbt) {
	        CompoundNBT compoundNBT = (CompoundNBT) nbt;
            IChampion.Server champion = data.getServer();

            if (compoundNBT.contains(TIER_TAG)) {
                int tier = compoundNBT.getInt(TIER_TAG);
                champion.setRank(RankManager.getRank(tier));
            }

            if (compoundNBT.contains(AFFIX_TAG)) {
                ListNBT list = compoundNBT.getList(AFFIX_TAG, Constants.NBT.TAG_COMPOUND);
                List<IAffix> affixes = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
	                CompoundNBT tag = list.getCompound(i);
                    String id = tag.getString(ID_TAG);
                    Champions.API.getAffix(id).ifPresent(affix -> {
                        affixes.add(affix);

                        if (tag.hasUUID(DATA_TAG)) {
                            champion.setData(id, tag.getCompound(DATA_TAG));
                        }
                    });
                }
                champion.setAffixes(affixes);
            }
        }

	    public void invalidate() {
		    this.optional.invalidate();
	    }
    }
}
