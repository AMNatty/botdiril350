package com.botdiril.userdata.icon;

import com.botdiril.MajorFailureException;
import org.intellij.lang.annotations.MagicConstant;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import cz.tefek.pluto.util.asl.YAMLPropertiesReader;

public class Icons
{
    public static final String XP = "XP";

    public static final String COIN = "$!currency.coin$";
    public static final String KEK = "$!currency.kek$";
    public static final String TOKEN = "$!currency.token$";
    public static final String MEGAKEK = "$!currency.megakek$";
    public static final String DUST = "$!currency.dust$";
    public static final String KEY = "$!currency.key$";


    public static final String CARDS = "$!card.cards$";

    public static final String CARD_BASIC = "$!card.rarity.basic$";

    public static final String CARD_COMMON = "$!card.rarity.common$";
    public static final String CARD_RARE = "$!card.rarity.rare$";
    public static final String CARD_LEGENDARY = "$!card.rarity.legendary$";
    public static final String CARD_ULTIMATE = "$!card.rarity.ultimate$";
    public static final String CARD_MYTHIC = "$!card.rarity.mythic$";

    public static final String CARD_LEGACY = "$!card.rarity.legacy$";
    public static final String CARD_LEGACYLEGENDARY = "$!card.rarity.legacyLegendary$";
    public static final String CARD_LIMITED = "$!card.rarity.limited$";
    public static final String CARD_UNIQUE = "$!card.rarity.unique$";


    public static final String CRATE_BASIC = "$!item.crate.basic$";
    public static final String CRATE_IRON = "$!item.crate.iron$";
    public static final String CRATE_UNCOMMON = "$!item.crate.uncommon$";
    public static final String CRATE_EPIC = "$!item.crate.epic$";
    public static final String CRATE_GOLDEN = "$!item.crate.golden$";
    public static final String CRATE_LEGENDARY = "$!item.crate.legendary$";
    public static final String CRATE_ULTIMATE = "$!item.crate.ultimate$";
    public static final String CRATE_HYPER = "$!item.crate.hyper$";
    public static final String CRATE_GLITCHY = "$!item.crate.glitchy$";
    public static final String CRATE_INFERNAL = "$!item.crate.infernal$";
    public static final String CRATE_VOID = "$!item.crate.void$";
    public static final String CRATE_LEAGUE = "$!item.crate.league$";

    public static final String CARDPACK_BASIC = "$!item.cardPack.basic$";
    public static final String CARDPACK_NORMAL = "$!item.cardPack.normal$";
    public static final String CARDPACK_GOOD = "$!item.cardPack.good$";
    public static final String CARDPACK_VOID = "$!item.cardPack.void$";

    public static final String PICKAXE_I = "$!item.pickaxe.I$";
    public static final String PICKAXE_II = "$!item.pickaxe.II$";
    public static final String PICKAXE_III = "$!item.pickaxe.III$";
    public static final String PICKAXE_IV = "$!item.pickaxe.IV$";
    public static final String PICKAXE_V = "$!item.pickaxe.V$";

    public static final String GEM_GREEN = "$!item.gem.green$";
    public static final String GEM_RED = "$!item.gem.red$";
    public static final String GEM_PURPLE = "$!item.gem.purple$";
    public static final String GEM_BLUE = "$!item.gem.blue$";
    public static final String GEM_BLACK = "$!item.gem.black$";
    public static final String GEM_RAINBOW = "$!item.gem.rainbow$";

    public static final String MINE_COAL = "$!item.mineral.coal$";
    public static final String MINE_COPPER = "$!item.mineral.copper$";
    public static final String MINE_IRON = "$!item.mineral.iron$";
    public static final String MINE_GOLD = "$!item.mineral.gold$";
    public static final String MINE_PLATINUM = "$!item.mineral.platinum$";
    public static final String MINE_URANIUM = "$!item.mineral.uranium$";
    public static final String MINE_KEKIUM = "$!item.mineral.kekium$";
    public static final String MINE_EMERALD = "$!item.mineral.emerald$";
    public static final String MINE_DIAMOND = "$!item.mineral.diamond$";

    public static final String RARE_GEMDIRIL = "$!item.rareResource.gemdiril$";
    public static final String RARE_TIMEWARP = "$!item.rareResource.timewarp$";
    public static final String RARE_OIL = "$!item.rareResource.oil$";
    public static final String RARE_GOLDENOIL = "$!item.rareResource.goldenOil$";
    public static final String RARE_STRANGE_METAL = "$!item.rareResource.strangeMetal$";

    public static final String SCROLL = "$!item.scroll.basic$";
    public static final String SCROLL_RARE = "$!item.scroll.rare$";
    public static final String SCROLL_UNIQUE = "$!item.scroll.unique$";

    public static final String ITEM_MISC_TOOLBOX = "$!item.misc.toolBox$";
    public static final String ITEM_MISC_REPAIR_KIT = "$!item.misc.repairKit$";
    public static final String ITEM_MISC_TRASH = "$!item.misc.trash$";

    public static final String ACHIEVEMENT_BOT = "$!achievement.bot$";
    public static final String ACHIEVEMENT_BETA = "$!achievement.beta$";

    public static final String OTHER_KEKOVERDRIVE = "$!other.kekOverdrive$";
    public static final String OTHER_THEFAST = "$!other.theFast$";
    public static final String OTHER_MAX = "$!other.max$";

    public static List<String> SKINS;

    private static final Path ICONS_CONFIG_FILE = Path.of("assets/icons.yaml");
    private static final Path SKINS_CONFIG_FILE = Path.of("assets/kek-skins.yaml");

    private static final Map<String, String> ICON_MAPPINGS = new HashMap<>();

    public static void load()
    {
        try
        {
            var mappings = YAMLPropertiesReader.loadFromFile(ICONS_CONFIG_FILE);
            mappings.forEach((k, v) -> ICON_MAPPINGS.put("$!%s$".formatted(k), v));

            try (var br = Files.newBufferedReader(SKINS_CONFIG_FILE))
            {
                var constructor = new Constructor(String[].class);
                var yaml = new Yaml(constructor);
                SKINS = Arrays.asList(yaml.load(br));
            }
        }
        catch (Exception e)
        {
            throw new MajorFailureException("An exception has occurred while loading icons", e);
        }
    }

    public static String get(@MagicConstant(flagsFromClass = Icons.class) String tag)
    {
        return ICON_MAPPINGS.get(tag);
    }

    public static String getOrDefault(@MagicConstant(flagsFromClass = Icons.class) String tag, String defaultValue)
    {
        return ICON_MAPPINGS.getOrDefault(tag, defaultValue);
    }
}
