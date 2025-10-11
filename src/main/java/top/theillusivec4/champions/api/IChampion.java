package top.theillusivec4.champions.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.rank.Rank;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IChampion {

    Client getClient();

    Server getServer();

    LivingEntity getLivingEntity();

    interface Client {

        Optional<Tuple<Integer, String>> getRank();

        void setRank(Tuple<Integer, String> rank);

        List<IAffix> getAffixes();

        void setAffixes(Set<ResourceLocation> affixIds);

        Optional<IAffix> getAffix(String id);

        void setData(String identifier, CompoundNBT data);

	    CompoundNBT getData(String identifier);
    }

    interface Server {

        Optional<Rank> getRank();

        void setRank(Rank rank);

        List<IAffix> getAffixes();

        void setAffixes(List<IAffix> affixes);

	    CompoundNBT getData(String identifier);

        void setData(String identifier, CompoundNBT data);
    }
}
