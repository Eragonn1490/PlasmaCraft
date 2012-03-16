package net.minecraft.src.Plasmacraft;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;

public class BlockCausticStationary extends BlockCausticFluids implements ITextureProvider
{
    public BlockCausticStationary(int i, int j, int k, int l, int i1, int j1, float lightvalue)
    {
        super(i, j, k, l, i1, j1);
        setHardness(5F);
        setLightOpacity(3);
        setLightValue(lightvalue);
        
    }

    public int getBlockTextureFromSide(int i)
    {
        if(i == 0 || i == 1)
        {
            return stillTextureID;
        }
        else
        {
            return flowingTextureID;
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        if(world.getBlockId(i, j, k) == blockID)
        {
            func_27038_j(world, i, j, k);
        }
    }

    private void func_27038_j(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        world.editingBlocks = true;
        world.setBlockAndMetadata(i, j, k, flowingBlockID, l);
        world.markBlocksDirty(i, j, k, i, j, k);
        world.scheduleBlockUpdate(i, j, k, flowingBlockID, tickRate());
        world.editingBlocks = false;
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if(blockMaterial == Material.lava)
        {
            int l = random.nextInt(3);
            for(int i1 = 0; i1 < l; i1++)
            {
                i += random.nextInt(3) - 1;
                j++;
                k += random.nextInt(3) - 1;
                int j1 = world.getBlockId(i, j, k);
                if(j1 == 0)
                {
                    if(isFlammable(world, i - 1, j, k) || isFlammable(world, i + 1, j, k) || isFlammable(world, i, j, k - 1) || isFlammable(world, i, j, k + 1) || isFlammable(world, i, j - 1, k) || isFlammable(world, i, j + 1, k))
                    {
                        world.setBlockWithNotify(i, j, k, Block.fire.blockID);
                        return;
                    }
                    continue;
                }
                if(Block.blocksList[j1].blockMaterial.isSolid())
                {
                    return;
                }
            }

        }
    }

    private boolean isFlammable(World world, int i, int j, int k)
    {
        return world.getBlockMaterial(i, j, k).getCanBurn();
    }
}
