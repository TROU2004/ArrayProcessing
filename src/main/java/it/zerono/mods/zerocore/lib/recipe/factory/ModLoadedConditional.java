package it.zerono.mods.zerocore.lib.recipe.factory;

import com.google.gson.JsonObject;
import java.util.function.BooleanSupplier;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;

public class ModLoadedConditional implements IConditionFactory {
      public BooleanSupplier parse(JsonContext context, JsonObject json) {
            String modId = JsonUtils.getString(json, "modid");
            return () -> {
                  return Loader.isModLoaded(modId);
            };
      }
}
