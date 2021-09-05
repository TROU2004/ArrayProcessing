package trou.array_processing.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import trou.array_processing.ArrayProcessing;

public class BlockStructure extends Block {
    public BlockStructure(String identity) {
        super(Material.ROCK);
        this.setRegistryName("array_" + identity);
        this.setTranslationKey("array_" + identity);
        this.setCreativeTab(ArrayProcessing.TAB);
    }
}
