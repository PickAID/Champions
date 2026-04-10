//package top.theillusivec4.champions.mixin;
//
//import net.minecraft.network.Connection;
//import net.minecraft.network.TickablePacketListener;
//import net.minecraft.network.protocol.game.GameProtocols;
//import net.minecraft.network.protocol.game.ServerGamePacketListener;
//import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.network.CommonListenerCookie;
//import net.minecraft.server.network.ServerCommonPacketListenerImpl;
//import net.minecraft.server.network.ServerGamePacketListenerImpl;
//import net.minecraft.server.network.ServerPlayerConnection;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import top.theillusivec4.champions.champion.ChampionHelper;
//
///**
// * 实现使用鼠标中键点击实体时获取实体刷怪蛋的功能
// * TODO
// */
//@Mixin(ServerGamePacketListenerImpl.class)
//public abstract class ServerGamePacketListenerImplMixin extends ServerCommonPacketListenerImpl implements ServerGamePacketListener, ServerPlayerConnection, TickablePacketListener, GameProtocols.Context {
//	@Shadow
//	public ServerPlayer player;
//
//	private ServerGamePacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
//		super(server, connection, cookie);
//	}
//
//	@Shadow
//	protected abstract void tryPickItem(ItemStack stack);
//
//	@Inject(method = "handlePickItemFromEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;tryPickItem(Lnet/minecraft/world/item/ItemStack;)V"), cancellable = true)
//	private void modifyItemStack(ServerboundPickItemFromEntityPacket packet, CallbackInfo ci) {
//		ServerLevel serverLevel = this.player.level();
//		Entity entity = serverLevel.getEntity(packet.id());
//		if (entity != null) {
//			ItemStack itemStack = ChampionHelper.getSpawnEgg(entity);
//			if (!itemStack.isEmpty()) {
//				this.tryPickItem(itemStack);
//				ci.cancel();
//			}
//    }
//  }
//}
