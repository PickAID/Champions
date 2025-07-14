package top.theillusivec4.champions.api.affix;

import net.minecraft.nbt.CompoundTag;
import top.theillusivec4.champions.api.IChampion;

public interface IAffixSyncable {
    /**
     * sync champion data to champion mob
     *
     * @param champion to sync
     */
    void sync(IChampion champion);

    /**
     * Read synced data at a client
     *
     * @param champion client champion to save data
     * @param tag      data to save
     */
    default void readSyncTag(IChampion champion, CompoundTag tag) {

    }

    /**
     * Write data for sync to server
     *
     * @param champion target to sync
     * @return to sync data
     */
    default CompoundTag writeSyncTag(IChampion champion) {
        return new CompoundTag();
    }

}