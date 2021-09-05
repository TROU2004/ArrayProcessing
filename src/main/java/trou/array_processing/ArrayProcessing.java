package trou.array_processing;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import trou.array_processing.block.BlockArrayGlass;
import trou.array_processing.block.BlockArrayWall;
import trou.array_processing.block.BlockStructure;

@Mod(modid = ArrayProcessing.MODID, name = ArrayProcessing.NAME, version = ArrayProcessing.VERSION, dependencies = "required-after:zerocoreap")
@Mod.EventBusSubscriber(modid = ArrayProcessing.MODID)
public class ArrayProcessing {
    public static final String MODID = "array_processing";
    public static final String NAME = "Array Processing";
    public static final String VERSION = "1.0.0";
    public static final CreativeTabs TAB = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.FURNACE);
        }
    };
    public static final Block[] BLOCK_LIST = new Block[]{
            new BlockArrayGlass(),
            new BlockArrayWall()
    };
    private Logger logger;

    @Mod.Instance
    public static ArrayProcessing INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        info(Loader.isModLoaded("zerocore") ? "There are two zerocore running in your game, may causes problems." : "ArrayProcessing is running with zerocoreap.");
    }

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
        for (Block block : BLOCK_LIST) {
            event.getRegistry().register(block);
            ((BlockStructure) block).registerTileEntity();
        }
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        for (Block block : BLOCK_LIST) {
            if (block.getRegistryName() != null) {
                Item item = new ItemBlock(block).setRegistryName(block.getRegistryName());
                event.getRegistry().register(item);
            }
        }
    }

    @SubscribeEvent
    public static void onModelRegistration(ModelRegistryEvent event) {
        for (Block block : BLOCK_LIST) {
            ((BlockStructure) block).registerModelItem();
        }
    }

    public static void info(String str) {
        INSTANCE.logger.info(str);
    }
}
