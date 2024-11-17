package top.theillusivec4.champions.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.registry.ModDamageTypes;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import static top.theillusivec4.champions.common.registry.ModAffixTypes.*;
import static top.theillusivec4.champions.common.registry.ModDamageTypes.*;
import static top.theillusivec4.champions.common.registry.ModEntityTypes.*;
import static top.theillusivec4.champions.common.registry.ModItems.*;
import static top.theillusivec4.champions.common.registry.ModMobEffects.*;

public class ModLanguageProvider extends LanguageProvider {
  final private String locale;

  public ModLanguageProvider(PackOutput output, String locale) {
    super(output, Champions.MODID, locale);
    this.locale = locale;
  }
  public ModLanguageProvider(PackOutput output) {
    super(output, Champions.MODID, "en_us");
    this.locale = "en_us";
  }


  @Override
  protected void addTranslations() {
    switch (this.locale){
      case "zh_cn":
        this.addChineseTranslations();
        break;
      case "en_us":
      default:
        this.addEnglishTranslations();
    }
  }

  /**
   * zh_cn
   */
  protected void addChineseTranslations(){
    // 词缀
    this.addAffix(ADAPTABLE.get(), "适应");
    this.addAffix(ARCTIC.get(), "极寒");
    this.addAffix(DAMPENING.get(), "抑制");
    this.addAffix(DESECRATING.get(), "亵渎");
    this.addAffix(ENKINDLING.get(), "点燃");
    this.addAffix(HASTY.get(), "急速");
    this.addAffix(INFESTED.get(), "感染");
    this.addAffix(KNOCKING.get(), "爆震");
    this.addAffix(LIVELY.get(), "活力");
    this.addAffix(MAGNETIC.get(), "磁力");
    this.addAffix(MOLTEN.get(), "熔融");
    this.addAffix(PARALYZING.get(), "瘫痪");
    this.addAffix(PLAGUED.get(), "瘟疫");
    this.addAffix(REFLECTIVE.get(), "反射");
    this.addAffix(SHIELDING.get(), "保护");
    this.addAffix(WOUNDING.get(), "创伤");
    // 头衔
    this.addRank(1, "普通");
    this.addRank(2, "稀有");
    this.addRank(3, "精英");
    this.addRank(4, "传奇");
    this.addRank(5, "终极");
    // 物品
    add(CHAMPION_EGG_ITEM.get(), "强敌蛋");
    // 物品说明
    add("item.champions.egg.tooltip", "随机词缀");
    // 实体类型
    add(ModEntityTypes.ENKINDLING_BULLET.get(), "火焰弹");
    add(ARCTIC_BULLET.get(), "寒冰弹");
    // 状态效果
    add(PARALYSIS_EFFECT_TYPE.get(), "麻痹");
    add(WOUND_EFFECT_TYPE.get(), "创伤");
    // 伤害类型
    addDamageType(ModDamageTypes.ENKINDLING_BULLET, "%1$s被火焰击中", "%1$s在与%2$s的战斗中被火焰击中");
    addDamageType(REFLECTION_DAMAGE, "%1$s遭报应了!", "");
    // 动态命令异常类型
    add("argument.champions.affix.unknown", "未知词缀 %s");
    // 命令
    add("commands.champions.summon.success", "召唤实体 %s");
    add("commands.champions.egg.success", "创建实体 %s");
    add("command.champions.egg.unknown_entity", "未知生物");
    // 配置
    add("config.jade.plugin_champions.enable_affix_compact", "启用jade词条兼容");
    // 进度
    add("advancements.champions.kill_a_champion.title", "冠军猎人");
    add("advancements.champions.kill_a_champion.description", "击杀一个强大的敌对怪物");
    // 统计数据
    add("stat.champions.champion_mobs_killed", "冠军怪物击杀数");

  }
  /**
   * en_us
   */
  protected void addEnglishTranslations(){
    // Affix
    this.addAffix(ADAPTABLE.get(), "Adaptable");
    this.addAffix(ARCTIC.get(), "Arctic");
    this.addAffix(DAMPENING.get(), "Dampening");
    this.addAffix(DESECRATING.get(), "Desecrating");
    this.addAffix(ENKINDLING.get(), "Enkindling");
    this.addAffix(HASTY.get(), "Hasty");
    this.addAffix(INFESTED.get(), "Infested");
    this.addAffix(KNOCKING.get(), "Knocking");
    this.addAffix(LIVELY.get(), "Lively");
    this.addAffix(MAGNETIC.get(), "Magnetic");
    this.addAffix(MOLTEN.get(), "Molten");
    this.addAffix(PARALYZING.get(), "Paralyzing");
    this.addAffix(PLAGUED.get(), "Plagued");
    this.addAffix(REFLECTIVE.get(), "Reflective");
    this.addAffix(SHIELDING.get(), "Shielding");
    this.addAffix(WOUNDING.get(), "Wounding");
    // Rank
    this.addRank(1, "Common");
    this.addRank(2, "Skilled");
    this.addRank(3, "Elite");
    this.addRank(4, "Legendary");
    this.addRank(5, "Ultimate");
    // Item
    add(CHAMPION_EGG_ITEM.get(), "Champion Egg");
    // Item Tooltip
    add("item.champions.egg.tooltip", "Random Affixes");
    // Entity Type
    add(ModEntityTypes.ENKINDLING_BULLET.get(), "Enkindling bullet");
    add(ARCTIC_BULLET.get(), "Arctic bullet");
    // Effect
    add(PARALYSIS_EFFECT_TYPE.get(), "Paralysis");
    add(WOUND_EFFECT_TYPE.get(), "Wound");
    // Damage Type
    addDamageType(ModDamageTypes.ENKINDLING_BULLET, "%1$s was struck by flames", "%1$s was struck by flames whilst fighting %2$s");
    addDamageType(REFLECTION_DAMAGE, "%1$s got a taste of their own medicine", "");
    // Dynamic Command Exception Type
    add("argument.champions.affix.unknown", "Unknown affix %s");
    // Command
    add("commands.champions.summon.success", "Summoned new %s");
    add("commands.champions.egg.success", "Created new %s");
    add("command.champions.egg.unknown_entity", "Unknown entity");
    // Config
    add("config.jade.plugin_champions.enable_affix_compact", "Enable Jade affix compact");
    // Advancement
    add("advancements.champions.kill_a_champion.title", "Champion Hunter");
    add("advancements.champions.kill_a_champion.description", "Kill a powerful hostile monster");
    // Stats
    add("stat.champions.champion_mobs_killed", "Champion Mobs Killed");
  }
  /**
   * 添加伤害死亡消息翻译 Add damage type death message translation
   * @param damageType damage type Object
   * @param translate death message translation
   * @param byPlayerTranslate player killed by the player death message translation
   */
  protected void addDamageType(ResourceKey<DamageType> damageType, String translate, String byPlayerTranslate){
    add("death.attack." + damageType.location().getPath(), translate);
    add("death.attack." + damageType.location().getPath() + ".player", byPlayerTranslate);
  }
  /**
   * 添加Rank翻译 Add rank translation
   * @param level level of number
   * @param translate translation
   */
  protected void addRank(int level, String translate){
    add("rank.champions.title." + level, translate);
  }
  /**
   * 添加词缀翻译 Add affix translation
   * @param affix 词缀 affix
   * @param translate 翻译 translation
   */
  protected void addAffix(IAffix affix, String translate){
    this.add("affix", affix.getIdentifier(), translate);
  }
  /**
   * 通用翻译 Universal translation
   * @param pre 前缀 形似block
   * @param post 注册名 形似minecraft.block
   * @param translate 翻译 block.minecraft.block -> 石头
   */
  protected void add(String pre, String post, String translate){
    add(pre + ":" + post, translate);
  }
  /**
   * 通用翻译 Universal translation
   * @param pre 前缀 形似block
   * @param post 注册名 形似minecraft.block
   * @param translate 翻译 block.minecraft.block -> 石头
   */
  protected void add(String pre, ResourceLocation post, String translate){
    add(post.toLanguageKey(pre), translate);
  }
}
