package it.zerono.mods.zerocore.api.multiblock.tier;

import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.HashMap;
import javax.annotation.Nonnull;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class MultiblockDescriptor {
      private final Class _controllerClass;
      private final Class _tierClass;
      private final EnumSet _validTiers;
      private final EnumSet _activeTiers;
      private final HashMap _tierMetaMap;
      private final Enum _defaultTier;
      private final HashMap _tierData;
      private PropertyEnum _tierProperty;

      @Nonnull
      public Class getMultiblockControllerType() {
            return this._controllerClass;
      }

      @Nonnull
      public MultiblockControllerBase createMultiblockController(@Nonnull World world) {
            try {
                  Constructor constructor = this._controllerClass.getConstructor(World.class);
                  return (MultiblockControllerBase)constructor.newInstance(world);
            } catch (NoSuchMethodException var3) {
                  ZeroCore.getLogger().error("No suitable constructor the found in Multiblock Controller class %s", this._controllerClass.getName());
                  throw new RuntimeException("Multiblock Controller creation failed", var3);
            } catch (InstantiationException var4) {
                  ZeroCore.getLogger().error("Failed to instantiate the Multiblock Controller class %s", this._controllerClass.getName());
                  throw new RuntimeException("Multiblock Controller creation failed", var4);
            } catch (IllegalAccessException var5) {
                  ZeroCore.getLogger().error("Unable to access the constructor of the Multiblock Controller class %s", this._controllerClass.getName());
                  throw new RuntimeException("Multiblock Controller creation failed", var5);
            } catch (InvocationTargetException var6) {
                  ZeroCore.getLogger().error("Error caught while constructing the Multiblock Controller class %s", this._controllerClass.getName());
                  throw new RuntimeException("Multiblock Controller creation failed", var6);
            }
      }

      @Nonnull
      public TierDescriptor getTierDescriptor(@Nonnull Enum tier) {
            TierDescriptor descriptor = (TierDescriptor)this._tierData.get(tier);
            if (null == descriptor) {
                  throw new RuntimeException(String.format("No descriptor exist for tier %s", ((IStringSerializable)tier).getName()));
            } else {
                  return descriptor;
            }
      }

      @Nonnull
      public EnumSet getValidTiers() {
            return this._validTiers;
      }

      @Nonnull
      public EnumSet getActiveTiers() {
            return this._activeTiers;
      }

      @Nonnull
      public Enum getTierFromMeta(int meta) {
            Enum tier = (Enum)this._tierMetaMap.get(meta);
            return null != tier ? tier : this._defaultTier;
      }

      @Nonnull
      public Enum getDefaultTier() {
            return this._defaultTier;
      }

      @Nonnull
      public PropertyEnum getTierProperty() {
            if (null == this._tierProperty) {
                  throw new RuntimeException("Tier blockstate property not yet build!");
            } else {
                  return this._tierProperty;
            }
      }

      protected MultiblockDescriptor(@Nonnull Class controllerClass, @Nonnull Enum defaultTier, @Nonnull Class tierClass) {
            this._controllerClass = controllerClass;
            this._tierClass = tierClass;
            this._validTiers = EnumSet.noneOf(tierClass);
            this._activeTiers = EnumSet.noneOf(tierClass);
            this._tierMetaMap = new HashMap();
            this._defaultTier = defaultTier;
            this._tierData = new HashMap();
      }

      protected void addTier(boolean active, @Nonnull Enum tier) {
            this._validTiers.add(tier);
            this._tierMetaMap.put(((IPropertyValue)tier).toMeta(), tier);
            if (active) {
                  this._activeTiers.add(tier);
            }

      }

      protected void buildProperties() {
            this._tierProperty = PropertyEnum.create("tier", this._tierClass, this._activeTiers);
      }

      protected void addTierDescriptor(@Nonnull TierDescriptor descriptor) {
            this._tierData.put(descriptor.Tier, descriptor);
      }
}
