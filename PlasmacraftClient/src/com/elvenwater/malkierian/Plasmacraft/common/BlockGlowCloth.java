package com.elvenwater.malkierian.Plasmacraft.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockGlowCloth extends Block
{
    protected BlockGlowCloth(int i, int j)
    {
        super(i, j, Material.cloth);
        setLightValue(1F);
        setStepSound(Block.soundClothFootstep);
		setCreativeTab(CreativeTabs.tabBlock);
		setBlockName("glowCloth");
		setHardness(0.8F);
		setRequiresSelfNotify();
    }
    
    public void addCreativeItems(ArrayList itemList)
    {
    	for(int i = 0; i < 8; i++)
    	{
    		itemList.add(new ItemStack(this, 1, i));
    	}
    }

    public int idDropped(int i, Random random)
    {
        return blockID;
    }
    
    protected int damageDropped(int i)
    {
    	return i;
    }
    
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
    	switch (j)
    	{
    	case 0:
    		return PlasmaCraft.glowClothAcidIndex;
    	case 1:
    		return PlasmaCraft.glowClothRadioniteIndex;
    	case 2:
    		return PlasmaCraft.glowClothNetherflowIndex;
    	case 3:
    		return PlasmaCraft.glowClothNeptuniumIndex;
    	case 4:
    		return PlasmaCraft.glowClothUraniumIndex;
    	case 5:
    		return PlasmaCraft.glowClothPlutoniumIndex;
    	case 6:
    		return PlasmaCraft.glowClothCryoniteIndex;
    	default:
    		return PlasmaCraft.glowClothObsidiumIndex;
    	}
    	
    }
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < 8; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}

    public int quantityDropped(Random random)
    {
        return 1;
    }

	@Override
	public String getTextureFile()
	{
		return CommonProxy.BLOCK_PNG;
	}
}
