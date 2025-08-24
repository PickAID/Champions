package top.theillusivec4.champions.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.LanguageProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.registry.ModDamageTypes;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import static top.theillusivec4.champions.common.registry.AffixTypes.*;
import static top.theillusivec4.champions.common.registry.ModDamageTypes.REFLECTION_DAMAGE;
import static top.theillusivec4.champions.common.registry.ModEntityTypes.ARCTIC_BULLET;
import static top.theillusivec4.champions.common.registry.ModItems.CHAMPION_EGG_ITEM;
import static top.theillusivec4.champions.common.registry.ModMobEffects.PARALYSIS_EFFECT_TYPE;
import static top.theillusivec4.champions.common.registry.ModMobEffects.WOUND_EFFECT_TYPE;

public class ModLanguageProvider extends LanguageProvider {
    final private String locale;

    public ModLanguageProvider(PackOutput output, String locale) {
        this(output, Champions.MODID, locale);
    }

    public ModLanguageProvider(PackOutput output) {
        this(output, "en_us");
    }

    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.locale = locale;
    }


    @Override
    protected void addTranslations() {
        switch (this.locale) {
            case "zh_cn":
                this.addChineseTranslations();
                break;
            case "ko_kr":
                this.addKoreaTranslations();
                break;
            case "ru_ru":
                this.addRussianTranslations();
                break;
            case "tr_tr":
                this.addTurkishTranslations();
                break;
            case "uk_ua":
                this.addUkrainianTranslations();
                break;
	        case "pt_br":
		        this.addBrazilianPortugueseTranslations();
		        break;
            case "en_us":
            default:
                this.addEnglishTranslations();
        }
    }

	private void addBrazilianPortugueseTranslations() {
		this.addAffix(ADAPTABLE.get(), "Adaptável");
		this.addAffix(ARCTIC.get(), "Ártico");
		this.addAffix(DAMPENING.get(), "Amortecedor");
		this.addAffix(DESECRATING.get(), "Profanador");
		this.addAffix(ENKINDLING.get(), "Incendiário");
		this.addAffix(HASTY.get(), "Apresado");
		this.addAffix(INFESTED.get(), "Infestado");
		this.addAffix(KNOCKING.get(), "Empurrador");
		this.addAffix(LIVELY.get(), "Vigoroso");
		this.addAffix(MAGNETIC.get(), "Magnético");
		this.addAffix(MOLTEN.get(), "Fundido");
		this.addAffix(PARALYZING.get(), "Paralisante");
		this.addAffix(PLAGUED.get(), "Pestilento");
		this.addAffix(REFLECTIVE.get(), "Reflexivo");
		this.addAffix(SHIELDING.get(), "Protetor");
		this.addAffix(WOUNDING.get(), "Dilacerante");
		// Rank
		this.addRank(1, "Comum");
		this.addRank(2, "Habilidoso");
		this.addRank(3, "Elite");
		this.addRank(4, "Lendário");
		this.addRank(5, "Supremo");
		// Item
		add(CHAMPION_EGG_ITEM.get(), "Ovo de Campeão");
		// Item Tooltip
		add("item.champions.egg.tooltip", "Afixos Aleatórios");
		// Entity Type
		add(ModEntityTypes.ENKINDLING_BULLET.get(), "Projétil Incendiário");
		add(ARCTIC_BULLET.get(), "Projétil Ártico");
		// Effect
		add(PARALYSIS_EFFECT_TYPE.get(), "Paralisia");
		add(WOUND_EFFECT_TYPE.get(), "Ferimento");
		// Damage Type
		addDamageType(ModDamageTypes.ENKINDLING_BULLET, "%1$s foi atingido por chamas", "%1$s foi atingido por chamas enquanto lutava contra %2$s");
		addDamageType(REFLECTION_DAMAGE, "%1$s provou do próprio veneno", "");
		// Dynamic Command Exception Type
		add("argument.champions.affix.unknown", "Afixo desconhecido %s");
		// Command
		add("commands.champions.summon.success", "Invocado novo %s");
		add("commands.champions.egg.success", "Criado novo %s");
		add("command.champions.egg.unknown_entity", "Entidade desconhecida");
		// Config
		add("config.jade.plugin_champions.enable_affix_compact", "Ativar visualização compacta de afixos no Jade");
		// Advancement
		add("advancements.champions.kill_a_champion.title", "Caçador de Campeões");
		add("advancements.champions.kill_a_champion.description", "Mate um monstro hostil poderoso");
		// Stats
		add("stat.champions.champion_mobs_killed", "Campeões Derrotados");
	}

	/**
     * uk_ua
     */
    private void addUkrainianTranslations() {
        // Affix
        this.addAffix(ADAPTABLE.get(), "Адаптований");
        this.addAffix(ARCTIC.get(), "Арктика");
        this.addAffix(DAMPENING.get(), "Зволоження");
        this.addAffix(DESECRATING.get(), "Desecrating");
        this.addAffix(ENKINDLING.get(), "Осквернення");
        this.addAffix(HASTY.get(), "Поспішно");
        this.addAffix(INFESTED.get(), "Заражений");
        this.addAffix(KNOCKING.get(), "Стукіт");
        this.addAffix(LIVELY.get(), "Жвавий");
        this.addAffix(MAGNETIC.get(), "Магнітний");
        this.addAffix(MOLTEN.get(), "Розплавлений");
        this.addAffix(PARALYZING.get(), "Паралізуючий");
        this.addAffix(PLAGUED.get(), "Заражений");
        this.addAffix(REFLECTIVE.get(), "Світловідбиваючі");
        this.addAffix(SHIELDING.get(), "Екранізація");
        this.addAffix(WOUNDING.get(), "Пораненний");
        // Rank
        this.addRank(1, "Звичайний");
        this.addRank(2, "Кваліфікований");
        this.addRank(3, "Еліта");
        this.addRank(4, "Легендарний");
        this.addRank(5, "Кінцевий");
        // Item
        add(CHAMPION_EGG_ITEM.get(), "Чемпіонське яйце");
        // Item Tooltip
        add("item.champions.egg.tooltip", "Випадкові афікси");
        // Entity Type
        add(ModEntityTypes.ENKINDLING_BULLET.get(), "Enkindling bullet");
        add(ARCTIC_BULLET.get(), "Arctic bullet");
        // Effect
        add(PARALYSIS_EFFECT_TYPE.get(), "Параліч");
        add(WOUND_EFFECT_TYPE.get(), "Поранення");
        // Damage Type
        addDamageType(ModDamageTypes.ENKINDLING_BULLET, "%1$s був охоплений полум'ям", "%1$s був охоплений полум'ям під час бою %2$s");
        addDamageType(REFLECTION_DAMAGE, "%1$s відчули смак власних ліків", "");
        // Dynamic Command Exception Type
        add("argument.champions.affix.unknown", "Невідомий афікс %s");
        // Command
        add("commands.champions.summon.success", "Викликається новий %s");
        add("commands.champions.egg.success", "Створено новий %s");
        add("command.champions.egg.unknown_entity", "Невідомий суб'єкт");
        // Config
        add("config.jade.plugin_champions.enable_affix_compact", "Enable Jade affix compact");
        // Advancement
        add("advancements.champions.kill_a_champion.title", "Чемпіонський мисливець");
        add("advancements.champions.kill_a_champion.description", "Убийте потужного ворожого монстра");
        // Stats
        add("stat.champions.champion_mobs_killed", "Чемпіонські натовпи вбито");
    }

    /**
     * zh_cn
     */
    protected void addChineseTranslations() {
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
    protected void addEnglishTranslations() {
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
     * tr_tr
     */
    protected void addTurkishTranslations() {
        // Affix
        this.addAffix(ADAPTABLE.get(), "Adaptif");
        this.addAffix(ARCTIC.get(), "Buz-gibi");
        this.addAffix(DAMPENING.get(), "Soğurucu");
        this.addAffix(DESECRATING.get(), "Saygısız");
        this.addAffix(ENKINDLING.get(), "Tutuşturucu");
        this.addAffix(HASTY.get(), "Aceleci");
        this.addAffix(INFESTED.get(), "Böcekli");
        this.addAffix(KNOCKING.get(), "Tepici");
        this.addAffix(LIVELY.get(), "Yaşam-dolu");
        this.addAffix(MAGNETIC.get(), "Manyetik");
        this.addAffix(MOLTEN.get(), "Erimiş");
        this.addAffix(PARALYZING.get(), "Felç-edici");
        this.addAffix(PLAGUED.get(), "Hastalıklı");
        this.addAffix(REFLECTIVE.get(), "Yansıtıcı");
        this.addAffix(SHIELDING.get(), "Korumalı");
        this.addAffix(WOUNDING.get(), "Yaralayıcı");
        // Rank
        this.addRank(1, "Yaygın");
        this.addRank(2, "Yetenekli");
        this.addRank(3, "Elit");
        this.addRank(4, "Efsanevi");
        this.addRank(5, "Nihai");
        // Item
        add(CHAMPION_EGG_ITEM.get(), "Şampiyon Yumurtası");
        // Item Tooltip
        add("item.champions.egg.tooltip", "Rasgele Özellikli");
        // Entity Type
        add(ModEntityTypes.ENKINDLING_BULLET.get(), "Enkindling bullet");
        add(ARCTIC_BULLET.get(), "Arctic bullet");
        // Effect
        add(PARALYSIS_EFFECT_TYPE.get(), "Felç");
        add(WOUND_EFFECT_TYPE.get(), "Yaralanma");
        // Damage Type
        addDamageType(ModDamageTypes.ENKINDLING_BULLET, "%1$s yandı", "%1$s ,  %2$s ile savaşırken yandı.");
        addDamageType(REFLECTION_DAMAGE, "%1$s kendi ilacının tadına baktı", "");
        // Dynamic Command Exception Type
        add("argument.champions.affix.unknown", "%s - Bilinmeyen özellik");
        // Command
        add("commands.champions.summon.success", "Yeni %s çağrıldı");
        add("commands.champions.egg.success", "Yeni %s oluşturuldu");
        add("command.champions.egg.unknown_entity", "Bilinmeyen Yaratık");
        // Config
        add("config.jade.plugin_champions.enable_affix_compact", "Enable Jade affix compact");
        // Advancement
        add("advancements.champions.kill_a_champion.title", "Şampiyon Avcısı");
        add("advancements.champions.kill_a_champion.description", "Güçlü bir düşman öldür");
        // Stats
        add("stat.champions.champion_mobs_killed", "Öldürülen Şampiyon Sayısı");
    }

    /**
     * ru_ru
     */
    protected void addRussianTranslations() {
        // Affix
        this.addAffix(ADAPTABLE.get(), "Адаптируемый");
        this.addAffix(ARCTIC.get(), "Снежный");
        this.addAffix(DAMPENING.get(), "Водянистый");
        this.addAffix(DESECRATING.get(), "Оскверненный");
        this.addAffix(ENKINDLING.get(), "Раскалённый");
        this.addAffix(HASTY.get(), "Ловкий");
        this.addAffix(INFESTED.get(), "Зараженный");
        this.addAffix(KNOCKING.get(), "Отбивающий");
        this.addAffix(LIVELY.get(), "Живучий");
        this.addAffix(MAGNETIC.get(), "Магнитный");
        this.addAffix(MOLTEN.get(), "Расплавленный");
        this.addAffix(PARALYZING.get(), "Парализующий");
        this.addAffix(PLAGUED.get(), "Чумной");
        this.addAffix(REFLECTIVE.get(), "Рефлекторный");
        this.addAffix(SHIELDING.get(), "Укрепленный");
        this.addAffix(WOUNDING.get(), "Убойный");
        // Rank
        this.addRank(1, "Обыкновенный");
        this.addRank(2, "Умелый");
        this.addRank(3, "Элитный");
        this.addRank(4, "Легендарный");
        this.addRank(5, "Ультимативный");
        // Item
        add(CHAMPION_EGG_ITEM.get(), "Яйцо Чемпионского Моба");
        // Item Tooltip
        add("item.champions.egg.tooltip", "Моб с случайными усиливающими особенностями");
        // Entity Type
        add(ModEntityTypes.ENKINDLING_BULLET.get(), "Enkindling bullet");
        add(ARCTIC_BULLET.get(), "Arctic bullet");
        // Effect
        add(PARALYSIS_EFFECT_TYPE.get(), "Паралич");
        add(WOUND_EFFECT_TYPE.get(), "Ранение");
        // Damage Type
        addDamageType(ModDamageTypes.ENKINDLING_BULLET, "%1$s was struck by flames", "%1$s was struck by flames whilst fighting %2$s");
        addDamageType(REFLECTION_DAMAGE, "%1$s попробовал на вкус собственное лекарство", "");
        // Dynamic Command Exception Type
        add("argument.champions.affix.unknown", "Unknown affix %s");
        // Command
        add("commands.champions.summon.success", "Призван новый %s");
        add("commands.champions.egg.success", "Рожден новый %s");
        add("command.champions.egg.unknown_entity", "Unknown entity");
        // Config
        add("config.jade.plugin_champions.enable_affix_compact", "Enable Jade affix compact");
        // Advancement
        add("advancements.champions.kill_a_champion.title", "Охотник на Чемпионов");
        add("advancements.champions.kill_a_champion.description", "Убейте чемпионского моба");
        // Stats
        add("stat.champions.champion_mobs_killed", "Убито чемпионских мобов");
    }

    /**
     * ko_kr
     */
    protected void addKoreaTranslations() {
        // Affix
        this.addAffix(ADAPTABLE.get(), "적응");
        this.addAffix(ARCTIC.get(), "극점");
        this.addAffix(DAMPENING.get(), "감쇠");
        this.addAffix(DESECRATING.get(), "모독");
        this.addAffix(ENKINDLING.get(), "맹화");
        this.addAffix(HASTY.get(), "신속");
        this.addAffix(INFESTED.get(), "감염");
        this.addAffix(KNOCKING.get(), "넉백");
        this.addAffix(LIVELY.get(), "활력");
        this.addAffix(MAGNETIC.get(), "자력");
        this.addAffix(MOLTEN.get(), "융해");
        this.addAffix(PARALYZING.get(), "마비");
        this.addAffix(PLAGUED.get(), "질병");
        this.addAffix(REFLECTIVE.get(), "반사");
        this.addAffix(SHIELDING.get(), "방패");
        this.addAffix(WOUNDING.get(), "상처");
        // Rank
        this.addRank(1, "평범한");
        this.addRank(2, "숙련된");
        this.addRank(3, "엘리트");
        this.addRank(4, "전설적인");
        this.addRank(5, "궁극의");
        // Item
        add(CHAMPION_EGG_ITEM.get(), "챔피언 생성 알");
        // Item Tooltip
        add("item.champions.egg.tooltip", "무작위 수식어");
        // Entity Type
        add(ModEntityTypes.ENKINDLING_BULLET.get(), "Enkindling bullet");
        add(ARCTIC_BULLET.get(), "Arctic bullet");
        // Effect
        add(PARALYSIS_EFFECT_TYPE.get(), "마비");
        add(WOUND_EFFECT_TYPE.get(), "상처");
        // Damage Type
        addDamageType(ModDamageTypes.ENKINDLING_BULLET, "%1$s은(는) 불타올랐습니다", "%1$s은(는) %2$s과(와) 싸우다가 불타올랐습니다");
        addDamageType(REFLECTION_DAMAGE, "%1$s은(는) 자기 꾀에 자기가 넘어갔습니다", "");
        // Dynamic Command Exception Type
        add("argument.champions.affix.unknown", "알 수 없는 수식어 %s");
        // Command
        add("commands.champions.summon.success", "새로운 %s을(를) 소환");
        add("commands.champions.egg.success", "새로운 %s을(를) 생성");
        add("command.champions.egg.unknown_entity", "Unknown entity");
        // Config
        add("config.jade.plugin_champions.enable_affix_compact", "Enable Jade affix compact");
        // Advancement
        add("advancements.champions.kill_a_champion.title", "챔피언 사냥꾼");
        add("advancements.champions.kill_a_champion.description", "강력한 적대적 몬스터를 죽이세요");
        // Stats
        add("stat.champions.champion_mobs_killed", "챔피언 처치");
    }

    /**
     * 添加伤害死亡消息翻译 Add damage type death message translation
     *
     * @param damageType        damage type Object
     * @param translate         death message translation
     * @param byPlayerTranslate player killed by the player death message translation
     */
    protected void addDamageType(ResourceKey<DamageType> damageType, String translate, String byPlayerTranslate) {
        add("death.attack." + damageType.location().getPath(), translate);
        add("death.attack." + damageType.location().getPath() + ".player", byPlayerTranslate);
    }

    /**
     * 添加Rank翻译 Add rank translation
     *
     * @param level     level of number
     * @param translate translation
     */
    protected void addRank(int level, String translate) {
        add("rank.champions.title." + level, translate);
    }

    /**
     * 添加词缀翻译 Add affix translation
     *
     * @param affix     词缀 affix
     * @param translate 翻译 translation
     */
    protected void addAffix(IAffix affix, String translate) {
        this.add("affix", affix.getIdentifier(), translate);
    }

    /**
     * 通用翻译 Universal translation
     *
     * @param pre       前缀 形似block
     * @param post      注册名 形似minecraft.block
     * @param translate 翻译 block.minecraft.block -> 石头
     */
    protected void add(String pre, String post, String translate) {
        add(pre + ":" + post, translate);
    }

    /**
     * 通用翻译 Universal translation
     *
     * @param pre       前缀 形似block
     * @param post      注册名 形似minecraft.block
     * @param translate 翻译 block.minecraft.block -> 石头
     */
    protected void add(String pre, ResourceLocation post, String translate) {
        add(post.toLanguageKey(pre), translate);
    }
}
