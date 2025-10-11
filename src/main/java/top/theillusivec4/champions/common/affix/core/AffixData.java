package top.theillusivec4.champions.common.affix.core;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public abstract class AffixData {

    private IChampion champion;
    private String identifier;

    protected AffixData() {
    }

    @Nullable
    public static <T extends AffixData> T getData(IChampion champion, String id, Class<T> clazz) {
        T data = null;

        try {
            data = clazz.getDeclaredConstructor().newInstance();
            data.readData(champion, id);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            Champions.LOGGER.error("Error reading data from class {}", clazz);
        }
        return data;
    }

    public void readData(IChampion champion, String identifier) {
        this.champion = champion;
        this.identifier = identifier;
        LivingEntity livingEntity = champion.getLivingEntity();
	    CompoundNBT tag;

        if (!livingEntity.level.isClientSide()) {
            tag = champion.getServer().getData(identifier);
        } else {
            tag = champion.getClient().getData(identifier);
        }
        readFromNBT(tag);
    }

    public abstract void readFromNBT(CompoundNBT tag);

    public abstract CompoundNBT writeToNBT();

    public void saveData() {
        LivingEntity livingEntity = champion.getLivingEntity();

        if (!livingEntity.level.isClientSide()) {
            champion.getServer().setData(identifier, writeToNBT());
        } else {
            champion.getClient().setData(identifier, writeToNBT());
        }
    }

    public static class BooleanData extends AffixData {

        public boolean mode;

        @Override
        public void readFromNBT(CompoundNBT tag) {
            mode = tag.getBoolean("mode");
        }

        @Override
        public CompoundNBT writeToNBT() {
	        CompoundNBT compound = new CompoundNBT();
            compound.putBoolean("mode", mode);
            return compound;
        }
    }

    public static class IntegerData extends AffixData {

        public int num;

        @Override
        public void readFromNBT(CompoundNBT tag) {
            num = tag.getInt("num");
        }

        @Override
        public CompoundNBT writeToNBT() {
	        CompoundNBT compound = new CompoundNBT();
            compound.putInt("num", num);
            return compound;
        }
    }
}
