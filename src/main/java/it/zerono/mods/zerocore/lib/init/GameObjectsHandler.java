package it.zerono.mods.zerocore.lib.init;

import com.google.common.collect.*;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import it.zerono.mods.zerocore.lib.config.ConfigHandler;
import it.zerono.mods.zerocore.lib.data.fixer.GameObjectWalker;
import it.zerono.mods.zerocore.lib.data.fixer.IGameObjectDataWalker;
import it.zerono.mods.zerocore.util.CodeHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Action;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GameObjectsHandler implements IModInitializationHandler {
      private final String _modId;
      private final ImmutableList _configHandlers;
      private ImmutableMap _blocks;
      private ImmutableMap _items;
      private final List _blocksMappers = Lists.newArrayList();
      private final List _itemsMappers = Lists.newArrayList();
      private final int _dataVersion;
      private ModFixs _modFixs;
      private GameObjectWalker _tilesWalker;

      protected GameObjectsHandler(int dataVersion, ConfigHandler... configs) {
            this._dataVersion = dataVersion;
            this._modId = CodeHelper.getModIdFromActiveModContainer();
            this._configHandlers = null != configs ? ImmutableList.copyOf(configs) : ImmutableList.of();
            this._tilesWalker = null;
            this.addBlockRemapper(new GameObjectsHandler.LowerCaseMapper());
            this.addItemRemapper(new GameObjectsHandler.LowerCaseMapper());
            MinecraftForge.EVENT_BUS.register(this);
            this.syncConfigHandlers();
      }

      @Nonnull
      protected String getModId() {
            return this._modId;
      }

      protected int getDataVersion() {
            return this._dataVersion;
      }

      protected void onRegisterBlocks(@Nonnull IForgeRegistry registry) {
      }

      protected void onRegisterTileEntities() {
      }

      protected void onRegisterItems(@Nonnull IForgeRegistry registry) {
      }

      protected void onRegisterOreDictionaryEntries() {
      }

      protected void onRegisterRecipes(@Nonnull IForgeRegistry registry) {
      }

      protected void onRegisterDataFixers() {
            if (null != this._tilesWalker) {
                  this.registerDataWalker(FixTypes.BLOCK_ENTITY, this._tilesWalker);
            }

      }

      protected void addBlockRemapper(@Nonnull IGameObjectMapper remapper) {
            this._blocksMappers.add(remapper);
      }

      protected void addItemRemapper(@Nonnull IGameObjectMapper remapper) {
            this._itemsMappers.add(remapper);
      }

      protected void registerTileEntity(@Nonnull Class tileEntityClass) {
            this.registerTileEntity(tileEntityClass, (IGameObjectDataWalker)null);
      }

      protected void registerTileEntity(@Nonnull Class tileEntityClass, @Nullable IGameObjectDataWalker walker) {
            ResourceLocation id = new ResourceLocation(this.getModId(), tileEntityClass.getSimpleName());
            GameRegistry.registerTileEntity(tileEntityClass, id);
            if (null != walker) {
                  if (null == this._tilesWalker) {
                        this._tilesWalker = new GameObjectWalker();
                  }

                  this._tilesWalker.addObjectWalker(id, walker);
            }

      }

      protected void registerDataFixer(@Nonnull FixTypes type, @Nonnull IFixableData fixer) {
            this.getModFixs().registerFix(type, fixer);
      }

      protected void registerDataWalker(@Nonnull FixTypes type, @Nonnull IDataWalker walker) {
            FMLCommonHandler.instance().getDataFixer().registerWalker(type, walker);
      }

      @Nullable
      protected Block getTrackedBlock(@Nonnull String name) {
            return (Block)this._blocks.get(name);
      }

      @Nullable
      protected Item getTrackedItem(@Nonnull String name) {
            return (Item)this._items.get(name);
      }

      @SubscribeEvent
      protected void onMissinBlockMappings(MissingMappings event) {
            remapObjects(this._blocksMappers, event.getMappings());
      }

      @SubscribeEvent
      protected void onMissingItemMapping(MissingMappings event) {
            remapObjects(this._itemsMappers, event.getMappings());
      }

      @SubscribeEvent
      protected void onConfigChangedFromGUI(OnConfigChangedEvent event) {
            if (this._modId.equalsIgnoreCase(event.getModID())) {
                  this.syncConfigHandlers();
                  this.notifyConfigListeners();
            }

      }

      public void onPreInit(FMLPreInitializationEvent event) {
      }

      public void onInit(FMLInitializationEvent event) {
            this.notifyConfigListeners();
            this.onRegisterDataFixers();
      }

      public void onPostInit(FMLPostInitializationEvent event) {
      }

      private static void raiseRegisterItemBlocks(@Nonnull ImmutableCollection objects, @Nonnull IForgeRegistry registry) {
            UnmodifiableIterator var2 = objects.iterator();

            while(var2.hasNext()) {
                  IForgeRegistryEntry object = (IForgeRegistryEntry)var2.next();
                  if (object instanceof IGameObject) {
                        ((IGameObject)object).onRegisterItemBlocks(registry);
                  }
            }

      }

      private static void raiseRegisterOreDictionaryEntries(@Nonnull ImmutableCollection objects) {
            UnmodifiableIterator var1 = objects.iterator();

            while(var1.hasNext()) {
                  IForgeRegistryEntry object = (IForgeRegistryEntry)var1.next();
                  if (object instanceof IGameObject) {
                        ((IGameObject)object).onRegisterOreDictionaryEntries();
                  }
            }

      }

      private static void raiseRegisterRecipes(@Nonnull ImmutableCollection objects, @Nonnull IForgeRegistry registry) {
            UnmodifiableIterator var2 = objects.iterator();

            while(var2.hasNext()) {
                  IForgeRegistryEntry object = (IForgeRegistryEntry)var2.next();
                  if (object instanceof IGameObject) {
                        ((IGameObject)object).onRegisterRecipes(registry);
                  }
            }

      }

      @SideOnly(Side.CLIENT)
      private static void raiseRegisterModels(@Nonnull ImmutableCollection objects) {
            UnmodifiableIterator var1 = objects.iterator();

            while(var1.hasNext()) {
                  IForgeRegistryEntry object = (IForgeRegistryEntry)var1.next();
                  if (object instanceof IGameObject) {
                        ((IGameObject)object).onRegisterModels();
                  }
            }

      }

      private static void remapObjects(@Nonnull List mappers, @Nonnull ImmutableList mappings) {
            UnmodifiableIterator var2 = mappings.iterator();

            while(var2.hasNext()) {
                  Mapping mapping = (Mapping)var2.next();
                  Iterator var4 = mappers.iterator();

                  while(var4.hasNext()) {
                        IGameObjectMapper mapper = (IGameObjectMapper)var4.next();
                        mapper.remap(mapping);
                        if (Action.DEFAULT != mapping.getAction()) {
                              break;
                        }
                  }
            }

      }

      private void syncConfigHandlers() {
            UnmodifiableIterator var1 = this._configHandlers.iterator();

            while(var1.hasNext()) {
                  ConfigHandler handler = (ConfigHandler)var1.next();
                  handler.sync();
            }

      }

      private void notifyConfigListeners() {
            UnmodifiableIterator var1 = this._configHandlers.iterator();

            while(var1.hasNext()) {
                  ConfigHandler handler = (ConfigHandler)var1.next();
                  handler.notifyListeners();
            }

      }

      @Nonnull
      private ModFixs getModFixs() {
            if (null == this._modFixs) {
                  this._modFixs = FMLCommonHandler.instance().getDataFixer().init(this.getModId(), this.getDataVersion());
            }

            return this._modFixs;
      }

      private static class LowerCaseMapper implements IGameObjectMapper {
            private ImmutableMap _map;

            private LowerCaseMapper() {
            }

            public void linkObjectsMap(@Nonnull ImmutableMap map) {
                  this._map = map;
            }

            public void remap(@Nonnull Mapping mapping) {
                  String candidateName = mapping.key.getPath().toLowerCase();
                  if (this._map.containsKey(candidateName)) {
                        IForgeRegistryEntry replacement = (IForgeRegistryEntry)this._map.get(candidateName);
                        mapping.remap(replacement);
                  }

            }

            // $FF: synthetic method
            LowerCaseMapper(Object x0) {
                  this();
            }
      }

      private static class TrackingForgeRegistry implements IForgeRegistry {
            private final IForgeRegistry _registry;
            private final Map _trackedObjects;

            public TrackingForgeRegistry(@Nonnull IForgeRegistry registry) {
                  this._registry = registry;
                  this._trackedObjects = Maps.newHashMap();
            }

            @Nonnull
            public ImmutableMap getTrackedObjects() {
                  return ImmutableMap.copyOf(this._trackedObjects);
            }

            public Class getRegistrySuperType() {
                  return this._registry.getRegistrySuperType();
            }

            public void register(IForgeRegistryEntry value) {
                  this._registry.register(value);
                  this._trackedObjects.put(value.getRegistryName().getPath(), value);
            }

            public void registerAll(IForgeRegistryEntry[] values) {
                  IForgeRegistryEntry[] var2 = values;
                  int var3 = values.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                        IForgeRegistryEntry value = var2[var4];
                        this.register(value);
                  }

            }

            public boolean containsKey(ResourceLocation key) {
                  return this._registry.containsKey(key);
            }

            public boolean containsValue(IForgeRegistryEntry value) {
                  return this._registry.containsValue(value);
            }

            @Nullable
            public IForgeRegistryEntry getValue(ResourceLocation key) {
                  return this._registry.getValue(key);
            }

            @Nullable
            public ResourceLocation getKey(IForgeRegistryEntry value) {
                  return this._registry.getKey(value);
            }

            @Nonnull
            public Set getKeys() {
                  return this._registry.getKeys();
            }

            @Nonnull
            public List getValues() {
                  return this._registry.getValues();
            }

            @Nonnull
            public Set getEntries() {
                  return this._registry.getEntries();
            }

            public Object getSlaveMap(ResourceLocation slaveMapName, Class type) {
                  return this._registry.getSlaveMap(slaveMapName, type);
            }

            public Iterator iterator() {
                  return this._registry.iterator();
            }
      }
}
