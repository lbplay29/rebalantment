package net.ona.rebal;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Boolean> UNBREAK;
    public static final ModConfigSpec.ConfigValue<Double> ENCHLVL;
    public static final ModConfigSpec.ConfigValue<Boolean> REMOVELVL;
    public static final ModConfigSpec.ConfigValue<Double> DURAB;
    static {
        UNBREAK = BUILDER.comment("Removes durability from all items, including any not from vanilla").define("unbreakable_items", true);
        ENCHLVL = BUILDER.comment("Sets the level of enchantments to this number. -1 will use default mod behavior, being half of each enchantment's max level. 0 will keep the normal enchantment max levels").define("enchantment_level", (double) -1);
        REMOVELVL = BUILDER.comment("If true, all enchantments will have no levels, and only have one version of the above value, written without a level indicator").define("remove_levels", true);
        DURAB = BUILDER.comment("All items durability gets multiplied by this number").define("durability_multiplier", (double) 1);

        SPEC = BUILDER.build();
    }

}
