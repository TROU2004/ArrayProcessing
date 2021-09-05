package trou.array_processing;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import trou.array_processing.block.*;
import trou.array_processing.tile.TileArrayEnergyHatch;
import trou.array_processing.tile.TileArrayItemHatch;
import trou.array_processing.tile.TileEntityBase;

import java.util.ArrayList;
import java.util.Locale;

@Mod(modid = ArrayProcessing.MODID, name = ArrayProcessing.NAME, version = ArrayProcessing.VERSION, dependencies = "required-after:zerocoreap")
@Mod.EventBusSubscriber(modid = ArrayProcessing.MODID)
public class ArrayProcessing {
    public static final String MODID = "array_processing";
    public static final String NAME = "Array Processing";
    public static final String VERSION = "1.0.0";
    public static final CreativeTabs TAB = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BLOCK_LIST[0]);
        }
    };
    private Logger logger;
    public static final ArrayList<Item> ITEM_LIST = new ArrayList<>();
    public static final ArrayList<Class<? extends TileEntity>> TILEENTITY_LIST = new ArrayList<>();
    public static final Block[] BLOCK_LIST = new Block[]{
            new BlockArrayWall(),
            new BlockArrayGlass(),
            new BlockArrayInputHatch(),
            new BlockArrayOutputHatch(),
            new BlockArrayEnergyHatch()
    };

    @Mod.Instance
    public static ArrayProcessing INSTANCE;

    public ArrayProcessing() {
        TILEENTITY_LIST.add(TileArrayItemHatch.class);
        TILEENTITY_LIST.add(TileArrayEnergyHatch.class);
        TILEENTITY_LIST.add(TileEntityBase.class);
        for (Block block : BLOCK_LIST) {
            if (block.getRegistryName() != null) {
                ITEM_LIST.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        info(Loader.isModLoaded("zerocore") ? "There are two zerocore running in your game, may causes problems." : "ArrayProcessing is running with zerocoreap.");
    }

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
        for (Block block : BLOCK_LIST) {
            event.getRegistry().register(block);
        }
        for (Class<? extends TileEntity> clazz : TILEENTITY_LIST) {
            GameRegistry.registerTileEntity(clazz, new ResourceLocation(ArrayProcessing.MODID, clazz.getSimpleName().toLowerCase(Locale.ROOT)));
        }
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        for (Item item : ITEM_LIST) {
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent event) {
        for (Item item : ITEM_LIST) {
            if (item.getRegistryName() != null) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }
    }

    public static void info(String str) {
        INSTANCE.logger.info(str);
    }
}
