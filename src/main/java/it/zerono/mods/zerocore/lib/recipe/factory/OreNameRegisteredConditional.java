package it.zerono.mods.zerocore.lib.recipe.factory;

import com.google.gson.JsonObject;
import java.util.function.BooleanSupplier;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

public class OreNameRegisteredConditional implements IConditionFactory {
      public BooleanSupplier parse(JsonContext context, JsonObject json) {
            String oreName = JsonUtils.getString(json, "ore");
            return () -> {
                  return OreDictionary.doesOreNameExist(oreName);
            };
      }
}
