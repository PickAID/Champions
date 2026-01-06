//package top.theillusivec4.champions.deprecated.client.integration.jade;
//
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.util.Tuple;
//import snownee.jade.api.EntityAccessor;
//import snownee.jade.api.IEntityComponentProvider;
//import snownee.jade.api.ITooltip;
//import snownee.jade.api.JadeIds;
//import snownee.jade.api.config.IPluginConfig;
//import top.theillusivec4.champions.deprecated.api.IChampion;
//import top.theillusivec4.champions.deprecated.client.config.ClientChampionsConfig;
//import top.theillusivec4.champions.deprecated.common.capability.ChampionAttachment;
//import top.theillusivec4.champions.deprecated.common.rank.Rank;
//import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;
//import top.theillusivec4.champions.utils.Utils;
//
//public enum ChampionComponentProvider implements IEntityComponentProvider {
//  INSTANCE;
//
//  private static Component getChampionName(Tuple<Integer, String> rank, IChampion champion) {
//    return Component.translatable("rank.champions.title." + rank.getA()).append(" " + champion.getLivingEntity().getName().getString()).withColor(Rank.getColor(rank.getB()));
//  }
//
//  @Override
//  public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
//    ChampionAttachment.getAttachment(entityAccessor.getEntity()).ifPresent(
//      champion -> {
//        var clientChampion = champion.getClient();
//        if (ChampionHelper.isValidChampion(clientChampion)) {
//          champion.getClient().getRank().ifPresent(rank -> {
//            iTooltip.replace(JadeIds.CORE_OBJECT_NAME, getChampionName(rank, champion));
//            // add star to jade, based on rank
//            var starElement = StarElement.of(rank.getA(), rank.getB(), ClientChampionsConfig.jadeStarSpacing);
//            // add new line for star element(bellow name, at heart info top)
//            iTooltip.add(1, starElement);
//          });
//          champion.getClient().getAffixes().forEach(
//            affix -> iTooltip.add(Component.translatable(affix.toLanguageKey()))
//          );
//        }
//      });
//  }
//
//  @Override
//  public Identifier getUid() {
//    return Utils.getLocation("enable_affix_compact");
//  }
//}
