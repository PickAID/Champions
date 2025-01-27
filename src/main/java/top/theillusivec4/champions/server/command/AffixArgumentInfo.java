package top.theillusivec4.champions.server.command;

import com.google.gson.JsonObject;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class AffixArgumentInfo implements ArgumentTypeInfo<AffixArgumentType, AffixArgumentInfo.Template> {

    @Override
    public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
    }

    @Override
    public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
        return new Template();
    }

    @Override
    public void serializeToJson(Template template, JsonObject json) {
    }

    @Override
    public Template unpack(AffixArgumentType argument) {
        return new Template();
    }

    public class Template implements ArgumentTypeInfo.Template<AffixArgumentType> {
        @Override
        public AffixArgumentType instantiate(CommandBuildContext context) {
            return new AffixArgumentType();
        }

        @Override
        public ArgumentTypeInfo<AffixArgumentType, ?> type() {
            return AffixArgumentInfo.this;
        }
    }
}
