package com.elvenwater.malkierian.Plasmacraft.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.World;

public class BlockPlasmaBench extends BlockContainer
{
	private Random plasmificatorRand;
    private final boolean isActive;
    private final int frontIdleTexture;
    private final int frontActiveTexture;
    private static boolean keepPlasmificatorInventory = false;

    protected BlockPlasmaBench(int i)
    {
        super(i, Material.rock);
        isActive = false;
        plasmificatorRand = new Random();
        blockIndexInTexture = PlasmaCraft.plasmaBenchSidesIndex;
        frontIdleTexture = PlasmaCraft.plasmaBenchFrontIdleIndex;
        frontActiveTexture = PlasmaCraft.plasmaBenchFrontAnim;
        setHardness(3F);
        setStepSound(Block.soundStoneFootstep);
        setLightValue(0.0f);
    }

    @Override
    public void addCreativeItems(ArrayList itemList)
    {    	
    	itemList.add(new ItemStack(this, 1));
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
    	if(world.getBlockMetadata(x, y, z) > 8)
    		return 12;
    	else
    		return 0;
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return PlasmaCraft.plasmaBench.blockID;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        if(!world.isRemote)
        {
            return;
        }
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
        {
            byte0 = 3;
        }
        if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 2;
        }
        if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
        {
            byte0 = 5;
        }
        if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
        {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        int i1 = iblockaccess.getBlockMetadata(i, j, k);
        int meta = i1 > 8 ? i1 - 8 : i1; 
        if(l != meta)
        {
            return blockIndexInTexture;
        }
        if(i1 > 8)
        {
            return frontActiveTexture;
        } else
        {
            return frontIdleTexture;
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        super.randomDisplayTick(world, i, j, k, random);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(i == 3)
        {
        	if(j > 8)
        		return frontActiveTexture;
        	else
        		return frontIdleTexture;
        } else
        {
            return blockIndexInTexture;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
    	// Drop through if the player is sneaking
		if(entityplayer.isSneaking())
			return false;
		
    	TileEntity tileentity = world.getBlockTileEntity(i, j, k);
    	
    	if(tileentity == null)
    		return false;
		
		if(world.isRemote)
			return true;
		else
		{
			entityplayer.openGui(PlasmaCraft.instance, GuiIds.PLASMIFICATOR, world, i, j, k);
    	   	return true;
		}
    }

    public static void updatePlasmificatorBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
//        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
//        keepPlasmificatorInventory = true;
        if(flag)
        {
        	if(l < 8)
        		l += 8;
        }
        else
        {
        	if(l > 8)
        		l -= 8;
        }
        
        //keepPlasmificatorInventory = false;
        world.setBlockMetadataWithNotify(i, j, k, l);
        world.markBlockNeedsUpdate(i, j, k);
//        if(tileentity != null)
//        {
//            tileentity.validate();
//            world.setBlockTileEntity(i, j, k, tileentity);
//        }
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2);
        }
        if(l == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5);
        }
        if(l == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3);
        }
        if(l == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4);
        }
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
    	onBlockRemoved(world, i, j, k);
    	super.onBlockDestroyedByPlayer(world, i, j, k, l);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4)
    {
    	onBlockRemoved(par1World, par2, par3, par4);
    	super.onBlockDestroyedByExplosion(par1World, par2, par3, par4);
    }

    public void onBlockRemoved(World world, int i, int j, int k)
    {
        if(!keepPlasmificatorInventory)
        {
            TilePlasmaBench tileentityfurnace = (TilePlasmaBench)world.getBlockTileEntity(i, j, k);
            if(tileentityfurnace != null)
            {
label0:
                for(int l = 0; l < tileentityfurnace.getSizeInventory(); l++)
                {
                    ItemStack itemstack = tileentityfurnace.getStackInSlot(l);
                    if(itemstack == null)
                    {
                        continue;
                    }
                    float f = plasmificatorRand.nextFloat() * 0.8F + 0.1F;
                    float f1 = plasmificatorRand.nextFloat() * 0.8F + 0.1F;
                    float f2 = plasmificatorRand.nextFloat() * 0.8F + 0.1F;
                    do
                    {
                        if(itemstack.stackSize <= 0)
                        {
                            continue label0;
                        }
                        int i1 = plasmificatorRand.nextInt(21) + 10;
                        if(i1 > itemstack.stackSize)
                        {
                            i1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= i1;
                        EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float)plasmificatorRand.nextGaussian() * f3;
                        entityitem.motionY = (float)plasmificatorRand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float)plasmificatorRand.nextGaussian() * f3;
                        world.spawnEntityInWorld(entityitem);
                    } while(true);
                }
            }
        }
    }

	@Override
	public String getTextureFile()
	{
		return CommonProxy.BLOCK_PNG;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TilePlasmaBench();
	}
}
